package com.soft1851.swl.face.service.impl;

import cn.hutool.core.date.DateTime;
import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.common.ResultCode;
import com.soft1851.swl.face.dto.NoteDto;
import com.soft1851.swl.face.entity.Note;
import com.soft1851.swl.face.entity.StudentNote;
import com.soft1851.swl.face.entity.Subject;
import com.soft1851.swl.face.enums.LogType;
import com.soft1851.swl.face.enums.NoteStatue;
import com.soft1851.swl.face.mapper.NoteMapper;
import com.soft1851.swl.face.mapper.StudentNoteMapper;
import com.soft1851.swl.face.mo.Log;
import com.soft1851.swl.face.service.LogService;
import com.soft1851.swl.face.service.NoteService;
import com.soft1851.swl.face.util.RandomNumUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/11
 */
@Service
@Slf4j
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class NoteServiceImpl implements NoteService {

    public  final NoteMapper noteMapper;
    public  final StudentNoteMapper studentNoteMapper;
    public final LogService logService;

    @Override
    public ResponseResult addNote(NoteDto noteDto,String studentId) {
        String id = RandomNumUtil.getVerifyCode(8);
        Note note = Note.builder()
                .noteId(id)
                .subjectId(noteDto.getSubjectId())
                .subjectName(noteDto.getSubjectName())
                .updateTime(DateTime.now())
                .createTime(DateTime.now())
                .reason(noteDto.getReason())
                .noteStatue(0)
                .deleteFlag(0)
                .build();
        StudentNote studentNote = StudentNote.builder()
                .noteId(id)
                .studentId(studentId)
                .build();
        this.studentNoteMapper.insertOne(studentNote);
        this.noteMapper.addNote(note);
        Log log = Log.builder()
                .id(RandomNumUtil.getVerifyCode(8))
                .type(LogType.ADD.value)
                .operatorId(studentId)
                .objectId(id)
                .content("新增假条")
                .createTime(new Date())
                .build();
        this.logService.saveOneLog(log);
        return ResponseResult.success(note);
    }

    @Override
    public ResponseResult updateNote(NoteDto noteDto, String noteId) {
        Note note = Note.builder()
                .noteId(noteId)
                .subjectId(noteDto.getSubjectId())
                .subjectName(noteDto.getSubjectName())
                .reason(noteDto.getReason())
                .updateTime(DateTime.now())
                .build();
        this.noteMapper.updateNote(note);
        Log log = Log.builder()
                .id(RandomNumUtil.getVerifyCode(8))
                .type(LogType.UPDATE.value)
                .operatorId(this.studentNoteMapper.queryStudentIdByNoteId(noteId))
                .objectId(noteId)
                .content("编辑假条")
                .createTime(new Date())
                .build();
        this.logService.saveOneLog(log);
        return ResponseResult.success(note);
    }

    @Override
    public ResponseResult updateNoteStatue(Boolean ifPass, String noteId) {
        Integer noteStatue = ifPass ? NoteStatue.PASS.type :NoteStatue.NOTPASS.type;
        this.noteMapper.updateNoteStatue(noteStatue,noteId);
        Log log = Log.builder()
                .id(RandomNumUtil.getVerifyCode(8))
                .type(LogType.ADD.value)
                .operatorId("001")
                .objectId(noteId)
                .content("假条审核状态修改")
                .createTime(new Date())
                .build();
        this.logService.saveOneLog(log);
        return ResponseResult.success(noteStatue);
    }

    @Override
    public ResponseResult updateStatus(String noteId) {
        Note note = this.noteMapper.selectByPrimaryKey(noteId);
        if(note!=null){
            Integer deleteFlag = note.getDeleteFlag();
            Integer newInteger = deleteFlag == 0 ? 1:0;
            this.noteMapper.updateStatus(newInteger,noteId);
            log.info("假条{}的状态修改成功",noteId);
            Log log = Log.builder()
                    .id(RandomNumUtil.getVerifyCode(8))
                    .type(LogType.ADD.value)
                    .operatorId("001")
                    .objectId(noteId)
                    .content("假条删除状态修改")
                    .createTime(new Date())
                    .build();
            this.logService.saveOneLog(log);
            return ResponseResult.success();
        }else{
            log.error("该假条账号不存在");
            return ResponseResult.failure(ResultCode.USER_NOT_FOUND);
        }
    }
}
