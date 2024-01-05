package org.example.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                //localhost .allowedOrigins("*")允许所有源
                .allowedOrigins("http://localhost:8080") // 允许的源地址
                .allowedMethods("GET", "POST", "PUT", "DELETE") // 允许的请求方法
                .allowedHeaders("Content-Type") // 允许的请求头
                .allowCredentials(true); // 是否允许发送身份验证信息
    }
}
