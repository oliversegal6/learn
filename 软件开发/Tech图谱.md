## Java Language

#### 集合对象
- java各种集合对象的实现原理
-  熟练使用各种数据结构和算法，数组、哈希、链表、排序树, 要么是时间换空间要么是空间换时间

#### NIO + Netty高并发编程
- io
- nio
- 网络编程的NIO、AIO编程模型讲解
- 通信框架Netty实战部署以及Netty服务讲解
- Netty的TCP粘包拆包、序列化以及自定义协议
- Netty实战数据通信以及集群心跳检测服务
- 网络编程的伪异步IO模式、模型概念、原理

#### 序列化
系统之间通过网络传输，或者存储到文件时，肯定只有一种格式，就是字符串。要将复杂的数据结构与字符串之间进行转换，就需要用到序列化。
- JSON和XML， JSON和XML只能用于传递数据
- protobuf、thrift， protobuf与thrift还可以用来做RPC协议

#### 线程、锁基础知识
- java多线程同步异步
- 池技术，对象池，连接池，线程池
- 深入剖析volatile、synchronized、Lock、AtomicX关键字（一）
- 深入剖析volatile、synchronized、Lock、AtomicX关键字（二）
- 深入剖析java concurrent 包 阻塞队列、ConcurrentMap
- 深入剖析java concurrent包 闭锁、栅栏、交换机、信号量
- 深入剖析java concurrent 包 执行器服务、线程池、Jvm调优

#### JVM
- jvm虚拟机原理、调优
- "直接内存"的特点
- java反射技术，java字节码技术;

#### 通信协议
- 熟悉http协议，尤其是http头，session和cookie的生命周期以及它们之间的关联。
- 熟悉tcp协议，创建连接三次握手和断开连接四次握手的整个过程
- RPC方式
- RESTFul， 对性能等参数有个量化的了解
- 安全设计 比如MySQL和PostgreSQL的协议都有安全的设计。
- 加密/签名技术：常见如truecrypt、openssl、gnu pg、sha1、md5、scrypt等，了解各种加密/签名技术的安全性，字长等，
- OAuth：与其他网站联合认证的方式，有多种，分别了解。
- 用户认证：一开始就应该花大精力设计好用户认证系统，包括不要明文存储密码，包括严格限制Cookie和Session的使用，包括用户认证信息的缓存等。如果需要设计一个长期运行的大系统，强烈建议使用签名来保证Cookie的不可伪造，同时常见信息直接存储Cookie，这样可以避免每次Request都访问数据库。

## 系统架构
#### 分布式系统+Zookeeper 

#### 消息中间件: RocketMQ, Kafka
异步处理、缓解服务器压力，解藕系统
- RocketMQ
- Kafka

#### 搜索引擎相关技术知识: ElasticSearch, Logstash, Kibana

#### 高并发+高可用+微服务分布式互联网架构实战
- Nginx
- Redis Transaction事务、pipeline以及优化和Sentinel高可用集群
- Nginx分离zuul+Redis实现token网关登录认证
- 系统集群、负载均衡、反向代理、动静分离，网站静态化 。 

#### Hbase Spark Kafka Storm分布式实时存储与流式计算


## 数据存储

#### 关系式数据库
- Create、Query、Delete、Update操作外。还需要能自行建立索引
- 数据库的平行扩展
- 如何进行性能调试。以及了解常见查询操作的性能级别，常见的查询性能瓶颈点
- mysql，对它基本的参数优化，慢查询日志分析，主从复制的配置

#### Hadoop Hive分布式大数据存储
nfs,fastdfs,tfs,Hadoop了解他们的优缺点，适用场景 。 

#### MongoDB

#### 分布式缓存技术memcached,redis
把硬盘上的内容放到内存里来提速，算法一致性hash


## 分布式数据挖掘、机器学习、人工智能

#### Python

#### TensorFlow 


## Server

如果一开始就将静态文件与主站内容混杂在一起，未来就是个灾难，具体参考CDN的应用方式
#### Apache

#### nginx

#### Tomcat, Jetty

业务要求，性能要求，具备可扩展性（scalability），可拓展性（extendability），前后兼容性等

1. 博客/论坛：博客和论坛有很多，并且各个网站也都很常用，了解下其原理，最好自己写个练习下。
2. 微博：Twitter/weibo等，涉及到大量的联表查询，需要用多种办法来优化查询性能。
3. 云计算：云计算的几个常见平台的服务提供方式，如Google和Amazon的，如果精力够用最好了解下OpenStack等搭建私有云的方式，总的来说，这是未来的大方向。
4. 视频播放：在网页上播放视频的技术，包括基于Flash和HTML5的，各种浏览器对视频的兼容性等，了解通过ffmpeg将一个视频转换为标准mp4(HTML5可以播放的)的方式和参数
5. CDN：了解应用CDN的方式，包括拆分静态文件的域名，静态文件版本化，Cookie拆分等相关技术

