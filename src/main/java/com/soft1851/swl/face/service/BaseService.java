package com.soft1851.swl.face.service;

import com.soft1851.swl.face.common.ResponseResult;

import java.util.Map;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/10
 */
public interface BaseService {


    /**
     * 退出登录
     *
     * @param userId
     * @return
     */
    ResponseResult layout(String userId);

    /***
     * 通过id得出是学生还是教师还是管理员实体
     * @param userId
     * @return
     */
    Map<String,Object> getUserById(String userId);
}
