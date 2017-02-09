package com.junbaor.sharding;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Startup {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        applicationContext.registerShutdownHook();
    }
}
