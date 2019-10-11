package com.vainglory.realm;

import com.vainglory.domain.User;
import com.vainglory.service.UserService;
import com.vainglory.service.serviceImpl.UserServiceImpl;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;

/**
 * @author vaingloryss
 * @date 2019/10/9 0009 下午 5:10
 */
public class CustomRealm extends AuthorizingRealm {
    /**
     * 查权限信息
     * 查询方式：通过用户名查询，并返回即可，不用任何比对
     */

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        Object username = principals.getPrimaryPrincipal();
        return null;
    }

    /**
     * 查身份信息（如登录）
     * 作用：查询用户信息，并返回即可，不用任何比对
     */

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();//得到用户名
        //查询
        UserService userService = new UserServiceImpl();
        User user = userService.findByusername(username);

        //String password = new String((char[]) token.getCredentials());//得到密码
        return null;
    }
}
