package com.soft1851.swl.face.service;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.AddImg64Dto;
import com.soft1851.swl.face.mo.FaceImg64;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/17
 */
public interface FaceImg64Service {

    /**
     *
     *
     * @param faceImg64
     */
    ResponseResult saveOrUpdateFaceImg64(FaceImg64 faceImg64);
}
