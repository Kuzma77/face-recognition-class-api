package com.soft1851.swl.face.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

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

    @NotNull(message = "account 不能为空")
    private String account;
    @NotNull(message = "password 不能为空")
    private String password;
}
