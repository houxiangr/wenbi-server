package com.houxiang.wenbiserver.dto;

//判断用户是否登陆中间数据结构
public class UserDataDto {

    private boolean loginState;
    private String nickname;

    public UserDataDto(boolean loginState, String nickname) {
        this.loginState = loginState;
        this.nickname = nickname;
    }

    public UserDataDto(boolean loginState) {
        this.loginState = loginState;
    }

    public boolean isLoginState() {
        return loginState;
    }

    public void setLoginState(boolean loginState) {
        this.loginState = loginState;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
