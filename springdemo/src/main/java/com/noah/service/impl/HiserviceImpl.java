package com.noah.service.impl;

import com.noah.service.HiService;
import org.springframework.stereotype.Service;

/**
 * 描述:
 * helloservice
 *
 * @author Noah
 * @create 2020-08-08 8:01 上午
 */
@Service
public class HiserviceImpl implements HiService {
    @Override
    public void sayHi() {
        System.out.println("say hi");
    }

    @Override
    public void justWantoException() {
        throw new RuntimeException("hello excepotion");
    }
}
