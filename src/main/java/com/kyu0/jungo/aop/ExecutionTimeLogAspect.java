package com.kyu0.jungo.aop;

import java.time.Duration;
import java.time.Instant;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.log4j.Log4j2;

@Aspect
@Component
@Log4j2
public class ExecutionTimeLogAspect {
    
    @Around("@annotation(com.kyu0.jungo.aop.ExecutionTimeLog)")
    public Object executionTimeLog(ProceedingJoinPoint joinPoint) throws Throwable {
        Instant start = Instant.now();
        Object result = joinPoint.proceed();
        Instant end = Instant.now();
        log.info("Execution Time : {}ms", Duration.between(start, end).toMillis());
        
        return result;
    }
}
