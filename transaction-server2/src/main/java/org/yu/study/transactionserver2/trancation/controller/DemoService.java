package org.yu.study.transactionserver2.trancation.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yu.study.transactionserver2.trancation.annotation.YhyTtransactional;

@Service
public class DemoService {

    @Autowired
    private DemoDao demoDao;

    //@YhyTtransactional(isEnd = true)
    @Transactional
    public void insert() {
        demoDao.insert("server11");
    }
}
