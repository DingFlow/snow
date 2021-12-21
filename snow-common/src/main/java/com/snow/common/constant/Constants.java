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

    /**
     * 中文
     */
    public static final String ZH_CN="zh_CN";


    /**
     * 支付宝公钥
     */
    public static final String ALIPAY_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAq1t0PeHM9KjQksI/qQTGXc5z3ic9a7sq6uThianOEB8xUZMT2Qe505L3XGWiyk7UNpnyEqOmvgrkG7SkbF/LJlmnowrL8Akkju72famdJ/imTsPRdHyEFFo5hmqUTsaeR56SKMcLINlCX+bIunva9c8uN1N6C1DmSYywOUx1Wj7wpVWWFHs1KsM13Nbe45VL4v76XXUvXOXSEYNlfu8/MvK8rjPHliqwDp2CRUG/oj14SJmZI1myGgqYHVMHmoBQI1wJg66ow5XdLKYy252edMKwOSnOBfcR/uNFw0292Cv4TKFDmyn3ox/MptHW2UqLSqVC80eJpIVPGjlYpyC5PwIDAQAB";

    /***
     * 公钥
      */
    public static final String ALIPAY_RSA_PUBLIC_KEY="MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApbNsg+nw0thcPCvrmBNPrpfGSQHh5gtlZTuo7/A8n9rHImXQvvp2n+SBulJ2i/Ay8RpDyGTz9z107nYbjpNIDarhSiEycSf94Cfn51NgALrHyC9E3kbDH1vMLPFdSUfCxCHaYX79BH5enKL6GIDVLhrTmS+ZPYsKt9CBMqTGytu9ukRVTe8Y3dPXjK4v5C38PL1YConTea4DQI5DrIm6KR6SoSCrHkS33BaTwazZZzydvFOxd0aeU0OzhCxwxM750fTdZuhyL/CieRVFQgoBl5fokg1xmhwh/0H7LSa0hBwC4XdViCgUmAQB3pHNj+o5S/17mxHfc8jsG/qNJIYfXwIDAQAB";

    /**
     * 私钥
     */
    public static final String ALIPAY_RSA_PRIVATE_KEY="MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCls2yD6fDS2Fw8K+uYE0+ul8ZJAeHmC2VlO6jv8Dyf2sciZdC++naf5IG6UnaL8DLxGkPIZPP3PXTudhuOk0gNquFKITJxJ/3gJ+fnU2AAusfIL0TeRsMfW8ws8V1JR8LEIdphfv0Efl6covoYgNUuGtOZL5k9iwq30IEypMbK2726RFVN7xjd09eMri/kLfw8vVgKidN5rgNAjkOsibopHpKhIKseRLfcFpPBrNlnPJ28U7F3Rp5TQ7OELHDEzvnR9N1m6HIv8KJ5FUVCCgGXl+iSDXGaHCH/QfstJrSEHALhd1WIKBSYBAHekc2P6jlL/XubEd9zyOwb+o0khh9fAgMBAAECggEATQQpUuLgUU/JBxqu5T9MkOzwd6yNT7xKQ5RIQEWcF/y7A+zo6mtFyamO9ogvm3loZBH+IV1y257QA6oXwtwH6HpdPnRRmrCo2pARxL5G5+/ovc3ip6GiPagQLW9+GXpmN304oIpCld0aoplJvyvaahSD9zZddnJy597cvipCT7S1dAvnnT72TVhIOZ/jdGwFncnhn1mm7EC8H/bMPWHmw7kIQvBWerb/2KT5CxHB438Ls71ltqNs1EgsyVJJP8A3iBDtdRJJJMd2qnAyLBYT0CNONWiW3+fICOJIJSsmXWnJ9RJR7R8Hqy3z327j3hLDc+rGVzHsJs9N07/haVlCSQKBgQDOF1s9RaP4lQ+M8qENw3+SMjrCaspLbiOhdclXz+nlR1XELwecryWBFE48DRekdNBQUICgOMzr2aqELEAblpBJHphYJqcNrYKGmjVpq3gK7jTN2OyqFOtwiza6UNbaBu04jMfEfpbEwj5jW2CyaVf55sKD1gERYlnyz4jOVGLngwKBgQDN1BBJE/ZH/dA0KdIgeCxeEziXLUW8Uec+v8b2OydJ/1xGXYT2cI4V8r8PABrEPHs325uWZlOuXAHXe1zviBthwfjxeus5AB45Kx5QBHYUSwP3T3BMJILlIirtsoYZGHDCl3rfT26YjgMC59nNAkKHjE7TJQOZW/mNtS4F5vMF9QKBgQDIKq1GfK4+0WBSMJI2kQwFzd1+WUR0MNxQhNty+5CcTUXDDz6gcwjFLxixgLHW3FI7v4S+mYyHAll4mJYHdZxcOGgVLi2QvlaJL5vzL0SfsS7+Q9PRehIWtFAKdFvJVXLwOuQIOD9tiZAY5narsl9tbDC+5YsR/GmKyQGCDTomywKBgF+lKaQryIgPo855mJFKBqP4npVxjjRCgwifqV8Gge1a4pA4KO8zwnLkiG/6BnYl/MQqIpSzHDOrwZdWVgtg3giBUKM2EVQ9f83lAYMBb7ViXodvvKlFViYPyI8IJFRJDSz/ozNd//CYHUIeZxFi+XNvebmYO6HqIX7+MFLBzTB5AoGBAIrlu7b46OlZcU3A+Cweaf9UEPSYSuGxsu4AlUnNuFu6n9sCVtipcuOhadsVpgrtdFRB6VG43+WDoavM7r9gVaPw64DegT8J/QnECgLnHYAmvK/EJyUBk+qhN7lLOmzfr7Fr9KuT0eY75kjnHHKWCLLRPuhxMhmXfuinv/kIJtOx";
}
