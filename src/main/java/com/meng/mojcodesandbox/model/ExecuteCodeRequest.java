package com.meng.mojcodesandbox.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @DESCRIPTION: 请求
 * @AUTHOR: MENGLINGQI
 * @TIME: 2024/10/16 17:08
 **/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ExecuteCodeRequest {

    private List<String> inputList;
    private String code;
    private String language;


}
