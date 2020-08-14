package com.noah;

import com.noah.controller.HelloController;
import com.noah.controller.HiController;
import com.noah.entity.User;
import com.noah.entity.factory.UserFactoryBean;
import com.noah.service.WelcomeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.support.FileSystemXmlApplicationContext;

@EnableAspectJAutoProxy
@Configuration
@ComponentScan("com.noah")
public class Entrance {
    public static void main1(String[] args) {

        System.out.println("Hello World!");
        String xmlPath = "/Users/codingprh/Documents/javaSoftware/noah_cloud/spring/springframework5.2.0.release/springdemo/src/main/resources/spring/spring-config.xml";
        ApplicationContext applicationContext = new FileSystemXmlApplicationContext(xmlPath);
        WelcomeService welcomeService = (WelcomeService) applicationContext.getBean("welcomeService");
        welcomeService.sayHello("强大的spring框架");

        //得到无参构造函数创建的对象:
        User user1a = (User) applicationContext.getBean("user1");
        User user1b = (User) applicationContext.getBean("user1");
//得到静态工厂创建的对象：
        User user2a = (User) applicationContext.getBean("user2");
        User user2c = (User) applicationContext.getBean("user2");
//得到实例工厂创建的对象：
        User user3a = (User) applicationContext.getBean("user3");
        User user3b = (User) applicationContext.getBean("user3");

        //得到beanFactory创建的对象：
        UserFactoryBean userFactoryBean4a = (UserFactoryBean) applicationContext.getBean("&userFactoryBean");
        User user4b = (User) applicationContext.getBean("userFactoryBean");

        System.out.println("无参构造函数创建的对象:" + user1a);
        System.out.println("无参构造函数创建的对象:" + user1b);
        System.out.println("静态工厂创建的对象：" + user2a);
        System.out.println("静态工厂创建的对象：" + user2c);
        System.out.println("实例工厂创建的对象：" + user3a);
        System.out.println("实例工厂创建的对象：" + user3b);
        System.out.println("factorybean对象：" + userFactoryBean4a);
        System.out.println("factorybean创建的对象：" + user4b);

    }

    public static void main(String[] args) {

        ApplicationContext applicationContext = new AnnotationConfigApplicationContext(Entrance.class);
        HiController hiController = (HiController) applicationContext.getBean("hiController");
        hiController.hi();

        HelloController helloController = (HelloController) applicationContext.getBean("helloController");
        helloController.hello();

        //String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        //
        //for (String beanDefinitionName : beanDefinitionNames) {
        //    System.out.println(beanDefinitionName);
        //}
        //
        //WelcomeController welcomeController = (WelcomeController) applicationContext.getBean("welcomeController");
        //welcomeController.handleRequest();
        //User user5 = (User) applicationContext.getBean("user5");
        //System.out.println("CustomizedBeanDefinitionRegistryPostProcessor创建的对象：" + user5);
    }
}
