package com.soft1851.swl.face.enums;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/18
 */
public enum LogType {

    ADD(1, "增"),
    DELETE(2, "删"),
    UPDATE(3, "改"),
    SELECT(4, "查"),
    LOGIN(5, "登录"),
    LAYOUT(6,"退出登录");

    public final Integer type;
    public final String value;

    LogType(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}
