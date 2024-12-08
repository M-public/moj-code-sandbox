package com.meng.mojcodesandbox.model;

import lombok.Data;

/**
 * @DESCRIPTION: 进程执行信息
 * @AUTHOR: MENGLINGQI
 * @TIME: 2024/11/15 10:08
 **/
@Data
public class ExecuteMessage {

    private Integer exitValue;

    private String message;

    private String errorMessage;

    private Long time;

}
