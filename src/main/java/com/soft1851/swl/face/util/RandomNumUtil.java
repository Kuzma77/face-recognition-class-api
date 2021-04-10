package com.soft1851.swl.face.util;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/10
 */
public class RandomNumUtil {

    private static final String SYMBOLS = "0123456789"; // 数字

    private static final Random RANDOM = new SecureRandom();

    public static void main(String[] args) {
        System.out.println(getVerifyCode(8));
    }

    /**
     * 获取长度为len的随机数字
     * @return 随机数字
     * @date
     */
    public static String getVerifyCode(Integer len) {

        char[] nonceChars = new char[len];

        for (int index = 0; index < nonceChars.length; ++index) {
            nonceChars[index] = SYMBOLS.charAt(RANDOM.nextInt(SYMBOLS.length()));
        }
        return new String(nonceChars);
    }

}
