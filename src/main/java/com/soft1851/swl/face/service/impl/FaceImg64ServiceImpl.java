package com.soft1851.swl.face.service.impl;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.mo.FaceImg64;
import com.soft1851.swl.face.repository.FaceImg64Repository;
import com.soft1851.swl.face.service.FaceImg64Service;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/17
 */

@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FaceImg64ServiceImpl implements FaceImg64Service {

    public  final FaceImg64Repository faceImg64Repository;
    @Override
    public ResponseResult saveOrUpdateFaceImg64(FaceImg64 faceImg64) {
        faceImg64Repository.save(faceImg64);
        return ResponseResult.success();
    }
}
