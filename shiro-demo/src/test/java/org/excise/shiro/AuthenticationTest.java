package org.excise.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.realm.SimpleAccountRealm;
import org.apache.shiro.subject.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.Collections;

public class AuthenticationTest {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationTest.class);

    SimpleAccountRealm realm = new SimpleAccountRealm();

    /**
     * 在认证前添加用户，让他具有admin和user权限
     */
    @BeforeEach
    public void addUser() {
        // realm.addAccount("uuser", "u123456");
        realm.addAccount("uuser", "u123456", "admin", "user");
    }

    @Test
    public void testAuth() {
        // 构建SecurityManager环境
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(realm);

        // 主体提交认证请求
        // 设置SecurityManager环境
        SecurityUtils.setSecurityManager(securityManager);
        // 获取当前主体
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("uuser", "u123456");
        // 登录
        subject.login(token);
        logger.info("是否是登录状态: {}", subject.isAuthenticated());

        logger.info("是否有admin、user角色：{}", subject.hasAllRoles(Arrays.asList("admin", "user")));

        // 登出
        subject.logout();
        logger.info("是否是登录状态: {}", subject.isAuthenticated());

    }
}
