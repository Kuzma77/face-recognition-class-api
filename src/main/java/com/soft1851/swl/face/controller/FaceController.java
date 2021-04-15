package com.soft1851.swl.face.controller;

import com.soft1851.swl.face.annocation.ControllerWebLog;
import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.OwnerFaceDto;
import com.soft1851.swl.face.service.FaceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/15
 */

@RestController
@Slf4j
@RequestMapping("/face")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "人脸关系接口",value = "提供人脸关系接口相关的Rest API")
public class FaceController {


    public final FaceService faceService;

    @PostMapping("/add")
    @ControllerWebLog
    @ApiOperation(value = "添加人脸关系",notes = "添加人脸关系")
    public ResponseResult addFace(@Valid @RequestBody OwnerFaceDto ownerFaceDto){
        return this.faceService.addFace(ownerFaceDto);
    }
}
