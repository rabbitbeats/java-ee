package com.changwen.java.base1.task_executor;

//import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author changwen on 2017/7/21.
*/
//@Configurable
//@ComponentScan("com.changwen.java.base1.task_executor")
//@EnableAsync  // 利用这个注解开启异步任务支持
//public class TaskExecutorConfig implements AsyncConfigurer {
//
//    @Override
//    public Executor getAsyncExecutor() {
//        ThreadPoolTaskExecutor taskExecutor = new ThreadPoolTaskExecutor();
//        taskExecutor.setCorePoolSize(5);
//        taskExecutor.setMaxPoolSize(10);
//        taskExecutor.setQueueCapacity(25);
//        taskExecutor.initialize();
//        return taskExecutor;
//    }
//
//    @Override
//    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
//        return null;
//    }
//}
