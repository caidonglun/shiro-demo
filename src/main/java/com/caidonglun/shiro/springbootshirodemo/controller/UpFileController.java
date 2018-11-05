package com.caidonglun.shiro.springbootshirodemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
public class UpFileController {

    Logger logger=LoggerFactory.getLogger(getClass());

    @RequestMapping(value = "/uploadFiles", method = RequestMethod.POST)
    public @ResponseBody String handleFileUpload(HttpServletRequest request, @RequestParam("name") String name) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        System.out.println("name==>"+name);
        MultipartFile file = null;
        BufferedOutputStream out = null;
        for (int i = 0; i < files.size(); ++i) {
            logger.info("文件名称为："+files.get(i).getOriginalFilename());
            file = files.get(i);
            if (!file.isEmpty()) {
                try {
                    byte[] bytes = file.getBytes();
                    out = new BufferedOutputStream(new FileOutputStream(new File("E:\\test" + "\\" + file.getOriginalFilename())));
                    out.write(bytes);
                } catch (Exception e) {
                    return "failed to upload " + i + " => " + e.getMessage();
                } finally {
                    if (null != out) {
                        try {
                            out.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        out = null;
                    }
                }
            } else {
                return "上传文件的第 " + i + "个文件是空的，无法上传";
            }
        }
        return "上传成功。";
    }
}
