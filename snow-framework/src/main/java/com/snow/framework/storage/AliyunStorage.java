package com.snow.framework.storage;


import cn.hutool.core.util.ObjectUtil;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.aliyun.oss.model.PutObjectRequest;
import com.snow.common.exception.BusinessException;
import com.snow.common.utils.ServletUtils;
import com.snow.system.domain.SysFile;
import com.snow.system.service.impl.SysFileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author qimingjin
 * @Title:
 * @Description: 阿里云对象存储
 * @date 2021/1/11 11:15
 */
@Slf4j
public class AliyunStorage implements Storage{
    private final Log logger = LogFactory.getLog(AliyunStorage.class);

    @Autowired
    private SysFileServiceImpl sysFileService;


    private String endpoint;
    private String accessKeyId;
    private String accessKeySecret;
    private String bucketName;

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getAccessKeyId() {
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId) {
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret() {
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret) {
        this.accessKeySecret = accessKeySecret;
    }

    public String getBucketName() {
        return bucketName;
    }

    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }

    /**
     * 获取阿里云OSS客户端对象
     *
     * @return ossClient
     */
    private OSS getOSSClient() {
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);
    }

    private String getBaseUrl() {
        return "https://" + bucketName + "." + endpoint + "/";
    }

    /**
     * 阿里云OSS对象存储简单上传实现
     */
    @Override
    public void store(String keyName, MultipartFile file) {
        try {
            // 简单文件上传, 最大支持 5 GB, 适用于小文件上传, 建议 20M以下的文件使用该接口
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());
            // 对象键（Key）是对象在存储桶中的唯一标识。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, keyName, file.getInputStream(), objectMetadata);
            getOSSClient().putObject(putObjectRequest);
            // 关闭OSSClient
            getOSSClient().shutdown();
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
        }

    }

    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public void load(String keyName) {
        HttpServletResponse response = ServletUtils.getResponse();
        SysFile sysFile = sysFileService.selectSysFileByKey(keyName);
        if (ObjectUtil.isEmpty(sysFile)){
            throw new BusinessException("该文件不存在");
        }

        OSSObject object = getOSSClient().getObject(bucketName, keyName);
        response.setCharacterEncoding("utf-8");
        response.setContentType("multipart/form-data");
        //通知浏览器以附件形式下载
        response.setHeader("Content-Disposition",
                "attachment;filename=" + sysFile.getName());
        try {
            // 读取文件内容。
            BufferedInputStream in = new BufferedInputStream(object.getObjectContent());
            BufferedOutputStream out = new BufferedOutputStream(response.getOutputStream());
            byte[] buffer = new byte[1024];
            int lenght = 0;
            while ((lenght = in.read(buffer)) != -1) {
                out.write(buffer, 0, lenght);
            }
            if(ObjectUtil.isNotEmpty(out)){
                out.flush();
                out.close();
            }
            if(ObjectUtil.isNotEmpty(in)){
                in.close();
            }
            // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
            in.close();
            // 关闭OSSClient。
            getOSSClient().shutdown();
        } catch (IOException e) {
            e.printStackTrace();
            throw new BusinessException("下载文件异常");
        }


    }

    @Override
    public Resource loadAsResource(String keyName) {
        try {
            URL url = new URL(getBaseUrl() + keyName);
            Resource resource = new UrlResource(url);
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                return null;
            }
        } catch (MalformedURLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    @Override
    public void delete(String keyName) {
        try {
            getOSSClient().deleteObject(bucketName, keyName);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

    }

    @Override
    public String generateUrl(String keyName) {
        return getBaseUrl() + keyName;
    }
}
