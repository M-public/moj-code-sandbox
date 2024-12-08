package com.meng.mojcodesandbox.unsafe;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @DESCRIPTION: 读取项目文件（获取项目文件信息）
 * @AUTHOR: MENGLINGQI
 * @TIME: 2024/11/20 22:42
 **/
public class ReadFileError {

    public static void main(String[] args) throws InterruptedException, IOException {
        String userDir = System.getProperty("user.dir");
        String filePath = userDir + File.separator + "/src/main/resources/application.yml";
        List<String> readLines = Files.readAllLines(Paths.get(filePath));
        System.out.println(String.join("\n", readLines));
    }
}
