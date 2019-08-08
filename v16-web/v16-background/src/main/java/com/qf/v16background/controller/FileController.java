package com.qf.v16background.controller;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.qf.v16.common.pojo.ResultBean;
import com.qf.v16.common.pojo.WangEditorResultBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

/**
 * @author huangguizhao
 * 处理文件的服务接口
 */
@RestController
@RequestMapping("file")
public class FileController {

    @Autowired
    private FastFileStorageClient client;

    @Value("${image.server}")
    private String imageServer;

    @RequestMapping("upload")
    public ResultBean upload(MultipartFile file){
        //将文件上传到FastDFS上
        //1.获取后缀名
        String originalFilename = file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        //2.上传文件
        try {
            StorePath storePath = client.uploadImageAndCrtThumbImage(
                    file.getInputStream(), file.getSize(), extName, null);
            //获取上传后的保存路径
            String path = storePath.getFullPath();
            //传递会给客户端进行展示
            return new ResultBean("200",path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new ResultBean("500","文件上传失败");
    }

    @RequestMapping("multiUpload")
    public WangEditorResultBean multiUpload(MultipartFile[] files){
        //创建一个数组，来保存上传的图片路径
        String[] data = new String[files.length];

        for (int i=0;i<files.length;i++) {
            //将文件上传到FastDFS上
            //1.获取后缀名
            MultipartFile file = files[i];
            String originalFilename = file.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
            //2.上传文件
            try {
                StorePath storePath = client.uploadImageAndCrtThumbImage(
                        file.getInputStream(), file.getSize(), extName, null);
                //获取上传后的保存路径
                String path = storePath.getFullPath();
                //保存到数组中 StringBuilder
                data[i] = imageServer+path;
            } catch (IOException e) {
                e.printStackTrace();
                return new WangEditorResultBean("-1",null);
            }
        }
        //传递会给客户端进行展示
        return new WangEditorResultBean("0",data);
    }
}
