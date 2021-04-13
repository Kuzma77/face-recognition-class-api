package com.soft1851.swl.face.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/13
 */
@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@PropertySource("classpath:file.properties")
@ConfigurationProperties(prefix = "file")
public class FileResource {
    private String host;
    private String endpoint;
    private String bucketName;
    private String ossHost;
    private String objectName;
}
