package com.soft1851.swl.face.service.impl;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.common.ResultCode;
import com.soft1851.swl.face.dto.*;
import com.soft1851.swl.face.entity.*;
import com.soft1851.swl.face.enums.FaceVerifyType;
import com.soft1851.swl.face.enums.LogType;
import com.soft1851.swl.face.mapper.AdminMapper;
import com.soft1851.swl.face.mapper.OwnerFaceMapper;
import com.soft1851.swl.face.mapper.StudentMapper;
import com.soft1851.swl.face.mapper.TeacherMapper;
import com.soft1851.swl.face.mo.Log;
import com.soft1851.swl.face.service.BaseService;
import com.soft1851.swl.face.service.FaceService;
import com.soft1851.swl.face.service.LogService;
import com.soft1851.swl.face.service.RedisService;
import com.soft1851.swl.face.util.*;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

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
    public final LogService logService;
    public final FaceManageUtil faceManageUtil;

    @Override
    public ResponseResult addFace(OwnerFaceDto ownerFaceDto) {
        String id = RandomNumUtil.getVerifyCode(8);

        OwnerFace ownerFace = OwnerFace.builder()
                .id(id)
                .faceId(ownerFaceDto.getFaceId())
                .ownerId(ownerFaceDto.getOwnerId())
                .build();
        this.ownerFaceMapper.addOne(ownerFace);
        Log log = Log.builder()
                .id(RandomNumUtil.getVerifyCode(8))
                .type(LogType.ADD.value)
                .operatorId("001")
                .objectId(ownerFaceDto.getOwnerId())
                .content("人脸入库成功")
                .createTime(new Date())
                .build();
        this.logService.saveOneLog(log);
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
                        .role("student")
                        .build();
//                将token添加到redis
                redisService.set("USER_TOKEN:"+student.getStudentId(),token);
                Log log = Log.builder()
                        .id(RandomNumUtil.getVerifyCode(8))
                        .type(LogType.LOGIN.value)
                        .operatorId(student.getStudentId())
                        .objectId(student.getStudentId())
                        .content("人脸识别录成功")
                        .createTime(new Date())
                        .build();
                this.logService.saveOneLog(log);
                return ResponseResult.success(loginResDto);
            }else if(map.get("role")=="teacher") {
                Object ob = map.get("data");
                ObjectMapper objectMapper = new ObjectMapper();
                Teacher tea = objectMapper.convertValue(ob, Teacher.class);
                Teacher teacher = this.teacherMapper.selectByPrimaryKey(tea.getTeacherId());
                Map<String, Object> teacherInfo = new HashMap<>(3);
                teacherInfo.put("id", teacher.getTeacherId());
                teacherInfo.put("name", teacher.getTeacherName());
                teacherInfo.put("password", teacher.getPassword());
                String token = jwtTokenUtil.generateToken(teacherInfo);
                log.info(
                        "{}登录成功，生成的token = {},有效期到:{}",
                        teacher.getTeacherName(),
                        token,
                        jwtTokenUtil.getExpirationTime()
                );
                LoginResDto loginResDto = LoginResDto.builder().userDto(UserDto.builder()
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
                        .role("teacher")
                        .build();
                //                将token添加到redis
                redisService.set("USER_TOKEN:" + teacher.getTeacherId(), token);
                Log log = Log.builder()
                        .id(RandomNumUtil.getVerifyCode(8))
                        .type(LogType.LOGIN.value)
                        .operatorId(teacher.getTeacherId())
                        .objectId(teacher.getTeacherId())
                        .content("人脸识别登录成功")
                        .createTime(new Date())
                        .build();
                this.logService.saveOneLog(log);
                return ResponseResult.success(loginResDto);
            }
            Log log = Log.builder()
                    .id(RandomNumUtil.getVerifyCode(8))
                    .type(LogType.LOGIN.value)
                    .operatorId(faceLoginDto.getId())
                    .objectId(faceLoginDto.getId())
                    .content("人脸识别登录失败")
                    .createTime(new Date())
                    .build();
            this.logService.saveOneLog(log);
            return ResponseResult.failure(ResultCode.FACE_NOT_SAVE_ERROR);
        }
        Log log = Log.builder()
                .id(RandomNumUtil.getVerifyCode(8))
                .type(LogType.LOGIN.value)
                .operatorId(faceLoginDto.getId())
                .objectId(faceLoginDto.getId())
                .content("人脸识别登录失败")
                .createTime(new Date())
                .build();
        this.logService.saveOneLog(log);
        return ResponseResult.failure(ResultCode.USER_FACE_LOGIN_ERROR);
    }

    @Override
    public ResponseResult addFaceData(AddFaceDto addFaceDto) throws Exception {
        if(!this.faceManageUtil.getFaceEntity(addFaceDto.getUserId())){
            this.faceManageUtil.addFaceEntity(addFaceDto.getUserId(),addFaceDto.getUserName());
            log.info("创建样本{}成功",addFaceDto.getUserId());
            return ResponseResult.success(this.faceManageUtil.addFaceData(addFaceDto));
        }
        return ResponseResult.success(this.faceManageUtil.addFaceData(addFaceDto));
    }

    @Override
    public ResponseResult searchFace(SearchFaceDto searchFaceDto) throws Exception {
        List list = Arrays.asList(JsonUtil.jsonToPojo(new Gson().toJson(this.faceManageUtil.SearchFace(searchFaceDto).get("data")), Map.class));
        List matchList = Arrays.asList(JsonUtil.jsonToPojo(new Gson().toJson(list.get(0)), Map.class).get("matchList"));
        List ob = JsonUtil.jsonToList(new Gson().toJson(matchList.get(0)), Map.class);
        Map<String,Object> map = JsonUtil.jsonToPojo(new Gson().toJson(ob.get(0)), Map.class);
        List faceItems = Arrays.asList(map.get("faceItems"));
        List rt = JsonUtil.jsonToList(new Gson().toJson(faceItems.get(0)), Map.class);
        Map<String, Object> map1 = JsonUtil.jsonToPojo(new Gson().toJson(rt.get(0)), Map.class);
        System.out.println(map1.get("score"));
        if(Double.valueOf((Double) map1.get("score"))>0.7){
            return ResponseResult.success(map1.get("entityId"));
        }
        return ResponseResult.failure(ResultCode.FACE_SEARCH_FAIL);
    }
}
