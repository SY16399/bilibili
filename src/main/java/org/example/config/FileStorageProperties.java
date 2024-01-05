package org.example.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
@Configuration
@ConfigurationProperties(prefix = "file")
public class FileStorageProperties {
    //上传到服务器的文件路径
    private String uploadDir;

    //获取上传路径
    public String getUploadDir() {
        return uploadDir;
    }

    //设置上传路径
    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}