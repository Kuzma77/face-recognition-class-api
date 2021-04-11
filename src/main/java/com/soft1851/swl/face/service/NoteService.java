package com.soft1851.swl.face.service;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.NoteDto;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/11
 */
public interface NoteService {

    /**
     * 新增假条
     *
     * @param noteDto
     * @param studentId
     * @return
     */
    ResponseResult addNote(NoteDto noteDto,String studentId);

    /**
     * 修改假条基础信息
     *
     * @param noteDto
     * @param noteId
     * @return
     */
    ResponseResult updateNote(NoteDto noteDto,String noteId);

    /**
     * 修改假条审核状态
     *
     * @param ifPass
     * @param noteId
     * @return
     */
    ResponseResult updateNoteStatue(Boolean ifPass,String noteId);
}
