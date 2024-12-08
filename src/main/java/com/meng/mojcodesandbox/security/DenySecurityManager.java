package com.meng.mojcodesandbox.security;

import java.security.Permission;

/**
 * @DESCRIPTION: 默认安全管理器
 * @AUTHOR: MENGLINGQI
 * @TIME: 2024/11/25 21:35
 **/
public class DenySecurityManager extends SecurityManager{

    /**
     * @description:  检查所有权限
     * * @param[1] perm
     * @throws:
     * @return:
     */
    @Override
    public void checkPermission(Permission perm) {
        System.out.println("默认不做任何处理。");
        System.out.println("权限信息：" + perm);
        throw new SecurityException("权限不足" + perm.getActions());
    }
}
