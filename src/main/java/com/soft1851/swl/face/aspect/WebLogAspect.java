package com.soft1851.swl.face.aspect;

import com.soft1851.swl.face.annocation.ControllerWebLog;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/7
 */
@Aspect
@Component
@Order(100)
@Slf4j
public class WebLogAspect {

    private ThreadLocal<Map<String,Object>> threadLocal = new ThreadLocal<>();
    /**
     * 定义切点
     *
     */
    @Pointcut("execution(public * com.soft1851.swl.face.controller..*.*(..))")
    public void webLog(){
    }

    @Before(value = "webLog() && @annotation(controllerWebLog)")
    public void doBefore(JoinPoint joinPoint, ControllerWebLog controllerWebLog){
        //接受请求
        RequestAttributes at = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) at;
        //以下通过连接点和注解获取信息
        assert sra != null;
        HttpServletRequest request = sra.getRequest();
        log.info("请求URI:"+request.getRequestURI());
        log.info("请求URL:"+request.getRequestURL());
        log.info("请求头的User-Agent:"+request.getHeader("User-Agent"));
        log.info("请求地址："+request.getRemoteAddr());
        log.info("连接点对象通过反射获得类名和方法名"+joinPoint.getSignature().getDeclaringTypeName()+"."+
                joinPoint.getSignature().getName());
        log.info("AOP拦截获得参数:"+ Arrays.toString(joinPoint.getArgs()));
        log.info("执行方法:"+controllerWebLog.name());
        //定义一个Map用来记录日志信息，并将其put入threadLocal
        Map<String,Object> map = new HashMap<>(16);
        map.put("uri",request.getRequestURI());
        map.put("url",request.getRequestURL());
        map.put("user-agent",request.getHeader("User-Agent"));
        map.put("request-method",request.getMethod());
        map.put("remote-address",request.getRemoteAddr());
        map.put("class-method",joinPoint.getSignature().getDeclaringTypeName()+"."+
                joinPoint.getSignature().getName());
        map.put("argument",Arrays.toString(joinPoint.getArgs()));
        map.put("execute-method",controllerWebLog.name());
        threadLocal.set(map);
    }

    @AfterReturning(value = "webLog()&& @annotation(controllerWebLog)",returning = "ret")
    public void doAfterReturning(ControllerWebLog controllerWebLog,Object ret) throws Throwable{
        //从当前线程变量取出数据
        Map<String,Object> threadInfo = threadLocal.get();
        //将请求的目标方法的执行返回结果存入线程对象
        threadInfo.put("result",ret);
        if(controllerWebLog.isSaved()){
            log.info("日志已经存入数据库");
        }
        //处理请求完成，返回类容
        log.info("RESPONSE:"+ret);
    }


    @Around(value = "webLog()")
    public Object doAround(ProceedingJoinPoint proceedingJoinPoint) throws Throwable{
        //得到开始时间
        long startTime = System.currentTimeMillis();
        //执行连接点的目标方法
        Object ob = proceedingJoinPoint.proceed();
        Map<String,Object> threadInfo = threadLocal.get();
        //计算方法执行时间
        Long takeTime = System.currentTimeMillis() - startTime;
        //存入线程变量
        threadInfo.put("takeTime",takeTime);
        log.info("耗时："+takeTime);
        threadLocal.set(threadInfo);
        return  ob;
    }


    @AfterThrowing(value = "webLog()", throwing = "throwable")
    public void doAfterThrowing(Throwable throwable){
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
        assert sra != null;
        HttpServletRequest request = sra.getRequest();
        //异常信息
        log.info("{}接口调用异常,异常信息{}",request.getRequestURI(), throwable);
    }
}

