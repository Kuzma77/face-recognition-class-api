package com.soft1851.swl.face.controller;

import com.soft1851.swl.face.annocation.ControllerWebLog;
import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.AddImg64Dto;
import com.soft1851.swl.face.dto.OwnerFaceDto;
import com.soft1851.swl.face.mo.FaceImg64;
import com.soft1851.swl.face.service.FaceImg64Service;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Date;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/17
 */

@RestController
@Slf4j
@RequestMapping("/faceImg64")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "人脸img64图片编码维护",value = "提供人脸img64图片编码维护接口相关的Rest API")
public class FaceImg64Controller {


    public final FaceImg64Service faceImg64Service;


    @PostMapping("/save")
    @ControllerWebLog
    @ApiOperation(value = "人脸img64图片编码保存或更新",notes = "人脸img64图片编码保存或更新")
    public ResponseResult addFace(@Valid @RequestBody AddImg64Dto addImg64Dto){
        FaceImg64 faceImg64 = new FaceImg64();
        BeanUtils.copyProperties(addImg64Dto,faceImg64);
        faceImg64.setCreateTime(new Date());
        faceImg64.setUpdateTime(new Date());
        faceImg64Service.saveOrUpdateFaceImg64(faceImg64);
        return ResponseResult.success(faceImg64);
    }

}
