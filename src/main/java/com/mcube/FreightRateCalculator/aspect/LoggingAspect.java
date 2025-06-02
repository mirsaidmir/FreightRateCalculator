package com.mcube.FreightRateCalculator.aspect;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.hibernate.mapping.Join;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Arrays;

@Component
@Aspect
@Slf4j
public class LoggingAspect {

    @Pointcut("execution(public * com.mcube.FreightRateCalculator.controller.*.*(..))")
    public void controllerLog() {
    }

    @Pointcut("execution(public * com.mcube.FreightRateCalculator.service.*.*(..))")
    public void serviceLog() {
    }

    @Before("controllerLog()")
    public void doBeforeController(JoinPoint joinPoint) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = null;
        if (attributes != null) {
            request = attributes.getRequest();
        }
        if (request != null) {
            log.info("NEW REQUEST: IP: {}, URL: {}, HTTP_METHOD: {}, CONTROLLER_METHOD: {}.{}",
                    request.getRemoteAddr(),
                    request.getRequestURL().toString(),
                    request.getMethod(),
                    joinPoint.getSignature().getDeclaringTypeName(),
                    joinPoint.getSignature().getName()
            );
        }
    }

    @Before("serviceLog()")
    public void doBeforeService(JoinPoint joinPoint) {
        String className = joinPoint.getSignature().getDeclaringTypeName();
        String methodName = joinPoint.getSignature().getName();

        Object[] args = joinPoint.getArgs();
        String argsString = args.length > 0 ? Arrays.toString(args) : "Method has no arguments";

        log.info("RUN SERVICE: SERVICE_METHOD: {}.{} METHOD ARGUMENTS: ({}) ",
                className, methodName, argsString);
    }

    @AfterReturning(returning = "returningObject", pointcut = "controllerLog()")
    public void doAfterReturning(Object returnObject, JoinPoint joinPoint) {
        log.info("Return value: {}", returnObject);
    }

    @After("controllerLog()")
    public void doAfter(JoinPoint joinPoint) {
        log.info("Controller method executed successfully: {}.{}",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName());
    }

    @Around("controllerLog()")
    public Object logExecutionTime(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object proceed = joinPoint.proceed();
        long finish = System.currentTimeMillis();

        log.info("Execution method {}.{} Execution time: {}ms.",
                joinPoint.getSignature().getDeclaringTypeName(),
                joinPoint.getSignature().getName(),
                finish - start);

        return proceed;
    }

    @AfterThrowing(throwing = "ex", pointcut = "controllerLog()")
    public void throwsException(JoinPoint joinPoint, Exception ex) {
        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();

        log.error("Exception in{}.{} with arguments {}. Exception message: {}",
                className, methodName, Arrays.toString(joinPoint.getArgs()), ex.getMessage());

    }
}
