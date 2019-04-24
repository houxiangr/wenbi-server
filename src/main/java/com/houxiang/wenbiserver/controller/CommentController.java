package com.houxiang.wenbiserver.controller;

import com.houxiang.wenbiserver.dto.CommentsMessage;
import com.houxiang.wenbiserver.dto.CommonMessage;
import com.houxiang.wenbiserver.model.Comment;
import com.houxiang.wenbiserver.model.SimpleUser;
import com.houxiang.wenbiserver.service.CommentService;
import com.houxiang.wenbiserver.stateEnum.CommentAddEnum;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value = "/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    //通过条件查询对应评论
    @ResponseBody
    @RequestMapping(value = "/getComment", produces = {"application/json;charset=UTF-8"}, method = RequestMethod.POST)
    public CommentsMessage getComment(HttpServletRequest request) {
        int essayId = Integer.parseInt(request.getParameter("essayid"));
        int pos = Integer.parseInt(request.getParameter("pos"));
        int startpos = Integer.parseInt(request.getParameter("startpos"));
        //通过计算动态调整每次需要获取的评论数
        int commentCount = Integer.parseInt(request.getParameter("pagecount"));
        List<Comment> comments = commentService.getCommentsByPos(essayId, pos, commentCount, startpos);
        if (comments.size() == 0) {
            return new CommentsMessage(false, "已没有最新的评论");
        } else {
            return new CommentsMessage(true, comments);
        }
    }

    //添加评论
    @ResponseBody
    @RequestMapping(value = "/addComment", produces = {"application/json;charset=UTF-8"},method=RequestMethod.POST)
    public CommonMessage addComment(Comment comment,HttpSession httpSession, HttpServletRequest request){
        String commentContent = comment.getCommentContent();
        if(commentContent.equals("")){
            return new CommonMessage(false, CommentAddEnum.INPUTEMPTY.getName());
        }
        SimpleUser userData = (SimpleUser) httpSession.getAttribute("userdata");
        if(userData == null){
            return new CommonMessage(false, CommentAddEnum.NOTLOGIN.getName());
        }
        int authorId = userData.getUserId();
        comment.setAuthorId(authorId);
        comment.setCommentDate(new Timestamp(System.currentTimeMillis()));
        if(commentService.addComment(comment)){
            return new CommonMessage(true, CommentAddEnum.SUCCESS.getName());
        }else{
            return new CommonMessage(false, CommentAddEnum.ERROR.getName());
        }
    }

    //评论点赞
    @ResponseBody
    @RequestMapping(value = "upComment",produces = {"application/json;charset=UTF-8"},method=RequestMethod.POST)
    @Transactional
    public CommonMessage upComment(HttpSession httpSession,HttpServletRequest request){
        int commentId = Integer.parseInt(request.getParameter("commentId"));
        SimpleUser userData = (SimpleUser) httpSession.getAttribute("userdata");
        if(userData == null){
            return new CommonMessage(false);
        }
        commentService.addUpAuthor(commentId,userData.getUserId());
        if(commentService.upComment(commentId)){
            return new CommonMessage(true);
        }else{
            return new CommonMessage(false);
        }
    }
}
