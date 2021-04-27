package com.soft1851.swl.face.controller;

import com.soft1851.swl.face.annocation.ControllerWebLog;
import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.service.BaseService;
import com.soft1851.swl.face.util.AliOssUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/10
 */

@RestController
@Slf4j
@RequestMapping("/base")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "公共接口",value = "提供公共接口相关的Rest API")
public class BaseController {

    public  final BaseService baseService;

    @PostMapping("/layout")
    @ControllerWebLog
    @ApiOperation(value = "退出登录",notes = "退出登录")
    public ResponseResult login(@RequestParam @Valid String userId){
        return baseService.layout(userId);
    }

    @PostMapping("/uploadFile")
    @ControllerWebLog
    @ApiOperation(value = "上传文件到OSS",notes = "上传文件到OSS")
    public ResponseResult uploadSingle(@RequestParam MultipartFile file) {
        String url = AliOssUtil.upload(file);
        return ResponseResult.success(url);
    }
}
