package com.soft1851.swl.face;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;


/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/5
 */

@SpringBootApplication
@MapperScan("com.soft1851.swl.face.mapper")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
