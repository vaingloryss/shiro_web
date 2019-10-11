package com.vainglory.realm;

import org.apache.shiro.authc.*;
import org.apache.shiro.realm.Realm;

/**
 * @author vaingloryss
 * @date 2019/9/28 0028 下午 7:07
 */
public class MyRealm3 implements Realm {
    //返回唯一的Realm名称
    @Override
    public String getName() {
        return "myRealm3";
    }

    //判断此Realm是否支持此Token
    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof UsernamePasswordToken;
    }

    //根据Token获取认证信息
    @Override
    public AuthenticationInfo getAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        System.out.println("MyRealm3日志:=================");
        String username = (String) token.getPrincipal();//得到用户名
        String password = new String((char[]) token.getCredentials());//得到密码
        if (!"wang".equals(username)){
            throw new UnknownAccountException("用户名错误");//如果用户名错误
        }
        if(!"123".equals(password)){
            throw new IncorrectCredentialsException("密码错误");//如果密码错误
        }
        //如果身份认证验证成功，返回一个AuthenticationInfo实现
        return new SimpleAuthenticationInfo(username+"qq.com",password,getName());
    }
}
