package com.meng.mojcodesandbox.security;

import java.security.Permission;

/**
 * @DESCRIPTION: description
 * @AUTHOR: MENGLINGQI
 * @TIME: 2024/11/25 21:52
 **/
public class MySecurityManager extends SecurityManager{

    // 检查所有权限
    @Override
    public void checkPermission(Permission perm) {
        System.out.println("所有权限。");
        super.checkPermission(perm);
    }

    // 检查程序是否可执行文件
    @Override
    public void checkExec(String cmd) {
        throw new SecurityException("checkExec 权限不足：" + cmd);
    }

    // 检查程序是否可读文件
    @Override
    public void checkRead(String file) {
        throw new SecurityException("checkRead 权限不足：" + file);
    }

    // 检查程序是否可以写文件
    @Override
    public void checkWrite(String file) {
        throw new SecurityException("checkWrite 权限不足：" + file);
    }

    // 检查程序是否可以删除文件
    @Override
    public void checkDelete(String file) {
        throw new SecurityException("checkDelete 权限不足：" + file);
    }

    // 检查程序是否可以连接网络
    @Override
    public void checkConnect(String host, int port) {
        throw new SecurityException("checkConnect 权限不足：" + host + ":" + port);
    }
}
