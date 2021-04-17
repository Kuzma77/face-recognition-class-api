package com.soft1851.swl.face.controller;

import com.soft1851.swl.face.annocation.ControllerWebLog;
import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.LoginDto;
import com.soft1851.swl.face.service.AdminService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/17
 */

@RestController
@Slf4j
@RequestMapping("/admin")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "管理员接口",value = "提供管理员相关的Rest API")
public class AdminController {

    public final AdminService adminService;

    @PostMapping("/login")
    @ControllerWebLog
    @ApiOperation(value = "管理员账密登录",notes = "管理员账密登录")
    public ResponseResult login(@RequestBody @Valid LoginDto loginDto){
        return this.adminService.loginByAdminId(loginDto);
    }
}
