package com.caidonglun.shiro.springbootshirodemo.shiro;

import com.caidonglun.shiro.springbootshirodemo.entity.Permission;
import com.caidonglun.shiro.springbootshirodemo.entity.Student;
import com.caidonglun.shiro.springbootshirodemo.service.StudentService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;


//实现AuthorizingRealm接口用户用户认证
public class MyShiroRealm extends AuthorizingRealm {

    Logger logger=LoggerFactory.getLogger(getClass());

    @Autowired
    ApplicationContext applicationContext;

    //用于用户查询
    @Autowired
    private StudentService loginService;

    //角色权限和对应权限添加
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        //获取登录用户名
        String name= (String) principalCollection.getPrimaryPrincipal();

        logger.info("用于权限查询！");
        //查询用户名称
        Student user = loginService.findStudent(name);
        //添加角色和权限
//        SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();

        String[] beanDefinitionNames = applicationContext.getBeanDefinitionNames();
        for(int i=0;i<beanDefinitionNames.length;i++)
        logger.info("beanName:"+beanDefinitionNames[i]);

        SimpleAuthorizationInfo simpleAuthorizationInfo= (SimpleAuthorizationInfo) applicationContext.getBean("getSimpleAuthorizationInfo");

//        for (Role role:user.getRoles()) {
            //添加角色
//            simpleAuthorizationInfo.addRole(role.getRoleName());

        Permission permission = loginService.finRoleAndPermission(name);
        logger.info("permission="+permission.toString());
        simpleAuthorizationInfo.addRole(permission.getRoleName());

//            for (Permission permission:role.getPermissions()) {
                //添加权限
//                simpleAuthorizationInfo.addStringPermission(permission.getPermission());

        simpleAuthorizationInfo.addStringPermission(permission.getPermissionName());

//            }
//        }
        logger.info("数据执行挖完成"+String.valueOf(simpleAuthorizationInfo.getStringPermissions()+""+simpleAuthorizationInfo.getRoles()));
        return simpleAuthorizationInfo;
    }

    //用户认证
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        //加这一步的目的是在Post请求的时候会先进认证，然后在到请求
        UsernamePasswordToken usernamePasswordToken= (UsernamePasswordToken) authenticationToken;
//        if (authenticationToken.getPrincipal() == null) {
//            return null;
//        }


        String username = usernamePasswordToken.getUsername();
        logger.info("用户名字为："+username);
        if(username==null||username==""){
            return null;
//            throw new AccountException("请输入用户名和密码");
        }

        //获取用户信息
        String name = authenticationToken.getPrincipal().toString();


        logger.info("用户认证！"+name);
        Student user = loginService.findStudent(name);
        logger.info("账号："+usernamePasswordToken.getUsername());
        if (user == null) {
            //这里返回后会报出对应异常
            return null;
//            throw new AccountException("用户名不正确");
        } else {
            //这里验证authenticationToken和simpleAuthenticationInfo的信息
            SimpleAuthenticationInfo simpleAuthenticationInfo = new SimpleAuthenticationInfo(name, user.getPassword(), this.getClass().getName());

//            这里不用我们自己去验证用户是否正确，只需要将从数据库获取出来的正确账户放入SimpleAuthenticationInfo
            logger.info("验证中！");
            return simpleAuthenticationInfo;
        }
    }
}