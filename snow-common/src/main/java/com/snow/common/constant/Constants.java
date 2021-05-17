package com.snow.common.constant;

/**
 * 通用常量信息
 * 
 * @author snow
 */
public class Constants
{
    /**
     * UTF-8 字符集
     */
    public static final String UTF8 = "UTF-8";

    /**
     * GBK 字符集
     */
    public static final String GBK = "GBK";

    /**
     * 通用成功标识
     */
    public static final String SUCCESS = "0";

    /**
     * 通用失败标识
     */
    public static final String FAIL = "1";

    /**
     * 登录成功
     */
    public static final String LOGIN_SUCCESS = "Success";

    /**
     * 注销
     */
    public static final String LOGOUT = "Logout";

    /**
     * 注册
     */
    public static final String REGISTER = "Register";

    /**
     * 登录失败
     */
    public static final String LOGIN_FAIL = "Error";

    /**
     * 当前记录起始索引
     */
    public static final String PAGE_NUM = "pageNum";

    /**
     * 每页显示记录数
     */
    public static final String PAGE_SIZE = "pageSize";

    /**
     * 排序列
     */
    public static final String ORDER_BY_COLUMN = "orderByColumn";

    /**
     * 排序的方向 "desc" 或者 "asc".
     */
    public static final String IS_ASC = "isAsc";

    /**
     * 参数管理 cache name
     */
    public static final String SYS_CONFIG_CACHE = "sys-config";

    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 字典管理 cache name
     */
    public static final String SYS_DICT_CACHE = "sys-dict";

    /**
     * 字典管理 cache key
     */
    public static final String SYS_DICT_KEY = "sys_dict:";


    /**
     * 本地文件上传key
     */
    public static final String LOCAL_ADDRESS = "sys.local.file.address";

    /**
     * 阿里云accessKeyId
     */
    public static final String ALIYUN_ACCESSKEYID = "sys.aliyun.file.accessKeyId";

    /**
     * 阿里云accessKeySecret
     */
    public static final String ALIYUN_ACCESSKEYSECRET = "sys.aliyun.file.accessKeySecret";

    /**
     * 阿里云bucketName
     */
    public static final String ALIYUN_BUCKETNAME= "sys.aliyun.file.bucketName";


    /**
     * 阿里云endpoint
     */
    public static final String ALIYUN_ENDPOINT = "sys.aliyun.file.endpoint";
    /**
     * 资源映射路径 前缀
     */
    public static final String RESOURCE_PREFIX = "/profile";

    /**
     * 系统用户授权缓存
     */
    public static final String SYS_AUTH_CACHE = "sys-authCache";

    public static final String POST="POST";

    public static final String GET="GET";
    /**
     * 钉钉企业内部APPkey
     */
    public static final String ENTERPRICE_APP_KEY="enterprice.app.key";
    /**
     * 钉钉企业内部ENTERPRICE_APP_SECRET
     */
    public static final String ENTERPRICE_APP_SECRET="enterprice.app.secret";
    /**
     * 钉钉企业内部 AGENT_ID
     */
    public static final String AGENT_ID="agent.id";

    /**
     * 通讯录事件type_key
     */
    public static final String ADDRESS_BOOK="address_book";
    /**
     * 钉钉回调成功返回
     */
    public static final String CALL_BACK_SUCCESS_RETURN="success";
    /**
     * 钉钉回调失败返回
     */
    public static final String CALL_BACK_FAIL_RETURN="fail";


    /**
     * dingding消息类型
     */
    public static final Integer NEWS_DINGDING_TYPE=1;

    /**
     * 邮件消息类型
     */
    public static final Integer NEWS_EMAIL_TYPE=2;

    /**
     * DingFlow标识
     */
    public static final String Ding_Flow="DingFlow";

}
