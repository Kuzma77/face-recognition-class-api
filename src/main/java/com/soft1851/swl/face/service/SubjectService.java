package com.soft1851.swl.face.service;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.SubjectDto;
import com.soft1851.swl.face.entity.Subject;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

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
     */
    ResponseResult addSubject(SubjectDto subjectDto);


    /**
     * 修改课程基础信息
     *
     * @param subjectDto subjectId
     * @return
     */
    ResponseResult updateSubject(SubjectDto subjectDto,String subjectId);


    /***
     * 修改课程签到状态
     *
     * @param subjectId
     * @return
     */
    ResponseResult updateSignStatue(String subjectId);

    /**
     * 签到时间延长
     *
     * @param addTime
     * @param subjectId
     */
    ResponseResult addSignTime(long addTime,String subjectId);

    /**
     * 更改课程状态
     *
     * @param subjectId
     * @return
     */
    ResponseResult updateStatus(String subjectId);
}
