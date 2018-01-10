package com.iamycx.club.model;

import org.springframework.stereotype.Component;

/**
 * Created by iamycx on 15:59 2017/12/24
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users = new ThreadLocal<User>();

    public User getUser(){
        return users.get();
    }

    public void setUser(User user){
        users.set(user);
    }

    public void clear(){
        users.remove();
    }
}
