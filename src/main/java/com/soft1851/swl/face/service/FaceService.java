package com.soft1851.swl.face.service;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.AddFaceDto;
import com.soft1851.swl.face.dto.FaceLoginDto;
import com.soft1851.swl.face.dto.OwnerFaceDto;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/15
 */
public interface FaceService {


    /**
     * 用户人脸关系添加
     * @param ownerFaceDto
     */
    ResponseResult addFace(OwnerFaceDto ownerFaceDto);

    /**
     * 人脸识别登录
     * @param faceLoginDto
     * @return
     */
    ResponseResult faceLogin(FaceLoginDto faceLoginDto);

    /**
     * 添加人脸数据到阿里云平台
     *
     * @param addFaceDto
     * @return
     */
    ResponseResult addFaceData(AddFaceDto addFaceDto) throws Exception;
}
