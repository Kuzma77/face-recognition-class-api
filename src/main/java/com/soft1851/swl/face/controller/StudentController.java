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
import javax.validation.constraints.Size;

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
@Api(tags = "学生接口",value = "提供学生相关的Rest API")
public class StudentController {


    public final StudentService studentService;

    @GetMapping("/all")
    @ControllerWebLog
    @ApiOperation(value = "查询所有学生信息",notes = "查询所有学生信息")
    public ResponseResult test(){
        return ResponseResult.success(this.studentService.queryAllStudent());
    }


    @GetMapping("/one")
    @ControllerWebLog
    @ApiOperation(value = "通过id查询学生信息",notes = "通过id所有学生信息")
    public ResponseResult getStudentById (@RequestParam String studentId){
        return ResponseResult.success(this.studentService.getStudentById(studentId));
    }

    @PostMapping("/login")
    @ControllerWebLog
    @ApiOperation(value = "学生账密登录",notes = "学生账密登录")
    public ResponseResult login(@RequestBody @Valid LoginDto loginDto){
        return this.studentService.loginByStudentId(loginDto);
    }

    @PostMapping("/updateStatus")
    @ControllerWebLog
    @ApiOperation(value = "修改学生账号状态",notes = "修改学生账号状态")
    public ResponseResult updateStatus(@RequestParam String studentId){
        return this.studentService.updateStatus(studentId);
    }

    @PostMapping("/updatePassword")
    @ControllerWebLog
    @ApiOperation(value = "修改学生密码",notes = "修改学生密码")
    public ResponseResult updatePassword (@RequestParam @Size(min = 6,message = "密码不能小于6位") String password, @RequestParam String studentId){
        return this.studentService.updatePassword(password, studentId);
    }

}
