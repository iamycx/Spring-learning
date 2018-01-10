package com.iamycx.club.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.iamycx.club.async.EventHandler;
import com.iamycx.club.async.EventModel;
import com.iamycx.club.async.EventType;
import com.iamycx.club.model.EntityType;
import com.iamycx.club.model.Feed;
import com.iamycx.club.model.Question;
import com.iamycx.club.model.User;
import com.iamycx.club.service.*;
import com.iamycx.club.util.JedisAdapter;
import com.iamycx.club.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by iamycx on 20:31 2018/1/4
 */
@Component
public class FeedHandler implements EventHandler {

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    private String buildFeedData(EventModel model) {
        Map<String, String> map = new HashMap<>();
        User actor = userService.getUser(model.getActorId());
        if (actor == null) {
            return null;
        }
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());

        if ((model.getType() == EventType.COMMENT) || (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION)) {
            Question question = questionService.getById(model.getEntityId());
            if (question == null) {
                return null;
            }
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandle(EventModel model) {
        //方便测试数据
        Random r = new Random();
        model.setActorId(1+r.nextInt(10));


        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setUserId(model.getActorId());
        feed.setType(model.getType().getValue());
        feed.setData(buildFeedData(model));
        if (feed.getData() == null) {
            return;
        }
        feedService.addFeed(feed);

        //给事件的粉丝推
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER,model.getActorId(),Integer.MAX_VALUE);
        followers.add(0);
        for (int follow:followers){
            String timelineKey = RedisKeyUtil.getTimelineKey(follow);
            jedisAdapter.lpush(timelineKey,String.valueOf(feed.getId()));
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }
}
