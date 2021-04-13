package com.soft1851.swl.face.service.impl;

import cn.hutool.core.date.DateTime;
import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.common.ResultCode;
import com.soft1851.swl.face.dto.SubjectDto;
import com.soft1851.swl.face.entity.Subject;
import com.soft1851.swl.face.entity.Teacher;
import com.soft1851.swl.face.enums.SignFlag;
import com.soft1851.swl.face.mapper.SubjectMapper;
import com.soft1851.swl.face.service.SubjectService;
import com.soft1851.swl.face.util.RandomNumUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/11
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SubjectServiceImpl implements SubjectService {

    public final SubjectMapper subjectMapper;

    @Override
    public ResponseResult addSubject(SubjectDto subjectDto) {
        String id = RandomNumUtil.getVerifyCode(8);
        Subject subject = Subject.builder()
                .subjectId(id)
                .subjectName(subjectDto.getSubjectName())
                .beginTime(subjectDto.getBeginTime())
                .endTime(subjectDto.getEndTime())
                .deleteFlag(0)
                .signFlag(SignFlag.UNSIGN.type)
                .signTime(0)
                .createTime(DateTime.now())
                .updateTime(DateTime.now())
                .build();
        this.subjectMapper.addSubject(subject);
        return ResponseResult.success(subject);
    }

    @Override
    public ResponseResult updateSubject(SubjectDto subjectDto, String subjectId) {
        Subject subject = Subject.builder()
                .subjectName(subjectDto.getSubjectName())
                .beginTime(subjectDto.getBeginTime())
                .endTime(subjectDto.getEndTime())
                .updateTime(DateTime.now())
                .build();
        this.subjectMapper.updateSubject(subject,subjectId);
        return ResponseResult.success(subject);
    }

    @Override
    public ResponseResult updateSignStatue(String subjectId) {
        Integer sFlag = this.subjectMapper.selectByPrimaryKey(subjectId).getSignFlag();
        int newSignFlag = 0;
        String message = "";
        switch (sFlag){
            case 0:
                newSignFlag = SignFlag.SIGNING.type;
                message = "开始签到";
                break;
            case 1:
                newSignFlag = SignFlag.HASSIGN.type;
                message = "结束签到";
                break;
            case 2:
                newSignFlag = SignFlag.SIGNING.type;
                message = "重新开始签到";
                break;
            default:
                return ResponseResult.failure(ResultCode.PARAM_IS_INVALID);
        }
        this.subjectMapper.updateSignStatue(newSignFlag,subjectId);
        return ResponseResult.success("课程"+subjectId+message);
    }

    @Override
    public ResponseResult addSignTime(long addTime, String subjectId) {
        long sTime = this.subjectMapper.selectByPrimaryKey(subjectId).getSignTime();
        long newSignTime = sTime + addTime;
        this.subjectMapper.addSignTime(newSignTime, subjectId);
        return ResponseResult.success(newSignTime);
    }

    @Override
    public ResponseResult updateStatus(String subjectId) {
        Subject subject = this.subjectMapper.selectByPrimaryKey(subjectId);
        if(subject!=null){
            Integer deleteFlag = subject.getDeleteFlag();
            Integer newInteger = deleteFlag == 0 ? 1:0;
            this.subjectMapper.updateStatus(newInteger,subjectId);
            log.info("课程{}的状态修改成功",subjectId);
            return ResponseResult.success();
        }else{
            log.error("该课程账号不存在");
            return ResponseResult.failure(ResultCode.USER_NOT_FOUND);
        }
    }
}
