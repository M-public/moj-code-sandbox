package com.meng.mojcodesandbox.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @DESCRIPTION: description
 * @AUTHOR: MENGLINGQI
 * @TIME: 2024/11/13 16:41
 **/
@RestController("/")
public class MainController {

    @GetMapping("/health")
    public String healthCheck(){
        return "ok";
    }
}
