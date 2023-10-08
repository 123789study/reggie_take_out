package org.example.reggie.controller;

import lombok.extern.slf4j.Slf4j;
import org.example.reggie.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.Servlet;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        log.info(file.toString());

        // 原文件名
        var originalFilename = file.getOriginalFilename();

        // 后缀
        var suffix = originalFilename.substring(originalFilename.lastIndexOf("."));

        // UUID
        String fileName= UUID.randomUUID().toString()+suffix;
        final var file1 = new File(basePath);


        if (!file1.exists()){
            file1.mkdirs();
        }

        file.transferTo(new File(basePath+fileName));
        return R.success(fileName);
    }

    /**
     * 文件下载
     * @param name
     * @param response
     */
    @GetMapping("/download")
    public void download(String name, HttpServletResponse response){
        try {

            // 输入输出流,读取文件夹里面的文件
            FileInputStream fileInputStream=new FileInputStream(new File(basePath+name));

            //输入输出流,在浏览器上显示图片
            ServletOutputStream outputStream=response.getOutputStream();

            response.setContentType("image/jpeg");

            int len=0;
            byte[] bytes=new byte[1024];
            while ((len=fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }

            outputStream.close();
            fileInputStream.close();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
