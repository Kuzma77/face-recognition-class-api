package com.soft1851.swl.face.enums;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/13
 */
public enum NoteStatue {

    UNPASS(0, "待审核"),
    PASS(1, "审核通过"),
    NOTPASS(2, "审核不通过");

    public final Integer type;
    public final String value;

    NoteStatue(Integer type, String value) {
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
            if (tempStatus == UNPASS.type || tempStatus == PASS.type || tempStatus == NOTPASS.type) {
                return true;
            }
        }
        return false;
    }
}
