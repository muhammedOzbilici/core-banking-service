package com.bank.corebankingservice.util;

import com.bank.corebankingservice.service.RabbitMQSender;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class AOPLogging {
    private final RabbitMQSender rabbitMQSender;

    @Autowired
    public AOPLogging(RabbitMQSender rabbitMQSender) {
        this.rabbitMQSender = rabbitMQSender;
    }

    @AfterReturning(
            value = "execution(* com.bank.corebankingservice.service.AccountService.*(..))",
            returning = "result"
    )
    public void afterCreateAccount(JoinPoint joinPoint, Object result) {
        log.info(joinPoint.getSignature() + " " + result.toString());
        rabbitMQSender.send(result);
    }

    @AfterReturning(
            value = "execution(* com.bank.corebankingservice.service.TransactionService.*(..))",
            returning = "result"
    )
    public void afterCreateTransaction(JoinPoint joinPoint, Object result) {
        log.info(joinPoint.getSignature() + " " + result.toString());
        rabbitMQSender.send(result);
    }
}
