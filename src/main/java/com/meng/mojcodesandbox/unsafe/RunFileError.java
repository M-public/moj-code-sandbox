package com.meng.mojcodesandbox.unsafe;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @DESCRIPTION: 执行危险程序（已写入的危险命令或者电脑上的其他程序）
 * @AUTHOR: MENGLINGQI
 * @TIME: 2024/11/20 22:42
 **/
public class RunFileError {

    public static void main(String[] args) throws InterruptedException, IOException {
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + "/src/main/resources/木马程序.bat";
        Process runProcess = Runtime.getRuntime().exec(filePath);

        runProcess.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }
        System.out.println("运行程序结束");
    }
}
