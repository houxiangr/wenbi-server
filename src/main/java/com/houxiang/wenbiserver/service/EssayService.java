package com.houxiang.wenbiserver.service;

import com.houxiang.wenbiserver.itemCF.EssayScore;
import com.houxiang.wenbiserver.itemCF.ItemCFEngine;
import com.houxiang.wenbiserver.mapper.EssayMapper;
import com.houxiang.wenbiserver.model.Essay;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.hadoop.fs.FsShell;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class EssayService {

    @Autowired
    private EssayMapper essayMapper;

    @Autowired
    FsShell fsShell;

    @Autowired
    private ItemCFEngine itemCFEngine;

    private List<Essay> tenHotEssays = new ArrayList<>();

    private Map<Integer,String> userRecommenceMap = new HashMap<>();

    private int RECOMMENT_COUNT = 10;

    @PostConstruct
    public void EssayInit() throws IOException, URISyntaxException, InterruptedException, ClassNotFoundException {
        updateHotEssayList();
        updateRecommendMap();
    }

    // 添加文章到数据库
    public boolean addEssay(Essay essay){
        return essayMapper.insertEssay(essay) == 1;
    }

    // 查找同一作者是否有同一标题文章
    public boolean searchTheSameTitle(int authorId,String essayTitle){
        return essayMapper.selectEssayByAuthorIdAndEssayTitle(authorId,essayTitle) >= 1;
    }

    // 通过文章id查找文章详细信息
    public Essay searchEssayByEssayId(int essayId,int userId){
        return essayMapper.selectEssayByEssayId(essayId,userId);
    }

    //收藏文章
    public boolean collectEssay(int essayId,int userId){
        return essayMapper.collectEssay(essayId,userId) == 1;
    }

    public List<Essay> getCollectEssayByUserId(Integer userId){
        return essayMapper.getCollectEssayByUserId(userId);
    }

    public List<Essay> getCreateEssayByUserId(Integer userId){
        return essayMapper.getCreateEssayByUserId(userId);
    }

    //获取最新文章
    public List<Essay> getLastTenEssay(){
        return essayMapper.getLastTenEssay();
    }

    //定时函数更新最热文章列表
    @Scheduled(cron = "0 0/1 * * * ?")
    private void updateHotEssayList(){
        tenHotEssays.clear();
        List<Essay> essaysHotCount = essayMapper.getHotEssayData();
        Collections.sort(essaysHotCount);
        tenHotEssays.addAll(essaysHotCount);
        System.out.println("tenHotEssays init");
    }

    @Scheduled(cron = "0 0/2 * * * ?")
    private void updateRecommendMap() throws IOException, URISyntaxException, ClassNotFoundException, InterruptedException {
        itemCFEngine.getRecommendEssay();
        userRecommenceMap.clear();
        Configuration conf = new Configuration();
        FileSystem fs = FileSystem.get(new URI("hdfs://192.168.42.132:9000"),conf);
        FSDataInputStream in = fs.open(new Path("/itemCF/step5_output/part-r-00000"));
        BufferedReader br = new BufferedReader(new InputStreamReader(in));
        String line = null;
        while((line = br.readLine())!=null) {
            Integer userId = Integer.valueOf(line.split("\t")[0]);
            String itemScore = line.split("\t")[1];
            userRecommenceMap.put(userId,itemScore);
        }
        in.close();
        br.close();
    }


    public List<Essay> getRecommendEssays(int userID){
        String essayScores = userRecommenceMap.get(userID);
        System.out.println(essayScores);
        String[] essay_scores = essayScores.split(",");
        List<EssayScore> essayScoreList = new ArrayList<>();
        for(String essay_score : essay_scores){
            Integer essayID = Integer.valueOf(essay_score.split("_")[0]);
            Double score = Double.valueOf(essay_score.split("_")[1]);
            essayScoreList.add(new EssayScore(essayID,score));
        }
        Collections.sort(essayScoreList);
        if(essayScoreList.size()<RECOMMENT_COUNT){
            RECOMMENT_COUNT = essayScoreList.size();
        }
        Integer[] recommentEssayID = new Integer[RECOMMENT_COUNT];
        for(int i=0;i<RECOMMENT_COUNT;i++){
            recommentEssayID[i] = essayScoreList.get(i).getEssayID();
        }
        return essayMapper.getEssayByEssayIdList(recommentEssayID);
    }

    //获取热门文章
    public List<Essay> getHotTenEssay(){
        return tenHotEssays;
    }

    //添加访问记录
    public boolean addVisitCount(int essayId){
        return essayMapper.addVisitCount(essayId)==1;
    }
}
