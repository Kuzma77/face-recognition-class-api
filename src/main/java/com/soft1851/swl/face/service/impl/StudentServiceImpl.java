package com.soft1851.swl.face.service.impl;

import com.soft1851.swl.face.common.ResponseResult;
import com.soft1851.swl.face.common.ResultCode;
import com.soft1851.swl.face.dto.*;
import com.soft1851.swl.face.entity.Student;
import com.soft1851.swl.face.entity.StudentSubject;
import com.soft1851.swl.face.entity.Subject;
import com.soft1851.swl.face.enums.AttendStatue;
import com.soft1851.swl.face.enums.LogType;
import com.soft1851.swl.face.exception.CustomException;
import com.soft1851.swl.face.mapper.StudentMapper;
import com.soft1851.swl.face.mapper.StudentSubjectMapper;
import com.soft1851.swl.face.mapper.SubjectMapper;
import com.soft1851.swl.face.mo.Log;
import com.soft1851.swl.face.service.FaceService;
import com.soft1851.swl.face.service.LogService;
import com.soft1851.swl.face.service.RedisService;
import com.soft1851.swl.face.service.StudentService;
import com.soft1851.swl.face.util.JwtTokenUtil;
import com.soft1851.swl.face.util.Md5Util;
import com.soft1851.swl.face.util.RandomNumUtil;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.NumberFormat;
import java.util.*;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/7
 */

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class StudentServiceImpl implements StudentService {

    public final StudentMapper studentMapper;
    public final SubjectMapper subjectMapper;
    public final JwtTokenUtil jwtTokenUtil;
    public  final RedisService redisService;
    public  final LogService logService;
    public final FaceService faceService;
    public final StudentSubjectMapper studentSubjectMapper;
    Double allCount = 0.00;
    Double signCount = 0.00;
    Double attendance = 0.00;
    Integer factAttendance = 0;
    Integer shouldAttendance = 0;
    Integer notAttendance = 0;
    Integer noteAttendance = 0;

    @Override
    public List<Student> queryAllStudent() {
        return this.studentMapper.selectAll();
    }

    @Override
    public ResponseResult getStudentById(String studentId) {
        Student student = this.studentMapper.getStudentById(studentId);
        if(student!=null){
            UserDto userDto = UserDto.builder()
                    .id(studentId)
                    .name(student.getStudentName())
                    .avatar(student.getAvatar())
                    .gender(student.getGender())
                    .phoneNumber(student.getPhoneNumber())
                    .build();
            return ResponseResult.success(userDto);
        }else {
            return ResponseResult.failure(ResultCode.USER_NOT_FOUND);
        }
    }

    @Override
    public ResponseResult loginByStudentId(LoginDto loginDto) {
        Student student = this.studentMapper.selectByPrimaryKey(loginDto.getId());
        System.out.println(student);
        if(student!=null){
            String pass = Md5Util.getMd5(loginDto.getPassword(),true,32);
            if(student.getPassword().equals(pass)){
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
                        .operatorId(loginDto.getId())
                        .objectId(loginDto.getId())
                        .content("账密登录成功")
                        .createTime(new Date())
                        .build();
                this.logService.saveOneLog(log);
                return ResponseResult.success(loginResDto);
            }else {
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
        }else {
            log.error("该学生账号不存在");
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
    public ResponseResult updateStatus(String studentId) {
        Student student = this.studentMapper.selectByPrimaryKey(studentId);
        if(student!=null){
            Integer deleteFlag = student.getDeleteFlag();
            Integer newInteger = deleteFlag == 0 ? 1:0;
            this.studentMapper.updateStatus(newInteger,studentId);
            log.info("学生{}的状态修改成功",studentId);
            Log log = Log.builder()
                    .id(RandomNumUtil.getVerifyCode(8))
                    .type(LogType.UPDATE.value)
                    .operatorId(studentId)
                    .objectId(studentId)
                    .content("学生删除状态修改")
                    .createTime(new Date())
                    .build();
            this.logService.saveOneLog(log);
            return ResponseResult.success();
        }else{
            log.error("该学生账号不存在");
            return ResponseResult.failure(ResultCode.USER_NOT_FOUND);
        }
    }

    @Override
    public ResponseResult updatePassword(String password, String studentId) {
        Student student = this.studentMapper.selectByPrimaryKey(studentId);
        if(student!=null){
            String newPass = Md5Util.getMd5(password,true,32);
            this.studentMapper.updatePassword(newPass,studentId);
            log.info("学生{}的密码修改成功",studentId);
            Log log = Log.builder()
                    .id(RandomNumUtil.getVerifyCode(8))
                    .type(LogType.UPDATE.value)
                    .operatorId(studentId)
                    .objectId(studentId)
                    .content("修改密码成功")
                    .createTime(new Date())
                    .build();
            this.logService.saveOneLog(log);
            return ResponseResult.success();
        }else{
            log.error("该学生账号不存在");
            return ResponseResult.failure(ResultCode.USER_NOT_FOUND);
        }
    }



    @Override
    public ResponseResult sign(SignDto signDto) throws Exception {
        SearchFaceDto searchFaceDto = SearchFaceDto.builder()
                .DbName(signDto.getSearchFaceDto().getDbName())
                .DbNames(signDto.getSearchFaceDto().getDbNames())
                .imgUrl(signDto.getSearchFaceDto().getImgUrl())
                .limit(signDto.getSearchFaceDto().getLimit())
                .build();
        //1.通过人脸url去库中搜索,返回用户id
        Map<String, Object> map = this.faceService.searchFace(searchFaceDto);
        log.info("对比结果为"+map.get("score"));
        if(Double.valueOf((Double) map.get("score"))>0.7){
            String userId = map.get("entityId").toString();
            //2.根据用户id判断是否为该课学生
            StudentSubject studentSubject = this.studentSubjectMapper.queryByStudentIdAndSubjectId(userId,signDto.getSubjectId());
            if(studentSubject!=null){
                //3.看是否已经签过到
                if(studentSubject.getAttendFlag()==0){
                    //已经签过到直接返回
                    return ResponseResult.failure(ResultCode.USER_HAS_SIGNED);
                }
                //4.签到成功,更改状态
                StudentSubject studentSubject1 = StudentSubject.builder()
                        .studentId(userId)
                        .subjectId(signDto.getSubjectId())
                        .attendFlag(AttendStatue.HASATTENDED.type)
                        .attendTime(new Date())
                        .build();
                this.studentSubjectMapper.updateAttendStatue(studentSubject1);
                log.info(userId+"签到成功");
                return ResponseResult.success(userId);
            }else {
                return ResponseResult.failure(ResultCode.FACE_SEARCH_FAIL);
            }
        }
        return ResponseResult.failure(ResultCode.USER_FACE_LOGIN_ERROR);
    }

    @Override
    public ResponseResult querySubjectsByStudentId(String studentId) {
        List<StudentSubject> subjects = this.studentSubjectMapper.querySubjectsByStudentId(studentId);
        Map<String,Object> map = new HashMap<>(8);
        map.put("studentId",studentId);
        List<Map<String,Object>> list = new ArrayList<>();
        if(subjects!=null){
            subjects.forEach(subject -> {
                Map<String,Object> map1 = new HashMap<>(8);
                map1.put("subject",this.subjectMapper.selectByPrimaryKey(subject.getSubjectId()));
                map1.put("signStatue",subject.getAttendFlag());
                if(subject.getAttendFlag()==0){
                    map1.put("signTime",subject.getAttendTime());
                }
                list.add(map1);
            });
            map.put("subjects",list);
            return ResponseResult.success(map);
        }else {
            map.put("subjects",list);
            return ResponseResult.success(map);
        }
    }

    @Override
    public ResponseResult queryAttendanceByStudentId(String studentId) {
        //1.查询当前时间之前的所有课
        List<StudentSubject> subjects = this.studentSubjectMapper.querySubjectsByStudentId(studentId);

        subjects.forEach(studentSubject -> {
            if(this.subjectMapper.selectByPrimaryKey(studentSubject.getSubjectId()).getBeginTime().before(new Date())){
                allCount++;
                if(studentSubject.getAttendFlag()==0){
                    signCount++;
                }
            }
        });
        System.out.println(allCount);
        System.out.println(signCount);
        attendance = signCount/allCount;
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(2);
        System.out.println(nf.format(attendance));
        SeriesDto[] seriesDtos = new SeriesDto[1];
        SeriesDto seriesDto = SeriesDto.builder()
                .color("#1890ff")
                .name("出勤率")
                .data(Double.valueOf(nf.format(attendance)))
                .build();
        seriesDtos[0] = seriesDto;
        return ResponseResult.success(seriesDtos);
    }

    @Override
    public ResponseResult queryAttendance(String studentId) {
        List<StudentSubject> subjects = this.studentSubjectMapper.querySubjectsByStudentId(studentId);
        subjects.forEach(studentSubject -> {
            if(this.subjectMapper.selectByPrimaryKey(studentSubject.getSubjectId()).getBeginTime().before(new Date())){
                shouldAttendance++;
                if(studentSubject.getAttendFlag()==0){
                    factAttendance++;
                }
                if(studentSubject.getAttendFlag()==1){
                    notAttendance++;
                }
                if(studentSubject.getAttendFlag()==2){
                    noteAttendance++;
                }
            }
        });
        Integer[] integers = new Integer[4];
        integers[0] = factAttendance;
        integers[1] = shouldAttendance;
        integers[2] = notAttendance;
        integers[3] = noteAttendance;
        return ResponseResult.success(integers);
    }
}
