package com.caidonglun.shiro.springbootshirodemo.controller;

import com.caidonglun.shiro.springbootshirodemo.entity.Student;
import com.caidonglun.shiro.springbootshirodemo.service.StudentService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@Controller
public class StudentController {
    Logger logger=LoggerFactory.getLogger(getClass());
    @Autowired
    StudentService studentService;


    @RequestMapping("loginService")
    public String login(String username, String password, HttpServletResponse response) throws IOException {
        logger.info("user="+username+" "+"password="+password);
        if(username!=null||password!=null) {
            Subject subject = SecurityUtils.getSubject();
//        Student student = studentService.findStudent(username);
//                下面这个UsernamePasswordToken是将用户输入的账户放入里面，然后使用subject.login()来传递token，然后shiro自动验证。
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            try {
                subject.login(token);
                logger.info( "登录成功---------------------------->");
                return "index";
//                response.sendRedirect("/index.html");
            }catch (Exception e){
                logger.info( "登录失败---------------------------->");
//                response.sendRedirect("/login.html");
                return "login";
            }
        }else{
            return "login";
//            response.sendRedirect("/login.html");
        }
    }


    @RequestMapping(value = "/index")
    public String index(){
        return "index.html";
    }

    //登出
    @RequestMapping(value = "/logout")
    public String logout(){
        Subject subject = SecurityUtils.getSubject();
        subject.logout();

        return "logout";
    }

    //错误页面展示
    @RequestMapping(value = "/error",method = RequestMethod.POST)
    public String error(){
        return "error";
    }


    //数据初始化
    @RequestMapping(value = "/addUser")
    public String addUser(@RequestBody Map<String,Object> map){
        Student user = studentService.addUser(map);
        return "addUser is ok! \n" + user;
    }

    //角色初始化
    @RequestMapping(value = "/addRole")
    public String addRole(@RequestBody Map<String,Object> map){
//        Role role = studentService.addRole(map);
        return "addRole is ok! \n" /*+ role*/;
}

    //注解的使用
    @RequiresRoles("admin")
    @RequiresPermissions("create")
    @RequestMapping(value = "/create")
    public String create(){
        return "Create success!";
    }



//

}