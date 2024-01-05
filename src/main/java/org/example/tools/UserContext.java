package org.example.tools;


/**
 * 用于存储登录用户信息
 */
public class UserContext {

    private static final ThreadLocal<LoginUser> userHolder = new ThreadLocal<LoginUser>();

    public static void setUser(LoginUser loginUser) {
        userHolder.set(loginUser);
    }

    public static LoginUser getUser() {
        return userHolder.get();
    }
}
