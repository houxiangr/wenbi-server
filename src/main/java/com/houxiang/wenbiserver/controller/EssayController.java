package com.houxiang.wenbiserver.controller;

import com.houxiang.wenbiserver.dto.CommonMessage;
import com.houxiang.wenbiserver.model.Essay;
import com.houxiang.wenbiserver.model.SimpleUser;
import com.houxiang.wenbiserver.service.EssayService;
import com.houxiang.wenbiserver.stateEnum.EssayAddEnum;
import com.houxiang.wenbiserver.stateEnum.EssayState;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Map;

@RestController
@RequestMapping(value = "/essay")
public class EssayController {

    @Autowired
    private EssayService essayService;

    @Value("${file.uploadFolder}")
    private String uploadFolder;

    @Value("${myconfig.domain}")
    private String domain;

    @Value("${myconfig.getimginterface}")
    private String getimginterface;

    @ResponseBody
    @RequestMapping(value="/addEssay", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    // 接受上传的文件 @RequestParam("essay-cover") MultipartFile essayCover
    public CommonMessage addEssay(Essay essay, HttpSession httpSession, HttpServletRequest req, @RequestParam("essay-cover") MultipartFile essayCover) {
        SimpleUser userData = (SimpleUser) httpSession.getAttribute("userdata");
        if(userData == null){
            return new CommonMessage(false, EssayAddEnum.NOTLOGIN.getName());
        }
        int authorId = userData.getUserId();
        // 根据时间戳创建新的文件名
        String fileName = System.currentTimeMillis() + ".jpg";
        // 通过配置类配置当前图片存放路径，然后拼接前面的文件名
        String destFileName = uploadFolder + File.separator+fileName;
        // 拼接图片的访问路径
        String getImgUrl = domain + getimginterface + fileName;
        File destFile = new File(destFileName);
        try {
            essayCover.transferTo(destFile);
        } catch (IOException e) {
            // TODO 错误处理
            e.printStackTrace();
        }
        String essayTitle = essay.getEssayTitle();
        String essayContent = essay.getEssayContent();
        if(essayTitle.equals("")||essayContent.equals("")){
            return new CommonMessage(false, EssayAddEnum.INPUTEMPTY.getName());
        }
        if(essayTitle.length() > 20){
            return new CommonMessage(false, EssayAddEnum.TITLETOOLONG.getName());
        }
        Timestamp now = new Timestamp(System.currentTimeMillis());
        if(essayService.searchTheSameTitle(authorId,essayTitle)){
            return new CommonMessage(false, EssayAddEnum.ESSAYEXIST.getName());
        }
        essay.setAuthorId(authorId);
        essay.setEssayDate(now);
        essay.setEssayState(EssayState.EXAMING.getName());
        essay.setEssayCover(getImgUrl);
        if(essayService.addEssay(essay)){
            return new CommonMessage(true, EssayAddEnum.SUCCESS.getName());
        }else{
            return new CommonMessage(false, EssayAddEnum.ERROR.getName());
        }
    }

    @ResponseBody
    @RequestMapping(value="/viewEssay", produces= {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public Essay viewEssay(@RequestBody Map<String, Object> map) {
        int essid = Integer.parseInt((String) map.get("essid"));
        Essay temp = essayService.searchEssayByEssayId(essid);
        System.out.println(temp);
        return temp;
    }
}
