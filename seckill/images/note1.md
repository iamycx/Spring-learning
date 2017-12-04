### (一)Java高并发秒杀APi之业务分析与DAO层代码编写
#### 构建项目的基本骨架
 * 首先我们要搭建出一个符合Maven约定的目录来:
 - 使用命令行手动构建一个maven结构的目录,当然我基本不会这样构建
```
mvn archetype:generate -DgroupId=com.iamycx.seckill -DartifactId=seckill -Dversion=1.0.0 -DarchetypeArtifactId=maven-archetype-webapp
```  
这里要注意的是使用`archetype:generate`进行创建,在Maven老版本中是使用`archetype:create`,现在这种方法已经被弃用了,所以使用命令行创建的话注意了,稍微解释下这段语句的意思,就是构建一个一个`maven-archetype-webapp`骨架的Webapp项目,然后`groupId`为`com.suny.seckill `,`artifactId`为`seckill`,这里是Maven相关知识,可以按照自己的情况进行修改    
  
#### 构建pom文件
  
  项目基本的骨架我们就创建出来了,接下来我们要添加一些基本的JAR包的依赖,也就是在`pom.xml`中添加各种开源组件的三坐标了    
  
  ```xml
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
  xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/maven-v4_0_0.xsd">
  <modelVersion>4.0.0</modelVersion>
  <groupId>com.iamycx</groupId>
  <artifactId>seckill</artifactId>
  <packaging>war</packaging>
  <version>1.0.0</version>
  <name>seckill Maven Webapp</name>
  <url>http://maven.apache.org</url>
  <dependencies>
    <dependency>
      <groupId>junit</groupId>
      <artifactId>junit</artifactId>
      <version>4.11</version>
      <scope>test</scope>
    </dependency>
    <!--补全项目依赖-->
    <!--1：日志 java日志：slf4j，log4j，logback，common-logging
        slf4j是规范、接口
        日志实现：log4j，logback，common-logging
        使用：slf4j+logback
    -->
    <dependency>
      <groupId>org.slf4j</groupId>
      <artifactId>slf4j-api</artifactId>
      <version>1.7.12</version>
    </dependency>
    <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-core</artifactId>
      <version>1.1.1</version>
    </dependency>
    <!--实现slf4j接口并整合 -->
    <dependency>
      <groupId>ch.qos.logback</groupId>
      <artifactId>logback-classic</artifactId>
      <version>1.1.1</version>
    </dependency>
    <!--2.数据库相关依赖 -->
    <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
      <version>5.1.35</version>
      <scope>runtime</scope>
    </dependency>
    <dependency>
      <groupId>c3p0</groupId>
      <artifactId>c3p0</artifactId>
      <version>0.9.1.1</version>
    </dependency>
    <!-- DAO框架 Mybatis依赖 -->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis</artifactId>
      <version>3.3.0</version>
    </dependency>
    <!--mybatis自身实现的spring整合依赖 -->
    <dependency>
      <groupId>org.mybatis</groupId>
      <artifactId>mybatis-spring</artifactId>
      <version>1.2.3</version>
    </dependency>
    <!--3. Servlet Web相关依赖 -->
    <dependency>
      <groupId>taglibs</groupId>
      <artifactId>standard</artifactId>
      <version>1.1.2</version>
    </dependency>
    <dependency>
      <groupId>jstl</groupId>
      <artifactId>jstl</artifactId>
      <version>1.2</version>
    </dependency>
    <dependency>
      <groupId>com.fasterxml.jackson.core</groupId>
      <artifactId>jackson-databind</artifactId>
      <version>2.5.4</version>
    </dependency>
    <dependency>
      <groupId>javax.servlet</groupId>
      <artifactId>javax.servlet-api</artifactId>
      <version>3.1.0</version>
    </dependency>
    <!--4. Spring依赖 -->
    <!--1)spring核心依赖 -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-core</artifactId>
      <version>4.1.7.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-beans</artifactId>
      <version>4.1.7.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-context</artifactId>
      <version>4.1.7.RELEASE</version>
    </dependency>
    <!--2)spring dao层依赖 -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-jdbc</artifactId>
      <version>4.1.7.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-tx</artifactId>
      <version>4.1.7.RELEASE</version>
    </dependency>
    <!--3)springweb相关依赖 -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-web</artifactId>
      <version>4.1.7.RELEASE</version>
    </dependency>
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-webmvc</artifactId>
      <version>4.1.7.RELEASE</version>
    </dependency>
    <!--4)spring test相关依赖 -->
    <dependency>
      <groupId>org.springframework</groupId>
      <artifactId>spring-test</artifactId>
      <version>4.1.7.RELEASE</version>
    </dependency>

    <!--添加redis依赖 -->
    <dependency>
      <groupId>redis.clients</groupId>
      <artifactId>jedis</artifactId>
      <version>2.7.3</version>
    </dependency>

    <!--prostuff序列化依赖 -->
    <dependency>
      <groupId>com.dyuproject.protostuff</groupId>
      <artifactId>protostuff-core</artifactId>
      <version>1.0.8</version>
    </dependency>
    <dependency>
      <groupId>com.dyuproject.protostuff</groupId>
      <artifactId>protostuff-runtime</artifactId>
      <version>1.0.8</version>
    </dependency>
    <dependency>
      <groupId>commons-collections</groupId>
      <artifactId>commons-collections</artifactId>
      <version>3.2</version>
    </dependency>
    <!--redis客户端 Jedis-->
    <dependency>
      <groupId>redis.clients</groupId>
      <artifactId>jedis</artifactId>
      <version>2.7.3</version>
    </dependency>
    <!--protostuff序列化依赖-->
    <dependency>
      <groupId>com.dyuproject.protostuff</groupId>
      <artifactId>protostuff-core</artifactId>
      <version>1.0.8</version>
    </dependency>
    <dependency>
      <groupId>com.dyuproject.protostuff</groupId>
      <artifactId>protostuff-runtime</artifactId>
      <version>1.0.8</version>
    </dependency>
    <dependency>
      <groupId>commons-collections</groupId>
      <artifactId>commons-collections</artifactId>
      <version>3.2</version>
    </dependency>
  </dependencies>
  <build>
    <finalName>seckill</finalName>
  </build>
</project>

  ```
#### 建立数据库
在根目录下有一个[sql](sql)文件夹里面有一个[sql数据库脚本](sql/seckill.sql),如果你不想自己手写的话就直接导入到你的数据库里面去吧,不过还是建议自己手写一遍加深印象

```sql

--数据库初始化脚本

--创建数据库
CREATE DATABASE seckill;
--使用数据库
use seckill;
--创建秒杀库存表
CREATE TABLE seckill(
`seckill_id` bigint NOT NULL AUTO_INCREMENT COMMENT '商品库存id',
`name` varchar(120) NOT NULL COMMENT '商品名称',
`number` int NOT NULL COMMENT '库存数量',
`start_time` TIMESTAMP NOT NULL COMMENT '秒杀开始时间',
`end_time` TIMESTAMP NOT NULL COMMENT '秒杀结束时间',
`create_time` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
PRIMARY KEY (seckill_id),
KEY idx_start_time(start_time),
KEY idx_end_time(end_time),
KEY idx_create_time(create_time)
)ENGINE=InnoDB AUTO_INCREMENT=1000 DEFAULT CHARSET=utf8 COMMENT='秒杀库存表';
--初始化数据
INSERT INTO seckill(name,number,start_time,end_time)
VALUES
  ('1000元秒杀iPhoneX',100,'2017-12-10 00:00:00','2017-12-11 00:00:00'),
  ('500元秒杀iPhone8',200,'2017-12-10 00:00:00','2017-12-11 00:00:00'),
  ('300元秒杀iPad',300,'2017-12-01 00:00:00','2017-12-11 00:00:00'),
  ('200元秒杀iPhone7',400,'2017-12-01 00:00:00','2017-12-02 00:00:00');
--秒杀成功明细表
--用户登录认证相关信息
CREATE TABLE success_killed(
`seckill_id` bigint NOT NULL COMMENT '秒杀商品id',
`user_phone` bigint NOT NULL COMMENT '用户手机号',
`state` tinyint NOT NULL DEFAULT -1 COMMENT '状态标志：-1：无效 0：成功 1：已付款 2：已发货',
`create_time` TIMESTAMP NOT NULL COMMENT '创建时间',
PRIMARY KEY(seckill_id,user_phone),/*联合主键*/
KEY idx_create_time(create_time)
)ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='秒杀成功明细表';
--连接数据库 可以用MySQL Workbench

```
 
---
#### 建立实体类
 - 首先建立`SuccessKilled`  秒杀状态表
```java
package com.iamycx.entity;

import java.util.Date;

/**
 * @Author: iamycx
 * @Date: Created in 11:55 2017/11/29
 */
public class SuccessKilled {
    private long seckillId;
    private long userPhone;
    private short state;
    private Date createTime;
    //变通  多对一
    private Seckill seckill;

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public short getState() {
        return state;
    }

    public void setState(short state) {
        this.state = state;
    }

    public Date getCreatTime() {
        return createTime;
    }

    public void setCreatTime(Date createTime) {
        this.createTime = createTime;
    }

    public Seckill getSeckill() {
        return seckill;
    }

    public void setSeckill(Seckill seckill) {
        this.seckill = seckill;
    }

    @Override
    public String toString() {
        return "SuccessKilled{" +
                "seckillId=" + seckillId +
                ", userPhone=" + userPhone +
                ", state=" + state +
                ", createTime=" + createTime +
                '}';
    }
}

```
 - 再建立`Seckill` 秒杀商品信息
```java
package com.iamycx.entity;

import java.util.Date;

/**
 * @Author: iamycx
 * @Date: Created in 11:53 2017/11/29
 */
public class Seckill {
    private long seckillId;
    private String name;
    private int number;
    private Date startTime;
    private Date endTime;
    private Date createTime;

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public String toString() {
        return "Seckill{" +
                "seckillId=" + seckillId +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                '}';
    }
}

```
 #### 对实体类创建对应的`mapper`接口,也就是`dao`接口类
 - 首先创建`SeckillDao`,在我这里位于`com.iamycx.dao`包下
 ```java
package com.iamycx.dao;

import com.iamycx.entity.Seckill;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @Author: iamycx
 * @Date: Created in 12:02 2017/11/29
 */
public interface SeckillDao {

    //减库存 return如果影响的行数>1，表示更新的记录行数
    int reduceNumber(@Param("seckillId") long seckillId, @Param("killTime") Date killTime);
    //根据id查询秒杀对象
    Seckill queryById(long seckillId);
    //根据偏移量查询秒杀商品列表
    List<Seckill> queryAll(@Param("offset") int offset,@Param("limit") int limit);
    //使用存储过程执行秒杀
    void killByProcedure(Map<String,Object> paramMap);
}

```
 - 再创建`SuccessKilledDao`
 ```java
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

```
#### 接下来书写`xml`配置文件
##### 建立对应的`mapper.xml`  

首先在`src/main/resources`建立`mapper`这个包,这样符合Maven的约定,就是资源放置在`Resource`包下,`Java`包下则是放置`java`类文件,编译后最后还是会在同一个目录下.  
- 首先建立`SeckillDao.xml`
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.iamycx.dao.SeckillDao">
    <!--目的：为DAO接口方法提供sql语句配置-->

    <update id="reduceNumber">
        <!--具体的sql语句-->
        UPDATE seckill SET NUMBER = NUMBER - 1
        WHERE seckill_id=#{seckillId}
        AND start_time <![CDATA[ <= ]]> #{killTime}
        AND end_time >= #{killTime}
        AND NUMBER >0;
    </update>

    <select id="queryById" resultType="Seckill" parameterType="long">
        SELECT seckill_id,NAME,NUMBER,start_time,end_time,create_time
        FROM seckill
        WHERE seckill_id = #{seckillId}
    </select>

    <select id="queryAll" resultType="Seckill">
        SELECT seckill_id,NAME,NUMBER,start_time,end_time,create_time
        FROM seckill
        ORDER BY create_time DESC
        limit #{offset},#{limit}
    </select>
    <!--mybatis调用 存储过程 -->
    <select id="killByProcedure" statementType="CALLABLE">
        call execute_seckill(
        #{seckillId,jdbcType=BIGINT,mode=IN},
        #{phone,jdbcType=BIGINT,mode=IN},
        #{killTime,jdbcType=TIMESTAMP,mode=IN},
        #{result,jdbcType=INTEGER,mode=OUT}
        )
    </select>
</mapper>
```
- 建立`SuccessKilledDao.xml`
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-mapper.dtd">

<mapper namespace="com.iamycx.dao.SuccessKilledDao">
    <update id="insertSuccessKilled">
        <!--主键冲突的时候，忽视报错-->
        INSERT ignore INTO success_killed(seckill_id,user_phone,state)
        VALUES (#{secKillId},#{userPhone},0)
    </update>

    <select id="queryByIdWithSeckill" resultType="SuccessKilled">
        <!--根据id查询SucceessKilled并携带秒杀产品对象Seckill实体-->
        <!--如果告知MyBatis把结果映射到SuccessKilled同时映射seckill属性-->
        <!--可以自由控制SQL-->
        SELECT
          sk.seckill_id,
          sk.user_phone,
          sk.create_time,
          sk.state,
          s.seckill_id "seckill.seckill_id",
          s.name "seckill.name",
          s.number "seckill.number",
          s.start_time "seckill.start_time",
          s.end_time "seckill.end_time",
          s.create_time "seckill.create_time"
        FROM success_killed sk
        INNER JOIN seckill s ON sk.seckill_id=s.seckill_id
        WHERE sk.seckill_id = #{seckillId} AND sk.user_phone=#{userPhone}
    </select>
</mapper>
```
- 建立`Mybatis`的配置文件`mybatis-config.xml`
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "http://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <!--配置全局属性-->
    <settings>
        <!--使用JDBC的getGeneratedKeys获取数据库自增主键值-->
        <setting name="useGeneratedKeys" value="true"/>
        <!--使用列别名替换列名 默认：true     select name as title from table-->
        <setting name="useColumnLabel" value="true"/>
        <!--开启驼峰命名转换：Table(create_time)->Entity(createTime)-->
        <setting name="mapUnderscoreToCamelCase" value="true"/>
    </settings>
</configuration>
```
- 然后建立连接数据库的配置文件`jdbc.properties`,这里的属性要根据自己的需要去进行修改,切勿直接复制使用  
```properties
driver=com.mysql.jdbc.Driver
url=jdbc:mysql://localhost:3306/seckill?useUnicode=true&characterEncoding=utf8
user=root
password=root
```
- 建立`Spring`的`dao`的配置文件,在`resources/spring`包下创建`spring-dao.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns:context="http://www.springframework.org/schema/context"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
 http://www.springframework.org/schema/beans/spring-beans.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd">
    <!--配置整合MyBatis过程-->

    <!--1：配置数据库相关参数 properties的属性：${url}-->
    <context:property-placeholder location="classpath:jdbc.properties"/>

    <!--2：数据库连接池-->
    <bean id="dataSource" class="com.mchange.v2.c3p0.ComboPooledDataSource">
        <!--配置连接池属性-->
        <property name="driverClass" value="${driver}"/>
        <property name="jdbcUrl" value="${url}"/>
        <property name="user" value="${user}"/>
        <property name="password" value="${password}"/>

        <!--c3p0连接池的私有属性配置-->
        <property name="maxPoolSize" value="30"/>
        <property name="minPoolSize" value="10"/>
        <!--关闭连接后不自动commit-->
        <property name="autoCommitOnClose" value="false"/>
        <!--获取连接超时时间-->
        <property name="checkoutTimeout" value="1000"/>
        <!--当获取连接失败重试次数-->
        <property name="acquireRetryAttempts" value="2"/>
    </bean>

    <!--3：配置SqlSessionFactory对象-->
    <bean id="sqlSessionFactory" class="org.mybatis.spring.SqlSessionFactoryBean">
        <!--注入数据库连接池-->
        <property name="dataSource" ref="dataSource"/>
        <!--配置MyBatis全局配置文件：mybatis-config.xml-->
        <property name="configLocation" value="classpath:mybatis-config.xml"/>
        <!--扫描entity包，使用别名-->
        <property name="typeAliasesPackage" value="com.iamycx.entity"/>
        <!--扫描sql配置文件：mapper需要的xml文件-->
        <property name="mapperLocations" value="classpath:mapper/*.xml"/>
    </bean>

    <!--4：配置扫描Dao接口包，动态实现Dao接口，注入到Spring容器中-->
    <bean class="org.mybatis.spring.mapper.MapperScannerConfigurer">
        <!--注入sqlSessionFactory-->
        <property name="sqlSessionFactoryBeanName" value="sqlSessionFactory"/>
        <!--给出需要扫描Dao接口包-->
        <property name="basePackage" value="com.iamycx.dao"/>
    </bean>

    <!--注入RedisDao-->
    <bean id="redisDao" class="com.iamycx.dao.cache.RedisDao">
        <constructor-arg index="0" value="localhost"/>
        <constructor-arg index="1" value="6379"/>
    </bean>
</beans>
```

- 基础的部分我们搭建完成了,然后要开始测试了
 在`IDEA`里面有一个快速建立测试的快捷键`Ctrl+Shift+T`,在某个要测试的类里面按下这个快捷键就会出现`Create new Test`,然后选择你要测试的方法跟测试的工具就可以了,这里我们使用Junit作为测试
  + 建立`SeckillDaoTest`文件,代码如下
 ```java
package com.iamycx.dao;

import com.iamycx.entity.Seckill;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

import java.util.Date;
import java.util.List;

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
```
测试中可能会出现`Mybatis`参数绑定失败的错误,在`mapper`接口中的方法里面添加`@Param`的注解,显示的告诉mybatis参数的名称是什么,例如
```java
 List<Seckill> queryAll(@Param("offset") int offset, @Param("limit") int limit);
```  