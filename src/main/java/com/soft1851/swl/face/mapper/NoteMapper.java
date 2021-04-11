package com.soft1851.swl.face.mapper;

import com.soft1851.swl.face.entity.Note;
import com.soft1851.swl.face.entity.Subject;
import org.apache.ibatis.annotations.Insert;
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
}
