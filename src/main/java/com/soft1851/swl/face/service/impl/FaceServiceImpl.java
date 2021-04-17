package com.soft1851.swl.face.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.common.ResultCode;
import com.soft1851.swl.face.dto.*;
import com.soft1851.swl.face.entity.Admin;
import com.soft1851.swl.face.entity.OwnerFace;
import com.soft1851.swl.face.entity.Student;
import com.soft1851.swl.face.entity.Teacher;
import com.soft1851.swl.face.enums.FaceVerifyType;
import com.soft1851.swl.face.mapper.AdminMapper;
import com.soft1851.swl.face.mapper.OwnerFaceMapper;
import com.soft1851.swl.face.mapper.StudentMapper;
import com.soft1851.swl.face.mapper.TeacherMapper;
import com.soft1851.swl.face.service.BaseService;
import com.soft1851.swl.face.service.FaceService;
import com.soft1851.swl.face.service.RedisService;
import com.soft1851.swl.face.util.FaceVerifyUtil;
import com.soft1851.swl.face.util.JwtTokenUtil;
import com.soft1851.swl.face.util.RandomNumUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.formula.functions.T;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/15
 */
@Slf4j
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class FaceServiceImpl implements FaceService {


    public final OwnerFaceMapper ownerFaceMapper;
    public final RestTemplate restTemplate;
    public final FaceVerifyUtil faceVerifyUtil;
    public final BaseService baseService;
    public final JwtTokenUtil jwtTokenUtil;
    public  final RedisService redisService;
    public final StudentMapper studentMapper;
    public final TeacherMapper teacherMapper;
    public final AdminMapper adminMapper;

    @Override
    public ResponseResult addFace(OwnerFaceDto ownerFaceDto) {
        String id = RandomNumUtil.getVerifyCode(8);

        OwnerFace ownerFace = OwnerFace.builder()
                .id(id)
                .faceId(ownerFaceDto.getFaceId())
                .ownerId(ownerFaceDto.getOwnerId())
                .build();
        this.ownerFaceMapper.addOne(ownerFace);
        return ResponseResult.success(ownerFace);
    }

    @Override
    public ResponseResult faceLogin(FaceLoginDto faceLoginDto) {
        //0、判断用户名和人脸信息不能为空
        if(StringUtils.isBlank(faceLoginDto.getId())){
            return ResponseResult.failure(ResultCode.USER_NOT_FOUND);
        }
        String tempFace64 = faceLoginDto.getImg64();
        if(StringUtils.isBlank(tempFace64)){
            return ResponseResult.failure(ResultCode.USER_FACE_NULL_ERROR);
        }
        //1、从数据库中根据id查询出faceId
        String faceId = null;
        OwnerFace ownerFace = this.ownerFaceMapper.queryFaceIdByOwnerId(faceLoginDto.getId());
        if(ownerFace!=null){
            faceId = ownerFace.getFaceId();
            System.out.println(faceId);
        }else {
            return ResponseResult.failure(ResultCode.FACE_NOT_SAVE_ERROR);
        }
        //2.请求文件服务，根据faceId获得人脸数据的base64数据
        String fileServerUrl = "http://localhost:8888/fs/readFace64?faceId="+faceId;
        //得到的是封装的结果
        ResponseEntity<ResponseResult> resultResponseEntity = restTemplate.getForEntity(fileServerUrl,ResponseResult.class);
        ResponseResult bodyResult = resultResponseEntity.getBody();
        System.out.println(bodyResult);
        assert bodyResult != null;
        String base64 = (String) bodyResult.getData();
        //3、调用阿里ai进行人脸对比失败，判断可信度，从而实现人脸登录
        boolean result = faceVerifyUtil.faceVerify(FaceVerifyType.BASE64.type,tempFace64,base64,60);
        if(result){
            Map<String,Object> map = this.baseService.getUserById(faceLoginDto.getId());
            if(map.get("role")=="student"){
                Object ob = map.get("data");
                //Object类型转实体类
                ObjectMapper objectMapper = new ObjectMapper();
                Student stu = objectMapper.convertValue(ob,Student.class);
                Student student = this.studentMapper.selectByPrimaryKey(stu.getStudentId());
                Map<String,Object> studentInfo = new HashMap<>(3);
                studentInfo.put("id",student.getStudentId());
                studentInfo.put("name",student.getStudentName());
                studentInfo.put("password",student.getPassword());
                String token = jwtTokenUtil.generateToken(studentInfo);
                log.info(
                        "{}登录成功，生成的token = {},有效期到:{}",
                        student.getStudentName(),
                        token,
                        jwtTokenUtil.getExpirationTime()
                );
                LoginResDto loginResDto =  LoginResDto.builder().userDto(UserDto.builder()
                        .id(student.getStudentId())
                        .name(student.getStudentName())
                        .avatar(student.getAvatar())
                        .gender(student.getGender())
                        .phoneNumber(student.getPhoneNumber())
                        .build())
                        .token(JwtTokenRespDto
                                .builder()
                                .token(token)
                                .expirationTime(jwtTokenUtil.getExpirationTime().getTime())
                                .build())
                        .build();
//                将token添加到redis
                redisService.set("USER_TOKEN:"+student.getStudentId(),token);
                return ResponseResult.success(loginResDto);
            }else if(map.get("role")=="teacher"){
                Object ob = map.get("data");
                ObjectMapper objectMapper = new ObjectMapper();
                Teacher tea = objectMapper.convertValue(ob,Teacher.class);
                Teacher teacher = this.teacherMapper.selectByPrimaryKey(tea.getTeacherId());
                Map<String,Object> teacherInfo = new HashMap<>(3);
                teacherInfo.put("id",teacher.getTeacherId());
                teacherInfo.put("name",teacher.getTeacherName());
                teacherInfo.put("password",teacher.getPassword());
                String token = jwtTokenUtil.generateToken(teacherInfo);
                log.info(
                        "{}登录成功，生成的token = {},有效期到:{}",
                        teacher.getTeacherName(),
                        token,
                        jwtTokenUtil.getExpirationTime()
                );
                LoginResDto loginResDto =  LoginResDto.builder().userDto(UserDto.builder()
                        .id(teacher.getTeacherId())
                        .name(teacher.getTeacherName())
                        .avatar(teacher.getAvatar())
                        .gender(teacher.getGender())
                        .phoneNumber(teacher.getPhoneNumber())
                        .build())
                        .token(JwtTokenRespDto
                                .builder()
                                .token(token)
                                .expirationTime(jwtTokenUtil.getExpirationTime().getTime())
                                .build())
                        .build();
                //                将token添加到redis
                redisService.set("USER_TOKEN:"+teacher.getTeacherId(),token);
                return ResponseResult.success(loginResDto);
            }
            Object ob = map.get("data");
            ObjectMapper objectMapper = new ObjectMapper();
            Admin adm = objectMapper.convertValue(ob,Admin.class);
            Admin admin = this.adminMapper.selectByPrimaryKey(adm.getAdminId());
            Map<String,Object> adminInfo = new HashMap<>(3);
            adminInfo.put("id",admin.getAdminId());
            adminInfo.put("name",admin.getAdminName());
            adminInfo.put("password",admin.getAdminName());
            String token = jwtTokenUtil.generateToken(adminInfo);
            log.info(
                    "{}登录成功，生成的token = {},有效期到:{}",
                    admin.getAdminName(),
                    token,
                    jwtTokenUtil.getExpirationTime()
            );
            LoginResDto loginResDto =  LoginResDto.builder().userDto(UserDto.builder()
                    .id(admin.getAdminId())
                    .name(admin.getAdminName())
                    .avatar(admin.getAvatar())
                    .gender(admin.getGender())
                    .build())
                    .token(JwtTokenRespDto
                            .builder()
                            .token(token)
                            .expirationTime(jwtTokenUtil.getExpirationTime().getTime())
                            .build())
                    .build();
            //                将token添加到redis
            redisService.set("USER_TOKEN:"+admin.getAdminId(),token);
            return ResponseResult.success(loginResDto);
        }else {
            return ResponseResult.failure(ResultCode.USER_FACE_LOGIN_ERROR);
        }
    }
}
