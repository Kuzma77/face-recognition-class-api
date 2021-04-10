package com.soft1851.swl.face.dto;

import com.soft1851.swl.face.entity.Student;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResDto {
    private UserDto userDto;
    private JwtTokenRespDto token;
}
