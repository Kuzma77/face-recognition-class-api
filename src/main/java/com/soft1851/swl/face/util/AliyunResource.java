package com.soft1851.swl.face.util;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/13
 */
@Component
@PropertySource("classpath:aliyun.properties")
@ConfigurationProperties(prefix = "aliyun")
public class AliyunResource {
    private String accessKeyId;
    private String accessKeySecret;

    public String getAccessKeyId(){
        return accessKeyId;
    }

    public void setAccessKeyId(String accessKeyId){
        this.accessKeyId = accessKeyId;
    }

    public String getAccessKeySecret(){
        return accessKeySecret;
    }

    public void setAccessKeySecret(String accessKeySecret){
        this.accessKeySecret = accessKeySecret;
    }

}