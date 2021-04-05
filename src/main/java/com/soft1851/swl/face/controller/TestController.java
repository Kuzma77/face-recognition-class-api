package com.soft1851.swl.face.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/5
 */

@RestController
@Slf4j
public class TestController {
    @GetMapping("/get")
    public String test(){
        return "hello swl";
    }
}
