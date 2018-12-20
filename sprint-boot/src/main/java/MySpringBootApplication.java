/*
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
*/

/**
 * SpringBootApplication这个注解是Spring Boot核心注解，也是一个组合注解，
 * 该注解里会自动扫描这个注解 【所在类的同级包以及下级包里的Bean】
 *
 * 通过源码可以看出，关闭特定的自定配置应该使用SpringBootApplication注解的exclude参数
 * @author changwen on 2017/7/21.
 */
/*@RestController
@SpringBootApplication
public class MySpringBootApplication {

    @RequestMapping("/")
    public String home() {
        return "hello Docker!!";
    }

    public static void main(String args[]) {
        SpringApplication.run(MySpringBootApplication.class, args);
    }
}*/
