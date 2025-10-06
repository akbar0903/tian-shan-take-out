package com.akbar.service.impl;

import com.akbar.constant.MessageConstant;
import com.akbar.dto.UserLoginDTO;
import com.akbar.entity.User;
import com.akbar.exception.LoginFailedException;
import com.akbar.mapper.UserMapper;
import com.akbar.properties.WeChatProperties;
import com.akbar.service.UserService;
import com.akbar.utils.HttpClientUtil;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    public static final String WEIXIN_SERVER_URL = "https://api.weixin.qq.com/sns/jscode2session";

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private WeChatProperties weChatProperties;

    /**
     * 小程序用户登录
     */
    @Override
    public User login(UserLoginDTO userLoginDTO) {

        // 根据code获取openid
        String openid = getOpenId(userLoginDTO.getCode());

        // 如果拿不到openid，抛出宜昌
        if (openid == null) {
            throw new LoginFailedException(MessageConstant.LOGIN_FAILED);
        }

        // 判断这个用户的openid在数据库中是否存在
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<User>()
                .eq(User::getOpenid, openid);
        User user = userMapper.selectOne(wrapper);

        // 如果是新用户，则插入数据库
        if (user == null) {
            user = User.builder()
                    .openid(openid)
                    .build();
            userMapper.insert(user);
        }

        return user;
    }


    /**
     * 工具方法
     * 根据code获取openid
     */
    private String getOpenId(String code) {
        // 根据code获取openid
        Map<String, String> params = new HashMap<>();
        params.put("appid", weChatProperties.getAppid());
        params.put("secret", weChatProperties.getSecret());
        params.put("js_code", code);
        params.put("grant_type", weChatProperties.getGrantType());
        String jsonResponse = HttpClientUtil.doGet(WEIXIN_SERVER_URL, params);

        // 解析json
        JSONObject jsonObject = JSONObject.parseObject(jsonResponse);

        return jsonObject.getString("openid");
    }
}
