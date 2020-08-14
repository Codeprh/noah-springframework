package com.noah.controller;

import com.noah.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * 描述:
 * helloController
 *
 * @author Noah
 * @create 2020-08-08 8:04 上午
 */
@Controller
public class HelloController {

    @Autowired
    private HelloService helloService;

    public void hello() {
        helloService.sayHello();
        helloService.justSayHello();
    }
}
