package com.houxiang.wenbiserver.service;

import com.houxiang.wenbiserver.mapper.UserMapper;
import com.houxiang.wenbiserver.model.SimpleUser;
import com.houxiang.wenbiserver.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public boolean addUser(User user) {
        return userMapper.insertUser(user) == 1;
    }

    public boolean isPhoneExist(String phoneNum) {
        return userMapper.selectUserCountrByPhone(phoneNum) > 0;
    }

    public SimpleUser isPasswordMatched(User user){
        return userMapper.matchThePhoneAndPassword(user);
    }

    public User getUserDataByPhone(String phoneNum) {
        return userMapper.selectUserDataByPhone(phoneNum);
    }
}
