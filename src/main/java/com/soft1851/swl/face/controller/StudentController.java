package com.soft1851.swl.face.controller;

import com.soft1851.swl.face.annocation.ControllerWebLog;
import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.LoginDto;
import com.soft1851.swl.face.service.StudentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/5
 */

@RestController
@Slf4j
@RequestMapping("/student")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "用户接口",value = "提供用户相关的Rest API")
public class StudentController {


    public final StudentService studentService;

    @GetMapping("/all")
    @ControllerWebLog
    @ApiOperation(value = "查询所有学生信息",notes = "查询所有学生信息")
    public ResponseResult test(){
        return ResponseResult.success(this.studentService.queryAllStudent());
    }

    @PostMapping("/login")
    @ControllerWebLog
    @ApiOperation(value = "账密登录",notes = "账密登录")
    public ResponseResult login(@RequestBody @Valid LoginDto loginDto){
        return this.studentService.loginByAccount(loginDto);
    }

    @PostMapping("/layout")
    @ControllerWebLog
    @ApiOperation(value = "退出登录",notes = "退出登录")
    public ResponseResult login(@RequestParam @Valid String userId){
        return studentService.layout(userId);
    }
}
