package com.soft1851.swl.face.util;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyun.oss.models.PutObjectResponse;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/10
 */
@Slf4j
public class AliOssUtil {
    public static String upload(MultipartFile sourceFile){
        // 获取文件名
        String fileName = sourceFile.getOriginalFilename();
        //uuid生成主文件名
        String prefix = UUID.randomUUID().toString();
        assert fileName != null;
        //源文件的扩展名
        String suffix = fileName.substring(fileName.lastIndexOf("."));
        //创建File类型的临时文件
        File tempFile = null;
        try {
            tempFile = File.createTempFile(prefix,suffix);
            //将MultipartFile转换成File
            sourceFile.transferTo(tempFile);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
        assert tempFile != null;
        return upload(tempFile);
    }
    public static String upload(File file){
        String endpoint = "https://oss-cn-shanghai.aliyuncs.com";
        String accessKeyId = "LTAI4G3RbEnYyXpd2ez6jbo4";
        String accessKeySecret = "6vVUWNdOhUunRQCLyrjszgKwVZMjkv";
        String bucketName = "face-manage-kuzma";
        String filePath = "faces1/";
        String fileName = file.getName();
        String newFileName = UUID.randomUUID().toString() + fileName.substring(fileName.indexOf("."));
        //创建OSSClient实例
        OSS ossClient  = new OSSClientBuilder().build(endpoint,accessKeyId,accessKeySecret);
        //上传文件到指定位置，并使用UUID更名
        PutObjectResult response  = ossClient.putObject(bucketName,filePath + newFileName,file);
//        Map<String,String> map = JsonUtil.jsonToPojo(new Gson().toJson(response), Map.class);
//        assert map != null;
//        String versionId = map.get("versionId");
        //拼接URL
        String url = "https://face-manage-kuzma.oss-cn-shanghai.aliyuncs.com/" + filePath + newFileName;
        ossClient.shutdown();
        return url;
    }
    public static void main(String[] args) {
        File file = new File("C:\\Users\\HP\\Pictures\\Saved Pictures\\短发1.jpg");
        upload(file);
    }
}
