# Java

## 集合对象

- java各种集合对象的实现原理
- 熟练使用各种数据结构和算法，数组、哈希、链表、排序树, 要么是时间换空间要么是空间换时间

## 算法

### 查找

- 二分查找

### 排序

- 选择
- 冒泡
- 插入
- 快速
- 归并
- 堆
- 桶排序
- 基数

### 高级算法

- 贪婪
- 回溯
- 减枝
- 动态规划

## NIO + Netty高并发编程

- io
- nio
- 网络编程的BIO、伪异步IO模式、NIO、AIO编程模型讲解
- 通信框架Netty实战部署以及Netty服务讲解
- Netty的TCP粘包拆包、序列化以及自定义协议
- Netty实战数据通信以及集群心跳检测服务

## 序列化

系统之间通过网络传输，或者存储到文件时，肯定只有一种格式，就是字符串。要将复杂的数据结构与字符串之间进行转换，就需要用到序列化。

- JSON和XML，JSON和XML只能用于传递数据
- protobuf、thrift， protobuf与thrift还可以用来做RPC协议

## 线程、锁基础知识

- java多线程同步异步
- 池技术，对象池，连接池，线程池
- 深入剖析volatile、synchronized、Lock、AtomicX关键字（一）
- 深入剖析volatile、synchronized、Lock、AtomicX关键字（二）
- 深入剖析java concurrent 包 阻塞队列、ConcurrentMap
- 深入剖析java concurrent包 闭锁、栅栏、交换机、信号量
- 深入剖析java concurrent 包 执行器服务、线程池、Jvm调优

### 并发基础

#### AQS

#### CAS

Compare and Swap

#### Synchronized

#### volatile

#### ThreadLocal

#### Fork/Join

### 并发集合

### 线程池

- Executor
- ThreadPoolExecutor
- Callable Future
- ScheduledExecutorService

### 并发工具类

- CountDownLatch
- CyclicBarrier
- Semphore

### 线程通信与消息传递

### 锁

- ReentranLock
- ReadWriteLock
- Condition

#### 锁优化

- 自旋锁
- 偏向锁
- 轻量锁
- 重量锁

### 原子操作Atom

## JVM

- jvm虚拟机原理、调优
- "直接内存"的特点
- java反射技术，java字节码技术;

## 通信协议

- 熟悉http协议，尤其是http头，session和cookie的生命周期以及它们之间的关联。
- 熟悉tcp协议，创建连接三次握手和断开连接四次握手的整个过程
- RPC方式
- RESTFul， 对性能等参数有个量化的了解
- 安全设计 比如MySQL和PostgreSQL的协议都有安全的设计。
- 加密/签名技术：常见如truecrypt、openssl、gnu pg、sha1、md5、scrypt等，了解各种加密/签名技术的安全性，字长等，
- OAuth：与其他网站联合认证的方式，有多种，分别了解。
- 用户认证：一开始就应该花大精力设计好用户认证系统，包括不要明文存储密码，包括严格限制Cookie和Session的使用，包括用户认证信息的缓存等。如果需要设计一个长期运行的大系统，强烈建议使用签名来保证Cookie的不可伪造，同时常见信息直接存储Cookie，这样可以避免每次Request都访问数据库。
