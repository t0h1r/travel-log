package uz.travellog.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ExceptionHandlingAspect {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @AfterThrowing(pointcut = "execution(* uz.travellog.service.*.*(..))", throwing = "exception")
    public void handleException(JoinPoint joinPoint, Throwable exception) {
        logger.error("Exception in: " + joinPoint.getSignature().toShortString());
        logger.error("Exception message: " + exception.getMessage());
    }

}
