package com.iamycx.dao;

import com.iamycx.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by iamycx on 16:08 2017/11/29
 */
/**
 * 配置spring和junit整合，junit启动时加载springIOC容器
 * spring-test,junit
 */
@RunWith(SpringJUnit4ClassRunner.class)
//告知junit spring配置文件
@ContextConfiguration({"classpath:spring/spring-dao.xml"})
public class SeckillDaoTest {

    //注入Dao实现类依赖
    @Resource
    private SeckillDao seckillDao;

    @Test
    public void queryById() throws Exception {
        long id = 1000;
        Seckill seckill = seckillDao.queryById(id);
        System.out.println(seckill.getName());
        System.out.println(seckill);
        /**
         * 1000元秒杀iPhoneX
         Seckill{seckillId=1000, name='1000元秒杀iPhoneX', number=100, startTime=Sun Dec 10 00:00:00 CST 2017, endTime=Mon Dec 11 00:00:00 CST 2017, createTime=Wed Nov 29 11:48:42 CST 2017}
         */
    }

    @Test
    public void queryAll() throws Exception {
        /**
         * Caused by: org.apache.ibatis.binding.BindingException: Parameter 'offet' not found. Available parameters are [0, 1, param1, param2]
         *
         * List<Seckill> queryAll(int offet,int limit);
         * java没有保存形参的记录  queryAll(int offet,int limit)—>queryAll(arg0,arg1)
         * 改为List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
         */
        List<Seckill> seckills = seckillDao.queryAll(0,100);
        for (Seckill s:seckills){
            System.out.println(s);
        }
        /**
         * Seckill{seckillId=1000, name='1000元秒杀iPhoneX', number=100, startTime=Sun Dec 10 00:00:00 CST 2017, endTime=Mon Dec 11 00:00:00 CST 2017, createTime=Wed Nov 29 11:48:42 CST 2017}
         Seckill{seckillId=1001, name='500元秒杀iPhone8', number=200, startTime=Sun Dec 10 00:00:00 CST 2017, endTime=Mon Dec 11 00:00:00 CST 2017, createTime=Wed Nov 29 11:48:42 CST 2017}
         Seckill{seckillId=1002, name='300元秒杀iPad', number=300, startTime=Sun Dec 10 00:00:00 CST 2017, endTime=Mon Dec 11 00:00:00 CST 2017, createTime=Wed Nov 29 11:48:42 CST 2017}
         Seckill{seckillId=1003, name='1000元秒杀iPhone7', number=400, startTime=Sun Dec 10 00:00:00 CST 2017, endTime=Mon Dec 11 00:00:00 CST 2017, createTime=Wed Nov 29 11:48:42 CST 2017}
         */
    }

    @Test
    public void reduceNumber() throws Exception {
        Date killTime = new Date();
        int updateCount = seckillDao.reduceNumber(1000L,killTime);
        System.out.println("updateCount="+updateCount);
    }

}