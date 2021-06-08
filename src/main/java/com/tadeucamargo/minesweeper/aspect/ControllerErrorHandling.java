package com.tadeucamargo.minesweeper.aspect;


import com.tadeucamargo.minesweeper.exception.HttpException;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class ControllerErrorHandling {

    @Around("execution(* com.tadeucamargo.minesweeper.controller..*.*(..))")
    public Object respondAfterThrowing(ProceedingJoinPoint pjp) {
        try {
            return pjp.proceed();
        } catch (HttpException e) {
            return ResponseEntity.status(e.getHttpCode()).body(e.getMessage());
        } catch (Exception e) {
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return null;
    }
}