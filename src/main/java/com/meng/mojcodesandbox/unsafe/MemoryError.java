package com.meng.mojcodesandbox.unsafe;

import java.util.ArrayList;
import java.util.List;

/**
 * @DESCRIPTION: 无限占用空间（浪费系统内存）
 * @AUTHOR: MENGLINGQI
 * @TIME: 2024/11/20 22:42
 **/
public class MemoryError {
    public static void main(String[] args) throws InterruptedException {
        List<byte[]> list = new ArrayList<>();
        while (true)
            list.add(new byte[1024 * 1024]);
    }
}
