# Spring learning note(spring 学习笔记)

本项目对 spring 的实现机制通过例子进行展示和剖析,并附上部分源码的分析说明。

**如果觉得不错，请先在这个仓库上点个 star 吧**，这也是对我的肯定和鼓励，谢谢了。

不定时进行调整和补充，需要关注更新的请 Watch、Star、Fork


-----

## 仓库目录

- [spring-helloworld](/spring-helloworld):一个简单的用户登录小例子,涉及 mvc 和 数据库读写
- [spring-ioc](/spring-ioc):一些 ioc 的小例子,涉及生命周期,注入,bean 的配置等等
- [spring-aop](/spring-aop):一些 aop 的小例子,涉及各种切点知识,动态代理(JDK,CGLib),`@AspectJ`, LTW等等

-----	


## 安装和使用

环境准备:

- jdk 1.8+
- MyEclipse 2016 Stable 1.0
- mysql 5.1+
- maven 3.3+
- tomcat 8+


数据库导入:

- [spring-helloworld](/spring-helloworld) 模块中默认的数据库名为 `sampledb`
- 导入 [spring-helloworld/src/resources](/spring-helloworld/src/resources/) 中的 [helloword.sql](/spring-helloworld/src/resources/helloworld.sql) 添加数据表和测试数据


在 IDE 中添加 tomcat 容器:

- [怎样给Myeclipse配置tomcat服务器](https://jingyan.baidu.com/article/4853e1e53465271909f72690.html)
- [spring-helloworld](/spring-helloworld)模块需要添加一个 tomcat 容器


## TODO

计划逐步完善这个仓库，扩充并实现下面的功能。

* [x] 入门小例子，spring-helloworld
* [x] IoC
* [x] AOP
* [ ] spring MVC
* [ ] 整理部分源码分析,形成博客

	

-----

# 联系作者

- [iamycx's Personal Website](https://iamycx.github.io/)
- [janshu](http://www.jianshu.com/u/7df0994635b0)


-----

# Lisence

Lisenced under [Apache 2.0 lisence](http://opensource.org/licenses/Apache-2.0)
