package com.snow.dingtalk.aspectj;

import com.snow.common.annotation.DingTalkLog;
import com.snow.common.enums.BusinessStatus;
import com.snow.common.exception.DingTalkApiException;
import com.snow.common.exception.SyncDataException;
import com.snow.common.json.JSON;
import com.snow.common.utils.ServletUtils;
import com.snow.common.utils.StringUtils;
import com.snow.framework.manager.AsyncManager;
import com.snow.framework.manager.factory.AsyncFactory;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysDingtalkSyncLog;
import com.snow.system.domain.SysUser;
import com.taobao.api.ApiException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Optional;

/**
 * 操作日志记录处理
 * 
 * @author snow
 */
@Aspect
@Component
public class SyncLogAspect
{
    private static final Logger log = LoggerFactory.getLogger(SyncLogAspect.class);

    // 配置织入点
    @Pointcut("@annotation(com.snow.common.annotation.DingTalkLog)")
    public void logPointCut()
    {
    }

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "logPointCut()", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Object jsonResult)
    {
        handleLog(joinPoint, null, jsonResult);
    }

    /**
     * 拦截异常操作
     * 
     * @param joinPoint 切点
     * @param e 异常
     */
    @AfterThrowing(value = "logPointCut()", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Exception e)
    {
        handleLog(joinPoint, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, final Exception e, Object jsonResult)
    {
        try
        {
            // 获得注解
            DingTalkLog controllerLog = getAnnotationLog(joinPoint);
            if (controllerLog == null) {
                return;
            }
            // 获取当前的用户
            SysUser currentUser = ShiroUtils.getSysUser();
            SysDingtalkSyncLog sysDingtalkSyncLog = new SysDingtalkSyncLog();
            if(StringUtils.isNotNull(currentUser)){
                sysDingtalkSyncLog.setOperName(currentUser.getUserName());
                if (StringUtils.isNotNull(currentUser.getDept()) && StringUtils.isNotEmpty(currentUser.getDept().getDeptName())) {
                    sysDingtalkSyncLog.setDeptName(currentUser.getDept().getDeptName());
                }
                // 请求的地址
                String ip = ShiroUtils.getIp();
                sysDingtalkSyncLog.setOperIp(ip);
            }else {
                sysDingtalkSyncLog.setOperName("无");
                sysDingtalkSyncLog.setDeptName("无");
            }

            sysDingtalkSyncLog.setStatus(BusinessStatus.SUCCESS.ordinal());

            // 返回参数
            sysDingtalkSyncLog.setJsonResult(StringUtils.substring(JSON.marshal(jsonResult), 0, 2000));
            Optional.ofNullable(ServletUtils.getRequest()).ifPresent(t->sysDingtalkSyncLog.setOperUrl(ServletUtils.getRequest().getRequestURI()));
            if (e != null) {
                if(e instanceof SyncDataException ){
                    sysDingtalkSyncLog.setStatus(BusinessStatus.FAIL.ordinal());
                    sysDingtalkSyncLog.setOperDingtalkParam(((SyncDataException) e).getRequestParam());
                    sysDingtalkSyncLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
                }else if(e instanceof DingTalkApiException){
                    sysDingtalkSyncLog.setStatus(BusinessStatus.FAIL.ordinal());
                    sysDingtalkSyncLog.setOperDingtalkParam(((DingTalkApiException) e).getRequestParam());
                    sysDingtalkSyncLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
                } else if(e instanceof ApiException){
                    sysDingtalkSyncLog.setStatus(BusinessStatus.FAIL.ordinal());
                    sysDingtalkSyncLog.setErrorMsg(StringUtils.substring(((ApiException) e).getErrMsg(), 0, 2000));
                }
                else {
                    sysDingtalkSyncLog.setStatus(BusinessStatus.FAIL.ordinal());
                    sysDingtalkSyncLog.setErrorMsg(StringUtils.substring(e.getMessage(), 0, 2000));
                }
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();

            sysDingtalkSyncLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            Optional.ofNullable(ServletUtils.getRequest()).ifPresent(t->sysDingtalkSyncLog.setRequestMethod(ServletUtils.getRequest().getMethod()));

            // 处理设置注解上的参数
            getControllerMethodDescription(controllerLog,joinPoint, sysDingtalkSyncLog);
            // 保存数据库
            AsyncManager.me().execute(AsyncFactory.recordDingTalkSyncOper(sysDingtalkSyncLog));
        }
        catch (Exception exp)
        {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Service层注解
     * 
     * @param log 日志
     * @param sysDingtalkSyncLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(DingTalkLog log, JoinPoint joinPoint, SysDingtalkSyncLog sysDingtalkSyncLog) throws Exception
    {
        sysDingtalkSyncLog.setTitle(log.dingTalkLogType().getTitle());
        sysDingtalkSyncLog.setModuleType(log.dingTalkLogType().getModuleType());
        sysDingtalkSyncLog.setBusinessType(log.dingTalkLogType().getBusinessType());
        // 设置操作人类别
        sysDingtalkSyncLog.setOperatorType(log.dingTalkSyncType().getCode());
        sysDingtalkSyncLog.setLogType(log.syncLogTpye().getCode());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData())
        {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(sysDingtalkSyncLog,joinPoint);
        }
    }

    /**
     * 获取请求的参数，放到log中
     * 
     * @param operLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(SysDingtalkSyncLog operLog ,JoinPoint joinPoint) throws Exception
    {
        //获取切面的参数
        Object[] args = joinPoint.getArgs();
        operLog.setOperSourceParam(StringUtils.substring(com.alibaba.fastjson.JSON.toJSONString(args), 0, 4000));
    }

    /**
     * 是否存在注解，如果存在就获取
     */
    private DingTalkLog getAnnotationLog(JoinPoint joinPoint) throws Exception
    {
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();

        if (method != null)
        {
            return method.getAnnotation(DingTalkLog.class);
        }
        return null;
    }
}
