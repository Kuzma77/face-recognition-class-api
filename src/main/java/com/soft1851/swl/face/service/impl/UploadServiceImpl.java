package com.soft1851.swl.face.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.soft1851.swl.face.service.UploadService;
import com.soft1851.swl.face.util.AliyunResource;
import com.soft1851.swl.face.util.FileResource;
import com.soft1851.swl.face.util.Md5Util;
import com.soft1851.swl.face.util.RandomNumUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2020/11/19
 */
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UploadServiceImpl implements UploadService {

    public final FastFileStorageClient fastFileStorageClient;

    public final FileResource fileResource;

    public final AliyunResource aliyunResource;



    @Override
    public String uploadFdfs(MultipartFile file,String userId, String fileExtName) throws Exception {
        InputStream inputStream = file.getInputStream();
        StorePath storePath = fastFileStorageClient.uploadFile(inputStream,
                file.getSize(),
                fileExtName,
                null);
        inputStream.close();
        return storePath.getFullPath();
    }

    @Override
    public String uploadOSS(MultipartFile file, String userId, String fileExtName) throws Exception {
        //Endpoint以北京为例，其它的根据实际情况填写
        String endpoint = fileResource.getEndpoint();
        String accessKeyId = aliyunResource.getAccessKeyId();
        String accessKeySecret = aliyunResource.getAccessKeySecret();

        //创建OSSClient实例
        OSS ossClient = new OSSClientBuilder().build(endpoint,
                accessKeyId,
                accessKeySecret);

        String fileName = RandomNumUtil.getVerifyCode(8);
        String myObjectName = fileResource.getObjectName() + fileName + "." + fileExtName;
        //上传网络流
        InputStream inputStream = file.getInputStream();
        ossClient.putObject(fileResource.getBucketName(),
                myObjectName,
                inputStream);
        //关闭OSSClient
        ossClient.shutdown();
        return myObjectName;
    }
}