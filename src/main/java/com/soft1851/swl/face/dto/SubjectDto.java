package com.soft1851.swl.face.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author wl_sun
 * @description 新建课程
 * @Data 2021/4/10
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubjectDto {
    private String subjectName;
    private Date beginTime;
    private Date endTime;
}
