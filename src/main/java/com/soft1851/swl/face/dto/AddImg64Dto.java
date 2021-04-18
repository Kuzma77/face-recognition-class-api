package com.soft1851.swl.face.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/17
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddImg64Dto {

    private String id;

    private String faceId;

    private String img64;

    private Integer isDelete;
}
