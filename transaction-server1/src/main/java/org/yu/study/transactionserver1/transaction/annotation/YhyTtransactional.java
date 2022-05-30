package org.yu.study.transactionserver1.transaction.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface YhyTtransactional {
    boolean isStart() default false;
    boolean isEnd() default false;
}
