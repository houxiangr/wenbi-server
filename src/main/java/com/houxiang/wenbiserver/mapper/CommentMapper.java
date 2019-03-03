package com.houxiang.wenbiserver.mapper;

import com.houxiang.wenbiserver.model.Comment;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CommentMapper {

    //通过当前文章位置获取评论
    public List<Comment> getCommentsByPos(@Param("essayid") int essayid,@Param("start")int start,@Param("end")int end, @Param("commentCount")int commentCount,@Param("startpos") int startpos);

    //添加评论
    public int addComment(Comment comment);

    //评论点赞
    public int upComment(@Param("commentId") int commentId);

    //保存用户的点赞关系
    public int addUpAuthor(@Param("commentId") int commentId,@Param("userId") int userId);
}
