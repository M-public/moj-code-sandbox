package com.meng.mojcodesandbox.unsafe;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

/**
 * @DESCRIPTION: 写文件错误（向服务器写入木马程序）
 * @AUTHOR: MENGLINGQI
 * @TIME: 2024/11/20 22:42
 **/
public class WriteFileError {

    public static void main(String[] args) throws InterruptedException, IOException {
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + "/src/main/resources/木马程序.bat";
        String content = "echo 1 > /tmp/moj.txt";
        Files.write(Paths.get(filePath), Arrays.asList(content));
        System.out.println("写入木马成功");
    }
}
