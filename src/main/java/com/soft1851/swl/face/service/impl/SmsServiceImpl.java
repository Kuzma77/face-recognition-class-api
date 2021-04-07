package com.soft1851.swl.face.service.impl;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.dto.PhoneCodeDto;
import com.soft1851.swl.face.service.SmsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/7
 */
@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SmsServiceImpl implements SmsService {
    @Override
    public ResponseResult sendSms(String phoneNumber) {
        return null;
    }

    @Override
    public ResponseResult checkSms(PhoneCodeDto phoneCodeDto) {
        return null;
    }
}
