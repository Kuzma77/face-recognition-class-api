package com.soft1851.swl.face.controller;

import com.soft1851.swl.face.annocation.ControllerWebLog;
import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.NoteDto;
import com.soft1851.swl.face.service.NoteService;
import com.soft1851.swl.face.service.StudentService;
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
@RequestMapping("/note")
@Validated
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Api(tags = "假条接口",value = "提供假条接口相关的Rest API")
public class NoteController {

    public final NoteService noteService;


    @PostMapping("/add")
    @ControllerWebLog
    @ApiOperation(value = "新增假条",notes = "新增假条")
    public ResponseResult addNote(@Valid @RequestBody NoteDto noteDto, @RequestParam String studentId){
        return this.noteService.addNote(noteDto,studentId);
    }
}
