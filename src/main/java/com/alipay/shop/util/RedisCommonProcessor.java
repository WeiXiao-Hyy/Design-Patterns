package com.alipay.shop.util;

import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author hyy
 * @Description
 * @create 2023-12-16 22:13
 */
@Component
public class RedisCommonProcessor {

    @Autowired
    private RedisTemplate redisTemplate;

    public Object get(String key) {
        if (key == null) {
            throw new UnsupportedOperationException("Redis key could not be null!");
        }
        return redisTemplate.opsForValue().get(key);
    }

    public void set(String key, Object value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void set(String key, Object value, long time) {
        if (time > 0) {
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        } else {
            set(key, value);
        }
    }

    /**
     * 判断缓存中是否有对应的value
     *
     * @param key Redis键名
     * @return 是否存在
     */
    public Boolean exists(final String key) {
        return redisTemplate.hasKey(key);
    }

    public void remove(String key) {
        if (exists(key)) {
            redisTemplate.delete(key);
        }
    }
}