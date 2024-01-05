package org.example.intercepetor;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.example.anno.PassToken;
import org.example.anno.UserLoginToken;
import org.example.common.BusinessException;
import org.example.pojo.MyUser;
import org.example.service.UserService;
import org.example.tools.LoginUser;
import org.example.tools.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

public class AuthInterceptor implements HandlerInterceptor {
    @Autowired
    UserService userService;
    //@Autowired
    //RedisUtil redisUtil;
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader("token");
        //排除访问的是静态资源，而不是映射访问
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }
        //获取访问的方法
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();
        if (method.isAnnotationPresent(PassToken.class)) {
            PassToken passToken = method.getAnnotation(PassToken.class);
            if (passToken.requried()) {
                return true;
            }
        }
        if (method.isAnnotationPresent(UserLoginToken.class)) {
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            if (userLoginToken.requried()) {
                //判空
                if (token == null) {
                    throw new BusinessException("4001", "no token");
                }
                String userId;
                try {
                    //获取token的受众列表中的第一个受众，将其赋值给变量userId。
                    userId = JWT.decode(token).getAudience().get(0);
                } catch (Exception e) {
                    throw new BusinessException("4003", "decode token fails");
                }

                // Check the expire of token.
//                String tokenKey = userId + ":" + token;
//                boolean hasExisted = redisUtil.hasKey(tokenKey);
//                System.out.println("exist or not:" + hasExisted);
//                if (hasExisted == false) {
//                    throw new BusinessException("4005", "token expired!");
//                }
                int userIdt = Integer.parseInt(userId);
                System.out.println("userId is "+userIdt);
                MyUser myUser = userService.findUserById(userIdt);
                if (myUser == null){
                    throw new RuntimeException("user no exists");
                }
                try {
                    //验证JWT 令牌
                    JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(myUser.getPassword()+"MText!76&sQ^")).build();
                    //可以验证过期时间
                    jwtVerifier.verify(token);
                    //设置当前登录用户
                    LoginUser loginUser = new LoginUser();
                    loginUser.setId(userIdt);
                    UserContext.setUser(loginUser);
                }catch (JWTVerificationException e){
                    System.out.println(e.getMessage());
                    throw new BusinessException("4002",e.getMessage());
                }
            }
        }
        return true;

    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
