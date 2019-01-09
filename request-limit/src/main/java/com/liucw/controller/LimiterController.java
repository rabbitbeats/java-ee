package com.liucw.controller;

import com.liucw.annotation.RateLimit;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicInteger;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @author liucw
 * @version 1.0
 * @date 2019/1/9
 */
@RestController
public class LimiterController {
    @Autowired
    private RedisTemplate redisTemplate;

    // 10 秒中，可以访问10次
    @RateLimit(key = "test", time = 10, count = 10)
    @RequestMapping("/test")
    public String luaLimiter() {
        RedisAtomicInteger entityIdCounter = new RedisAtomicInteger("entityIdCounter", redisTemplate.getConnectionFactory());
        String date = DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss.SSS");
        return date + " 累计访问次数：" + entityIdCounter.getAndIncrement();
    }
}
