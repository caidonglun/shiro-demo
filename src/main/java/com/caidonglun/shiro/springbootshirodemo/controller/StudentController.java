package com.caidonglun.shiro.springbootshirodemo.controller;

import com.caidonglun.shiro.springbootshirodemo.entity.Student;
import com.caidonglun.shiro.springbootshirodemo.service.StudentService;
import com.google.code.kaptcha.impl.DefaultKaptcha;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.crypto.hash.Md5Hash;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.Map;

@Controller
public class StudentController {
    Logger logger=LoggerFactory.getLogger(getClass());
    @Autowired
    StudentService studentService;
    @Autowired
    CacheManager cacheManager;
    @Autowired
    DefaultKaptcha defaultKaptcha;

    @RequestMapping("loginService")
    public ModelAndView login(String username, String password, HttpServletResponse response) throws IOException {
        ModelAndView modelAndView=new ModelAndView();
        String caidonglun = Base64.encodeToString("123456".getBytes());
        Md5Hash md5Hash=new Md5Hash("123456","caidonglun",1);
        logger.info("Md5Hash:"+md5Hash);
        logger.info("Base64:"+caidonglun);
        logger.info("user="+username+" "+"password="+password);
        if(username!=null||password!=null) {
            Subject subject = SecurityUtils.getSubject();
//        Student student = studentService.findStudent(username);
//                下面这个UsernamePasswordToken是将用户输入的账户放入里面，然后使用subject.login()来传递token，然后shiro自动验证。
            UsernamePasswordToken token = new UsernamePasswordToken(username, password);
            try {
                subject.login(token);
                logger.info( "登录成功---------------------------->");
                modelAndView.setViewName("index.html");
                return modelAndView;
//                response.sendRedirect("/index.html");
            }catch (Exception e){
                logger.info( "登录失败---------------------------->");
//                response.sendRedirect("/login.html");
                modelAndView.setViewName("login.html");
                modelAndView.addObject("info","登录失败账号或密码不确");
                return modelAndView;
            }
        }else{
            modelAndView.setViewName("login.html");
            return modelAndView;
//            response.sendRedirect("/login.html");
        }
    }

// 登陆成功了后获取用户名，每一个用户名都是唯一的，很好用
    @RequestMapping("/username")
    @ResponseBody
    public String userName(){
        Subject subject1 = SecurityUtils.getSubject();
        Object principal = subject1.getPrincipal();
        logger.info("用户名:"+principal);
        return principal.toString();
    }

    @RequestMapping(value = "/index")
    public String index(){
        return "index.html";
    }

    //登出
    @RequestMapping(value = "/logout")
    public void logout(HttpServletResponse response) throws IOException {
        Subject subject = SecurityUtils.getSubject();
        logger.info("登出了！");
        if(subject.getPrincipal()==null){
            logger.info("为空》》》》》");
//            subject.logout();
            response.sendRedirect("login.html");
            return;
        }
        logger.info("登出》》》》》");
        subject.logout();
        logger.info("登出》》》》》完成");
            response.sendRedirect("logout.html");
            return;
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


    @RequestMapping("/defaultKaptcha")
    public void defaultKaptcha(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) throws Exception{
        byte[] captchaChallengeAsJpeg = null;
        ByteArrayOutputStream jpegOutputStream = new ByteArrayOutputStream();
        try {
            //生产验证码字符串并保存到session中
            String createText = defaultKaptcha.createText();
            httpServletRequest.getSession().setAttribute("vrifyCode", createText);
            //使用生产的验证码字符串返回一个BufferedImage对象并转为byte写入到byte数组中
            BufferedImage challenge = defaultKaptcha.createImage(createText);
            ImageIO.write(challenge, "jpg", jpegOutputStream);
        } catch (IllegalArgumentException e) {
            httpServletResponse.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        //定义response输出类型为image/jpeg类型，使用response输出流输出图片的byte数组
        captchaChallengeAsJpeg = jpegOutputStream.toByteArray();
        httpServletResponse.setHeader("Cache-Control", "no-store");
        httpServletResponse.setHeader("Pragma", "no-cache");
        httpServletResponse.setDateHeader("Expires", 0);
        httpServletResponse.setContentType("image/jpeg");
        ServletOutputStream responseOutputStream =
                httpServletResponse.getOutputStream();
        responseOutputStream.write(captchaChallengeAsJpeg);
        responseOutputStream.flush();
        responseOutputStream.close();
    }

    @RequestMapping("/imgvrifyControllerDefaultKaptcha")
    public void imgvrifyControllerDefaultKaptcha(String username, String password,String vrifyCode/*,boolean rememberMe*/,HttpServletRequest request, HttpServletResponse response) {
        String captchaId = (String) request.getSession().getAttribute("vrifyCode");
        String parameter = request.getParameter("vrifyCode");
//        大小写必须严格填写。
        System.out.println("生成的验证码为：" + captchaId + "输入的验证码为：" + vrifyCode);
        System.out.println("Session  vrifyCode " + captchaId + " form vrifyCode " + vrifyCode);

        /*if (rememberMe){
            logger.info("真");
        }else{
            logger.info("假");
        }*/

//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
        logger.info("user:"+username+"pass:"+password+"验证码："+vrifyCode);
        Subject subject = SecurityUtils.getSubject();
        UsernamePasswordToken usernamePasswordToken = new UsernamePasswordToken(username, password/*,rememberMe*/);
        try {
            captchaId = captchaId.toUpperCase();
            parameter = parameter.toUpperCase();
            subject.login(usernamePasswordToken);
            if (!captchaId.equals(parameter)) {
                logger.info("错误的验证码");
                response.sendRedirect("login.html");
            } else {
                logger.info("登录成功");
                response.sendRedirect("index.html");
            }
        } catch (Exception e) {
//            e.printStackTrace();
            logger.info("用户名或密码错误！");
            try {
                response.sendRedirect("login.html");
            } catch (Exception e1) {
//                e1.printStackTrace();
            }
        }

    }



//鲁大师测试文件暂时删除。
    @RequestMapping("/cai.exe")
    public void downloadFile(HttpServletResponse resp) throws IOException {
        String fileName = ResourceUtils.getURL("classpath:").getPath() + "static/ludashisetup.exe";
        File file = new File(fileName);
 resp.setHeader("content-type", "application/octet-stream");
		resp.setContentType("application/octet-stream");
		resp.setHeader("Content-Disposition", "attachment;filename=" + "ludashisetup.exe");
		byte[] buff = new byte[1024];
		BufferedInputStream bis = null;
		OutputStream os = null;
		try {
			os = resp.getOutputStream();
			bis = new BufferedInputStream(new FileInputStream(file));
			int i = bis.read(buff);
			while (i != -1) {
				os.write(buff, 0, buff.length);
				os.flush();
				i = bis.read(buff);
			}
		} catch (IOException e) {
			e.printStackTrace();
		    } finally {
			    if (bis != null) {
				    try {
					    bis.close();
				    } catch (IOException e) {
					    e.printStackTrace();
				    }
			    }
        }
    }


       public void cai(HttpServletRequest request, HttpServletResponse response) {
        try {
        logger.info("访问过了！"+request.getContextPath());
        logger.info("访问过了！"+ResourceUtils.getURL("classpath:").getPath());

        File file = new File(ResourceUtils.getURL("classpath:").getPath()+"static/ludashisetup.exe");
        InputStream inputStream = new FileInputStream(file);
        OutputStream outputStream = response.getOutputStream();
        byte[] bytes=new byte[1024];
        int len=-1;
        logger.info("fileSize:"+file.length());
        for (;(len=inputStream.read(bytes))!=-1;){
//            logger.info("len="+len);
            outputStream.write(bytes,0,len);
        }
        inputStream.close();
        outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
