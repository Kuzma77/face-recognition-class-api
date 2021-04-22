package com.soft1851.swl.face.enums;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/22
 */
public enum AttendStatue {

    HASATTENDED(0, "到勤"),
    UNATTEND(1, "缺勤"),
    HASNOTED(2, "已经请假");

    public final Integer type;
    public final String value;

    AttendStatue(Integer type, String value) {
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
            if (tempStatus == HASATTENDED.type || tempStatus == UNATTEND.type || tempStatus == HASNOTED.type) {
                return true;
            }
        }
        return false;
    }
}
