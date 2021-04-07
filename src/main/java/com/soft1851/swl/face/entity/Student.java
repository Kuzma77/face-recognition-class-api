package com.soft1851.swl.face.entity;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Table;
import java.util.Date;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "student")
@Builder
public class Student {

  private String studentId;
  private String studentName;
  private String classroom;
  private String password;
  private String phoneNumber;
  private String avatar;
  private String gender;
  private Date updateTime;
  private Date createTime;
  private String deleteFlag;
}
