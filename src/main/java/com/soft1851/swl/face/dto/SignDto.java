package com.soft1851.swl.face.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wl_sun
 * @description 签到
 * @Data 2021/4/21
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignDto {
    private SearchFaceDto searchFaceDto;
    private String subjectId;
}
