package com.soft1851.swl.face.exception;

import com.soft1851.swl.face.common.ResultCode;

/**
 * @ClassName CustomException
 * @Description 用户自定义异常
 * @Author wl_sun
 * @description TODO
 * @Data 2021/4/7
 */
public class CustomException extends RuntimeException {
    protected ResultCode resultCode;

    public CustomException(String msg, ResultCode resultCode) {
        super(msg);
        this.resultCode = resultCode;
    }

    public ResultCode getResultCode() {
        return resultCode;
    }
}
