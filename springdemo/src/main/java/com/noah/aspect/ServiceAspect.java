package com.noah.aspect;

import com.noah.declareParents.NoahDeclareParents;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * aspect使用大全
 *
 * @author codingprh
 */
@Component
@Aspect
public class ServiceAspect {

    @Pointcut("execution(* com.noah.service..*.*(..))")
    public void embed() {
    }

    @Before("embed()")
    public void before(JoinPoint joinPoint) {
        System.out.println("开始调用" + joinPoint);
    }

    @After("embed()")
    public void after(JoinPoint joinPoint) {
        System.out.println("结束调用" + joinPoint);
    }

    @Around("embed()")
    public Object aroundme(JoinPoint joinPoint) {
        long startTime = System.currentTimeMillis();
        Object returnValue = null;

        System.out.println("开始计时" + joinPoint);

        try {
            returnValue = ((ProceedingJoinPoint) joinPoint).proceed();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
            System.out.println("执行失败" + joinPoint);
        } finally {
            System.out.println("耗时=" + (System.currentTimeMillis() - startTime) + "ms");
        }
        return returnValue;
    }

    @AfterReturning(pointcut = "embed()", returning = "returnValue")
    public void afterReturning(JoinPoint joinPoint, Object returnValue) {
        System.out.println("无论是空置还是有值返回" + joinPoint + ",返回值是" + returnValue);
    }

    @AfterThrowing(pointcut = "embed()", throwing = "exception")
    public void afterException(JoinPoint joinPoint, Exception exception) {
        System.out.println("抛出异常通知" + joinPoint + ",异常信息为" + exception.getMessage());
    }

    @DeclareParents(value = "com.noah.controller..*", defaultImpl = com.noah.declareParents.NoahDeclareParentsImpl.class)
    public NoahDeclareParents noahDeclareParents;


}
