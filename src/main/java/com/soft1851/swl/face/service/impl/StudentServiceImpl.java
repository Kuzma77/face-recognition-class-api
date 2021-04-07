package com.soft1851.swl.face.service.impl;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.common.ResultCode;
import com.soft1851.swl.face.dto.LoginDto;
import com.soft1851.swl.face.dto.LoginResDto;
import com.soft1851.swl.face.entity.Student;
import com.soft1851.swl.face.exception.CustomException;
import com.soft1851.swl.face.mapper.StudentMapper;
import com.soft1851.swl.face.service.StudentService;
import com.soft1851.swl.face.util.Md5Util;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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

    @Override
    public List<Student> queryAllStudent() {
        return this.studentMapper.selectAll();
    }

    @Override
    public ResponseResult loginByAccount(LoginDto loginDto) {
        Student student = this.studentMapper.getStudentByAccount(loginDto.getAccount());
        if(student!=null){
            String pass = Md5Util.getMd5(loginDto.getPassword(),true,32);
            if(student.getPassword().equals(pass)){
                return ResponseResult.success(LoginResDto.builder().studentId(student.getStudentId())
                        .studentName(student.getStudentName())
                        .avatar(student.getAvatar())
                        .classroom(student.getClassroom())
                        .gender(student.getGender())
                        .phoneNumber(student.getPhoneNumber())
                        .build());
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
