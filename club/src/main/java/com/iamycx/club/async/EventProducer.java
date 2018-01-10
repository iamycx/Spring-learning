package com.iamycx.club.async;

import com.alibaba.fastjson.JSONObject;
import com.iamycx.club.util.JedisAdapter;
import com.iamycx.club.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by iamycx on 15:26 2017/12/29
 */
@Service
public class EventProducer {

    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel){
        try{
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key,json);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}
