package com.soft1851.swl.face.controller;

import com.soft1851.swl.face.annocation.ControllerWebLog;
import com.soft1851.swl.face.common.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/13
 */

@RestController
@Slf4j
@RequestMapping("/fs")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "文件相关上传接口",value = "提供文件相关上传接口相关的Rest API")
public class FileUploadController {



}
