package com.soft1851.swl.face.validator;

import com.soft1851.swl.face.annocation.PhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/7
 */
public class PhoneNumberValidator implements ConstraintValidator<PhoneNumber,String> {
    public static final Pattern phonePattern = Pattern.compile("^1(3|4|5|6|7|8)\\d{9}$");
    @Override
    public boolean isValid(String phoneNumber, ConstraintValidatorContext constraintValidatorContext) {
        return (phoneNumber.length() == 11 && phonePattern.matcher(phoneNumber).matches());
    }
}