package com.soft1851.swl.face.service.impl;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.common.ResultCode;
import com.soft1851.swl.face.dto.JwtTokenRespDto;
import com.soft1851.swl.face.dto.LoginDto;
import com.soft1851.swl.face.dto.LoginResDto;
import com.soft1851.swl.face.dto.UserDto;
import com.soft1851.swl.face.entity.Admin;
import com.soft1851.swl.face.entity.OwnerFace;
import com.soft1851.swl.face.enums.FaceVerifyType;
import com.soft1851.swl.face.mapper.AdminMapper;
import com.soft1851.swl.face.service.AdminService;
import com.soft1851.swl.face.service.RedisService;
import com.soft1851.swl.face.util.JwtTokenUtil;
import com.soft1851.swl.face.util.Md5Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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
public class AdminServiceImpl implements AdminService {

    public final JwtTokenUtil jwtTokenUtil;
    public  final RedisService redisService;
    public  final AdminMapper adminMapper;

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
                        .build();
                //                将token添加到redis
                redisService.set("USER_TOKEN:" + admin.getAdminId(), token);
                return ResponseResult.success(loginResDto);
            } else {
                log.error("密码错误");
                return ResponseResult.failure(ResultCode.USER_PASSWORD_ERROR);
            }
        } else {
            log.error("该管理员账号不存在");
            return ResponseResult.failure(ResultCode.USER_NOT_FOUND);
        }
    }

    @Override
    public ResponseResult loginByFace(LoginDto loginDto) {
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
        
    }
}
