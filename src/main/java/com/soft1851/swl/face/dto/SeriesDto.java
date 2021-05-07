package com.soft1851.swl.face.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/5/8
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SeriesDto {
    private String color;
    private String name;
    private Double data;
}
