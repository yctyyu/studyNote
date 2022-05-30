package org.yu.study.transactionserver2.trancation.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.yu.study.transactionserver2.trancation.connection.YhyConnection;
import org.yu.study.transactionserver2.trancation.transactional.YhyTrancationManager;

import java.sql.Connection;

@Aspect
@Component
public class YhyDataSourceAspect {

    @Around("execution(* javax.sql.DataSource.getConnection(..))")
    public Connection around(ProceedingJoinPoint joinPoint) throws Throwable {
        //获取spring的connection  通过返回自定义类获取事务控制权
        if (YhyTrancationManager.getCurrent() != null) {
            return new YhyConnection((Connection) joinPoint.proceed(), YhyTrancationManager.getCurrent());
        } else {
            return (Connection) joinPoint.proceed();
        }
    }
}
