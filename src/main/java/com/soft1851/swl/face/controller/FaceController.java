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
    private final OwnerFaceMapper ownerFaceMapper;
    private final  RestTemplate restTemplate;
    private final FaceVerifyUtil faceVerifyUtil;

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
    @PostMapping("/adminLogin")
    @ControllerWebLog
    public ResponseResult faceLogin(@RequestBody FaceLoginDto faceLoginDto,
                           HttpServletRequest request,
                           HttpServletResponse response){
        //0、判断用户名和人脸信息不能为空
        if(StringUtils.isBlank(faceLoginDto.getId())){
            return ResponseResult.failure(ResultCode.USER_NOT_FOUND);
        }
        String tempFace64 = faceLoginDto.getImg64();
        if(StringUtils.isBlank(tempFace64)){
            return ResponseResult.failure(ResultCode.USER_FACE_NULL_ERROR);
        }
        //1、从数据库中根据id查询出faceId
        String faceId = null;
        OwnerFace ownerFace = this.ownerFaceMapper.queryFaceIdByOwnerId(faceLoginDto.getId());
        if(ownerFace!=null){
            faceId = ownerFace.getFaceId();
            System.out.println(faceId);
        }else {
            return ResponseResult.failure(ResultCode.FACE_NOT_SAVE_ERROR);
        }
        //2.请求文件服务，根据faceId获得人脸数据的base64数据
        String fileServerUrl = "http://localhost:8888/fs/readFace64?faceId="+faceId;
        //得到的是封装的结果
        ResponseEntity<ResponseResult> resultResponseEntity = restTemplate.getForEntity(fileServerUrl,ResponseResult.class);
        ResponseResult bodyResult = resultResponseEntity.getBody();
        System.out.println(bodyResult);
        assert bodyResult != null;
        String base64 = (String) bodyResult.getData();
        //3、调用阿里ai进行人脸对比失败，判断可信度，从而实现人脸登录
        boolean result = faceVerifyUtil.faceVerify(FaceVerifyType.BASE64.type,tempFace64,base64,60);
        if(!result){
            return ResponseResult.failure(ResultCode.USER_FACE_LOGIN_ERROR);
        }
        return ResponseResult.success();
    }
}
