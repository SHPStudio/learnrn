package com.learnrn1.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

public class MyLogAopHandler {
    @Pointcut("execution(* com.learnrn1..*.*(..))")
    public void logAspect() {

    }

    @Around(value = "logAspect()")
    public Object logAround(ProceedingJoinPoint pjp) throws Throwable {
        // 1.检查类上是否存在注解
        Object result = null;
        try {
            MyLog myLog = pjp.getClass().getAnnotation(MyLog.class);
            if (myLog == null) {
                // 2.检查方法上是否有注解
                MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
                Method method = methodSignature.getMethod();
                myLog = method.getAnnotation(MyLog.class);
            }
            if (myLog != null) {
                System.out.println("开始切面");
                // 3.处理切面
                if (myLog.printArgs()) {
                    printArgs(pjp.getArgs());
                }
                result = pjp.proceed();
                if (myLog.printResult()) {
                    printResult(result);
                }
                System.out.println("退出切面");
            }else {
                result = pjp.proceed();
            }
            return result;
        } catch (Exception e) {
            System.out.println(e);
            return pjp.proceed();
        }
    }

    private void printArgs(Object[] args) {

    }

    private void printResult(Object result) {

    }
}
