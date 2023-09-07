package org.excise.shiro.realm;

import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class CustomizedRealm extends AuthorizingRealm {

    public CustomizedRealm() {
        setName("CustomizedRealm");
    }

    /**
     * 模拟数据库数据
     */
    private static Map<String, String> userMap = new HashMap<>();

    static {
        userMap.put("uName", "uPass");
    }


    /**
     * 获取授权信息
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String)principalCollection.getPrimaryPrincipal();
        // 从数据库获取角色和权限信息
        Set<String> roles = getRolesByUserName(userName);
        Set<String> permissions = getPermissionByUserName(userName);

        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        authorizationInfo.setRoles(roles);
        authorizationInfo.setStringPermissions(permissions);
        return authorizationInfo;
    }

    /**
     * 认证
     * @param authenticationToken 主体传过来的认证信息
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        // 从主体传过来的认证信息中获取用户名
        String userName = (String)authenticationToken.getPrincipal();

        // 通过用户名从数据库中取得凭证
        String password = getPasswordByUserName(userName);
        if (password == null) {
            return null;
        }
        return new SimpleAuthenticationInfo(userName, password, "CustomizedRealm");
    }

    /**
     * 模拟从数据库获取凭证信息
     * @param userName
     * @return
     */
    private String getPasswordByUserName(String userName) {
        return userMap.get(userName);
    }
    /**
     * 模拟从数据库获取权限数据
     * @param userName
     * @return
     */
    private Set<String> getPermissionByUserName(String userName) {
        Set<String> permissions = new HashSet<>();

        permissions.add("user:delete");
        permissions.add("user:add");

        return permissions;
    }

    /**
     * 模拟从数据库获取权限信息
     * @param userName
     * @return
     */
    private Set<String> getRolesByUserName(String userName) {
        Set<String> roles = new HashSet<>();
        roles.add("user");
        roles.add("admin");
        return roles;
    }
}
