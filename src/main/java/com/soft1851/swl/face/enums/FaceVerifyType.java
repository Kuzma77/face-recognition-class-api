package com.soft1851.swl.face.enums;

/**
 * @author wl_sun
 * @description 人脸比对类型 枚举
 * @Data 2021/4/15
 */
public enum FaceVerifyType {
    BASE64(1, "图片Base64对比"),
    IMAGE_URL(0, "URL图片地址对比");

    public final Integer type;
    public final String value;

    FaceVerifyType(Integer type, String value) {
        this.type = type;
        this.value = value;
    }
}