package com.soft1851.swl.face.service;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.LoginDto;
import com.soft1851.swl.face.dto.LoginResDto;
import com.soft1851.swl.face.entity.Student;

import java.util.List;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/7
 */
public interface StudentService {

    /**
     * 查询所有学生信息
     *
     * @return
     */
    List<Student> queryAllStudent();


    /**
     * 账密登录
     *
     * @param loginDto
     * @return
     */
    ResponseResult loginByStudentId(LoginDto loginDto);


    /**
     * 退出登录
     *
     * @param userId
     * @return
     */
    ResponseResult layout(String userId);
}
