package com.houxiang.wenbiserver.controller;

import com.houxiang.wenbiserver.dto.CommonMessage;
import com.houxiang.wenbiserver.dto.UserDataDto;
import com.houxiang.wenbiserver.model.SimpleUser;
import com.houxiang.wenbiserver.model.User;
import com.houxiang.wenbiserver.service.UserService;
import com.houxiang.wenbiserver.stateEnum.LoginEnum;
import com.houxiang.wenbiserver.stateEnum.RegisterEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ResponseBody
    @RequestMapping(value = "/register", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public CommonMessage register(User user) {
        String nickname = user.getNickname();
        String phonenumber = user.getPhonenumber();
        String password = user.getPassword();
        // 检查是否有空数据
        if (nickname.equals("") || phonenumber.equals("") || password.equals("")) {
            return new CommonMessage(false, RegisterEnum.INPUTEMPTY.getName());
        }
        // 检查名称长度是否超过20
        if (nickname.length() > 20) {
            return new CommonMessage(false, RegisterEnum.NAMETOOLONG.getName());
        }
        // 检查手机号格式是否正确
        if (!Pattern.matches("^1([34578])\\d{9}$", phonenumber)) {
            return new CommonMessage(false, RegisterEnum.PHONEFORMATWRONG.getName());
        }
        // 检查手机是否已注册
        if (userService.isPhoneExist(phonenumber)) {
            return new CommonMessage(false, RegisterEnum.PHONEISEXITS.getName());
        }
        //判断插入是否成功
        if (userService.addUser(user)) {
            return new CommonMessage(true, RegisterEnum.SUCCESS.getName());
        } else {
            return new CommonMessage(true, RegisterEnum.ERROR.getName());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/login", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public CommonMessage login(User user, HttpServletResponse response, HttpSession httpSession) {
        //跨域解决方案
        solveCrossDomain(response);
        String phonenumber = user.getPhonenumber();
        String password = user.getPassword();
        if (phonenumber.equals("") || password.equals("")) {
            return new CommonMessage(false, LoginEnum.INPUTEMPTY.getName());
        }
        // 检查手机号格式是否正确
        if (!Pattern.matches("^1([34578])\\d{9}$", phonenumber)) {
            return new CommonMessage(false, LoginEnum.PHONEFORMATWRONG.getName());
        }
        SimpleUser userData = userService.isPasswordMatched(user);
        if (userData != null) {
            userData.setPhonenumber(phonenumber);
            // 写入session
            httpSession.setAttribute("userdata", userData);
            return new CommonMessage(true, LoginEnum.SUCCESS.getName());
        } else {
            return new CommonMessage(false, LoginEnum.ERROR.getName());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/isLogin",  produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public UserDataDto checkIsLogin(HttpSession httpSession,HttpServletResponse response){
        solveCrossDomain(response);
        SimpleUser userData = (SimpleUser) httpSession.getAttribute("userdata");
        if(userData == null){
            return new UserDataDto(false);
        }else{
            return new UserDataDto(true,userData.getNickname());
        }
    }

    @ResponseBody
    @RequestMapping(value = "/exitLogin", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public void exitLogin(HttpSession httpSession,HttpServletResponse response){
        solveCrossDomain(response);
        httpSession.removeAttribute("userdata");
    }

    private void solveCrossDomain(HttpServletResponse response){
        //跨域解决方案
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Origin", "http://127.0.0.1:8081");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type, Access-Control-Allow-Headers, Authorization, X-Requested-With");
    }
}
