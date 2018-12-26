package com.houxiang.wenbiserver.service;

import com.houxiang.wenbiserver.mapper.EssayMapper;
import com.houxiang.wenbiserver.model.Essay;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EssayService {

    @Autowired
    private EssayMapper essayMapper;

    public boolean addEssay(Essay essay){
        return essayMapper.insertEssay(essay) == 1;
    }

}
