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
@PropertySource("classpath:baiduyun.properties")
@ConfigurationProperties(prefix = "baiduyun")
public class BaiDuYunResource {

    private String apiKey;
    private String secretKey;

    public String getApiKey(){
        return apiKey;
    }

    public void setApiKey(String apiKey){
        this.apiKey = apiKey;
    }

    public String getSecretKey(){
        return secretKey;
    }

    public void setSecretKey(String secretKey){
        this.secretKey = secretKey;
    }
}
