package com.soft1851.swl.face.service.impl;

import com.soft1851.swl.face.dto.SearchFaceDto;
import com.soft1851.swl.face.service.FaceService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FaceServiceImplTest {

    private FaceService faceService;
    @Test
    void searchFace() throws Exception {
        SearchFaceDto searchFaceDto = SearchFaceDto.builder()
                .DbName("face")
                .imgUrl("https://face-manage-kuzma.oss-cn-shanghai.aliyuncs.com/faces1/20210421182134.jpg")
                .limit(1)
                .build();
          this.faceService.searchFace(searchFaceDto);
    }
}