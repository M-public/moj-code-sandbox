package com.meng.mojcodesandbox.utils;

import com.meng.mojcodesandbox.model.ExecuteMessage;
import org.springframework.util.StopWatch;

import java.io.*;

/**
 * @DESCRIPTION: 进程工具类
 * @AUTHOR: MENGLINGQI
 * @TIME: 2024/11/15 10:07
 **/
public class ProcessUtils {

    /**
     * 运行进程，获取执行结果（静态输入）
     * @param runProcess
     * @param opName
     * @return
     */
    public static ExecuteMessage runProcessAndGetMessage(Process runProcess, String opName) {
        ExecuteMessage executeMessage = new ExecuteMessage();
        try{
            // 记录程序执行时间
            StopWatch stopWatch = new StopWatch();
            stopWatch.start();
            // 等待程序执行结束，获取错误码
            int exitValue = runProcess.waitFor();
            executeMessage.setExitValue(exitValue);
            // 正常退出
            if (exitValue == 0){
                System.out.println(opName + "成功");

                // 分批获取进程的输出。
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                StringBuilder compileOutputStringBuilder = new StringBuilder();
                // 逐行读取
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null)
                    compileOutputStringBuilder.append(compileOutputLine).append("\n");
                executeMessage.setMessage(compileOutputStringBuilder.toString());
            } else {
                // 异常退出
                System.out.println(opName + "失败，错误码：" + exitValue);

                // 分批获取进程的输出。
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(runProcess.getInputStream()));
                StringBuilder compileOutputStringBuilder = new StringBuilder();
                // 逐行读取返回信息
                String compileOutputLine;
                while ((compileOutputLine = bufferedReader.readLine()) != null)
                    compileOutputStringBuilder.append(compileOutputLine).append("\n");
                System.out.println(compileOutputStringBuilder.toString());


                // 分批获取进程的错误输出。
                BufferedReader errorBufferedReader = new BufferedReader(new InputStreamReader(runProcess.getErrorStream()));
                StringBuilder errorCompileOutputStringBuilder = new StringBuilder();
                // 逐行读取错误信息
                String errorCompileOutputLine;
                while ((errorCompileOutputLine = errorBufferedReader.readLine()) != null)
                    errorCompileOutputStringBuilder.append(errorCompileOutputLine);
                executeMessage.setErrorMessage(errorCompileOutputStringBuilder.toString());
            }
            stopWatch.stop();
            long runTaskTimeMillis = stopWatch.getLastTaskTimeMillis();
            executeMessage.setTime(runTaskTimeMillis);
        }catch (Exception e){
            e.printStackTrace();
        }
        return executeMessage;
    }

    /**
     *
     * @param runProcess
     * @param opName
     * @param args
     * @return
     */
    public static ExecuteMessage runInteractProcessAndGetMessage(Process runProcess, String opName, String args) {
        ExecuteMessage executeMessage = new ExecuteMessage();

        try{

            // 交互式向控制台输入用例
            OutputStream outputStream = runProcess.getOutputStream();

            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream);
            // 拼接命令行参数，加入回车符表示一个参数输入结束
            String[] argsArray = args.split(" ");
            String joinedArgs = String.join("\n", argsArray) + "\n";
            outputStreamWriter.write(joinedArgs);
            // 相当于按了回车，执行输入
            outputStreamWriter.flush();

            // 分批获取进程的输出。
            InputStream inputStream = runProcess.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder compileOutputStringBuilder = new StringBuilder();
            // 逐行读取
            String compileOutputLine;
            while ((compileOutputLine = bufferedReader.readLine()) != null)
                compileOutputStringBuilder.append(compileOutputLine);
            executeMessage.setMessage(compileOutputStringBuilder.toString());

            // 一定要记得资源回收，否则会卡死
            outputStreamWriter.close();
            outputStream.close();
            inputStream.close();
            // 销毁进程
            runProcess.destroy();

        }catch (Exception e){
            e.printStackTrace();
        }
        return executeMessage;
    }
}
