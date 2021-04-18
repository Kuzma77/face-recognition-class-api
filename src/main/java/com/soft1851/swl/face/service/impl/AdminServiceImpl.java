package com.soft1851.swl.face.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.common.ResultCode;
import com.soft1851.swl.face.dto.*;
import com.soft1851.swl.face.entity.Admin;
import com.soft1851.swl.face.entity.OwnerFace;
import com.soft1851.swl.face.enums.FaceVerifyType;
import com.soft1851.swl.face.enums.LogType;
import com.soft1851.swl.face.mapper.AdminMapper;
import com.soft1851.swl.face.mapper.OwnerFaceMapper;
import com.soft1851.swl.face.mo.Log;
import com.soft1851.swl.face.service.AdminService;
import com.soft1851.swl.face.service.BaseService;
import com.soft1851.swl.face.service.LogService;
import com.soft1851.swl.face.service.RedisService;
import com.soft1851.swl.face.util.FaceVerifyUtil;
import com.soft1851.swl.face.util.JwtTokenUtil;
import com.soft1851.swl.face.util.Md5Util;
import com.soft1851.swl.face.util.RandomNumUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/17
 */

@Service
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AdminServiceImpl implements AdminService{

    public final OwnerFaceMapper ownerFaceMapper;
    public final RestTemplate restTemplate;
    public final FaceVerifyUtil faceVerifyUtil;
    public final BaseService baseService;
    public final JwtTokenUtil jwtTokenUtil;
    public  final RedisService redisService;
    public  final AdminMapper adminMapper;
    public final LogService logService;

    @Override
    public ResponseResult getAdminById(String adminId) {
        Admin admin = this.adminMapper.getAdminById(adminId);
        return  admin == null ? ResponseResult.failure(ResultCode.USER_NOT_FOUND):ResponseResult.success(admin);
    }

    @Override
    public ResponseResult loginByAdminId(LoginDto loginDto) {
        Admin admin = this.adminMapper.selectByPrimaryKey(loginDto.getId());
        if (admin != null) {
            String pass = Md5Util.getMd5(loginDto.getPassword(), true, 32);
            if (admin.getPassword().equals(pass)) {
                Map<String, Object> adminInfo = new HashMap<>(3);
                adminInfo.put("id", admin.getAdminId());
                adminInfo.put("name", admin.getAdminName());
                adminInfo.put("password", admin.getAdminName());
                String token = jwtTokenUtil.generateToken(adminInfo);
                log.info(
                        "{}登录成功，生成的token = {},有效期到:{}",
                        admin.getAdminName(),
                        token,
                        jwtTokenUtil.getExpirationTime()
                );
                LoginResDto loginResDto = LoginResDto.builder().userDto(UserDto.builder()
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
                        .role("admin")
                        .build();
                //                将token添加到redis
                redisService.set("USER_TOKEN:" + admin.getAdminId(), token);
                //添加日志
                Log log = Log.builder()
                        .id(RandomNumUtil.getVerifyCode(8))
                        .type(LogType.LOGIN.value)
                        .operatorId(loginDto.getId())
                        .objectId(loginDto.getId())
                        .content("账密登录成功")
                        .createTime(new Date())
                        .build();
                this.logService.saveOneLog(log);
                return ResponseResult.success(loginResDto);
            } else {
                log.error("密码错误");
                Log log = Log.builder()
                        .id(RandomNumUtil.getVerifyCode(8))
                        .type(LogType.LOGIN.value)
                        .operatorId(loginDto.getId())
                        .objectId(loginDto.getId())
                        .content("账密登录失败")
                        .createTime(new Date())
                        .build();
                this.logService.saveOneLog(log);
                return ResponseResult.failure(ResultCode.USER_PASSWORD_ERROR);
            }
        } else {
            log.error("该管理员账号不存在");
            Log log = Log.builder()
                    .id(RandomNumUtil.getVerifyCode(8))
                    .type(LogType.LOGIN.value)
                    .operatorId(loginDto.getId())
                    .objectId(loginDto.getId())
                    .content("账密登录失败")
                    .createTime(new Date())
                    .build();
            this.logService.saveOneLog(log);
            return ResponseResult.failure(ResultCode.USER_NOT_FOUND);
        }
    }

    @Override
    public ResponseResult loginByFace(FaceLoginDto faceLoginDto) {
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
        Map<String,Object> map = this.baseService.getUserById(faceLoginDto.getId());
        if(result&&map.get("role")=="admin"){
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
                    .role("admin")
                    .build();
            //                将token添加到redis
            redisService.set("USER_TOKEN:"+admin.getAdminId(),token);
            Log log = Log.builder()
                    .id(RandomNumUtil.getVerifyCode(8))
                    .type(LogType.LOGIN.value)
                    .operatorId(faceLoginDto.getId())
                    .objectId(faceLoginDto.getId())
                    .content("人脸识别登录成功")
                    .createTime(new Date())
                    .build();
            this.logService.saveOneLog(log);
            return ResponseResult.success(loginResDto);
        }else {
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
    }
}
