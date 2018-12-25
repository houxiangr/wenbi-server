package com.houxiang.wenbiserver.mapper;

import com.houxiang.wenbiserver.model.SimpleUser;
import com.houxiang.wenbiserver.model.User;

public interface UserMapper {

    // 插入一条用户信息
    int insertUser(User record);

    // 通过电话号码查询当前电话账户数量
    int selectUserCountrByPhone(String phonenumber);

    //比对登陆手机号和密码
    SimpleUser matchThePhoneAndPassword(User user);

    // 通过电话号码查询用户信息
    User selectUserDataByPhone(String phonenumber);
}
