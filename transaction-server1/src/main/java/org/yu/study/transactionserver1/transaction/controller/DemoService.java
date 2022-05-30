package org.yu.study.transactionserver1.transaction.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.yu.study.transactionserver1.transaction.annotation.YhyTtransactional;
import org.yu.study.transactionserver1.transaction.util.HttpClient;

@Service
public class DemoService {

    @Autowired
    private DemoDao demoDao;

    //@YhyTtransactional(isStart = true)
    @Transactional
    public void insert() {
        demoDao.insert("server10");
        HttpClient.get("http://127.0.0.1:8082/ttt");
        int i = 100/0;
    }
}
