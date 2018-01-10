package com.iamycx.club.service;

import org.springframework.stereotype.Service;

/**
 * Created by iamycx on 16:32 2018/1/3
 */
@Service
public class ClubService {
    public String getMessage(int userId) {
        return "Hello Message:" + String.valueOf(userId);
    }
}
