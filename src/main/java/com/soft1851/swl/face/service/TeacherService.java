package com.soft1851.swl.face.service;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.LoginDto;
import com.soft1851.swl.face.entity.Teacher;
import org.apache.ibatis.annotations.Select;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/10
 */
public interface TeacherService {



    /**
     * 通过教工号查询教师
     *
     * @param teacherId
     * @return
     */
    @Select("SELECT * FROM teacher WHERE teacher_id = #{teacherId}")
    ResponseResult getTeacherById(String teacherId);


    /**
     * 账密登录
     *
     * @param loginDto
     * @return
     */
    ResponseResult loginByTeacherId(LoginDto loginDto);
}
