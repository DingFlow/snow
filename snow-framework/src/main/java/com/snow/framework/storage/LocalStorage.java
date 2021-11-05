package com.snow.framework.storage;

import cn.hutool.core.util.ObjectUtil;
import com.snow.common.config.ServerConfig;
import com.snow.common.constant.Constants;
import com.snow.common.exception.BusinessException;
import com.snow.common.exception.file.FileNameLengthLimitExceededException;
import com.snow.common.exception.file.FileSizeLimitExceededException;
import com.snow.common.exception.file.InvalidExtensionException;
import com.snow.common.utils.DateUtils;
import com.snow.common.utils.ServletUtils;
import com.snow.common.utils.StringUtils;
import com.snow.common.utils.file.FileUploadUtils;
import com.snow.common.utils.file.FileUtils;
import com.snow.common.utils.file.MimeTypeUtils;
import com.snow.system.domain.SysFile;
import com.snow.system.service.impl.SysFileServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * @author qimingjin
 * @Title:
 * @Description:
 * @date 2021/1/11 13:20
 */
@Slf4j
public class LocalStorage implements Storage {

    /**
     * 本地文件上传地址
     */
    private String address;


    @Autowired
    private ServerConfig serverConfig;

    @Autowired
    private SysFileServiceImpl sysFileService;

    /**
     * 默认大小 50M
     */
    public static final long DEFAULT_MAX_SIZE = 50 * 1024 * 1024;


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 以默认配置进行文件上传
     *
     * @param file 上传的文件
     * @return 文件名称
     * @throws Exception
     */
    public  final String upload(MultipartFile file) throws IOException
    {
        try
        {
            return upload(address, file,null, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        }
        catch (Exception e)
        {
            throw new IOException(e.getMessage(), e);
        }
    }
    /**
     * 根据文件路径上传
     *
     * @param file 上传的文件
     * @return 文件名称
     * @throws IOException
     */
    @Override
    public void store(String keyName,MultipartFile file) {

        try {
             String url =address + "/upload";
             upload(url, file,keyName, MimeTypeUtils.DEFAULT_ALLOWED_EXTENSION);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidExtensionException e) {
            e.printStackTrace();
        }

    }

    /**
     * 生成文件地址URL
     * @param keyName
     * @return
     */
    @Override
    public String generateUrl(String keyName) {
        String url = serverConfig.getUrl()+getPathFileName(address + "/upload",DateUtils.datePath() + "/" +keyName) ;
        return url;
    }


    /**
     * 文件上传
     *
     * @param baseDir 相对应用的基目录
     * @param file 上传的文件
     * @param allowedExtension 上传文件类型
     * @return 返回上传成功的文件名
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws FileNameLengthLimitExceededException 文件名太长
     * @throws IOException 比如读写文件出错时
     * @throws InvalidExtensionException 文件校验异常
     */
    public  String upload(String baseDir, MultipartFile file,String keyName, String[] allowedExtension)
            throws FileSizeLimitExceededException, IOException, FileNameLengthLimitExceededException, InvalidExtensionException {
        int fileNameLength = file.getOriginalFilename().length();
        if (fileNameLength > FileUploadUtils.DEFAULT_FILE_NAME_LENGTH) {
            throw new FileNameLengthLimitExceededException(FileUploadUtils.DEFAULT_FILE_NAME_LENGTH);
        }
        //文件大小和格式校验
        assertAllowed(file, allowedExtension);
        //编码文件名字
        String fileName = extractFilename(keyName);
        //创建file文件
        File desc = getAbsoluteFile(baseDir, fileName);
        //上传文件写到服务器上指定的文件
        file.transferTo(desc);
        String pathFileName = getPathFileName(baseDir, fileName);
        return pathFileName;
    }

    /**
     * 编码文件名
     */
    public static final String extractFilename(String keyName)
    {
        return DateUtils.datePath() + "/" + keyName;
    }

    /**
     * 创建File文件
     * @param uploadDir
     * @param fileName
     * @return
     * @throws IOException
     */
    private static final File getAbsoluteFile(String uploadDir, String fileName) throws IOException
    {
        File desc = new File(uploadDir + File.separator + fileName);

        if (!desc.exists())
        {
            if (!desc.getParentFile().exists())
            {
                desc.getParentFile().mkdirs();
            }
        }
        return desc;
    }

    private  String getPathFileName(String uploadDir, String fileName)
    {
        int dirLastIndex = address.length() + 1;
        String currentDir = StringUtils.substring(uploadDir, dirLastIndex);
        String pathFileName = Constants.RESOURCE_PREFIX + "/" + currentDir + "/" + fileName;
        return pathFileName;
    }


    /**
     * 文件大小校验
     *
     * @param file 上传的文件
     * @return
     * @throws FileSizeLimitExceededException 如果超出最大大小
     * @throws InvalidExtensionException
     */
    public static final void assertAllowed(MultipartFile file, String[] allowedExtension) throws  InvalidExtensionException {
        long size = file.getSize();
        if (DEFAULT_MAX_SIZE != -1 && size > DEFAULT_MAX_SIZE) {
            throw new FileNameLengthLimitExceededException(DEFAULT_MAX_SIZE / 1024 / 1024);
        }

        String fileName = file.getOriginalFilename();
        String extension = getExtension(file);
        if (allowedExtension != null && !isAllowedExtension(extension, allowedExtension))
        {
            if (allowedExtension == MimeTypeUtils.IMAGE_EXTENSION) {
                throw new InvalidExtensionException.InvalidImageExtensionException(allowedExtension, extension, fileName);
            } else if (allowedExtension == MimeTypeUtils.FLASH_EXTENSION) {
                throw new InvalidExtensionException.InvalidFlashExtensionException(allowedExtension, extension, fileName);
            } else if (allowedExtension == MimeTypeUtils.MEDIA_EXTENSION) {
                throw new InvalidExtensionException.InvalidMediaExtensionException(allowedExtension, extension, fileName);
            } else if (allowedExtension == MimeTypeUtils.VIDEO_EXTENSION) {
                throw new InvalidExtensionException(allowedExtension, extension, fileName);
            } else {
                throw new InvalidExtensionException(allowedExtension, extension, fileName);
            }
        }
    }

    /**
     * 判断MIME类型是否是允许的MIME类型
     *
     * @param extension
     * @param allowedExtension
     * @return
     */
    public static final boolean isAllowedExtension(String extension, String[] allowedExtension)
    {
        for (String str : allowedExtension)
        {
            if (str.equalsIgnoreCase(extension))
            {
                return true;
            }
        }
        return false;
    }

    /**
     * 获取文件名的后缀
     *
     * @param file 表单文件
     * @return 后缀名
     */
    public static final String getExtension(MultipartFile file)
    {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        if (StringUtils.isEmpty(extension))
        {
            extension = MimeTypeUtils.getExtension(file.getContentType());
        }
        return extension;
    }


    @Override
    public Stream<Path> loadAll() {
        return null;
    }

    @Override
    public void load(String fileKey) {
        try {
            HttpServletResponse response = ServletUtils.getResponse();
            SysFile sysFile = sysFileService.selectSysFileByKey(fileKey);
            if (ObjectUtil.isEmpty(sysFile)){
                throw new BusinessException("该文件不存在");
            }
            response.setCharacterEncoding("utf-8");
            response.setContentType("multipart/form-data");
            response.setHeader("Content-Disposition",
                    "attachment;fileName=" + sysFile.getName());
            //替换映射的url
            String url=sysFile.getUrl();
            StringBuilder stringBuilderUrl=new StringBuilder(url);
            //http://localhost/profile/upload/2021/03/23/2fa432e166334f158f7547200b1bf6c7.jpg
            String str1=url.substring(0, stringBuilderUrl.indexOf("/upload"));
            StringBuilder replace = stringBuilderUrl.replace(0, str1.length(), address);
            FileUtils.writeBytes(replace.toString(), response.getOutputStream());
        }catch (Exception e){
            e.getStackTrace();
        }

    }

    @Override
    public Resource loadAsResource(String keyName) {
        return null;
    }

    @Override
    public void delete(String keyName) {

    }
}
