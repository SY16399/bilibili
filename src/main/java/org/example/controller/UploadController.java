package org.example.controller;

import org.example.pojo.UploadFileResponse;
import org.example.service.FileStorageService;
import org.example.tools.ErrorEnum;
import org.example.tools.JsonResultObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class UploadController {
    @Autowired
    private FileStorageService fileStorageService;
    @PostMapping("/upload")
    public JsonResultObject uploadFileResponse(@RequestParam("single_file")MultipartFile file){//spring提供的接口
        //存储文件并获得保存后的文件名称
        String fileName = null;
        try {
            fileName = fileStorageService.storeFile(file);
        } catch (Exception e) {
            return new JsonResultObject("200","",e.getMessage(),Enum.valueOf(ErrorEnum.class,"BAD_PARAM").getErrorCode(),fileName);
        }
        //获取文件下载地址
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();
        //返回一个上传好的文件对象，这里返回下载地址，文件大小以及类型。
        UploadFileResponse uploadFileResponse = new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
        return new JsonResultObject("200","上传成功","","",uploadFileResponse);
    }
    //根据文件名获取上传好的文件信息
    @GetMapping("/downloadFile/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName, HttpServletRequest request){
        //根据文件名获取文件存储资源
        Resource resource = fileStorageService.loadFileResource(fileName);
        //尝试确定文件的内容类型
        //Try  to determine file `s content type
        String contentType = null;
        try{
            //根据资源调用getMimeType()方法来获取文件的MIME类型（即文件的互联网媒体类型，例如"text/plain", "image/jpg"等
            contentType = request.getServletContext().getMimeType(resource.getFile()
                    .getAbsolutePath());
        } catch (IOException e) {
            System.out.println("Could not determine file type.");
        }
        //如果无法确定类型，则退回到默认内容类型
        //Fallback to the default content type if type could not be determined
        //如果无法确定文件的MIME类型，将其默认设置为"application/octet-stream"。
        // 这是一种二进制文件流的MIME类型，常用于表示不能识别文件类型的情况。
        if (contentType == null){
            contentType = "application/octect-stream";
        }
        //返回文件流
        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).header(HttpHeaders.CONTENT_DISPOSITION
                ,"attachment;filename=\""+resource.getFilename()+"\"").body(resource);
    }

}
