package com.soft1851.swl.face.controller;

import com.soft1851.swl.face.annocation.ControllerWebLog;
import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.common.ResultCode;
import com.soft1851.swl.face.dto.FaceLoginDto;
import com.soft1851.swl.face.dto.OwnerFaceDto;
import com.soft1851.swl.face.entity.OwnerFace;
import com.soft1851.swl.face.enums.FaceVerifyType;
import com.soft1851.swl.face.mapper.OwnerFaceMapper;
import com.soft1851.swl.face.service.FaceService;
import com.soft1851.swl.face.util.FaceVerifyUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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


    private final FaceService faceService;


    @PostMapping("/add")
    @ControllerWebLog
    @ApiOperation(value = "添加人脸关系",notes = "添加人脸关系")
    public ResponseResult addFace(@Valid @RequestBody OwnerFaceDto ownerFaceDto){
        return this.faceService.addFace(ownerFaceDto);
    }

    /**
     * 人脸识别登录
     *
     * @param faceLoginDto 人脸识别登录BO类
     * @param request 请求
     * @param response 响应
     * @return GraceResult
     */
    @ApiOperation(value = "人脸识别登录",notes = "人脸识别登录",httpMethod = "POST")
    @PostMapping("/faceLogin")
    @ControllerWebLog
    public ResponseResult faceLogin(@RequestBody FaceLoginDto faceLoginDto,
                           HttpServletRequest request,
                           HttpServletResponse response){
        return this.faceService.faceLogin(faceLoginDto);
    }
}
