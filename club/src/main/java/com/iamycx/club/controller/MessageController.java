package com.iamycx.club.controller;

import com.iamycx.club.model.*;
import com.iamycx.club.service.CommentService;
import com.iamycx.club.service.MessageService;
import com.iamycx.club.service.QuestionService;
import com.iamycx.club.service.UserService;
import com.iamycx.club.util.ClubUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by iamycx on 15:17 2017/12/27
 */
@Controller
public class MessageController {
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    @Autowired
    HostHolder hostHolder;

    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;


    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addComment(@RequestParam("toName") String toName,
                             @RequestParam("content") String content) {
        try {
            if(hostHolder.getUser()==null){
                return ClubUtil.getJSONString(999,"用户未登录");
            }
            User user = userService.selectUserByName(toName);
            if(user==null){
                return ClubUtil.getJSONString(1,"用户不存在");
            }
            Message message = new Message();
            message.setCreatedDate(new Date());
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setContent(content);
            messageService.addMessage(message);
            return ClubUtil.getJSONString(0);

        } catch (Exception e) {
            logger.error("发送消息失败" + e.getMessage());
            return ClubUtil.getJSONString(1,"发送信息失败");
        }
    }

    @RequestMapping(path = {"/msg/list"}, method = {RequestMethod.GET})
    public String getConversationList(Model model) {
        if (hostHolder.getUser() == null) {
            return "redirect:/reglogin";
        }
        int localUserId = hostHolder.getUser().getId();
        List<Message> conversationList = messageService.getConversationList(localUserId, 0, 10);
        List<ViewObject> conversations = new ArrayList<ViewObject>();
        for (Message message : conversationList) {
            ViewObject vo = new ViewObject();
            vo.set("message", message);
            int targetId = message.getFromId() == localUserId ? message.getToId() : message.getFromId();
            vo.set("user", userService.getUser(targetId));
            vo.set("unread", messageService.getConversationUnreadCount(localUserId, message.getConversationId()));
            conversations.add(vo);
        }
        model.addAttribute("conversations", conversations);
        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"}, method = {RequestMethod.GET})
    public String addComment(Model model,@RequestParam("conversationId") String conversationId) {
        try{
            List<Message> messageList = messageService.getConversationByDetail(conversationId,0,10);
            List<ViewObject> messages = new ArrayList<ViewObject>();
            for (Message message:messageList){
                ViewObject vo = new ViewObject();
                vo.set("message",message);
                vo.set("user",userService.getUser(message.getFromId()));
                messages.add(vo);
            }
            model.addAttribute("messages",messages);
        }catch (Exception e){
            logger.error("获取详细信息失败"+e.getMessage());
        }
        return "letterDetail";
    }
}
