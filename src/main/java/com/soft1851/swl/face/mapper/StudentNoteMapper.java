package com.soft1851.swl.face.mapper;

import com.soft1851.swl.face.entity.StudentNote;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/11
 */
public interface StudentNoteMapper extends Mapper<StudentNote> {

    @Insert("INSERT INTO student_note VALUES (#{studentId},#{noteId})")
    void insertOne(StudentNote studentNote);

    @Select("SELECT student_id FROM student_note WHERE note_id=#{noteId}")
    String queryStudentIdByNoteId(String noteId);

    @Select("SELECT * FROM student_note WHERE student_id=#{studentId}")
    List<StudentNote> queryByStudentId(String studentId);
}
