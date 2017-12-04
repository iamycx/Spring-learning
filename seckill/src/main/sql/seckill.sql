--秒杀存储执行过程
DELIMITER $$ --console ; 转换为 $$
--定义存储过程
--参数in输入参数，out输出参数
--row_count()：返回上亿条修改类型sql(delete,insert,update)影响行数
--row_count:0未修改数据 >0表示修改的行数，<0代表sql错误或者未修改sql
CREATE PROCEDURE `seckill`.`execute_seckill` (IN v_seckill_id bigint,IN v_phone bigint, IN v_kill_time TIMESTAMP ,out r_result INT )
  BEGIN
    DECLARE insert_count INT DEFAULT 0;
    start TRANSACTION ;
    INSERT ignore INTO success_killed
      (seckill_id,user_phone,create_time) VALUES (v_seckill_id,v_phone,v_kill_time);
    SELECT ROW_COUNT() INTO insert_count;
    if(insert_count=0) THEN
      ROLLBACK ;
      SET r_result=-1;
    elseif(insert_count<0) THEN
      ROLLBACK ;
      SET r_result=-2;
    ELSE
      UPDATE seckill SET NUMBER = NUMBER -1
      WHERE seckill_id = v_seckill_id AND end_time>v_kill_time AND start_time<v_kill_time AND NUMBER >0;
      SELECT ROW_COUNT() INTO insert_count;
      if(insert_count=0) THEN
        ROLLBACK ;
        SET r_result=0;
      elseif(insert_count<0) THEN
        ROLLBACK ;
        set r_result=-2;
      ELSE
        COMMIT ;
        set r_result = 1;
      END if;
    END if;
  END;
$$
--存储过程定义结束


--验证存储过程是否有错
DELIMITER ;
SET @r_result=-3;
--执行存储过程
call execute_seckill(1002,13535458888,now(),@r_result);
--获取结果
SELECT @r_result;


--存储过程
--1.存储过程优化：让事务行级锁所持有的时间尽可能的短
--2.不要过度依赖存储过程
--3.简单的逻辑可以应用存储过程
--4.QPS:一个秒杀单6000/qps