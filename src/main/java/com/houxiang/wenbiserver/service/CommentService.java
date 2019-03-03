package com.houxiang.wenbiserver.service;

import com.houxiang.wenbiserver.mapper.CommentMapper;
import com.houxiang.wenbiserver.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    //通过当前文章位置获取评论
    public List<Comment> getCommentsByPos(int essayid, int pos, int commentCount, int startpos) {
        int start = pos - 10;
        int end = pos + 10;
        return commentMapper.getCommentsByPos(essayid, start, end, commentCount, startpos);
    }

    //添加文章
    public boolean addComment(Comment comment){
        return commentMapper.addComment(comment) == 1;
    }
}
