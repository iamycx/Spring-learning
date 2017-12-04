package com.iamycx.service;

import com.iamycx.dto.Exposer;
import com.iamycx.dto.SeckillExecution;
import com.iamycx.entity.Seckill;
import com.iamycx.exception.RepeatKillException;
import com.iamycx.exception.SeckillCloseException;
import com.iamycx.exception.SeckillException;

import java.util.List;

/**
 * Created by iamycx on 21:10 2017/11/29
 *
 * 业务接口：站在“使用者”角度设计接口
 * 三个方面：方法定义的粒度，参数，返回类型（return类型/异常）
 */
public interface SeckillService {
    //查询所有秒杀记录
    List<Seckill> getSeckillList();
    //查询单个秒杀记录
    Seckill getById(long seckillId);
    //秒杀开始时候输出秒杀接口地址，否者输出秒杀开始时间和系统时间
    Exposer exportSeckillUrl(long seckillId);
    //执行秒杀操作
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException,RepeatKillException,SeckillCloseException;
    //执行秒杀操作by存储过程
    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);
}
