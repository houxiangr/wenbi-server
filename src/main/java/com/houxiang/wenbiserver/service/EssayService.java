package com.houxiang.wenbiserver.service;

import com.houxiang.wenbiserver.mapper.EssayMapper;
import com.houxiang.wenbiserver.model.Essay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EssayService {

    @Autowired
    private EssayMapper essayMapper;


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

}
