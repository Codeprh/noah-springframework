package com.noah.controller;

import com.noah.service.HiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * 描述:
 * hellocontroller
 *
 * @author Noah
 * @create 2020-08-08 8:02 上午
 */
@Controller
public class HiController {

    @Autowired
    private HiService hiService;

    public void hi() {
        hiService.sayHi();
        hiService.justWantoException();
    }
}
