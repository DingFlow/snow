package com.snow.framework.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/1/11 11:20
 */
public interface Storage {
    /**
     * 存储一个文件对象
     *
     * @param file
     * @param keyName       文件名
     */
    void store(String keyName, MultipartFile file);

    /**
     *
     * @return
     */
    Stream<Path> loadAll();

    /**
     * 下载文件
     * @param keyName
     * @return
     */
    void load(String keyName);

    /**
     * 下载文件
     * @param keyName
     * @return
     */
    Resource loadAsResource(String keyName);

    /**
     * 删除
     * @param keyName
     */
    void delete(String keyName);

    String generateUrl(String keyName);
}
