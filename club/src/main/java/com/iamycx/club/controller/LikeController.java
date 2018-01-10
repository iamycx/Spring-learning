package com.iamycx.club.controller;

import com.iamycx.club.async.EventModel;
import com.iamycx.club.async.EventProducer;
import com.iamycx.club.async.EventType;
import com.iamycx.club.model.Comment;
import com.iamycx.club.model.EntityType;
import com.iamycx.club.model.HostHolder;
import com.iamycx.club.service.CommentService;
import com.iamycx.club.service.LikeService;
import com.iamycx.club.service.QuestionService;
import com.iamycx.club.util.ClubUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * Created by iamycx on 9:46 2017/12/29
 */
@Controller
public class LikeController {
    private static final Logger logger = LoggerFactory.getLogger(LikeController.class);

    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    CommentService commentService;

    @Autowired
    EventProducer eventProducer;


    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return ClubUtil.getJSONString(999);
        }

        Comment comment = commentService.getCommentById(commentId);

        eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(hostHolder.getUser().getId()).setEntityId(commentId).setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getUserId()).setExt("questionId", String.valueOf(comment.getEntityId())));
        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return ClubUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId) {
        if (hostHolder.getUser() == null) {
            return ClubUtil.getJSONString(999);
        }
        long likeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);
        return ClubUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
