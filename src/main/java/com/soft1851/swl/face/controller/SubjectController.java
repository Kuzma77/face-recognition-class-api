package com.soft1851.swl.face.controller;

import com.soft1851.swl.face.annocation.ControllerWebLog;
import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.BetTimeDto;
import com.soft1851.swl.face.dto.SubjectDto;
import com.soft1851.swl.face.service.SubjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/11
 */

@RestController
@Slf4j
@RequestMapping("/subject")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "课程接口",value = "提供课程相关的Rest API")
public class SubjectController {

    public final SubjectService subjectService;

    @PostMapping("/add")
    @ControllerWebLog
    @ApiOperation(value = "新增课程",notes = "新增课程")
    public ResponseResult addSubject(@Valid @RequestBody SubjectDto subjectDto, @RequestParam String teacherId){
        return this.subjectService.addSubject(subjectDto,teacherId);
    }

    @PostMapping("/update")
    @ControllerWebLog
    @ApiOperation(value = "修改课程",notes = "修改课程")
    public ResponseResult updateSubject(@Valid @RequestBody SubjectDto subjectDto, @RequestParam String subjectId,@RequestParam String teacherId){
        return this.subjectService.updateSubject(subjectDto,subjectId,teacherId);
    }


    @PostMapping("/updateSignFlag")
    @ControllerWebLog
    @ApiOperation(value = "开始/结束签到",notes = "开始/结束签到")
    public ResponseResult updateSignStatue(@RequestParam String subjectId,@RequestParam String teacherId){
        return this.subjectService.updateSignStatue(subjectId,teacherId);
    }

    @PostMapping("/addSignTime")
    @ControllerWebLog
    @ApiOperation(value = "延时签到",notes = "延时签到")
    public ResponseResult addSignTime(@Valid @RequestParam long addTime,@RequestParam String subjectId,@RequestParam String teacherId){
        return this.subjectService.addSignTime(addTime,subjectId,teacherId);
    }

    @PostMapping("/updateStatus")
    @ControllerWebLog
    @ApiOperation(value = "修改课程删除状态",notes = "修改课程删除状态")
    public ResponseResult updateStatus(@RequestParam String subjectId){
        return this.subjectService.updateStatus(subjectId);
    }

    @PostMapping("/querySubjectsBetweenTime")
    @ControllerWebLog
    @ApiOperation(value = "通过时间段和学生id获取到这段时间内的课程",notes = "通过时间段和学生id获取到这段时间内的课程")
    public ResponseResult querySubjectsBetweenTime(@RequestBody BetTimeDto betTimeDto){
        return this.subjectService.querySubjectsBetweenTime(betTimeDto);
    }


}
