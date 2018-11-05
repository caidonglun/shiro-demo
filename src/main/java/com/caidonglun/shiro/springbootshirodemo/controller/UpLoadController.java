package com.caidonglun.shiro.springbootshirodemo.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

@Controller
public class UpLoadController {

    Logger logger=LoggerFactory.getLogger(getClass());
    /**
     * @author van
     * 检查文件存在与否
     */
    @PostMapping("checkFile")
    @ResponseBody
    public Boolean checkFile(@RequestParam(value = "md5File") String md5File) {
//        synchronized (this) {
            logger.info("checkFile:" + md5File);
            Boolean exist = false;

            //实际项目中，这个md5File唯一值，应该保存到数据库或者缓存中，通过判断唯一值存不存在，来判断文件存不存在，这里我就不演示了
		/*if(true) {
			exist = true;
		}*/
            return exist;
//        }
    }

    /**
     * @author van
     * 检查分片存在与否
     */
    @PostMapping("checkChunk")
    @ResponseBody
    public Boolean checkChunk(@RequestParam(value = "md5File") String md5File,
                              @RequestParam(value = "chunk") Integer chunk) {
//        synchronized (this) {
            logger.info("checkChunk" + md5File + "chunk:" + chunk);
            Boolean exist = false;
            String path = "E:/test/" + md5File + "/";//分片存放目录
            String chunkName = chunk + ".tmp";//分片名
            File file = new File(path + chunkName);
            logger.info("文件名称:" + path + chunkName + "  name:" + chunkName);
            if (file.exists()) {
                exist = true;
            }
            logger.info("返回了！" + exist + "地址:" + path);
            logger.info("exist：" + exist);
            return exist;
//        }
    }

    /**
     * @author van
     * 上传，这里根据文件md5值生成目录，并将分片文件放到该目录下
     */
    @PostMapping("upload")
    @ResponseBody
    public Boolean upload(@RequestParam(value = "file") MultipartFile file,
                          @RequestParam(value = "md5File") String md5File,
                          @RequestParam(value = "chunk",required= false) Integer chunk) { //第几片，从0开始
//        synchronized (this) {
            logger.info("upload");
            String path = "E:/test/" + md5File + "/";
            File dirfile = new File(path);
            if (!dirfile.exists()) {//目录不存在，创建目录
                dirfile.mkdirs();
                logger.info("文件夹创建：" + path);
            } else {
                logger.info("文件夹存在：" + path);
            }
            String chunkName;
            if (chunk == null) {//表示是小文件，还没有一片
                chunkName = "0.tmp";
            } else {
                chunkName = chunk + ".tmp";
            }
            String filePath = path + chunkName;
            File savefile = new File(filePath);

            try {
                if (!savefile.exists()) {
                    savefile.createNewFile();//文件不存在，则创建
                }
                file.transferTo(savefile);//将文件保存
            } catch (IOException e) {
                return false;
            }

            return true;
        }
//    }

    /**
     * @author van
     * 合成分片
     */
    private Map<String,String> mergeName=new HashMap<>();

    @PostMapping("merge")
    @ResponseBody
    public Boolean  merge(@RequestParam(value = "chunks",required =false) Integer chunks,
                          @RequestParam(value = "md5File") String md5File,
                          @RequestParam(value = "name") String name) throws Exception {
        synchronized (this) {
        logger.info("merge");
        Subject subject = SecurityUtils.getSubject();
        mergeName.put(subject.getPrincipal().toString(),md5File);
            logger.info("mergeName:"+mergeName.toString());
        String path = "E:/test";
        FileOutputStream fileOutputStream = new FileOutputStream(path+"/"+name);  //合成后的文件
        logger.info("合并："+path+"/"+name);
        try {
            byte[] buf = new byte[1024];
            logger.info("缓存个数："+chunks);
            for(long i=0;i<chunks;i++) {
                String chunkFile=i+".tmp";
                File file = new File(path+"/"+md5File+"/"+chunkFile);
                logger.info("tmp文件地址："+path+"/"+md5File+"/"+chunkFile);
                InputStream inputStream = new FileInputStream(file);
                int len = 0;
                while((len=inputStream.read(buf))!=-1){
                    fileOutputStream.write(buf,0,len);
                }
                inputStream.close();
            }
            fileOutputStream.close();
            //合并完，要删除md5目录及临时文件，节省空间。这里代码省略
            File file=new File(path+"/"+md5File);
            int is_null=0;
            for(int i=0;i!=-1;i++){
                File file1=new File(path+"/"+md5File+"/"+i+".tmp");
                logger.info("delete:"+file.getPath());
                if (file1.exists()){
                    logger.info("执行删除！"+path+"/"+md5File+"/"+i+".tmp");
                    file1.delete();
                }else {
                    is_null++;
                }
                if (is_null>=3){
                    logger.info("删除完成");
                    break;
                }
            }
            file.delete();

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            fileOutputStream.close();
        }
        return true;
        }
    }

}
