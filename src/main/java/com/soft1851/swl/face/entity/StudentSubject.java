package com.soft1851.swl.face.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/21
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student_subject")
@Builder
public class StudentSubject {

    @Id
    @GeneratedValue(generator = "JDBC")
    private String studentId;

    @Column(name = "subject_id")
    private String subjectId;

    @Column(name = "attend_flag")
    private Integer attendFlag;

    @Column(name = "attend_time")
    private Date attendTime;
}
