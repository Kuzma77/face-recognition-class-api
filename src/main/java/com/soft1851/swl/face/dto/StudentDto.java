package com.soft1851.swl.face.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/7
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StudentDto {
    private String studentId;
    private String studentName;
    private String account;
    private String classroom;
    private String phoneNumber;
    private String avatar;
    private String gender;
}
