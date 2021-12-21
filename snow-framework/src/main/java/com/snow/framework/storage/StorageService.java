package com.snow.framework.storage;

import cn.hutool.core.util.IdUtil;
import com.snow.framework.util.ShiroUtils;
import com.snow.system.domain.SysFile;
import com.snow.system.domain.SysUser;
import com.snow.system.service.ISysFileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/1/11 11:23
 */
public class StorageService {

    private String active;

    private Storage storage;

    @Autowired
    private ISysFileService sysFileService;

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public Storage getStorage() {
        return storage;
    }

    public void setStorage(Storage storage) {
        this.storage = storage;
    }

    /**
     * 存储一个文件对象
     *
     * @param file   文件输入流
     */
    public SysFile store(MultipartFile file){
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        long size = file.getSize();
        String key = generateKey(fileName);
        storage.store(key,file);
        String url = generateUrl(key);
        SysFile storageInfo = new SysFile();
        storageInfo.setName(fileName);
        storageInfo.setSize(size);
        storageInfo.setType(contentType);
        storageInfo.setKey(key.substring(0,key.indexOf(",")));
        storageInfo.setUrl(url);
        storageInfo.setCreateBy(ShiroUtils.getLoginName());
        sysFileService.insertSysFile(storageInfo);

        return storageInfo;
    }

    private String generateKey(String originalFilename) {
        int index = originalFilename.lastIndexOf('.');
        String suffix;
        if(index==-1){
            suffix="profile";
        }else {
            suffix = originalFilename.substring(index);
        }
        return IdUtil.fastSimpleUUID() + suffix;
    }

    public Stream<Path> loadAll() {
        return storage.loadAll();
    }

    public void load(String keyName) {
         storage.load(keyName);
    }

    public Resource loadAsResource(String keyName) {
        return storage.loadAsResource(keyName);
    }

    public void delete(String keyName) {
        storage.delete(keyName);
    }

    private String generateUrl(String keyName) {
        return storage.generateUrl(keyName);
    }
}
