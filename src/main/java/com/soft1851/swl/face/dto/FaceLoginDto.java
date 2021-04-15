package com.soft1851.swl.face.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/15
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FaceLoginDto {

    private String id;
    private String img64;
}
