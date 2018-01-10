package com.iamycx.club;

import com.iamycx.club.dao.QuestionDao;
import com.iamycx.club.dao.UserDao;
import com.iamycx.club.model.EntityType;
import com.iamycx.club.model.Question;
import com.iamycx.club.model.User;
import com.iamycx.club.service.FollowService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.Random;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ClubApplication.class)
@Sql("/init-schema.sql")
public class InitDatabaseTests {
    @Autowired
    UserDao userDao;
    @Autowired
    QuestionDao questionDao;
    @Autowired
    FollowService followService;

    @Test
    public void contextLoads() {
        Random random = new Random();
        for (int i = 0; i < 11; ++i) {
            User user = new User();
            user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", random.nextInt(1000)));
            user.setName(String.format("USER%d", i));
            user.setPassword("");
            user.setSalt("");
            userDao.addUser(user);

            //互相关注
            for (int j=1;j<i;j++){
                followService.follow(j, EntityType.ENTITY_USER,i);
            }

            user.setPassword("yyy");
            userDao.updatePassword(user);

            Question question = new Question();
            question.setCommentCount(i);
            Date date = new Date();
            date.setTime(date.getTime()+1000*3600+i);
            question.setCreatedDate(date);
            question.setUserId(i+1);
            question.setTitle(String.format("Title %d",i));
            question.setContent(String.format("no. %d",i));

            questionDao.addQuestion(question);
        }
        Assert.assertEquals("yyy",userDao.selectById(1).getPassword());
//        userDao.deleteById(1);
//        Assert.assertNull(userDao.selectById(1));
        System.out.println(questionDao.selectLatestQuestions(0,0,10));
    }
}
