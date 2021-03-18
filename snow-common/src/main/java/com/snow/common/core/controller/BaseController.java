package com.snow.common.core.controller;

import java.beans.PropertyEditorSupport;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.snow.common.core.page.PageModel;
import com.snow.common.utils.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.snow.common.core.domain.AjaxResult;
import com.snow.common.core.domain.AjaxResult.Type;
import com.snow.common.core.page.PageDomain;
import com.snow.common.core.page.TableDataInfo;
import com.snow.common.core.page.TableSupport;
import com.snow.common.utils.DateUtils;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.sql.SqlUtil;

/**
 * web层通用数据处理
 * 
 * @author snow
 */
public class BaseController
{
    protected final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 将前台传递过来的日期格式的字符串，自动转化为Date类型
     */
    @InitBinder
    public void initBinder(WebDataBinder binder)
    {
        // Date 类型转换
        binder.registerCustomEditor(Date.class, new PropertyEditorSupport()
        {
            @Override
            public void setAsText(String text)
            {
                setValue(DateUtils.parseDate(text));
            }
        });
    }

    /**
     * 设置请求分页数据
     */
    protected void startPage()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        if (StringUtils.isNotNull(pageNum) && StringUtils.isNotNull(pageSize))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.startPage(pageNum, pageSize, orderBy);
        }
    }
    /**
     * 设置请求排序数据
     */
    protected void startOrderBy()
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        if (StringUtils.isNotEmpty(pageDomain.getOrderBy()))
        {
            String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
            PageHelper.orderBy(orderBy);
        }
    }

    /**
     * 获取request
     */
    public HttpServletRequest getRequest()
    {
        return ServletUtils.getRequest();
    }

    /**
     * 获取response
     */
    public HttpServletResponse getResponse()
    {
        return ServletUtils.getResponse();
    }

    /**
     * 获取session
     */
    public HttpSession getSession()
    {
        return getRequest().getSession();
    }

    /**
     * 响应请求分页数据
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected TableDataInfo getDataTable(List<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list);
        rspData.setTotal(new PageInfo(list).getTotal());
        rspData.setPageIndex(new PageInfo(list).getPageNum());
        rspData.setPageSize(new PageInfo(list).getPageSize());

        return rspData;
    }

    /**
     * flowable的分页
     * @param list
     * @return
     */
    protected TableDataInfo getFlowDataTable(PageModel<?> list)
    {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(0);
        rspData.setRows(list.getPagedRecords());
        rspData.setTotal(list.getTotalCount());
        rspData.setPageIndex(list.getPageNo());
        rspData.setPageSize(list.getPageSize());
        return rspData;
    }

    /**
     * 利用subList方法进行分页
     * @param list 分页数据
     */
    public TableDataInfo pageBySubList(List list) {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageIndex = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        TableDataInfo rspData = new TableDataInfo();
        int totalCount = list.size();
        int pageCount = 0;
        List<String> subList;
        int m = totalCount % pageSize;
        if (m > 0) {
            pageCount = totalCount / pageSize + 1;
        } else {
            pageCount = totalCount / pageSize;
        }
        if (m == 0) {
            subList = list.subList((pageIndex - 1) * pageSize, pageSize * (pageIndex));
        } else {
            if (pageIndex == pageCount) {
                subList = list.subList((pageIndex - 1) * pageSize, totalCount);
            } else {
                subList = list.subList((pageIndex - 1) * pageSize, pageSize * (pageIndex));
            }
        }
        rspData.setCode(0);
        rspData.setRows(subList);
        rspData.setTotal(totalCount);
        rspData.setPageIndex(pageIndex);
        rspData.setPageSize(pageSize);
        return rspData;
    }
    /**
     * 响应返回结果
     * 
     * @param rows 影响行数
     * @return 操作结果
     */
    protected AjaxResult toAjax(int rows)
    {
        return rows > 0 ? success() : error();
    }

    /**
     * 响应返回结果
     * 
     * @param result 结果
     * @return 操作结果
     */
    protected AjaxResult toAjax(boolean result)
    {
        return result ? success() : error();
    }

    /**
     * 返回成功
     */
    public AjaxResult success()
    {
        return AjaxResult.success();
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error()
    {
        return AjaxResult.error();
    }

    /**
     * 返回成功消息
     */
    public AjaxResult success(String message)
    {
        return AjaxResult.success(message);
    }

    /**
     * 返回失败消息
     */
    public AjaxResult error(String message)
    {
        return AjaxResult.error(message);
    }

    /**
     * 返回错误码消息
     */
    public AjaxResult error(Type type, String message)
    {
        return new AjaxResult(type, message);
    }

    /**
     * 页面跳转
     */
    public String redirect(String url)
    {
        return StringUtils.format("redirect:{}", url);
    }
}
