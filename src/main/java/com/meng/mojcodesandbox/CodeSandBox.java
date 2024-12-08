package com.meng.mojcodesandbox;


import com.meng.mojcodesandbox.model.ExecuteCodeRequest;
import com.meng.mojcodesandbox.model.ExecuteCodeResponse;

/**
 * @description: 代码沙箱接口定义
 * @throws:
 * @return:
 */
public interface CodeSandBox {

    ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest);
}
