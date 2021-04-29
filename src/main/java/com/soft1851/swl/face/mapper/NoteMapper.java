package com.soft1851.swl.face.mapper;

import com.soft1851.swl.face.dto.NoteDto;
import com.soft1851.swl.face.entity.Note;
import com.soft1851.swl.face.entity.Subject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/11
 */
public interface NoteMapper extends Mapper<Note> {

    /**
     * 新增假条
     * @param note
     */
    @Insert("INSERT INTO note VALUES (#{noteId},#{subjectId},#{subjectName},#{updateTime},#{createTime},#{reason},#{noteStatue},#{deleteFlag})")
    void addNote(Note note);


    /**
     * 修改假条基础信息
     *
     * @param note
     */
    @Update("UPDATE note SET subject_id=#{subjectId},subject_name=#{subjectName},update_time=#{updateTime},reason=#{reason} WHERE note_id=#{noteId}")
    void updateNote(Note note);

    /**
     * 修改假条审核状态
     *
     * @param noteStatue
     * @param noteId
     */
    @Update("UPDATE note SET note_statue=#{noteStatue} WHERE note_id=#{noteId}")
    void updateNoteStatue(Integer noteStatue,String noteId);

    /**
     * 更改假条状态
     *
     * @param deleteFlag
     * @param noteId
     */
    @Update("UPDATE note SET delete_flag=#{deleteFlag} WHERE note_id=#{noteId}")
    void updateStatus(Integer deleteFlag, String noteId);


}
