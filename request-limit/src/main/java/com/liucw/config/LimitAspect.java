package com.liucw.config;

import com.google.common.util.concurrent.RateLimiter;
import com.liucw.annotation.RateLimit;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.data.redis.RedisAutoConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;


/**
 * 描述:拦截器
 *
 * @author liucw
 * @version 1.0
 * @date 2019/1/9
 */
@Aspect
@Component
public class LimitAspect {
    private final static Logger logger = LoggerFactory.getLogger(LimitAspect.class);
    @Autowired
    private RedisTemplate<String, Serializable> limitRedisTemplate;
    @Autowired
    private DefaultRedisScript<Number> redisluaScript;

    @Around(value = "execution(* com.liucw.controller..*(..))")
    public Object aroundNotice(ProceedingJoinPoint joinPoint) throws Throwable {
        // logger.info("拦截到了{}方法...", joinPoint.getSignature().getName());
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        Class<?> targetClass = method.getDeclaringClass();

        RateLimit rateLimit = method.getAnnotation(RateLimit.class);

        if (rateLimit != null) {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String ipAddress = getIpAddr(request);

            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append(ipAddress)
                    .append("-").append(targetClass.getName())
                    .append("- ").append(method.getName())
                    .append("-").append(rateLimit.key());

            List<String> keys = Collections.singletonList(stringBuffer.toString());

            Number number = limitRedisTemplate.execute(redisluaScript, keys, rateLimit.count(), rateLimit.time());
            if (number != null && number.intValue() != 0 && number.intValue() <= rateLimit.count()) {
                logger.info("限流时间段内访问第：{} 次", number.toString());
                return joinPoint.proceed();
            }
        } else {
            return joinPoint.proceed();
        }
        throw new RuntimeException("已经到设置限流次数");
    }


    public static String getIpAddr(HttpServletRequest request) {
        String ipAddress = null;
        try {
            // 代理http请求获取客户端IP: https://blog.csdn.net/fengwind1/article/details/51992528
            // 这些请求头都不是http协议里的标准请求头，也就是说这个是各个代理服务器自己规定的表示客户端地址的请求头

            /*
            x-forwarded-for
            这是一个 Squid 开发的字段，只有在通过了 HTTP 代理或者负载均衡服务器时才会添加该项。
            格式为X-Forwarded-For: client1, proxy1, proxy2，一般情况下，第一个ip为客户端真实ip，后面的为经过的代理服务器ip。
            现在大部分的代理都会加上这个请求头
             */
            ipAddress = request.getHeader("x-forwarded-for");
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("Proxy-Client-IP");
            }

            /*
            Proxy-Client-IP/WL-Proxy-Client-IP
            这个一般是经过apache http服务器的请求才会有，
            用apache http做代理时一般会加上Proxy-Client-IP请求头，而WL-Proxy-Client-IP是他的weblogic插件加上的头
             */
            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getHeader("WL-Proxy-Client-IP");
            }

            if (ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {
                ipAddress = request.getRemoteAddr();

            }

            // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
            // "***.***.***.***".length() = 15
            if (ipAddress != null && ipAddress.length() > 15) {
                if (ipAddress.indexOf(",") > 0) {
                    ipAddress = ipAddress.substring(0, ipAddress.indexOf(","));
                }
            }
        } catch (Exception e) {
            ipAddress = "";
        }

        return ipAddress;
    }
}
