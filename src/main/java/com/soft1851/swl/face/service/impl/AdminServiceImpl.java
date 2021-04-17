package com.soft1851.swl.face.service.impl;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.common.ResultCode;
import com.soft1851.swl.face.dto.JwtTokenRespDto;
import com.soft1851.swl.face.dto.LoginDto;
import com.soft1851.swl.face.dto.LoginResDto;
import com.soft1851.swl.face.dto.UserDto;
import com.soft1851.swl.face.entity.Admin;
import com.soft1851.swl.face.mapper.AdminMapper;
import com.soft1851.swl.face.service.AdminService;
import com.soft1851.swl.face.service.RedisService;
import com.soft1851.swl.face.util.JwtTokenUtil;
import com.soft1851.swl.face.util.Md5Util;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
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
}
