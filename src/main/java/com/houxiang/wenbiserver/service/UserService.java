package com.houxiang.wenbiserver.service;

import com.houxiang.wenbiserver.mapper.UserMapper;
import com.houxiang.wenbiserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public int addUser(User user){
        return userMapper.insertUser(user);
    }
}
