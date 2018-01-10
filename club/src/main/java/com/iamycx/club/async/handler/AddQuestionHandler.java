package com.iamycx.club.async.handler;

import com.iamycx.club.async.EventHandler;
import com.iamycx.club.async.EventModel;
import com.iamycx.club.async.EventType;
import com.iamycx.club.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by iamycx on 13:41 2018/1/10
 */
@Component
public class AddQuestionHandler implements EventHandler{
    private static final Logger logger = LoggerFactory.getLogger(AddQuestionHandler.class);

    @Autowired
    SearchService searchService;

    @Override
    public void doHandle(EventModel model) {

        try {
            searchService.indexQuestion(model.getEntityId(),model.getExt("title"),model.getExt("content"));
        } catch (Exception e){
            logger.error("增加题目索引失败");
        }
    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.ADD_QUESTION);
    }
}
