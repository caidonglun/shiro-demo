package com.caidonglun.shiro.springbootshirodemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class TestController {

    Logger logger=LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/uploadTest",method = RequestMethod.POST)
    @ResponseBody
    public Object uploadTest(HttpServletRequest request, HttpServletResponse response){
        List<MultipartFile> file = ((MultipartHttpServletRequest) request).getFiles("file");


for (int i=0;i<file.size();i++) {
    logger.info("上传文件：" + file.get(i).getOriginalFilename());

    try {
        file.get(i).transferTo(new File("E:\\test\\" + file.get(i).getOriginalFilename()));
    } catch (IOException e) {
        e.printStackTrace();
    }
}
        Map map=new HashMap();
        map.put("success","成功");
        logger.info("map:"+map.toString());
        return map;
    }

}
