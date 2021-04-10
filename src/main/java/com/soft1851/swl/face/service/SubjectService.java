package com.soft1851.swl.face.service;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.SubjectDto;
import com.soft1851.swl.face.entity.Subject;
import org.apache.ibatis.annotations.Insert;

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
}
