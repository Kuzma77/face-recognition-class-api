package com.soft1851.swl.face.mapper;

import com.soft1851.swl.face.dto.LoginDto;
import com.soft1851.swl.face.dto.LoginResDto;
import com.soft1851.swl.face.entity.Student;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/7
 */
public interface StudentMapper extends Mapper<Student> {






    /**
     * 通过账号查询学生
     *
     * @param account
     * @return
     */
    @Select("SELECT * FROM student WHERE account = #{account}")
    Student getStudentByAccount(String account);
}
