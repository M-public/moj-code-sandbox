package com.meng.mojcodesandbox.controller;

import com.meng.mojcodesandbox.JavaNativeCodeSandBox;
import com.meng.mojcodesandbox.model.ExecuteCodeRequest;
import com.meng.mojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @DESCRIPTION: description
 * @AUTHOR: MENGLINGQI
 * @TIME: 2024/11/13 16:41
 **/
@RestController("/")
public class MainController {

    @Resource
    private JavaNativeCodeSandBox javaNativeCodeSandBox;

    @GetMapping("/health")
    public String healthCheck(){
        return "ok";
    }


    @PostMapping("/executeCode")
    ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest){

        if (executeCodeRequest == null){
            throw new RuntimeException("参数为空");
        }
        return javaNativeCodeSandBox.executeCode(executeCodeRequest);
    }


}
