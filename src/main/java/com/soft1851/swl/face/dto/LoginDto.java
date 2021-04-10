package com.soft1851.swl.face.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    @NotNull(message = "学号或者教工号不能为空")
    private String id;

    @NotNull(message = "password 不能为空")
    @Size(min = 6,message = "密码不能小于6位")
    private String password;
}
