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


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student")
@Builder
public class Student {

  @Id
  @GeneratedValue(generator = "JDBC")
  private String studentId;

  @Column(name = "student_name")
  private String studentName;

  @Column(name = "account")
  private String account;

  @Column(name = "classroom")
  private String classroom;

  @Column(name = "password")
  private String password;

  @Column(name = "phone_number")
  private String phoneNumber;

  @Column(name = "avatar")
  private String avatar;

  @Column(name = "gender")
  private String gender;

  @Column(name = "update_time")
  private Date updateTime;

  @Column(name = "create_time")
  private Date createTime;

  @Column(name = "delete_flag")
  private String deleteFlag;
}
