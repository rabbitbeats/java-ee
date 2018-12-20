package cn.liucw.shiro.realm;

import cn.liucw.shiro.model.User;
import cn.liucw.shiro.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Resource;


public class MySQLRealm extends AuthorizingRealm {
    private static final Logger LOGGER = LoggerFactory.getLogger(MySQLRealm.class);

//    @Resource
//    private UserService userService;

    /**
     * 认证：验证当前登录的用户
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String userName = (String) token.getPrincipal();
//        User user = userService.getByUserName(userName);
//        if (user != null) {
//            return new SimpleAuthenticationInfo(user.getUsername(), user.getPwd(), "xx");
//        }

        return null;
    }

    /**
     * 授权：为当限前登录的用户授予角色和权限
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        String userName = (String) principals.getPrimaryPrincipal();
        SimpleAuthorizationInfo authorizationInfo = new SimpleAuthorizationInfo();
        /*authorizationInfo.setRoles(userService.getRoles(userName));
        authorizationInfo.setStringPermissions(userService.getPermissions(userName));*/
        return authorizationInfo;
    }


}
