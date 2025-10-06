package com.akbar.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * 小程序用户登录DTO
 */
@Data
public class UserLoginDTO implements Serializable {

    private String code;

}
