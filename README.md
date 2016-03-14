# CAS Server Lab

## 简介
欢迎访问CAS Server Lab，旨在解决集成SSO中常用的场景分析及技术解决方案，如有问题欢迎大家随时提交issue。

## 环境
	tomcat8 + jdk 8
	base on Cas 4.0.0
	
### v0.0.0.1
- [x] [CAS服务端演示](./cas-server-demo)  
    1.登录地址：https://localhost:8443/cas/login （admin/admin）

### v0.0.0.2
- [x] [支持数据库连接，从DB中验证密码](./cas-server-demo)  
1.DB连接： HikariCP  
2.加密方式：security的BCryptPasswordEncoder

### v0.0.0.3
- [x] [集成RESTful API](./cas-server-demo)  
  ##### 参考文献
    https://wiki.jasig.org/display/CASUM/RESTful+API

## 意见反馈
欢迎大家随时提交反馈意见及功能需求，或fork之后new pull request，本人会及时对问题进行回复，[请戳我提交issue。](https://github.com/waterWang/cas-server-lab/issues/new)

## 异常情况测试汇总
- 客户端登录后，CAS服务宕机不影响客户端操作（客户端在宕机过程中不与CAS服务端交互情况），且宕机期间无法注销用户信息

## 参考文献
1.  CAS 官网：<http://www.jasig.org/cas>
2.  CAS Document API：<http://jasig.github.io/cas/4.0.x/index.html>
