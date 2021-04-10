package com.soft1851.swl.face.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
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
import javax.validation.constraints.Size;
import java.util.Date;


/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/10
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "teacher")
@Builder
public class Teacher {

  @Id
  @GeneratedValue(generator = "JDBC")
  @NotNull(message = "教工号 不能为空")
  private String teacherId;

  @Column(name = "teacher_name")
  @NotNull(message = "姓名不能为空")
  private String teacherName;

  @Column(name = "avatar")
  @NotNull(message = "头像不能为空")
  private String avatar;


  private String gender;

  @Column(name = "phone_number")
  @PhoneNumber
  @NotBlank(message = "手机号不能为空")
  private String phoneNumber;

  @JsonIgnore
  @Column(name = "password")
  @Size(min = 6,message = "密码不能小于6位")
  private String password;


  @Column(name = "update_time")
  private Date updateTime;

  @Column(name = "create_time")
  private Date createTime;

  @Column(name = "delete_flag")
  private Integer deleteFlag;




}
