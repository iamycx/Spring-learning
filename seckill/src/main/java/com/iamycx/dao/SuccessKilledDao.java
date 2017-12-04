package com.iamycx.dao;

import com.iamycx.entity.SuccessKilled;
import org.apache.ibatis.annotations.Param;

/**
 * Created by iamycx on 14:12 2017/11/29
 */
public interface SuccessKilledDao {
    //插入购买明细，可以过滤重复 return为插入的行数
    int insertSuccessKilled(@Param("secKillId") long secKillId,@Param("userPhone") long userPhone);
    //根据id查询SucceessKilled并携带秒杀产品对象实体
    SuccessKilled queryByIdWithSeckill(@Param("seckillId") long seckillId,@Param("userPhone") long userPhone);
}
