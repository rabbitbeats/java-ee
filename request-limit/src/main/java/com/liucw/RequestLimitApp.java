package com.liucw;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author liucw
 * @version 1.0
 * @date 2019/1/9
 */
@SpringBootApplication
public class RequestLimitApp {
    // http://127.0.0.1:8080/test
    public static void main(String[] args) {
        SpringApplication.run(RequestLimitApp.class, args);
        System.out.println("------启动成功---------");
    }
}
