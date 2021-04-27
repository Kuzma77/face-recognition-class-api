package com.soft1851.swl.face.mapper;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.entity.Student;
import com.soft1851.swl.face.entity.Teacher;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;


/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/10
 */
public interface TeacherMapper extends Mapper<Teacher> {

    /**
     * 通过教工号查询教师
     *
     * @param teacherId
     * @return
     */
    @Select("SELECT * FROM teacher WHERE teacher_id = #{teacherId}")
    Teacher getTeacherById(String teacherId);

    /**
     * 更改账号状态
     *
     * @param deleteFlag
     * @param teacherId
     */
    @Update("UPDATE teacher SET delete_flag=#{deleteFlag} WHERE teacher_id=#{teacherId}")
    void updateStatus(Integer deleteFlag, String teacherId);


    /**
     * 通过id更改密码
     *
     * @param password
     * @param teacherId
     */
    @Update("UPDATE teacher SET password=#{password} WHERE teacher_id=#{teacherId}")
    void updatePassword(String password, String teacherId);

    /**
     * 修改教师头像
     * @param teacherId
     * @param imgUrl
     */
    @Update("UPDATE teacher SET avatar=#{imgUrl} WHERE teacher_id=#{teacherId}")
    void updateAvatar(String teacherId,String imgUrl);
}
