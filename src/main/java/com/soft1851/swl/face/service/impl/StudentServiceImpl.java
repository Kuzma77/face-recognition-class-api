package com.soft1851.swl.face.service.impl;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.common.ResultCode;
import com.soft1851.swl.face.dto.JwtTokenRespDto;
import com.soft1851.swl.face.dto.LoginDto;
import com.soft1851.swl.face.dto.LoginResDto;
import com.soft1851.swl.face.dto.StudentDto;
import com.soft1851.swl.face.entity.Student;
import com.soft1851.swl.face.exception.CustomException;
import com.soft1851.swl.face.mapper.StudentMapper;
import com.soft1851.swl.face.service.RedisService;
import com.soft1851.swl.face.service.StudentService;
import com.soft1851.swl.face.util.JwtTokenUtil;
import com.soft1851.swl.face.util.Md5Util;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/7
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentServiceImpl implements StudentService {

    public final StudentMapper studentMapper;
    public final JwtTokenUtil jwtTokenUtil;
    public  final RedisService redisService;


    @Override
    public List<Student> queryAllStudent() {
        return this.studentMapper.selectAll();
    }

    @Override
    public ResponseResult loginByAccount(LoginDto loginDto) {
        Student student = this.studentMapper.getStudentByAccount(loginDto.getAccount());
        System.out.println(student);
        if(student!=null){
            String pass = Md5Util.getMd5(loginDto.getPassword(),true,32);
            if(student.getPassword().equals(pass)){
                Map<String,Object> studentInfo = new HashMap<>(3);
                studentInfo.put("id",student.getStudentId());
                studentInfo.put("name",student.getStudentName());
                studentInfo.put("password",student.getPassword());
                String token = jwtTokenUtil.generateToken(studentInfo);
                log.info(
                        "{}登录成功，生成的token = {},有效期到:{}",
                        student.getStudentName(),
                        token,
                        jwtTokenUtil.getExpirationTime()
                );
                LoginResDto loginResDto =  LoginResDto.builder().studentDto(StudentDto.builder()
                        .studentId(student.getStudentId())
                        .studentName(student.getStudentName())
                        .account(student.getAccount())
                        .avatar(student.getAvatar())
                        .classroom(student.getClassroom())
                        .gender(student.getGender())
                        .phoneNumber(student.getPhoneNumber())
                        .build())
                        .token(JwtTokenRespDto
                                .builder()
                                .token(token)
                                .expirationTime(jwtTokenUtil.getExpirationTime().getTime())
                                .build())
                        .build();
//                将token添加到redis
                redisService.set("token",token);
                return ResponseResult.success(loginResDto);
            }else {
                log.error("密码错误");
                return ResponseResult.failure(ResultCode.USER_PASSWORD_ERROR);
            }
        }else {
            log.error("账号不存在");
            return ResponseResult.failure(ResultCode.USER_NOT_FOUND);
        }
    }
}
