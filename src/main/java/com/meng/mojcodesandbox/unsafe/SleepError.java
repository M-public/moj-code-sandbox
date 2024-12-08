package com.meng.mojcodesandbox.unsafe;

/**
 * @DESCRIPTION: 无限睡眠程序（阻塞程序执行）
 * @AUTHOR: MENGLINGQI
 * @TIME: 2024/11/20 22:42
 **/
public class SleepError {

    public static void main(String[] args) throws InterruptedException {
        long ONE_HOUR = 60 * 60 * 1000L;
        Thread.sleep(ONE_HOUR);
        System.out.println("程序执行完毕");
    }
}
