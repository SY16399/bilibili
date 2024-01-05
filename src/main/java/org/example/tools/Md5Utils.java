package org.example.tools;

import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Component
public class Md5Utils {
    /**
     * 将字符串 md5化
     * @param plainText
     * @return 16进制表示的字符串
     */
    public static String stringToMD5(String plainText) {
        byte[] secretBytes = null;
        try {
            //使用MessageDigest类获取MD5算法实例，然后将输入字符串转换为字节数组并进行加密
            secretBytes = MessageDigest.getInstance("md5").digest(
                    plainText.getBytes());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("没有这个MD5算法");
        }
        //将加密后的字节数组转换为BigInteger对象，并使用16进制形式的字符串表示
        String md5code = new BigInteger(1, secretBytes).toString(16);
        for (int i = 0; i < 32 - md5code.length(); i++) {
            //对字符串进行前补0操作，确保字符串长度为32位
            md5code = "0" + md5code;
        }
        return md5code;
    }
}