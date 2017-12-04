package com.iamycx.dao;

import com.iamycx.entity.SuccessKilled;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import static org.junit.Assert.*;

/**
 * Created by iamycx on 17:10 2017/11/29
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SuccessKilledDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SuccessKilledDao successKilledDao;

    @Test
    public void insertSuccessKilled() throws Exception {
        /**
         * 第一次：insertCount=1
         * 第二次：insertCount=0
         * 不允许重复秒杀插入数据
         */
        long id = 1001L;
        long phone = 13588886667L;
        int insertCount = successKilledDao.insertSuccessKilled(id,phone);
        System.out.println("insertCount="+insertCount);
    }

    @Test
    public void queryByIdWithSeckill() throws Exception {
        long id = 1001L;
        long phone = 13588886667L;
        SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(id,phone);
        System.out.println(successKilled);
        System.out.println(successKilled.getSeckill());
        /**
         * SuccessKilled{seckillId=1001, userPhone=13588886667, state=0, createTime=Wed Nov 29 17:26:04 CST 2017}
         Seckill{seckillId=1001, name='500元秒杀iPhone8', number=200, startTime=Sun Dec 10 00:00:00 CST 2017, endTime=Mon Dec 11 00:00:00 CST 2017, createTime=Wed Nov 29 11:48:42 CST 2017}
         */
    }

}