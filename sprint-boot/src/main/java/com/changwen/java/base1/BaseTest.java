/*
package com.changwen.java.base1;

import com.changwen.java.base1.Conditional.ConditionConfig;
import com.changwen.java.base1.Conditional.ListService;
import com.changwen.java.base1.aopTest.AopConfig;
import com.changwen.java.base1.aopTest.DemoAnnotationService;
import com.changwen.java.base1.aopTest.DemoMethodService;
import com.changwen.java.base1.composit_annotion.DemoConfig;
import com.changwen.java.base1.composit_annotion.DemoService;
import com.changwen.java.base1.el.ELConfig;

import com.changwen.java.base1.event.DemoPublisher;
import com.changwen.java.base1.event.EventConfig;
import com.changwen.java.base1.schedule_task.TaskSchedulerConfig;
import com.changwen.java.base1.spring_aware.AwareConfig;
import com.changwen.java.base1.spring_aware.AwareService;
import com.changwen.java.base1.task_executor.AsyncTaskService;
import com.changwen.java.base1.task_executor.TaskExecutorConfig;
import org.junit.After;
import org.junit.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.IOException;

*/
/**
 * @author changwen on 2017/7/18.
 *//*

public class BaseTest {
    private static AnnotationConfigApplicationContext context;

    @After
    public void after() {
        context.close();
    }

    */
/**
     * 组合注解
     *//*

    @Test
    public void testCompositeAnnotation() {
        context = new AnnotationConfigApplicationContext(DemoConfig.class);
        DemoService demoService = context.getBean(DemoService.class);
        demoService.outputResult();  // 从组合注解配置照样获得的Bean
    }

    */
/**
     * 前面我们可以通过活动的profile获得不同的Bean。
     * -@Conditional根据满足某一个特定条件创建一个特定的Bean。
     *//*

    @Test
    public void testCondition() {
        context = new AnnotationConfigApplicationContext(ConditionConfig.class);

        ListService listService = context.getBean(ListService.class);
        System.out.println(context.getEnvironment().getProperty("os.name")
                + "系统下的列表命令为：" + listService.showListCmd());  // Mac OS X系统下的列表命令为：ls
    }

    */
/**
     * 计划/定时任务
     * -@EnableScheduling 注解开启对计划任务的支持，然后在要执行计划任务的方法上注解@Schedule，声明这是一个计划任务
     * 注解@Scheduled支持多种类型的计划任务，包含cron, fixDelay, fixRate
     *//*

    @Test
    public void testSchedule() {
        context = new AnnotationConfigApplicationContext(TaskSchedulerConfig.class);
    }

    */
/**
     * Spring 通过任务执行器(TaskExecutor)来实现多线程和并发编程。
     * 使用ThreadPoolExecutor可实现一个基于线程池的TaskExecutor。
     * 而实际开发中任务一般是非阻碍的，即异步的，
     * 所以我们要在配置类中通过@EnableAsync开启对异步任务的支持，并通过在实际执行的Bean的方法中使用@Async注解来声明其是一个异步任务。
     * <p>
     * Java: ThreadPoolExecutor [newSingleThreadExecutor, newFixedThreadPool(固定数量)，newFixedThreadPool(默认60S可重用里面的线程)，newScheduledThreadPool(定时，周期)]
     *//*

    @Test
    public void testTaskExecutor() throws InterruptedException {
        context = new AnnotationConfigApplicationContext(TaskExecutorConfig.class);

        AsyncTaskService asyncTaskService = context.getBean(AsyncTaskService.class);
        for (int i = 0; i < 10; i++) {
            asyncTaskService.executeAsyncTask(i);
            asyncTaskService.executeAsyncTaskPlus(i);
        }
        Thread.sleep(1000);  // 这里需要让其睡眠一下，不然直接执行完后，线程池里的线程还没执行主线程就结束了
        System.out.println("主线程:  " + Thread.currentThread().getName());

        // 结果是并发执行的，而不是顺序执行的
    }

    */
/**
     * Spring Aware的目的是为了让Bean获得Spring容器的服务。
     * 因为ApplicationContext接口集成了MessageSource接口、ApplicationEventPublisher接口和ResourceLoader接口，
     * 所以Bean继承ApplicationContextAware可以获得Spring容器的所有服务，但原则上我们还是用到什么接口，就实现什么接口。
     *//*

    @org.junit.Test
    public void testSpringAware() throws IOException {
        context = new AnnotationConfigApplicationContext(AwareConfig.class);

        AwareService awareService = context.getBean(AwareService.class);
        awareService.outputResult();

//        Bean 的名称：awareService
//        ResourceLoader加载的文件内容为：123
    }

    // -------------------------------------------

    */
/**
     * Spring的事件需要遵循如下流程：
     * 1.自定义事件，继承ApplicationEvent
     * 2.定义事件监听器，实现ApplicationListener
     * 3.使用容器发布事件。
     *//*

    @org.junit.Test
    public void testEvent() {
        context = new AnnotationConfigApplicationContext(EventConfig.class);

        DemoPublisher publisher = context.getBean(DemoPublisher.class);
        publisher.publish("hello application event");

        // 我DemoListener 接收到了 demoPublisher发布的消息：hello application event
    }

    @org.junit.Test
    public void testEL() {
        context = new AnnotationConfigApplicationContext(ELConfig.class);
        ELConfig elConfig = context.getBean(ELConfig.class);
        elConfig.outputResource();
    }

    @org.junit.Test
    public void testAopTest() {
        context = new AnnotationConfigApplicationContext(AopConfig.class);

        DemoAnnotationService demoAnnotationService = context.getBean(DemoAnnotationService.class);
        demoAnnotationService.add();

        DemoMethodService methodService = context.getBean(DemoMethodService.class);
        methodService.add();

        //    注解式拦截：注解式拦截的add操作
        //    方法规则式拦截：add
    }
}
*/
