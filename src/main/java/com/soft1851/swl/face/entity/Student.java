package com.soft1851.swl.face.entity;


import com.soft1851.swl.face.annocation.PhoneNumber;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student")
@Builder
public class Student {

  @Id
  @GeneratedValue(generator = "JDBC")
  @NotNull(message = "Id 不能为空")
  private String studentId;

  @Column(name = "student_name")
  @NotNull(message = "姓名不能为空")
  private String studentName;

  @Column(name = "account")
  @NotNull(message = "账号不能为空")
  private String account;

  @Column(name = "classroom")
  @NotNull(message = "教师不能为空")
  private String classroom;

  @Column(name = "password")
  private String password;

  @Column(name = "phone_number")
  @PhoneNumber
  @NotBlank(message = "手机号不能为空")
  private String phoneNumber;

  @Column(name = "avatar")
  @NotNull(message = "头像不能为空")
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
