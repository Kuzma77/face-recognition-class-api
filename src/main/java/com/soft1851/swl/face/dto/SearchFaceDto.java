package com.soft1851.swl.face.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/21
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SearchFaceDto {

    private String DbName;
    private String DbNames;
    private String imgUrl;
    private Integer limit;
}
