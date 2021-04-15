package com.soft1851.swl.face.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/15
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OwnerFaceDto {
    private String ownerId;
    private String faceId;
}
