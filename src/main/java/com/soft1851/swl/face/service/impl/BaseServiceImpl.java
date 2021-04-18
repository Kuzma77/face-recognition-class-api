package com.soft1851.swl.face.service.impl;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.common.ResultCode;
import com.soft1851.swl.face.enums.LogType;
import com.soft1851.swl.face.mapper.AdminMapper;
import com.soft1851.swl.face.mapper.StudentMapper;
import com.soft1851.swl.face.mapper.TeacherMapper;
import com.soft1851.swl.face.mo.Log;
import com.soft1851.swl.face.service.BaseService;
import com.soft1851.swl.face.service.LogService;
import com.soft1851.swl.face.service.RedisService;
import com.soft1851.swl.face.util.RandomNumUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/10
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class BaseServiceImpl implements BaseService {


    public final RedisService redisService;
    public final StudentMapper studentMapper;
    public final TeacherMapper teacherMapper;
    public final AdminMapper adminMapper;
    public final LogService logService;

    @Override
    public ResponseResult layout(String userId) {
        if(redisService.existsKey("USER_TOKEN:"+userId)){
            redisService.removeKey("USER_TOKEN:"+userId);
            Log log = Log.builder()
                    .id(RandomNumUtil.getVerifyCode(8))
                    .type(LogType.LAYOUT.value)
                    .operatorId(userId)
                    .objectId(userId)
                    .content("退出登录")
                    .createTime(new Date())
                    .build();
            this.logService.saveOneLog(log);
            return ResponseResult.success();
        }
        return ResponseResult.failure(ResultCode.PARAM_IS_INVALID);
    }

    @Override
    public Map<String,Object> getUserById(String userId) {
        Map<String,Object> map = new HashMap<>(8);
        if(this.studentMapper.getStudentById(userId)!=null){
            map.put("role","student");
            map.put("data",this.studentMapper.getStudentById(userId));
            return map;
        }
        else if(this.teacherMapper.getTeacherById(userId)!=null){
            map.put("role","teacher");
            map.put("data",this.teacherMapper.getTeacherById(userId));
            return map;
        }
            map.put("role","admin");
            map.put("data",this.adminMapper.getAdminById(userId));
            return map;
    }
}
