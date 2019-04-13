package com.houxiang.wenbiserver.service;

import com.houxiang.wenbiserver.mapper.EssayMapper;
import com.houxiang.wenbiserver.model.Essay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class EssayService {

    @Autowired
    private EssayMapper essayMapper;

    private List<Essay> tenHotEssays = new ArrayList<>();

    @PostConstruct
    public void EssayInit(){
        updateHotEssayList();
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
        int len = essaysHotCount.size();
        for(int i=0;i<len;i++){
            tenHotEssays.add(essaysHotCount.get(i));
        }
        System.out.println("tenHotEssays init");
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
