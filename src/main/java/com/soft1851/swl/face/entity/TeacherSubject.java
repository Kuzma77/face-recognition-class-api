package com.soft1851.swl.face.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/5/8
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teacher_subject")
@Builder
public class TeacherSubject {

    @Id
    @GeneratedValue(generator = "JDBC")
    private String subjectId;

    @Column(name = "teacher_id")
    private String teacherId;
}
