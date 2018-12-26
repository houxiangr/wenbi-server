package com.houxiang.wenbiserver.controller;

import com.houxiang.wenbiserver.dto.CommonMessage;
import com.houxiang.wenbiserver.model.Essay;
import com.houxiang.wenbiserver.model.SimpleUser;
import com.houxiang.wenbiserver.service.EssayService;
import com.houxiang.wenbiserver.stateEnum.EssayAddEnum;
import com.houxiang.wenbiserver.stateEnum.EssayState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.sql.Timestamp;

@RestController
@RequestMapping(value = "/essay")
public class EssayController {

    @Autowired
    private EssayService essayService;

    @ResponseBody
    @RequestMapping(value="/addEssay", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public CommonMessage addEssay(Essay essay, HttpSession httpSession){
        String essayTitle = essay.getEssayTitle();
        String essayContent = essay.getEssayContent();
        if(essayTitle.equals("")||essayContent.equals("")){
            return new CommonMessage(false, EssayAddEnum.INPUTEMPTY.getName());
        }
        if(essayTitle.length() > 20){
            return new CommonMessage(false, EssayAddEnum.TITLETOOLONG.getName());
        }
        SimpleUser userData = (SimpleUser) httpSession.getAttribute("userdata");
        if(userData == null){
            return new CommonMessage(false, EssayAddEnum.NOTLOGIN.getName());
        }
        essay.setAuthorId(userData.getUserId());
        essay.setEssayDate(new Timestamp(System.currentTimeMillis()));
        essay.setEssayState(EssayState.EXAMING.getName());
        if(essayService.addEssay(essay)){
            return new CommonMessage(true, EssayAddEnum.SUCCESS.getName());
        }else{
            return new CommonMessage(true, EssayAddEnum.ERROR.getName());
        }
    }
}
