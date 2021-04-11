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
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "note")
@Builder
public class Note {

  @Id
  @GeneratedValue(generator = "JDBC")
  @NotNull(message = "id不能为空")
  private String noteId;

  @Column(name = "student_id")
  private String subjectId;

  @Column(name = "student_name")
  @NotNull(message = "姓名不能为空")
  private String subjectName;

  @Column(name = "update_time")
  private Date updateTime;

  @Column(name = "create_time")
  private Date createTime;

  @Column(name = "reason")
  @NotNull(message = "原因不能为空")
  private String reason;

  @Column(name = "note_statue")
  private Integer noteStatue;

  @Column(name = "delete_flag")
  private Integer deleteFlag;
}
