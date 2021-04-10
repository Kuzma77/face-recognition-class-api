package com.soft1851.swl.face.mapper;

import com.soft1851.swl.face.entity.Student;
import com.soft1851.swl.face.entity.Teacher;
import org.apache.ibatis.annotations.Select;
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
}
