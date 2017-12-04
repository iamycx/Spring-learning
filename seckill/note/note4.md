### (四)Java高并发秒杀API之高并发优化

#### 下载`Redis`
  - 下载完后解压压缩包
   + 进入解压后的文件夹里面 ,执行命令  `make `
   + 然后再执行`sudo make install`
   + 最后再启动`Redis`,启动命令为`redis-server`
   + 执行命令'redis-cli -p 6379'查看运行情况
   
####  使用`Java`操作`Redis`
+ 导入操作`Redis`的`jedis`的 jar包
```xml
	<dependency>
            <groupId>redis.clients</groupId>
            <artifactId>jedis</artifactId>
            <version>2.9.0</version>
        </dependency>
```
+ 添加`protostuff-core`以及`protostuff-runtime`序列化jar包
```xml
	<dependency>
            <groupId>com.dyuproject.protostuff</groupId>
            <artifactId>protostuff-core</artifactId>
            <version>1.1.1</version>
        </dependency>
        <dependency>
            <groupId>com.dyuproject.protostuff</groupId>
            <artifactId>protostuff-runtime</artifactId>
            <version>1.1.1</version>
        </dependency>
```

+ 在`com.iamycx.dao`下建包`cache`
  + 然后建立类`RedisDao`
 ````java
package com.iamycx.dao.cache;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtostuffIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.iamycx.entity.Seckill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Created by iamycx on 10:53 2017/12/4
 */
public class RedisDao {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private JedisPool jedisPool;
    public RedisDao(String ip,int port){
        jedisPool = new JedisPool(ip,port);
    }
    private RuntimeSchema<Seckill> schema = RuntimeSchema.createFrom(Seckill.class);

    public Seckill getSeckill(long seckillId){
        //redis操作逻辑
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:"+seckillId;
                //并没有实现内部序列化操作
                //get->byte[]->反序列化->Object(Seckill)
                //采用自定义的序列化方式
                //protostuff:pojo
                byte[] bytes = jedis.get(key.getBytes());
                if(bytes!=null){
                    //空对象
                    Seckill seckill = schema.newMessage();
                    ProtostuffIOUtil.mergeFrom(bytes,seckill,schema);
                    return seckill;//seckill被反序列
                }
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }

    public String putSeckill(Seckill seckill){
        //set Object(Seckill)->序列化->byte[]
        try {
            Jedis jedis = jedisPool.getResource();
            try {
                String key = "seckill:"+seckill.getSeckillId();
                byte[] bytes = ProtostuffIOUtil.toByteArray(seckill,schema, LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
                //超时缓存
                int timeout = 60*60;//1小时
                String result = jedis.setex(key.getBytes(),timeout,bytes);
                return result;
            } finally {
                jedis.close();
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
        }
        return null;
    }
}

````

+ 下一步是在在`applicationContext-dao.xml`中注入`redisDao`
```xml
    <!--注入RedisDao-->
    <bean id="redisDao" class="com.iamycx.dao.cache.RedisDao">
        <constructor-arg index="0" value="localhost"/>
        <constructor-arg index="1" value="6379"/>
    </bean>
```
+ 改造`exportSeckillUrl`方法,一定要先注入`redisDao`
```java

    @Autowired
    private RedisDao redisDao;


    @Override
    public Exposer exportSeckillUrl(long seckillId) {
        //优化缓存
        //1.访问redis
        Seckill seckill = redisDao.getSeckill(seckillId);
        if(seckill==null){
            //2.访问数据库
            seckill = seckillDao.queryById(seckillId);
            if (seckill==null){
                return new Exposer(false,seckillId);
            }else {
                //3.放入redis
                redisDao.putSeckill(seckill);
            }
        }

        if(seckill==null){
            return new Exposer(false,seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        //系统的当前时间
        Date nowTime = new Date();
        if(nowTime.getTime()<startTime.getTime()||nowTime.getTime()>endTime.getTime()){
            return new Exposer(false,seckillId,nowTime.getTime(),startTime.getTime(),endTime.getTime());
        }
        //转化特定字符串的过程，不可逆的
        String md5 = getMD5(seckillId);
        return new Exposer(true,md5,seckillId);
    }
```
+ 写存储过程,然后去`Mysql`控制台执行储存过程

```sql
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
```
+ 在`SeckillDao`中编写`killByProcedure()`方法
```java
    //使用存储过程执行秒杀
    void killByProcedure(Map<String,Object> paramMap);
```
+ 然后在`SeckillDao.xml`中写`sql`语句

````xml
    <!--mybatis调用 存储过程 -->
    <select id="killByProcedure" statementType="CALLABLE">
        call execute_seckill(
        #{seckillId,jdbcType=BIGINT,mode=IN},
        #{phone,jdbcType=BIGINT,mode=IN},
        #{killTime,jdbcType=TIMESTAMP,mode=IN},
        #{result,jdbcType=INTEGER,mode=OUT}
        )
    </select>
````

+ 下一步在`SeckillService`接口中中编写 `killProduce()`方法
```java
    //执行秒杀操作by存储过程
    SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5);```
+ 导入`commons-collections`工具类
```xml
 <!--导入apache工具类-->
        <dependency>
            <groupId>commons-collections</groupId>
            <artifactId>commons-collections</artifactId>
            <version>3.2.2</version>
        </dependency>
```
+ 然后`SeckillServiceImpl`实现`killProduce()`方法
```java
    //使用存储过程的秒杀逻辑
    public SeckillExecution executeSeckillProcedure(long seckillId, long userPhone, String md5) {
        if(md5==null||!md5.equals(getMD5(seckillId))){
            return new SeckillExecution(seckillId,SeckillStatEnum.DATA_REWRITE);
        }
        Date killTime = new Date();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("seckillId",seckillId);
        map.put("phone",userPhone);
        map.put("killTime",killTime);
        map.put("result",null);
        try {
            seckillDao.killByProcedure(map);
            //获取result
            int result = MapUtils.getInteger(map,"result",-2);
            if (result==1){
                SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                return new SeckillExecution(seckillId,SeckillStatEnum.SUCCESS,successKilled);
            } else {
                return new SeckillExecution(seckillId,SeckillStatEnum.stateOf(result));
            }
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            return new SeckillExecution(seckillId,SeckillStatEnum.INNER_ERROR);
        }
    }
```
+ 改造执行秒杀`executeSeckill`方法,减少一道虚拟机`GC`程序,优化性能
```java
    @Transactional
    /**
     * 使用注解控制方法的优点：
     * 1.开发团队约定明确标注事务方法的风格；
     * 2.保证事务方法的执行时间尽可能短，不要穿插其他的网络操作:RPC/HTTP请求、或移动到事务方法外部；
     * 3.不是所有的方法都需要事务，如果只有一条修改操作，只读操作不需要事务控制。
     */
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException, RepeatKillException, SeckillCloseException {
        if(md5 == null || !md5.equals(getMD5(seckillId))){
            throw new SeckillException("seckill data rewrite");
        }
        Date nowTime = new Date();

        try {
            //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
            //唯一seckillId,userPhone
            if(insertCount<=0){
                //重复秒杀
                throw new RepeatKillException("seckill repeated");
            } else {
                //减库存,热点商品竞争
                int updateCount = seckillDao.reduceNumber(seckillId,nowTime);
                if(updateCount<=0){
                    //没有更新到记录，秒杀结束 rollback
                    throw new SeckillCloseException("seckill is closed");
                } else {
                    //秒杀成功 commit
                    SuccessKilled successKilled = successKilledDao.queryByIdWithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS,successKilled);
                }
            }

        } catch (SeckillCloseException e1){
            throw e1;
        } catch (RepeatKillException e2){
            throw e2;
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            //所有编译器异常转化为运行期异常
            throw new SeckillException("Seckill inner error"+e.getMessage());
        }
    }
```
+ 编写`SeckillServiceImpl`中的`killProduce()`方法的测试方法
```java
    @Test
    public void executeSeckillProcedure() throws Exception {
        long seckillId = 1002;
        long phone = 15694332288L;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
        if(exposer.isExposed()){
            String md5 = exposer.getMd5();
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId,phone,md5);
            logger.info(execution.getStateInfo());
        }
    }
```

+ 改造`SeckillController`中的`execute`方法调用,把一开始调用普通方法的改成调用储存过程的那个方法
```java
    @RequestMapping(value = "/{seckillId}/{md5}/execution",method = RequestMethod.POST,produces = {"application/json;charset=UTF-8"})
    @ResponseBody
    public SeckillResult<SeckillExecution> execute(@PathVariable("seckillId") Long seckillId,@PathVariable("md5") String md5,@CookieValue(value = "killPhone",required = false) Long phone){

        if (phone==null){
            return new SeckillResult<SeckillExecution>(false,"用户未注册");
        }

        try {
            //通过存储过程调用
            SeckillExecution execution = seckillService.executeSeckillProcedure(seckillId,phone,md5);
            return new SeckillResult<SeckillExecution>(true,execution);
        } catch (RepeatKillException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.REPEAT_KILL);
            return new SeckillResult<SeckillExecution>(false,execution);
        } catch (SeckillCloseException e) {
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.END);
            return new SeckillResult<SeckillExecution>(false,execution);
        } catch (Exception e) {
            logger.error(e.getMessage(),e);
            SeckillExecution execution = new SeckillExecution(seckillId, SeckillStatEnum.INNER_ERROR);
            return new SeckillResult<SeckillExecution>(false,execution);
        }
    }
```