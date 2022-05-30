package org.yu.study.transactionserver1.transaction.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.yu.study.transactionserver1.transaction.annotation.YhyTtransactional;
import org.yu.study.transactionserver1.transaction.transactional.TransactionType;
import org.yu.study.transactionserver1.transaction.transactional.YhyTrancationManager;
import org.yu.study.transactionserver1.transaction.transactional.YhyTransaction;

import java.lang.reflect.Method;

@Aspect
@Component
public class YhyTransactionAspect implements Ordered {

    @Around("@annotation(org.yu.study.transactionserver1.transaction.annotation.YhyTtransactional)")
    public void invoke(ProceedingJoinPoint joinPoint){
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        YhyTtransactional annotation = method.getAnnotation(YhyTtransactional.class);
        String groupId = "";
        if(annotation.isStart()){
            //创建事务组
            groupId = YhyTrancationManager.createYhyTransactionGroup();
        }else {
            //获取事务组id
            groupId = YhyTrancationManager.getCurrentGroupId();
        }
        //本地事务
        YhyTransaction yhyTransaction = YhyTrancationManager.createYhyTransaction(groupId);

        try {
            // spring会开启mysql事务
            joinPoint.proceed();
            YhyTrancationManager.addYhybTransaction(yhyTransaction, annotation.isEnd(), TransactionType.commit);
        } catch (Exception e) {
            YhyTrancationManager.addYhybTransaction(yhyTransaction, annotation.isEnd(), TransactionType.rollback);
            e.printStackTrace();
        } catch (Throwable throwable) {
            YhyTrancationManager.addYhybTransaction(yhyTransaction, annotation.isEnd(), TransactionType.rollback);
            throwable.printStackTrace();
        }
    }

    @Override
    public int getOrder() {
        return 100000;
    }
}
