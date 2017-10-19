package com.changWen.springMVC;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author liucw
 * @version 1.0
 * @date 2017/10/19
 */
@Controller
public class IndexController {
    @RequestMapping("/")
    public void index() {
        System.out.println("Index");
    }
}
