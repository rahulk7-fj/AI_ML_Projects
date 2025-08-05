package com.spring_rag.ai.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Fileupload {

    @RequestMapping("/")
    public String init(){
        return "Jesus Christ is the Lord";
    }
}
