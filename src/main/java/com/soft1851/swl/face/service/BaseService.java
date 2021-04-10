package com.soft1851.swl.face.service;

import com.soft1851.swl.face.common.ResponseResult;

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
}
