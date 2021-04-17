package com.soft1851.swl.face.service;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.LoginDto;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/17
 */
public interface AdminService {

    /**
     * 通过id查询管理员
     *
     * @param adminId
     * @return
     */
    ResponseResult getAdminById(String adminId);


    /**
     * 账密登录
     *
     * @param loginDto
     * @return
     */
    ResponseResult loginByAdminId(LoginDto loginDto);
}
