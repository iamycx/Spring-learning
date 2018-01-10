package com.iamycx.club.async;

import java.util.List;

/**
 * Created by iamycx on 16:29 2017/12/29
 */
public interface EventHandler {
    void doHandle(EventModel model);
    List<EventType> getSupportEventTypes();
}