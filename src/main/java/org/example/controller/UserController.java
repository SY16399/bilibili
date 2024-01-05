package org.example.controller;


import com.alibaba.fastjson.JSONObject;
import org.example.anno.UserLoginToken;
import org.example.common.BusinessException;
import org.example.pojo.MyUser;
import org.example.pojo.ValidFileType;
import org.example.service.FileStorageService;
import org.example.service.TokenService;
import org.example.service.UserService;
import org.example.tools.ErrorEnum;
import org.example.tools.JsonResultObject;
import org.example.tools.LoginUser;
import org.example.tools.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.Objects;

@CrossOrigin
@RestController
public class UserController {
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    UserService userService;
    @Autowired
    TokenService tokenService;

    @PostMapping("/register")
    public JsonResultObject register(@RequestBody MyUser user) {
        return userService.register(user);
    }

    @PostMapping("/login")
    public JsonResultObject login(@RequestBody LoginUser loginUser) {
        JsonResultObject result = userService.login(loginUser);
        if (!Objects.equals(result.getErrorMessage(), "")) {
            return result;
        } else {
            String token = tokenService.getToken((MyUser) result.getData());
            JSONObject returnObject = new JSONObject();
            //json没有健 ，序列化一下
            returnObject.put("token", token);
            result.setData(returnObject);
            return result;
        }
    }

    @UserLoginToken
    @GetMapping("/getUserInfo")
    public JsonResultObject getUserInfo() {
        LoginUser user = UserContext.getUser();
        if (user != null) {
            int id = (int) user.getId();
            JsonResultObject reult = new JsonResultObject<>("200", "查询用户成功", "", "", userService.findUserById(id));
            return reult;
        } else {
            throw new BusinessException("4001", "用户登录异常");
        }
    }

    //修改用户信息
    @UserLoginToken
    @PutMapping("/updateMyInfo")
    public JsonResultObject update(@RequestBody MyUser myUser) {
        //System.out.println(myUser);
        //LoginUser user = UserContext.getUser();
        JsonResultObject update = userService.update(myUser);
        return update;
    }

    //修改用户头像
    @UserLoginToken
    @PostMapping("/uploadAvatar")
    public JsonResultObject updateAvator(@RequestParam("uploadAvatar") MultipartFile file) {
        //存储文件并获得保存后的文件名称
        String fileName = null;
        try {
            List<String> fileType = new ValidFileType().getValidType();
            //看文件类型是不是图篇
            if (!fileType.contains(file.getContentType())) {
                System.out.println(file.getContentType());
                throw new BusinessException("", "不是图片文件格式");
            }
            fileName = fileStorageService.storeFile(file);
        } catch (Exception e) {
            return new JsonResultObject("200", "", e.getMessage(), Enum.valueOf(ErrorEnum.class, "BAD_PARAM").getErrorCode(), fileName);
        }
        //获取文件下载地址
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();
        //修改头像地址
        MyUser user = new MyUser();
        user.setAvatar(fileDownloadUri);
        JsonResultObject update = userService.update(user);

        update.setData(fileDownloadUri);
        //返回一个上传好的文件对象，这里返回下载地址，文件大小以及类型。
        //UploadFileResponse uploadFileResponse = new UploadFileResponse(fileName, fileDownloadUri, file.getContentType(), file.getSize());
        return update;
    }

}
