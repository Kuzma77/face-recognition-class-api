package com.soft1851.swl.face.controller;

import com.soft1851.swl.face.annocation.ControllerWebLog;
import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.LoginDto;
import com.soft1851.swl.face.service.TeacherService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Size;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/10
 */

@RestController
@Slf4j
@RequestMapping("/teacher")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "教师接口",value = "提供教师相关的Rest API")
public class TeacherController {

    public final TeacherService teacherService;


    @GetMapping("/one")
    @ControllerWebLog
    @ApiOperation(value = "通过id查询教师信息",notes = "通过id所有教师信息")
    public ResponseResult getTeacherById (@RequestParam String teacherId){
        return ResponseResult.success(this.teacherService.getTeacherById(teacherId));
    }

    @PostMapping("/login")
    @ControllerWebLog
    @ApiOperation(value = "教师账密登录",notes = "教师账密登录")
    public ResponseResult login(@RequestBody @Valid LoginDto loginDto){
        return this.teacherService.loginByTeacherId(loginDto);
    }

    @PostMapping("/updateStatus")
    @ControllerWebLog
    @ApiOperation(value = "修改教师账号状态",notes = "修改教师账号状态")
    public ResponseResult updateStatus(@RequestParam String teacherId){
        return this.teacherService.updateStatus(teacherId);
    }

    @PostMapping("/updatePassword")
    @ControllerWebLog
    @ApiOperation(value = "修改教师密码",notes = "修改教师密码")
    public ResponseResult updatePassword (@Valid @RequestParam @Size(min = 6,message = "密码不能小于6位") String password, @RequestParam String teacherId){
        return this.teacherService.updatePassword(password, teacherId);
    }

}
