package com.soft1851.swl.face.mapper;


import com.soft1851.swl.face.entity.StudentSubject;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/21
 */
public interface StudentSubjectMapper extends Mapper<StudentSubject> {


    /**
     * 更新到勤状态
     *
     * @param studentSubject
     */
    @Update("UPDATE student_subject SET attend_flag=#{attendFlag} WHERE student_id=#{studentId} AND subject_id=#{subjectId}")
    void updateAttendStatue(StudentSubject studentSubject);


    /***
     * 通过学生id和课程id查询该学生是否选了该课
     * @param studentId
     * @param subjectId
     * @return
     */
    @Select("SELECT * FROM student_subject WHERE student_id=#{studentId} AND subject_id=#{subjectId}")
    StudentSubject queryByStudentIdAndSubjectId(String studentId,String subjectId);
}
