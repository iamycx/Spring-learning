### (二)Java高并发秒杀API之Service层 
 首先在编写`Service`层代码前,我们应该首先要知道这一层到底时干什么的,这里摘取来自`ITEYE`一位博主的原话
 > Service层主要负责业务模块的逻辑应用设计。同样是首先设计接口,再设计其实现的类,接着再Spring的配置文件中配置其实现的关联。这样我们就可以在应用中调用Service接口来进行业务处理。Service层的业务实现,具体要调用到已定义的DAO层的接口,封装Service层的业务逻辑有利于通用的业务逻辑的独立性和重复利用性,程序显得非常简洁。  
 
 在项目中要降低耦合的话,分层是一种很好的概念,就是各层各司其职,尽量不做不相干的事,所以`Service`层的话顾名思义就是**业务逻辑**,处理程序中的一些业务逻辑,以及调用`DAO`层的代码,这里我们的`DAo`层就是连接数据库的那一层,调用关系可以这样表达:  
   View(页面)>Controller(控制层)>Service(业务逻辑)>Dao(数据访问)>Database(数据库)  
   + 首先还是接口的设计,设计Service秒杀商品的接口  ``SeckillService``
  首先在`som.iamycx`包下建立`service`这个包,这个包里面存放`Service`相关的接口,然后建立`SeckillService`接口文件,代码如下:
  ```java
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

```
建立后接口之后我们要写实现类了,在写实现类的时候我们肯定会碰到一个这样的问题,你要向前端返回`json`数据的话,你是返回什么样的数据好?直接返回一个数字状态码或者时文字?这样设计肯定是不好的,所以我们应该向前段返回一个实体信息`json`,里面包含了一系列的信息,无论是哪种状态都应该可以应对,既然是与数据库字段无关的类,那就不是`PO`了,所以我们建立一个`DTO`数据传输类,关于常见的几种对象我的解释如下:
 + PO:   也就是我们在为每一张数据库表写一个实体的类
 + VO,   对某个页面或者展现层所需要的数据,封装成一个实体类
 + BO,   就是业务对象,我也不是很了解
 + DTO,  跟VO的概念有点混淆,也是相当于页面需要的数据封装成一个实体类
 + POJO, 简单的无规则java对象
 
 在`com.iamycx`下建立`dto`包,然后建立`Exposer`类,这个类是秒杀时数据库那边处理的结果的对象
  ```java
package com.iamycx.dto;

/**
 * Created by iamycx on 21:17 2017/11/29
 *
 * 暴露秒杀地址DTO
 */
public class Exposer {
    //是否开启秒杀
    private boolean exposed;
    //一种加密措施
    private String md5;
    //id
    private long seckillId;
    //系统当前时间(毫秒)
    private long now;
    //开始时间
    private long start;
    //结束时间
    private long end;

    public Exposer(boolean exposed, String md5, long seckillId) {
        this.exposed = exposed;
        this.md5 = md5;
        this.seckillId = seckillId;
    }

    public Exposer(boolean exposed, long seckillId, long now, long start, long end) {
        this.exposed = exposed;
        this.seckillId = seckillId;
        this.now = now;
        this.start = start;
        this.end = end;
    }

    public Exposer(boolean exposed, long seckillId) {
        this.exposed = exposed;
        this.seckillId = seckillId;
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getNow() {
        return now;
    }

    public void setNow(long now) {
        this.now = now;
    }

    public long getStart() {
        return start;
    }

    public void setStart(long start) {
        this.start = start;
    }

    public long getEnd() {
        return end;
    }

    public void setEnd(long end) {
        this.end = end;
    }

    @Override
    public String toString() {
        return "Exposer{" +
                "exposed=" + exposed +
                ", md5='" + md5 + '\'' +
                ", seckillId=" + seckillId +
                ", now=" + now +
                ", start=" + start +
                ", end=" + end +
                '}';
    }
}
```
然后我们给页面返回的数据应该是更加友好的封装数据,所以我们再在`com.iamycx.dto`包下再建立`SeckillExecution`用来封装给页面的结果:

```java
package com.iamycx.dto;

import com.iamycx.entity.SuccessKilled;
import com.iamycx.enums.SeckillStatEnum;

/**
 * Created by iamycx on 21:27 2017/11/29
 *
 * 封装秒杀执行后的结果
 */
public class SeckillExecution {
    private long seckillId;
    //秒杀执行结果的状态
    private int state;
    //状态表示
    private String stateInfo;
    //秒杀成功对象
    private SuccessKilled successKilled;

    public SeckillExecution(long seckillId, SeckillStatEnum statEnum, SuccessKilled successKilled) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
        this.successKilled = successKilled;
    }

    public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SuccessKilled getSuccessKilled() {
        return successKilled;
    }

    public void setSuccessKilled(SuccessKilled successKilled) {
        this.successKilled = successKilled;
    }

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "seckillId=" + seckillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", successKilled=" + successKilled +
                '}';
    }
}

```
##### 定义秒杀中可能会出现的异常
  + 定义一个基础的异常,所有的子异常继承这个异常`SeckillException`
  ````java
package com.iamycx.exception;

/**
 * Created by iamycx on 21:36 2017/11/29
 */
public class SeckillException extends RuntimeException {

    public SeckillException(String message) {
        super(message);
    }

    public SeckillException(String message, Throwable cause) {
        super(message, cause);
    }
}

````
    + 首选可能会出现秒杀关闭后被秒杀情况,所以建立秒杀关闭异常`SeckillCloseException`,需要继承我们一开始写的基础异常 
```java
package com.iamycx.exception;

/**
 * Created by iamycx on 21:34 2017/11/29
 *
 * 秒杀关闭异常
 */
public class SeckillCloseException extends SeckillException{

    public SeckillCloseException(String message) {
        super(message);
    }

    public SeckillCloseException(String message, Throwable cause) {
        super(message, cause);
    }
}

```
   + 然后还有可能发生重复秒杀异常`RepeatKillException`
   ```java
package com.iamycx.exception;

/**
 * Created by iamycx on 21:31 2017/11/29
 *
 * 重复秒杀异常（运行期异常）
 */
public class RepeatKillException extends SeckillException{

    public RepeatKillException(String message) {
        super(message);
    }

    public RepeatKillException(String message, Throwable cause) {
        super(message, cause);
    }
}

```
#### 实现`Service`接口
```java
package com.iamycx.service.impl;

import com.iamycx.dao.SeckillDao;
import com.iamycx.dao.SuccessKilledDao;
import com.iamycx.dao.cache.RedisDao;
import com.iamycx.dto.Exposer;
import com.iamycx.dto.SeckillExecution;
import com.iamycx.entity.Seckill;
import com.iamycx.entity.SuccessKilled;
import com.iamycx.enums.SeckillStatEnum;
import com.iamycx.exception.RepeatKillException;
import com.iamycx.exception.SeckillCloseException;
import com.iamycx.exception.SeckillException;
import com.iamycx.service.SeckillService;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by iamycx on 21:40 2017/11/29
 */
@Service
public class SeckillServiceImpl implements SeckillService{

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    //注入service依赖
    @Autowired
    private SeckillDao seckillDao;
    @Autowired
    private SuccessKilledDao successKilledDao;
    @Autowired
    private RedisDao redisDao;
    //md5盐值字符串，用于混淆md5
    private final String slat = "fidie@#$((!!feoo3fi2335-i***43r9*/-++";

    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

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

    private String getMD5(long seckillId){
        String base = seckillId+"/"+slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

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
}

```
在这里我们捕获了运行时异常,这样做的原因就是`Spring`的事物默认就是发生了`RuntimeException`才会回滚,可以检测出来的异常是不会导致事物的回滚的,这样的目的就是你明知道这里会发生异常,所以你一定要进行处理.如果只是为了让编译通过的话,那捕获异常也没多意思,所以这里要注意事物的回滚.  
然后我们还发现这里存在硬编码的现象,就是返回各种字符常量,例如`秒杀成功`,`秒杀失败`等等,这些字符串时可以被重复使用的,而且这样维护起来也不方便,要到处去类里面寻找这样的字符串,所有我们使用枚举类来管理这样状态,在`con.iamycx`包下建立`enums`包,专门放置枚举类,然后再建立`SeckillStatEnum`枚举类:
```java
package com.iamycx.enums;

/**
 * Created by iamycx on 22:22 2017/11/29
 *
 * 使用枚举表示常量数据字段
 */
public enum SeckillStatEnum {
    SUCCESS(1,"秒杀成功"),
    END(0,"秒杀结束"),
    REPEAT_KILL(-1,"重复秒杀"),
    INNER_ERROR(-2,"系统异常"),
    DATA_REWRITE(-3,"数据篡改");

    private int state;
    private String stateInfo;

    SeckillStatEnum(int state, String stateInfo) {
        this.state = state;
        this.stateInfo = stateInfo;
    }

    public int getState() {
        return state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public static SeckillStatEnum stateOf(int index){
        for(SeckillStatEnum state:values()){
            if (state.getState() == index){
                return state;
            }
        }
        return null;
    }
}

```
既然把这些改成了枚举,那么在`SeckillServiceImpl`类中的`executeSeckill`方法中成功秒杀的返回值就应该修改为  
```java
return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
```

####  下一步肯定要注入Service了
首先在`resources/spring`下建立`spring-service.xml`文件,用来配置`Service层的相关代码`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xmlns:tx="http://www.springframework.org/schema/tx"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
       http://www.springframework.org/schema/beans/spring-beans.xsd
       http://www.springframework.org/schema/context
       http://www.springframework.org/schema/context/spring-context.xsd
       http://www.springframework.org/schema/tx
       http://www.springframework.org/schema/tx/spring-tx.xsd">
    <!--扫描service包下所有使用注解的类型-->
    <context:component-scan base-package="com.iamycx.service"/>

    <!--配置事务管理器-->
    <bean id="transactionManager" class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <!--注入数据库的连接池-->
        <property name="dataSource" ref="dataSource"/>
    </bean>

    <!--配置基于注解的声明式事物  默认使用注解来管理事物行为-->
    <tx:annotation-driven transaction-manager="transactionManager"/>
</beans>
```
在这里开启了基于**注解**的事物,常见的事物操作有以下几种方法
 + 在Spring早期版本中是使用ProxyFactoryBean+XMl方式来配置事物.
 + 在Spring配置文件使用tx:advice+aop命名空间,好处就是一次配置永久生效,你无须去关心中间出的问题,不过出错了你很难找出来在哪里出了问题
 + 注解@Transactional的方式,注解可以在`方法定义`,`接口定义`,`类定义`,`public方法上`,但是不能注解在`private`,`final`,`static`等方法上,因为Spring的事物管理默认是使用Cglib动态代理的:
   - private方法因为访问权限限制,无法被子类覆盖
   - final方法无法被子类覆盖
   - static时类级别的方法,无法被子类覆盖
   - protected方法可以被子类覆盖,因此可以被动态字节码增强
##### 不能被Spring AOP事物增强的方法  
  | 序号 | 动态代理策略 |不能被事物增强的方法 |
  |:-----:| :-----: |:-----:|
  |   1   |基于接口的动态代理  |出了public以外的所有方法,并且 public static 的方法也不能被增强 |
  |   2   |基于Cglib的动态代理  | private,static,final的方法 |
  
然后你要在`Service`类上添加注解`@Service`,不用在接口上添加注解：
```java
@Service
public class SeckillServiceImpl implements SeckillService
```
既然已经开启了基于注解的事物,那我们就去需要被事物的方法上加个注解`@Transactional`吧:
```java
    @Transactional
    @Override
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5) throws SeckillException 
```
#### Service层的测试
写测试类,我这里的测试类名为`SeckillServiceTest`:
```java
package com.iamycx.service;

import com.iamycx.dto.Exposer;
import com.iamycx.dto.SeckillExecution;
import com.iamycx.entity.Seckill;
import com.iamycx.exception.RepeatKillException;
import com.iamycx.exception.SeckillCloseException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;


/**
 * Created by iamycx on 9:53 2017/12/1
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"classpath:spring/spring-dao.xml","classpath:spring/spring-service.xml"})
public class SeckillServiceTest {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillService seckillService;

    @Test
    public void getSeckillList() throws Exception {
        List<Seckill> list = seckillService.getSeckillList();
        logger.info("list={}",list);
    }

    @Test
    public void getById() throws Exception {
        long seckillId = 1000;
        Seckill seckill = seckillService.getById(seckillId);
        logger.info("seckill={}",seckill);
    }

//    @Test
//    public void exportSeckillUrl() throws Exception {
//        long seckillId = 1000;
//        Exposer exposer = seckillService.exportSeckillUrl(seckillId);
//        logger.info("exposer={}",exposer);
//
//        /**
//         * Exposer{exposed=false, md5='null', seckillId=1000, now=1512094223846, start=1512835200000, end=1512921600000}
//         */
//    }
//
//    @Test
//    public void executeSeckill() throws Exception {
//        long seckillId = 1000;
//        long userPhone = 177232949822L;
//        String md5 = "fkjfwejfjfeijefi";
//        try {
//            SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
//            logger.info("result={}",execution);
//        } catch (RepeatKillException e) {
//            logger.error(e.getMessage());
//        } catch (SeckillCloseException e) {
//            logger.error(e.getMessage());
//        }
//    }

    //将上面两个测试代码集成完整逻辑，注意可以重复执行
    @Test
    public void testSeckillLogic() throws Exception {
        long seckillId = 1002;
        Exposer exposer = seckillService.exportSeckillUrl(seckillId);

        if (exposer.isExposed()){
            logger.info("exposer={}",exposer);
            long userPhone = 177232949822L;
            String md5 = exposer.getMd5();
            try {
                SeckillExecution execution = seckillService.executeSeckill(seckillId, userPhone, md5);
                logger.info("result={}",execution);
            } catch (RepeatKillException e) {
                logger.error(e.getMessage());
            } catch (SeckillCloseException e) {
                logger.error(e.getMessage());
            }
        } else {
            //秒杀未开启
            logger.warn("exposer={}",exposer);
        }
    }

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
}
```
测试的话如果每个方法测试都通过就说明通过,如果报错了话就仔细看下哪一步错了检查下 