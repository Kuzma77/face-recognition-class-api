package com.soft1851.swl.face.mapper;

import com.soft1851.swl.face.dto.SubjectDto;
import com.soft1851.swl.face.entity.Subject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;
import tk.mybatis.mapper.common.Mapper;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/10
 */
public interface SubjectMapper extends Mapper<Subject> {

    /**
     * 新增课程
     *
     * @param subject
     */
    @Insert("INSERT INTO subject VALUES (#{subjectId},#{subjectName},#{signFlag},#{signTime},#{updateTime},#{beginTime},#{endTime},#{createTime},#{deleteFlag})")
    void addSubject(Subject subject);

    /**
     * 修改课程基础信息
     *
     * @param subject subjectId
     */
    @Update("UPDATE subject SET subject_name=#{subject.subjectName},begin_time=#{subject.beginTime},end_time=#{subject.endTime},update_time=#{subject.updateTime} WHERE subject_id=#{subjectId}")
    void updateSubject(Subject subject,String subjectId);
}
