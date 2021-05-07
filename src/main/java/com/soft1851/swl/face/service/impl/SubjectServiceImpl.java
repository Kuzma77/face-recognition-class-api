package com.soft1851.swl.face.service.impl;

import cn.hutool.core.date.DateTime;
import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.common.ResultCode;
import com.soft1851.swl.face.dto.BetTimeDto;
import com.soft1851.swl.face.dto.SubjectDto;
import com.soft1851.swl.face.entity.StudentSubject;
import com.soft1851.swl.face.entity.Subject;
import com.soft1851.swl.face.entity.Teacher;
import com.soft1851.swl.face.entity.TeacherSubject;
import com.soft1851.swl.face.enums.LogType;
import com.soft1851.swl.face.enums.SignFlag;
import com.soft1851.swl.face.mapper.StudentSubjectMapper;
import com.soft1851.swl.face.mapper.SubjectMapper;
import com.soft1851.swl.face.mapper.TeacherSubjectMapper;
import com.soft1851.swl.face.mo.Log;
import com.soft1851.swl.face.service.LogService;
import com.soft1851.swl.face.service.SubjectService;
import com.soft1851.swl.face.util.RandomNumUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
    public final LogService logService;
    public final StudentSubjectMapper studentSubjectMapper;
    public final TeacherSubjectMapper teacherSubjectMapper;

    @Override
    public ResponseResult addSubject(SubjectDto subjectDto,String teacherId) {
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
        TeacherSubject teacherSubject = TeacherSubject.builder()
                .subjectId(id)
                .teacherId(teacherId)
                .build();
        this.teacherSubjectMapper.insert(teacherSubject);
        Log log = Log.builder()
                .id(RandomNumUtil.getVerifyCode(8))
                .type(LogType.ADD.value)
                .operatorId(teacherId)
                .objectId(id)
                .content("新增课程")
                .createTime(new Date())
                .build();
        this.logService.saveOneLog(log);
        return ResponseResult.success(subject);
    }

    @Override
    public ResponseResult updateSubject(SubjectDto subjectDto, String subjectId,String teacherId) {
        Subject subject = Subject.builder()
                .subjectName(subjectDto.getSubjectName())
                .beginTime(subjectDto.getBeginTime())
                .endTime(subjectDto.getEndTime())
                .updateTime(DateTime.now())
                .build();
        this.subjectMapper.updateSubject(subject,subjectId);
        Log log = Log.builder()
                .id(RandomNumUtil.getVerifyCode(8))
                .type(LogType.UPDATE.value)
                .operatorId(teacherId)
                .objectId(subjectId)
                .content("编辑课程")
                .createTime(new Date())
                .build();
        this.logService.saveOneLog(log);
        return ResponseResult.success(subject);
    }

    @Override
    public ResponseResult updateSignStatue(String subjectId,String teacherId) {
        Integer sFlag = this.subjectMapper.selectByPrimaryKey(subjectId).getSignFlag();
        int newSignFlag = 0;
        String message = "";
        switch (sFlag){
            case 0:
                newSignFlag = SignFlag.SIGNING.type;
                message = "开始签到";
                this.subjectMapper.addSignTime(60000,subjectId);
                break;
            case 1:
                newSignFlag = SignFlag.HASSIGN.type;
                message = "结束签到";
                this.subjectMapper.addSignTime(0,subjectId);
                break;
            case 2:
                newSignFlag = SignFlag.SIGNING.type;
                message = "重新开始签到";
                this.subjectMapper.addSignTime(60000,subjectId);
                break;
            default:
                return ResponseResult.failure(ResultCode.PARAM_IS_INVALID);
        }
        this.subjectMapper.updateSignStatue(newSignFlag,subjectId);
        Log log = Log.builder()
                .id(RandomNumUtil.getVerifyCode(8))
                .type(LogType.UPDATE.value)
                .operatorId(teacherId)
                .objectId(subjectId)
                .content("修改签到状态")
                .createTime(new Date())
                .build();
        this.logService.saveOneLog(log);
        return ResponseResult.success("课程"+subjectId+message);
    }

    @Override
    public ResponseResult addSignTime(long addTime, String subjectId,String teacherId) {
        long sTime = this.subjectMapper.selectByPrimaryKey(subjectId).getSignTime();
        long newSignTime = sTime + addTime;
        this.subjectMapper.addSignTime(newSignTime, subjectId);
        Log log = Log.builder()
                .id(RandomNumUtil.getVerifyCode(8))
                .type(LogType.UPDATE.value)
                .operatorId(teacherId)
                .objectId(subjectId)
                .content("修改签到时间")
                .createTime(new Date())
                .build();
        this.logService.saveOneLog(log);
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
            Log log = Log.builder()
                    .id(RandomNumUtil.getVerifyCode(8))
                    .type(LogType.UPDATE.value)
                    .operatorId("001")
                    .objectId(subjectId)
                    .content("课程删除状态修改")
                    .createTime(new Date())
                    .build();
            this.logService.saveOneLog(log);
            return ResponseResult.success();
        }else{
            log.error("该课程账号不存在");
            return ResponseResult.failure(ResultCode.USER_NOT_FOUND);
        }
    }

    @Override
    public ResponseResult querySubjectsBetweenTime(BetTimeDto betTimeDto) {
        List<StudentSubject> studentSubjects = this.studentSubjectMapper.querySubjectsByStudentId(betTimeDto.getStudentId());
        List<Subject> subjects = new ArrayList<>();
        List<Subject> aSubjects = new ArrayList<>();
        studentSubjects.forEach(studentSubject -> {
            subjects.add(this.subjectMapper.selectByPrimaryKey(studentSubject.getSubjectId()));
        });
        subjects.forEach(subject -> {
            if(((subject.getBeginTime().after(betTimeDto.getBeginTime())&&subject.getBeginTime().before(betTimeDto.getEndTime()))||(subject.getEndTime().after(betTimeDto.getBeginTime())&&subject.getEndTime().before(betTimeDto.getEndTime())))&&(subject.getDeleteFlag()==0)){
                aSubjects.add(subject);
            }
        });
        return ResponseResult.success(aSubjects);
    }

    @Override
    public ResponseResult querySubjectsByTeacherId(String teacherId) {
        return null;
    }
}
