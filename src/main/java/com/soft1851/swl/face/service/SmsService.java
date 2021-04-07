package com.soft1851.swl.face.service;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.PhoneCodeDto;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/7
 */
public interface SmsService {
    /**
     * 获取短信验证
     * @param phoneNumber
     * @return ResponseResult
     */
    ResponseResult sendSms(String phoneNumber);
    /**
     * 验证短信是否正确（signDto中有手机号和验证码两部分内容）
     * @param phoneCodeDto
     * @return ResponseResult
     */
    ResponseResult checkSms(PhoneCodeDto phoneCodeDto);
}
