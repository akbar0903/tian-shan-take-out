package com.akbar.service;

import com.akbar.dto.UserLoginDTO;
import com.akbar.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

public interface UserService extends IService<User> {
    User login(UserLoginDTO userLoginDTO);
}
