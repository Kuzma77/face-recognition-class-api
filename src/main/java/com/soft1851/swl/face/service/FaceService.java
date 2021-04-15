package com.soft1851.swl.face.service;

import com.soft1851.swl.face.common.ResponseResult;
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
}