package com.soft1851.swl.face.service;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.LoginDto;
import com.soft1851.swl.face.dto.LoginResDto;
import com.soft1851.swl.face.entity.Student;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

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
     * 通过学号查询学生
     *
     * @param studentId
     * @return
     */
    ResponseResult getStudentById(String studentId);


    /**
     * 账密登录
     *
     * @param loginDto
     * @return
     */
    ResponseResult loginByStudentId(LoginDto loginDto);


    /**
     * 更改账号状态
     *
     * @param studentId
     * @return
     */
    ResponseResult updateStatus(String studentId);

    /**
     * 通过id修改密码
     *
     * @param password,studentId
     * @return
     */
    ResponseResult updatePassword(String password,String studentId);


}
