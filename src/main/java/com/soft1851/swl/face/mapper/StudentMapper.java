package com.soft1851.swl.face.mapper;


import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.entity.Student;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/7
 */
public interface StudentMapper extends Mapper<Student> {
    /**
     * 通过学号查询学生
     *
     * @param studentId
     * @return
     */
    @Select("SELECT * FROM student WHERE student_id = #{studentId}")
    Student getStudentById(String studentId);

    /**
     * 更改账号状态
     *
     * @param deleteFlag
     * @param studentId
     */
    @Update("UPDATE student SET delete_flag=#{deleteFlag} WHERE student_id=#{studentId}")
    void updateStatus(Integer deleteFlag, String studentId);


    /**
     * 通过id更改密码
     *
     * @param password
     * @param studentId
     */
    @Update("UPDATE student SET password=#{password} WHERE student_id=#{studentId}")
    void updatePassword(String password, String studentId);


    /**
     * 修改学生头像
     * @param studentId
     * @param imgUrl
     */
    @Update("UPDATE student SET avatar=#{imgUrl} WHERE student_id=#{studentId}")
    void updateAvatar(String studentId,String imgUrl);
}
