package org.example.tools;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginUser {
    //登录 ID
    private long id;
    //登录账号
    private String username;
    //登录密码
    private String password;
}
