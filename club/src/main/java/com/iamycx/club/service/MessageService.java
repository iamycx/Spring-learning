package com.iamycx.club.service;

import com.iamycx.club.dao.MessageDao;
import com.iamycx.club.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by iamycx on 11:45 2017/12/27
 */
@Service
public class MessageService {
    @Autowired
    MessageDao messageDao;

    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message){
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDao.addMessage(message)>0?message.getId():0;
    }

    public List<Message> getConversationByDetail(String conversationId,int offset,int limit){
        return messageDao.getConversationByDetail(conversationId,offset,limit);
    }

    public List<Message> getConversationList(int userId, int offset, int limit) {
        return  messageDao.getConversationList(userId, offset, limit);
    }

    public int getConversationUnreadCount(int userId, String conversationId) {
        return messageDao.getConversationUnreadCount(userId, conversationId);
    }
}
