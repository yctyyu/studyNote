package org.yu.study.transactionserver1.transaction.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Autowired
    private DemoService demoService;

    @GetMapping("/ttt")
    public void insert(){
        demoService.insert();
    }
    @GetMapping("/test")
    public void insert1(){
        demoService.insert();
    }
}
