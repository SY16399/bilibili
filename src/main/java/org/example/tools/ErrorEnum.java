package org.example.tools;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum ErrorEnum {
    BAD_PARAM("1002","参数有错"),
    NOT_FOUNT("1003","资源不存在"),
    NO_PERMISSION("1004","权限不足"),
    BAD_INPUT_PARAM("1005","入参有问题"),
    PASSWORD_OR_USERNAME_WRONG("1006", "密码或者用户错误"),
    BAD_ID_PARAM("1007","id入参有问题"),
    BAD_IP("1008","非法IP"),
    INVALLD_TOKEN("1009","无效令牌"),
    TOO_MANY_PARAMS("1010","参数过载"),
    BAD_EMAIL_PARAM("1011","email入参有问题"),;

    private String errorCode;
    private String errorMsg;


    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}