package com.houxiang.wenbiserver.mapper;

import com.houxiang.wenbiserver.model.User;

public interface UserMapper {

    // 插入一条用户信息
    int insertUser(User record);

    // 通过电话号码查询账户
    int selectUseCountrByPhone(String phonenumber);

    //比对登陆手机号和密码
    int matchThePhoneAndPassword(User user);
}
