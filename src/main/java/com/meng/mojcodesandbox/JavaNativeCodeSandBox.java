package com.meng.mojcodesandbox;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ResourceUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.dfa.WordTree;
import com.meng.mojcodesandbox.model.ExecuteCodeRequest;
import com.meng.mojcodesandbox.model.ExecuteCodeResponse;
import com.meng.mojcodesandbox.model.ExecuteMessage;
import com.meng.mojcodesandbox.model.JudgeInfo;
import com.meng.mojcodesandbox.utils.ProcessUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

/**
 * @DESCRIPTION: description
 * @AUTHOR: MENGLINGQI
 * @TIME: 2024/11/13 18:42
 **/
@Component
public class JavaNativeCodeSandBox implements CodeSandBox {

    private static final String GLOBAL_CODE_DIR_NAME = "tmpCode";
    private static final String GLOBAL_JAVA_CLASS_NAME = "Main.java";
    // 超时时间
    private static final long TIME_OUT = 1000L;

    // 执行程序敏感关键字列表（黑名单）
    private static final List<String> SENSITIVE_KEYWORD_LIST = Arrays.asList("Runtime", "File", "exce");

    // 初始化字典树，用来校验程序是否包含黑名单关键字
    private static final WordTree wordTree;
    static {
        // 初始化敏感词字典树
        wordTree = new WordTree();
        wordTree.addWords(SENSITIVE_KEYWORD_LIST);
    }


    public static void main(String[] args) {
        JavaNativeCodeSandBox javaNativeCodeSandBox = new JavaNativeCodeSandBox();

        String code = ResourceUtil.readStr("testCode/unsafeCode/RunFileError.java", StandardCharsets.UTF_8);
//        String code = ResourceUtil.readStr("testCode/simpleComputeArgs/" + GLOBAL_JAVA_CLASS_NAME, StandardCharsets.UTF_8);

        ExecuteCodeRequest executeCodeRequest = ExecuteCodeRequest.builder()
                .inputList(Arrays.asList("1 2"))
                .code(code)
                .language("java")
                .build();

        ExecuteCodeResponse executeCodeResponse = javaNativeCodeSandBox.executeCode(executeCodeRequest);
        System.out.println(executeCodeResponse);
    }

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        // 安全管理器使用示例
        // System.setSecurityManager(new MySecurityManager());

        List<String> inputList = executeCodeRequest.getInputList();
        String code = executeCodeRequest.getCode();
        String language = executeCodeRequest.getLanguage();


        // 保存用户代码前，校验黑名单，排除危险程序
        if (wordTree.isMatch(code)){
            return ExecuteCodeResponse.builder()
                    .message("代码含有敏感关键字: " + wordTree.matchWord(code) + "，请修改")
                    .status(2)
                    .build();
        }


        // 1、把用户的代码保存为文件。
        String userDir = System.getProperty("user.dir");
        String globalCodePathName = userDir + File.separator + GLOBAL_CODE_DIR_NAME;
        // 判断全局代码目录是否存在，没有则新建
        if (!FileUtil.exist(globalCodePathName)){
            FileUtil.mkdir(globalCodePathName);
        }

        // 把用户代码隔离存放
        String userCodeParentPath = globalCodePathName + File.separator + UUID.randomUUID();
        String userCodePath = userCodeParentPath + File.separator + GLOBAL_JAVA_CLASS_NAME;
        File userCodeFile = FileUtil.writeString(code, userCodePath, StandardCharsets.UTF_8);

        // 2、编译代码，得到class文件。
        // javac -encoding utf-8 %s -d %s
        String compileCmd = String.format("javac -encoding utf-8 %s", userCodeFile.getAbsolutePath());
        try {
            Process compileProcess = Runtime.getRuntime().exec(compileCmd);
            ExecuteMessage compileMessage = ProcessUtils.runProcessAndGetMessage(compileProcess, "编译");
            System.out.println(compileMessage);
        } catch (Exception e) {
            return getErrorResponse(e);
        }

        // 3、执行代码，得到输出结果。
        List<ExecuteMessage> executeMessageList = new ArrayList<>();
        for (String inputArgs : inputList){
            // -Dfile.encoding=UTF-8 解决中文乱码问题
            String runCmd = String.format("java -Xmx256m -Dfile.encoding=UTF-8 -cp %s Main %s", userCodeParentPath, inputArgs);
            try {
                Process runProcess = Runtime.getRuntime().exec(runCmd);

                // 监控线程，超时自动杀掉执行代码线程 ()
                Thread thread = new Thread(() -> {
                    try {
                        Thread.sleep(TIME_OUT);
                        if (runProcess.isAlive()){
                            System.out.println("超时了，中断程序");
                            runProcess.destroy();
                        }

                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                });
                thread.start();

                ExecuteMessage executeMessage = ProcessUtils.runProcessAndGetMessage(runProcess, "运行");
                System.out.println(executeMessage);
                executeMessageList.add(executeMessage);
            } catch (Exception e) {
                return getErrorResponse(e);
            }
        }

        // 4、收集整理输出结果。
        List<String> outputList = new ArrayList<>();
        ExecuteCodeResponse executeCodeResponse = ExecuteCodeResponse.builder().build();
        // ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        // 程序执行最大时间（采用最大时间是因为只要有一个超时则判定为超时）
        long maxTime = 0;
        for (ExecuteMessage executeMessage : executeMessageList){
            String errorMessage = executeMessage.getErrorMessage();
            // 有错误信息
            if (StrUtil.isNotBlank(errorMessage)){
                executeCodeResponse.setMessage(errorMessage);
                // 执行中存在错误
                executeCodeResponse.setStatus(3);
                break;
            }
            outputList.add(executeMessage.getMessage());
            Long runTime = executeMessage.getTime();
            if (runTime != null){
                maxTime = Math.max(maxTime, runTime);
            }

        }
        executeCodeResponse.setOutputList(outputList);
        // 正常执行完成
        executeCodeResponse.setStatus(1);

        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setTime(maxTime);
        // 暂不统计内存耗用
        // judgeInfo.setMemory();
        executeCodeResponse.setJudgeInfo(judgeInfo);


        // 5、删除临时文件。
        if (userCodeFile.getParentFile() != null) {
            boolean del = FileUtil.del(userCodeParentPath);
            System.out.println("删除" + (del ? "成功" : "失败"));
        }
        return executeCodeResponse;
    }

    // 6、错误处理，提升程序健壮性。
    /**
     * @description:  异常处理方法，处理没有正常执行完程序的情况
     * * @param[1] e
     * @throws:
     * @return:  ExecuteCodeResponse
     */
    private ExecuteCodeResponse getErrorResponse(Throwable e){
        return ExecuteCodeResponse.builder()
                .outputList(null)
                .message(e.getMessage())
                // 表示代码沙箱错误
                .status(2)
                .judgeInfo(null)
                .build();
    }

}
