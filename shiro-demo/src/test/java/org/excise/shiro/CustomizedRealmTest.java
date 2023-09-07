package org.excise.shiro;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.mgt.DefaultSecurityManager;
import org.apache.shiro.subject.Subject;
import org.excise.shiro.realm.CustomizedRealm;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class CustomizedRealmTest {

    private static final Logger logger = LoggerFactory.getLogger(CustomizedRealmTest.class);

    @Test
    public void testAuth() {
        CustomizedRealm realm = new CustomizedRealm();

        // 构建SecurityManager环境
        DefaultSecurityManager securityManager = new DefaultSecurityManager();
        securityManager.setRealm(realm);

        // 主体提交认证请求
        // 设置SecurityManager环境
        SecurityUtils.setSecurityManager(securityManager);
        // 获取当前主体
        Subject subject = SecurityUtils.getSubject();

        UsernamePasswordToken token = new UsernamePasswordToken("uName", "uPass");
        // 登录
        subject.login(token);
        logger.info("是否是登录状态: {}", subject.isAuthenticated());

        logger.info("是否有admin、user角色：{}", subject.hasAllRoles(Arrays.asList("admin", "user")));
        logger.info("是否用于delete和add权限: {}", subject.isPermittedAll("user:add", "user:delete"));

        // 登出
        subject.logout();
        logger.info("是否是登录状态: {}", subject.isAuthenticated());


    }
}
