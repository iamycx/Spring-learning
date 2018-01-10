package com.iamycx.club.service;

import com.iamycx.club.dao.CommentDao;
import com.iamycx.club.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by iamycx on 20:26 2017/12/26
 */
@Service
public class CommentService {
    @Autowired
    CommentDao commentDao;

    @Autowired
    SensitiveService sensitiveService;

    public List<Comment> getCommentByEntity(int entityId,int entityType){
        return commentDao.selectCommentByEntity(entityId,entityType);
    }

    public int addComment(Comment comment){
        //敏感词 html标签过滤
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDao.addComment(comment)>0? comment.getId():0;
    }

    public int getCommentCount(int entityId, int entityType){
        return commentDao.getCommentCount(entityId,entityType);
    }

    public int getUserCommentCount(int userId) {
        return commentDao.getUserCommentCount(userId);
    }

    public boolean deleteComment(int commentId){
        return commentDao.updateStatus(commentId,1)>0;
    }

    public Comment getCommentById(int id){
        return commentDao.getCommentById(id);
    }
}
