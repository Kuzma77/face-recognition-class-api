package com.soft1851.swl.face.service.impl;

import com.soft1851.swl.face.service.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author wl_sun
 * @description TODO
 * @Data 2021/4/7
 */
@Service
@Slf4j
public class RedisServiceImpl implements RedisService {
    @Resource
    private RedisTemplate<Serializable, Object> redisTemplate;

    /**
     * 添加 Redis术语中 string 类型的数据
     *
     * @param key   String类型
     * @param value 任意类型，但必须实现序列化接口
     * @return boolean
     */
    @Override
    public boolean set(final String key, Object value) {
        boolean result = false;
        try {
            // 创建对简单值(Redis术语中的string类型)执行操作的对象
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 添加 Redis术语中 string 类型的数据,并设置超时
     *
     * @param key        String类型
     * @param value      任意类型，但必须实现序列化接口
     * @param expireTime 单位分钟
     * @return boolean
     */
    @Override
    public boolean set(final String key, Object value, Long expireTime) {
        boolean result = false;
        try {
            // 创建对简单值(Redis术语中的string类型)执行操作的对象
            ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
            operations.set(key, value);
            redisTemplate.expire(key, expireTime, TimeUnit.MINUTES);
            result = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 判断 key 是否存在
     *
     * @param key
     * @return boolean
     */
    @Override
    public boolean existsKey(final String key) {
        return redisTemplate.hasKey(key);
    }

    /**
     * 根据( Redis 术语中 string 类型的) key 获取值,如果出现异常则返回null
     *
     * @param key
     * @param type
     * @param <T>  存入 redis 时的类型
     * @return T
     */
    @Override
    public <T> T getValue(final String key, Class<T> type) {
        Object result;
        ValueOperations<Serializable, Object> operations = redisTemplate.opsForValue();
        try {
            result = operations.get(key);
            if (result == null) {
                return null;
            }
            // 将 Object 类型强转成 type 对应的类型
            return type.cast(result);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除对应的value
     *
     * @param key
     */
    @Override
    public void removeKey(final String key) {
        // 检查 key 是否存在
        if (existsKey(key)) {
            redisTemplate.delete(key);
        }
    }

    /**
     * 批量删除对应的value
     *
     * @param keys
     */
    @Override
    public void remove(final String... keys) {
        for (String key : keys) {
            removeKey(key);
        }
    }

    /**
     * 批量删除key
     *
     * @param pattern
     */
    @Override
    public void removePattern(final String pattern) {
        // 获取所有匹配的键
        Set<Serializable> keys = redisTemplate.keys(pattern);
        if (keys != null && keys.size() > 0) {
            redisTemplate.delete(keys);
        }
    }
}
