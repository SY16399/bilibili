package org.example.service;

import org.example.Exception.FileStorageException;
import org.example.config.FileStorageProperties;
import org.springframework.beans.MutablePropertyValues;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;

@Service
public class FileStorageService {

    private final Path fileStorageLocation;
    @Autowired(required = false)//找不到实例不会变异常而是设置为 null
    public FileStorageService(FileStorageProperties fileStorageProperties) {
        //normalize() 方法消除多余的斜杠.等。
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir()).toAbsolutePath().normalize();
        try{
            //创建路经
            Files.createDirectories(this.fileStorageLocation);
        } catch (IOException ex) {
            throw new FileStorageException("Could not create the directory" +
                    " while the uploaded files will be stored.",ex);
        }
    }

    /**
     * 储存文件
     *
     * @param file 文件流对象
     * @return  String 文件名 | Exception
     */
    public String storeFile(MultipartFile file) throws Exception{
        //规划文件名
        // 1.此方法用于获取上传文件的原始文件名。当处理HTTP文件上传时，你可以通过这个方法来访问上传文件的原始文件名（即用户上传的文件在用户设备上的名称。
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        /*try {*/
            //2.检查文件中是否含有无效字符
            if (fileName.contains("..")){
                throw new FileStorageException("抱歉，文件名里面有无效字符 "+fileName);
            }
            //3.resolve将当前路径追加在源路径之后
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            //复制文件到目标路径
            Files.copy(file.getInputStream(),targetLocation, StandardCopyOption.REPLACE_EXISTING);
            return fileName;
        /*} catch (IOException e) {
            throw new FileStorageException("不能保存文件"+fileName+". 请重新尝试！",e);
        }*/
    }

    /**
     *
     * 加载文件资源
     * @param fileName 文件名
     * @return  Resource | Exception
     */
    public Resource loadFileResource(String fileName){
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            //将filePath转成uri后将其 构建 Resource对象
            Resource resource = new UrlResource(filePath.toUri());
            if (resource.exists()){
                return resource;
            }else {
                throw new FileStorageException("文件没找到 "+fileName);
            }
        } catch (MalformedURLException e) {
            throw new FileStorageException("文件没找到 "+fileName,e);
        }
    }

}
