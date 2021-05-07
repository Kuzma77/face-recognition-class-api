package com.soft1851.swl.face.service;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.BetTimeDto;
import com.soft1851.swl.face.dto.SubjectDto;
import com.soft1851.swl.face.entity.Subject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

import java.util.Date;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/11
 */
public interface SubjectService {

    /**
     * 新增课程
     *
     * @param subjectDto
     * @param teacherId
     */
    ResponseResult addSubject(SubjectDto subjectDto,String teacherId);


    /**
     * 修改课程基础信息
     *
     * @param subjectDto subjectId
     * @return
     */
    ResponseResult updateSubject(SubjectDto subjectDto,String subjectId,String teacherId);


    /***
     * 修改课程签到状态
     *
     * @param subjectId
     * @return
     */
    ResponseResult updateSignStatue(String subjectId,String teacherId);

    /**
     * 签到时间延长
     *
     * @param addTime
     * @param subjectId
     */
    ResponseResult addSignTime(long addTime,String subjectId,String teacherId);

    /**
     * 更改课程状态
     *
     * @param subjectId
     * @return
     */
    ResponseResult updateStatus(String subjectId);


    /**
     * 通过时间段和学生id获取到这段时间内的课程
     *
     * @param betTimeDto
     * @return
     */
    ResponseResult querySubjectsBetweenTime(BetTimeDto betTimeDto);

    /**
     * 通过教工号查询所有课程
     *
     * @param teacherId
     * @return
     */
    ResponseResult querySubjectsByTeacherId(String teacherId);
}
