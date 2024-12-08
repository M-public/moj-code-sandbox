package com.meng.mojcodesandbox.controller;

import com.meng.mojcodesandbox.JavaNativeCodeSandBox;
import com.meng.mojcodesandbox.model.ExecuteCodeRequest;
import com.meng.mojcodesandbox.model.ExecuteCodeResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * @DESCRIPTION: description
 * @AUTHOR: MENGLINGQI
 * @TIME: 2024/11/13 16:41
 **/
@RestController("/")
public class MainController {

    // 定义鉴权请求头和密钥
    private static final String AUTH_REQUEST_HEADER = "auth";
    private static final String AUTH_REQUEST_SECRET = "secretKey";

    @Resource
    private JavaNativeCodeSandBox javaNativeCodeSandBox;

    @GetMapping("/health")
    public String healthCheck(){
        return "ok";
    }


    @PostMapping("/executeCode")
    ExecuteCodeResponse executeCode(@RequestBody ExecuteCodeRequest executeCodeRequest, HttpServletRequest request,
                                    HttpServletResponse response){

        String auth = request.getHeader(AUTH_REQUEST_HEADER);
        if (!AUTH_REQUEST_SECRET.equals(auth)){
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            return null;
        }

        if (executeCodeRequest == null){
            throw new RuntimeException("参数为空");
        }
        return javaNativeCodeSandBox.executeCode(executeCodeRequest);
    }


}
