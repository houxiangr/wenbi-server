package com.houxiang.wenbiserver.controller;

import com.houxiang.wenbiserver.dto.CommonMessage;
import com.houxiang.wenbiserver.dto.EssaysDto;
import com.houxiang.wenbiserver.esModel.Esessay;
import com.houxiang.wenbiserver.model.Essay;
import com.houxiang.wenbiserver.model.SimpleUser;
import com.houxiang.wenbiserver.service.EssayService;
import com.houxiang.wenbiserver.stateEnum.CommentEnum;
import com.houxiang.wenbiserver.stateEnum.EssayAddEnum;
import com.houxiang.wenbiserver.stateEnum.EssayState;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.http.entity.ContentType;
import org.apache.http.nio.entity.NStringEntity;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.Request;
import org.elasticsearch.client.Response;
import org.elasticsearch.client.RestClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping(value = "/essay")
public class EssayController {

    @Autowired
    private EssayService essayService;

    @Autowired
    private RestClient EsClient;

    @Value("${file.uploadFolder}")
    private String uploadFolder;

    @Value("${myconfig.domain}")
    private String domain;

    @Value("${myconfig.getimginterface}")
    private String getimginterface;

    //匹配HTML表达式
    private final static String regxpForHtml = "<([/]?[(img)|(strong)|(p)][^>]*)>";

    private Pattern spiritHtml=Pattern.compile(regxpForHtml);

    @ResponseBody
    @RequestMapping(value="/addEssay", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    // 接受上传的文件 @RequestParam("essay-cover") MultipartFile essayCover
    public CommonMessage addEssay(Essay essay, HttpSession httpSession, HttpServletRequest req, @RequestParam("essay-cover") MultipartFile essayCover) throws IOException, URISyntaxException {
        SimpleUser userData = (SimpleUser) httpSession.getAttribute("userdata");
        if(userData == null){
            return new CommonMessage(false, EssayAddEnum.NOTLOGIN.getName());
        }
        int authorId = userData.getUserId();
        // 根据时间戳创建新的文件名
        String essayTitle = essay.getEssayTitle();
        String essayContent = essay.getEssayContent();
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
            Esessay esessay = new Esessay(essay.getEssayId(),essayTitle,userData.getNickname(),HTMLSpirit(essayContent),getImgUrl,now);
            ResponseEntity<String> res = AddEssayToEs(esessay);
            if(res.getStatusCode()!=HttpStatus.OK){
                System.out.println("数据添加ES失败，原因："+res.toString());
            }
            try{
                appendUserAction(authorId,essay.getEssayId(),5);
            }catch (Exception e){
                e.printStackTrace();
            }
            return new CommonMessage(true, EssayAddEnum.SUCCESS.getName());
        }else{
            return new CommonMessage(false, EssayAddEnum.ERROR.getName());
        }
    }

    @ResponseBody
    @RequestMapping(value="/viewEssay", produces= {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public Essay viewEssay(HttpSession httpSession,@RequestBody Map<String, Object> map) {
        int essid = Integer.parseInt((String) map.get("essid"));
        int userId;
        SimpleUser userData = (SimpleUser) httpSession.getAttribute("userdata");
        if(userData == null){
            userId = -1;
        }else{
            userId = userData.getUserId();
        }
        try{
            appendUserAction(userId,essid,2);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(!essayService.addVisitCount(essid)){
            System.out.println("访问计数错误");
        }

        return essayService.searchEssayByEssayId(essid,userId);
    }

    //文章收藏
    @ResponseBody
    @RequestMapping(value="/collectEssay", produces= {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public CommonMessage collectEssay(HttpSession httpSession,HttpServletRequest request){
        int essayId = Integer.parseInt(request.getParameter("essayId"));
        SimpleUser userData = (SimpleUser) httpSession.getAttribute("userdata");
        if(userData == null){
            return new CommonMessage(false, CommentEnum.FAIL.getName());
        }
        try{
            appendUserAction(userData.getUserId(),essayId,8);
        }catch (Exception e){
            e.printStackTrace();
        }
        if(essayService.collectEssay(essayId,userData.getUserId())){
            return new CommonMessage(false, CommentEnum.SUCCESS.getName());
        }else{
            return new CommonMessage(false, CommentEnum.FAIL.getName());
        }
    }

    @ResponseBody
    @RequestMapping(value="/getUserCollect", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public EssaysDto getUserCollect(HttpSession httpSession, HttpServletResponse response){
        SimpleUser userData = (SimpleUser) httpSession.getAttribute("userdata");
        if(userData == null){
            return new EssaysDto("not login", false);
        }
        List<Essay> essays = essayService.getCollectEssayByUserId(userData.getUserId());
        if(essays != null){
            return new EssaysDto(essays,"ok",true);
        }else{
            return new EssaysDto("select essay error", false);
        }
    }

    @ResponseBody
    @RequestMapping(value="/getUserCreate", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public EssaysDto getUserCreate(HttpSession httpSession, HttpServletResponse response){
        SimpleUser userData = (SimpleUser) httpSession.getAttribute("userdata");
        if(userData == null){
            return new EssaysDto("not login", false);
        }
        List<Essay> essays = essayService.getCreateEssayByUserId(userData.getUserId());
        if(essays != null){
            return new EssaysDto(essays,"ok",true);
        }else{
            return new EssaysDto("select essay error", false);
        }
    }


    //获取最新文章
    @ResponseBody
    @RequestMapping(value="/lastEssay", produces= {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public EssaysDto getLastTenEssay(){
        List<Essay> essays = essayService.getLastTenEssay();
        if(essays != null){
            return new EssaysDto(essays,"ok",true);
        }else{
            return new EssaysDto("select essay error",false);
        }
    }
    //获取热度最高文章
    @ResponseBody
    @RequestMapping(value="/hotEssay", produces= {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public EssaysDto getHotTenEssay(){
        List<Essay> essays = essayService.getHotTenEssay();
        if(essays != null){
            return new EssaysDto(essays,"ok",true);
        }else{
            return new EssaysDto("select essay error",false);
        }
    }

    //关键词文章搜索
    @ResponseBody
    @RequestMapping(value="/searchEssay",produces= {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public EssaysDto searchEssayFromEs(HttpServletRequest request) throws IOException, ParseException {
        String matchStr = request.getParameter("matchStr");
        //System.out.println(matchStr);
        return matchEssay(matchStr);
    }

    //获取文章智能推荐结果
    @ResponseBody
    @RequestMapping(value="/intelligentEssay",produces= {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public EssaysDto intelligentRecommendEssay(HttpSession httpSession){
        SimpleUser userData = (SimpleUser) httpSession.getAttribute("userdata");
        if(userData == null){
            return new EssaysDto("not login", false);
        }
        List<Essay> essays = essayService.getRecommendEssays(userData.getUserId());
        if(essays != null){
            return new EssaysDto(essays,"ok",true);
        }else{
            return new EssaysDto("select essay error", false);
        }
    }

    //其他增加文章和用户之间评分关系的接口
    @ResponseBody
    @RequestMapping(value="/appendAction",produces= {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public CommonMessage otherAppendUserAction(HttpSession httpSession,HttpServletRequest request) throws IOException, URISyntaxException {
        SimpleUser userData = (SimpleUser) httpSession.getAttribute("userdata");
        if(userData == null){
            return new CommonMessage(false,"用户未登录");
        }
        String essayId = request.getParameter("essayId");
        String score = request.getParameter("score");
        appendUserAction(userData.getUserId(),Integer.valueOf(essayId),Integer.valueOf(score));
        return new CommonMessage(true,"增加成功");
    }


    //添加文章到ES数据库中
    private ResponseEntity<String> AddEssayToEs(Esessay esessay) throws IOException {
        Request request = new Request("POST","/essaydata/essay/"+esessay.getId());
        request.addParameter("pretty","true");
        String jsonString = esessay.toString();
        System.out.println(jsonString);
        request.setEntity(new NStringEntity(jsonString,ContentType.APPLICATION_JSON));
        Response response = EsClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        return new ResponseEntity<>(responseBody,HttpStatus.OK);
    }

    //过滤字符串中的html标签
    private String HTMLSpirit(String htmlStr){
        String res;
        Matcher m_html=spiritHtml.matcher(htmlStr);
        res=m_html.replaceAll("").replaceAll("\n", "").replaceAll("\"",""); //过滤
        return res;
    }

    //对文章执行match操作
    private EssaysDto matchEssay(String matchStr) throws IOException, ParseException {
        Request request = new Request("POST","/essaydata/_search");
        request.addParameter("pretty","true");
        String matchJson ="{\n" +
                "\t\"query\":{\n" +
                "\t\t\"multi_match\":{\n" +
                "\t\t\t\"analyzer\": \"optimizeIK\",\n" +
                "\t\t\t\"query\":\""+matchStr+"\",\n" +
                "\t\t\t\"fields\":[\"content^2\",\"title\",\"author\"]\n" +
                "\t\t}\n" +
                "\t},\n" +
                "\t\"from\":0,\n" +
                "\t\"size\":10\n" +
                "}";
        //System.out.println(matchJson);
        request.setEntity(new NStringEntity(matchJson,ContentType.APPLICATION_JSON));
        Response response = EsClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
//        System.out.println(responseBody);
        List<Essay> essays = new ArrayList<>();
        JSONObject root=new JSONObject(responseBody);
        JSONArray hits=root.getJSONObject("hits").getJSONArray("hits");
        for(int i=0;i<hits.length();i++){
            JSONObject hit = hits.getJSONObject(i);
            JSONObject source = hit.getJSONObject("_source");
            Essay tempEssay = new Essay();
            tempEssay.setEssayId( hit.getInt("_id"));
            tempEssay.setEssayTitle(source.getString("title"));
            tempEssay.setAuthorName(source.getString("author"));
            tempEssay.setEssayContent(source.getString("content").substring(0,200));
            tempEssay.setEssayCover(source.getString("img"));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            tempEssay.setEssayDate(Timestamp.valueOf(source.getString("date")));
            essays.add(tempEssay);
        }
        return new EssaysDto(essays,"查询成功",true);
    }

    //往Hadoop中追加用户操作记录
    private void appendUserAction(int userId,int essayID,int score) throws IOException, URISyntaxException {
        System.out.println("=========run start============");
        if(userId <= 0){
            return;
        }
        FileSystem fs = null;
        FSDataOutputStream out = null;
        try{
            Configuration conf = new Configuration();
            conf.set("dfs.support.append", "true");
            conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
            conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
            fs = FileSystem.get(new URI("hdfs://192.168.42.132:9000"),conf);
            out = fs.append(new Path("/itemCF/step1_input/UserAction.txt"));
            out.write(("\n"+userId+","+essayID+","+score).getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fs != null){
                fs.close();
            }
            if(out != null){
                out.close();
            }
        }
        System.out.println("===========run end===========");
    }

    @ResponseBody
    @RequestMapping(value="/appendTest",produces= {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    private void appendUserActionTest() throws IOException, URISyntaxException {
        System.out.println("=========run start============");
        FileSystem fs = null;
        FSDataOutputStream out = null;
        try{
            Configuration conf = new Configuration();
            conf.set("dfs.support.append", "true");
            conf.set("dfs.client.block.write.replace-datanode-on-failure.policy", "NEVER");
            conf.set("dfs.client.block.write.replace-datanode-on-failure.enable", "true");
            fs = FileSystem.get(new URI("hdfs://192.168.42.132:9000"),conf);
            out = fs.append(new Path("/itemCF/step1_input/UserAction.txt"));
            out.write(("\n1,1,1").getBytes());
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if(fs != null){
                fs.close();
            }
            if(out != null){
                out.close();
            }
        }
        System.out.println("===========run end===========");
    }

    //es连接测试方法
    @RequestMapping(value = "/esTest", method = RequestMethod.GET)
    public ResponseEntity<String> getEsInfo() throws IOException {
        Request request = new Request("GET", "/");
        Response response = EsClient.performRequest(request);
        String responseBody = EntityUtils.toString(response.getEntity());
        return new ResponseEntity<>(responseBody, HttpStatus.OK);
    }
}
