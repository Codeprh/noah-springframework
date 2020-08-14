package com.noah.service.impl;

import com.noah.service.HelloService;
import org.springframework.stereotype.Service;

/**
 * 描述:
 *
 * @author Noah
 * @create 2020-08-08 8:02 上午
 */
@Service
public class HelloServiceImp implements HelloService {
    @Override
    public void sayHello() {
        System.out.println("say hello");
    }

    @Override
    public String justSayHello() {
        return "hello";
    }
}
