package com.soft1851.swl.face.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/7
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class JwtTokenRespDto {

    /**
     * token
     */
    private String token;

    /**
     * 过期时间
     */
    private Long expirationTime;
}
