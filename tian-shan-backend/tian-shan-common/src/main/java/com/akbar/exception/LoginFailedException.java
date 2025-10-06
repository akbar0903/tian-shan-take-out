package com.akbar.exception;

/**
 * 小程序登录失败异常
 */
public class LoginFailedException extends BaseException {
    public LoginFailedException() {
    }

    public LoginFailedException(String message) {
        super(message);
    }
}
