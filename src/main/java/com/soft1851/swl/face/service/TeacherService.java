package com.soft1851.swl.face.service;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.LoginDto;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/10
 */
public interface TeacherService {


    /**
     * 账密登录
     *
     * @param loginDto
     * @return
     */
    ResponseResult loginByTeacherId(LoginDto loginDto);
}
