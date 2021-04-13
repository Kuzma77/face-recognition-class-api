package com.soft1851.swl.face.enums;

import org.omg.PortableInterceptor.ACTIVE;
import org.omg.PortableInterceptor.INACTIVE;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/13
 */
public enum SignFlag {


    UNSIGN(0, "未开始签到"),
    SIGNING(1, "正在签到"),
    HASSIGN(2, "签到已结束");

    public final Integer type;
    public final String value;

    SignFlag(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
    /**
     * 判断传入的用户状态是不是有效的值
     * @param tempStatus
     * @return
     */
    public static boolean isUserStatusValid(Integer tempStatus) {
        if (tempStatus != null) {
            if (tempStatus == UNSIGN.type || tempStatus == SIGNING.type || tempStatus == HASSIGN.type) {
                return true;
            }
        }
        return false;
    }

}
