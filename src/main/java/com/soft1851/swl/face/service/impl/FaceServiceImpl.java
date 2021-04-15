package com.soft1851.swl.face.service.impl;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.OwnerFaceDto;
import com.soft1851.swl.face.entity.OwnerFace;
import com.soft1851.swl.face.mapper.OwnerFaceMapper;
import com.soft1851.swl.face.service.FaceService;
import com.soft1851.swl.face.util.RandomNumUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/15
 */
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FaceServiceImpl implements FaceService {


    public  final OwnerFaceMapper ownerFaceMapper;

    @Override
    public ResponseResult addFace(OwnerFaceDto ownerFaceDto) {
        String id = RandomNumUtil.getVerifyCode(8);

        OwnerFace ownerFace = OwnerFace.builder()
                .id(id)
                .faceId(ownerFaceDto.getFaceId())
                .ownerId(ownerFaceDto.getOwnerId())
                .build();
        this.ownerFaceMapper.addOne(ownerFace);
        return ResponseResult.success(ownerFace);
    }
}
