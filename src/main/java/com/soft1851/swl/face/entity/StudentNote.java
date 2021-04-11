package com.soft1851.swl.face.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/11
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student_note")
@Builder
public class StudentNote {

    @Id
    @GeneratedValue(generator = "JDBC")
    private String studentId;

    @Column(name = "note_id")
    private String noteId;
}
