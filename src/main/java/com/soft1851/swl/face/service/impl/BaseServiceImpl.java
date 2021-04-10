package com.soft1851.swl.face.service.impl;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.common.ResultCode;
import com.soft1851.swl.face.service.BaseService;
import com.soft1851.swl.face.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/10
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseServiceImpl implements BaseService {


    public final RedisService redisService;

    @Override
    public ResponseResult layout(String userId) {
        if(redisService.existsKey("USER_TOKEN:"+userId)){
            redisService.removeKey("USER_TOKEN:"+userId);
            return ResponseResult.success();
        }
        return ResponseResult.failure(ResultCode.PARAM_IS_INVALID);
    }
}
