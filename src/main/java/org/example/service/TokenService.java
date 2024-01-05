package org.example.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.example.pojo.MyUser;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class TokenService {
    public String getToken(MyUser user){
        String token = "";
        //只有一个小时时间   60*60*1000
        Date start = new Date();
        long currentTime = System.currentTimeMillis() + 60*60*1000*24;//一天
        Date end = new Date(currentTime);
        token = JWT.create()
                .withAudience(String.valueOf(user.getId()))
                .withIssuedAt(start)//开始时间
                .withExpiresAt(end)//过期时间
                .sign(Algorithm.HMAC256(user.getPassword() + "MText!76&sQ^"));
        return token;

    }
}
