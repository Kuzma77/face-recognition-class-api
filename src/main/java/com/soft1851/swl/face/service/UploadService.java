package com.soft1851.swl.face.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/13
 */
public interface UploadService {

    /**
     * fdfs上传
     *
     * @param file 文件
     * @param fileExtName 扩展名
     * @return
     * @throws Exception 异常
     */
    String uploadFdfs(MultipartFile file, String userId, String fileExtName) throws Exception;

    /**
     * OSS上传文件
     *
     * @param file 文件
     * @param userId 用户id
     * @param fileExtName 扩展名
     * @return 返回
     * @throws Exception 异常
     */
    String uploadOSS(MultipartFile file,String userId,String fileExtName) throws Exception;
}
