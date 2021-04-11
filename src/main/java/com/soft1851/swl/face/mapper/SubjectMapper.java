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
     * @param subject
     * @param subjectId
     */
    @Update("UPDATE subject SET subject_name=#{subject.subjectName},begin_time=#{subject.beginTime},end_time=#{subject.endTime},update_time=#{subject.updateTime} WHERE subject_id=#{subjectId}")
    void updateSubject(Subject subject,String subjectId);

    /**
     * 修改课程签到状态
     *
     * @param signFlag
     * @param subjectId
     */
    @Update("UPDATE subject SET sign_flag=#{signFlag} WHERE subject_id=#{subjectId}")
    void updateSignStatue(Integer signFlag,String subjectId);

    /**
     * 签到时间延长
     *
     * @param newSignTime
     * @param subjectId
     */
    @Update("UPDATE subject SET sign_time=#{newSignTime} WHERE subject_id=#{subjectId}")
    void addSignTime(long newSignTime,String subjectId);

    /**
     * 更改课程状态
     *
     * @param deleteFlag
     * @param subjectId
     */
    @Update("UPDATE subject SET delete_flag=#{deleteFlag} WHERE subject_id=#{subjectId}")
    void updateStatus(Integer deleteFlag, String subjectId);
}
