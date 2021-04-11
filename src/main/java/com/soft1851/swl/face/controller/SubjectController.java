package com.soft1851.swl.face.controller;

import com.soft1851.swl.face.annocation.ControllerWebLog;
import com.soft1851.swl.face.common.ResponseResult;
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
    public ResponseResult addSubject(@Valid @RequestBody SubjectDto subjectDto){
        return this.subjectService.addSubject(subjectDto);
    }

    @PostMapping("/update")
    @ControllerWebLog
    @ApiOperation(value = "修改课程",notes = "修改课程")
    public ResponseResult updateSubject(@Valid @RequestBody SubjectDto subjectDto, @RequestParam String subjectId){
        return this.subjectService.updateSubject(subjectDto,subjectId);
    }


    @PostMapping("/updateSignFlag")
    @ControllerWebLog
    @ApiOperation(value = "开始/结束签到",notes = "开始/结束签到")
    public ResponseResult updateSignStatue(@RequestParam String subjectId){
        return this.subjectService.updateSignStatue(subjectId);
    }

    @PostMapping("/addSignTime")
    @ControllerWebLog
    @ApiOperation(value = "延时签到",notes = "延时签到")
    public ResponseResult addSignTime(@Valid @RequestParam long addTime,@RequestParam String subjectId){
        return this.subjectService.addSignTime(addTime,subjectId);
    }

}
