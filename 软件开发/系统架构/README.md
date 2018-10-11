# 系统框架

## 框架

### Spring

#### Spring Framework

- IOC原理
- 单例/原型模式，工厂模式
- AOP、装饰器模式
- 其他常用模式，适配器、观察者、策略，模板方法
- Spring JDBC. 声明式事物/事物嵌套原理

### Spring Boot

Spring Boot + Spring MVC

### 锁

- 悲观锁
- 乐观锁
- 行级锁
- 分片排队
- CAS

## 一致性

### 高级一致性算法

- 一致性hash
- paxos
- zab
- nwr
- raft
- gossip

### 传统一致性

- Consistency
- Isolation
- Atomic
- Duration

### 分布一致性

#### 理论

CAP，BASE

#### 协议

- 两段式提交协议
- 三段式提交协议
- TCC柔性事物

#### 最终一致性

- 查询模式
- 补偿模式
- 异步确保模式
- 消息确保模式
- 校对模式

#### 超时模式

- 快速失败
- 补偿模式

## 分布式架构设计

- 负载均衡
- 水平伸缩
- 集群
- 分片： key-hash, 一致性hash
- 异步
- 消峰
- 分表分库
- 分布式事务

### 消息幂等性设计

幂等性原理，实现方式，重试机制，

### 分布式架构通信

- 分布式架构通信原理
- 通信协议序列化和反序列化：Thrift, ProtoBuf, Hession, WebService

### 服务治理

zookeeper和选举机制
微服务原理，分布式服务治理，服务注册与发现

### 配置中心

配置中心原理/优点/配置变更

### 网关设计

网关模式/原理

### 声明式服务调用

Feign+Hystrix+客户端路由+服务降级

### 异步驱动设计，消息服务: JMS, RocketMQ, Kafka

异步处理、缓解服务器压力，解藕系统

- 消息系统概念，模型，Queue, Topic
- JMS
- RabbitMQ
- Kafka： 持久，复制，Partition， Stream

### 分布式架构JVM监控与JMX

Spring集成JMX,实现MBean，使用JConsole

### 流量控制设计

限流设计/熔断设计/服务降级/Nginx反向代理+limit限速

### 边缘计算模式

什么是边缘计算，使用它实现秒杀业务

### 消息中间件: RocketMQ, Kafka

异步处理、缓解服务器压力，解藕系统
- RocketMQ
- Kafka

### 搜索引擎相关技术知识: ElasticSearch, Logstash, Kibana

### 高并发+高可用+微服务分布式互联网架构实战

- Nginx
- Redis Transaction事务、pipeline以及优化和Sentinel高可用集群
- Nginx分离zuul+Redis实现token网关登录认证
- 系统集群、负载均衡、反向代理、动静分离，网站静态化 。 

#### Spark Storm分布式实时存储与流式计算
