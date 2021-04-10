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


/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/7
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "subject")
@Builder
public class Subject{

  @Id
  @GeneratedValue(generator = "JDBC")
  @NotNull(message = "课程号不能为空")
  private String subjectId;


  @Column(name = "subject_name")
  @NotNull(message = "课程名不能为空")
  private String subjectName;

  @Column(name = "sign_flag")
  private Integer signFlag;

  @Column(name = "sign_time")
  private long signTime;

  @Column(name = "update_time")
  private Date updateTime;

  @Column(name = "begin_time")
  private Date beginTime;

  @Column(name = "end_time")
  private Date endTime;

  @Column(name = "create_time")
  private Date createTime;

  @Column(name = "delete_time")
  private Integer deleteFlag;

}
