package com.soft1851.swl.face.service.impl;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.common.ResultCode;
import com.soft1851.swl.face.dto.JwtTokenRespDto;
import com.soft1851.swl.face.dto.LoginDto;
import com.soft1851.swl.face.dto.LoginResDto;
import com.soft1851.swl.face.dto.UserDto;
import com.soft1851.swl.face.entity.Student;
import com.soft1851.swl.face.entity.Teacher;
import com.soft1851.swl.face.enums.LogType;
import com.soft1851.swl.face.mapper.TeacherMapper;
import com.soft1851.swl.face.mo.Log;
import com.soft1851.swl.face.service.LogService;
import com.soft1851.swl.face.service.RedisService;
import com.soft1851.swl.face.service.TeacherService;
import com.soft1851.swl.face.util.JwtTokenUtil;
import com.soft1851.swl.face.util.Md5Util;
import com.soft1851.swl.face.util.RandomNumUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/10
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TeacherServiceImpl implements TeacherService {


    public final TeacherMapper teacherMapper;
    public final JwtTokenUtil jwtTokenUtil;
    public  final RedisService redisService;
    public final LogService logService;

    @Override
    public ResponseResult getTeacherById(String teacherId) {
        Teacher teacher = this.teacherMapper.getTeacherById(teacherId);
        if(teacher!=null){
            UserDto userDto = UserDto.builder()
                    .id(teacherId)
                    .name(teacher.getTeacherName())
                    .avatar(teacher.getAvatar())
                    .gender(teacher.getGender())
                    .phoneNumber(teacher.getPhoneNumber())
                    .build();
            return ResponseResult.success(userDto);
        }else {
            return ResponseResult.failure(ResultCode.USER_NOT_FOUND);
        }
    }

    @Override
    public ResponseResult loginByTeacherId(LoginDto loginDto) {
        Teacher teacher = this.teacherMapper.selectByPrimaryKey(loginDto.getId());
        System.out.println(teacher);
        if(teacher!=null){
            String pass = Md5Util.getMd5(loginDto.getPassword(),true,32);
            if(teacher.getPassword().equals(pass)){
                Map<String,Object> teacherInfo = new HashMap<>(3);
                teacherInfo.put("id",teacher.getTeacherId());
                teacherInfo.put("name",teacher.getTeacherName());
                teacherInfo.put("password",teacher.getPassword());
                String token = jwtTokenUtil.generateToken(teacherInfo);
                log.info(
                        "{}登录成功，生成的token = {},有效期到:{}",
                        teacher.getTeacherName(),
                        token,
                        jwtTokenUtil.getExpirationTime()
                );
                LoginResDto loginResDto =  LoginResDto.builder().userDto(UserDto.builder()
                        .id(teacher.getTeacherId())
                        .name(teacher.getTeacherName())
                        .avatar(teacher.getAvatar())
                        .gender(teacher.getGender())
                        .phoneNumber(teacher.getPhoneNumber())
                        .build())
                        .token(JwtTokenRespDto
                                .builder()
                                .token(token)
                                .expirationTime(jwtTokenUtil.getExpirationTime().getTime())
                                .build())
                        .role("teacher")
                        .build();
                //                将token添加到redis
                redisService.set("USER_TOKEN:"+teacher.getTeacherId(),token);
                Log log = Log.builder()
                        .id(RandomNumUtil.getVerifyCode(8))
                        .type(LogType.LOGIN.value)
                        .operatorId(loginDto.getId())
                        .objectId(loginDto.getId())
                        .content("账密登录成功")
                        .createTime(new Date())
                        .build();
                this.logService.saveOneLog(log);
                return ResponseResult.success(loginResDto);
            }else {
                log.error("密码错误");
                Log log = Log.builder()
                        .id(RandomNumUtil.getVerifyCode(8))
                        .type(LogType.LOGIN.value)
                        .operatorId(loginDto.getId())
                        .objectId(loginDto.getId())
                        .content("账密登录失败")
                        .createTime(new Date())
                        .build();
                this.logService.saveOneLog(log);
                return ResponseResult.failure(ResultCode.USER_PASSWORD_ERROR);
            }
        }else {
            log.error("该教师账号不存在");
            Log log = Log.builder()
                    .id(RandomNumUtil.getVerifyCode(8))
                    .type(LogType.LOGIN.value)
                    .operatorId(loginDto.getId())
                    .objectId(loginDto.getId())
                    .content("账密登录失败")
                    .createTime(new Date())
                    .build();
            this.logService.saveOneLog(log);
            return ResponseResult.failure(ResultCode.USER_NOT_FOUND);
        }
    }

    @Override
    public ResponseResult updateStatus(String teacherId) {
        Teacher teacher = this.teacherMapper.selectByPrimaryKey(teacherId);
        if(teacher!=null){
            Integer deleteFlag = teacher.getDeleteFlag();
            Integer newInteger = deleteFlag == 0 ? 1:0;
            this.teacherMapper.updateStatus(newInteger,teacherId);
            log.info("教师{}的状态修改成功",teacherId);
            Log log = Log.builder()
                    .id(RandomNumUtil.getVerifyCode(8))
                    .type(LogType.LOGIN.value)
                    .operatorId("001")
                    .objectId(teacherId)
                    .content("教师删除状态修改")
                    .createTime(new Date())
                    .build();
            this.logService.saveOneLog(log);
            return ResponseResult.success();
        }else{
            log.error("该教师账号不存在");
            return ResponseResult.failure(ResultCode.USER_NOT_FOUND);
        }
    }

    @Override
    public ResponseResult updatePassword(String password, String teacherId) {
        Teacher teacher = this.teacherMapper.selectByPrimaryKey(teacherId);
        if(teacher!=null){
            String newPass = Md5Util.getMd5(password,true,32);
            this.teacherMapper.updatePassword(newPass,teacherId);
            log.info("教师{}的密码修改成功",teacherId);
            Log log = Log.builder()
                    .id(RandomNumUtil.getVerifyCode(8))
                    .type(LogType.LOGIN.value)
                    .operatorId(teacherId)
                    .objectId(teacherId)
                    .content("修改密码成功")
                    .createTime(new Date())
                    .build();
            this.logService.saveOneLog(log);
            return ResponseResult.success();
        }else{
            log.error("该教师账号不存在");
            return ResponseResult.failure(ResultCode.USER_NOT_FOUND);
        }
    }
}
