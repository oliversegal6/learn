 [Kafka](https://kafka.apache.org "Kafka") is a distributed, partitioned, replicated commit log service. ”

它提供了一个非常特殊的消息机制，不同于传统的mq

## kafka基基原理

       通常来讲，消息模型可以分为两种：队列和发布-订阅式。队列的处理方式是一组消费者从服务器读取消息，一条消息只有其中的一个消费者来处理。在发布-订阅模型中，消息被广播给所有的消费者，接收到消息的消费者都可以处理此消息。Kafka为这两种模型提供了单一的消费者抽象模型： 消费者组(consumer group)。消费者用一个消费者组名标记自己。

       一个发布在Topic上消息被分发给此消费者组中的一个消费者。假如所有的消费者都在一个组中，那么这就变成了queue模型。假如所有的消费者都在不同的组中，那么就完全变成了发布-订阅模型。更通用的， 我们可以创建一些消费者组作为逻辑上的订阅者。每个组包含数目不等的消费者，一个组内多个消费者可以用来扩展性能和容错。       

       并且，kafka能够保证生产者发送到一个特定的Topic的分区上，消息将会按照它们发送的顺序依次加入，也就是说，如果一个消息M1和M2使用相同的producer发送，M1先发送，那么M1将比M2的offset低，并且优先的出现在日志中。消费者收到的消息也是此顺序。如果一个Topic配置了复制因子（replication facto）为N,那么可以允许N-1服务器宕机而不丢失任何已经提交（committed）的消息。此特性说明kafka有比传统的消息系统更强的顺序保证。但是，相同的消费者组中不能有比分区更多的消费者，否则多出的消费者一直处于空等待，不会收到消息。

## 主题和日志 (Topic和Log)

      每一个分区(partition)都是一个顺序的、不可变的消息队列,并且可以持续的添加。分区中的消息都被分了一个序列号,称之为偏移量(offset),在每个分区中此偏移量都是唯一的。Kafka集群保持所有的消息,直到它们过期,无论消息是否被消费了。实际上消费者所持有的仅有的元数据就是这个偏移量，也就是消费者在这个log中的位置。 这个偏移量由消费者控制：正常情况当消费者消费消息的时候，偏移量也线性的的增加。但是实际偏移量由消费者控制，消费者可以将偏移量重置为更老的一个偏移量，重新读取消息。 可以看到这种设计对消费者来说操作自如， 一个消费者的操作不会影响其它消费者对此log的处理。 再说说分区。Kafka中采用分区的设计有几个目的。一是可以处理更多的消息，不受单台服务器的限制。Topic拥有多个分区意味着它可以不受限的处理更多的数据。第二，分区可以作为并行处理的单元，稍后会谈到这一点。

## 分布式(Distribution)

       Log的分区被分布到集群中的多个服务器上。每个服务器处理它分到的分区。根据配置每个分区还可以复制到其它服务器作为备份容错。 每个分区有一个leader，零或多个follower。Leader处理此分区的所有的读写请求，而follower被动的复制数据。如果leader宕机，其它的一个follower会被推举为新的leader。 一台服务器可能同时是一个分区的leader，另一个分区的follower。 这样可以平衡负载，避免所有的请求都只让一台或者某几台服务器处理。

## 与传统的mq区别
- 更快！单机上万TPS
- 传统的MQ，消息被消化掉后会被mq删除，而kafka中消息被消化后不会被删除，而是到配置的expire时间后，才删除
- 传统的MQ，消息的Offset是由MQ维护，而kafka中消息的Offset是由客户端自己维护
- 分布式，把写入压力均摊到各个节点。可以通过增加节点降低压力

------------
## 基本术语
### Producer/Consumer
这两个与传统的MQ一样

### Topic
Kafka中的topic其实对应传统MQ的channel，即消息管道，例如同一业务用同一根管道

### Broker
集群中的KafkaServer，用来提供Partition服务

### Partition 
假如说传统的MQ，传输消息的通道(channel)是一条双车道公路，那么Kafka中，Topic就是一个N车道的高速公路。每个车道都可以行车，而每个车道就是Partition。

一个Topic中可以有一个或多个partition。
一个Broker上可以跑一个或多个Partition。集群中尽量保证partition的均匀分布，例如定义了一个有3个partition的topic，而只有两个broker，那么一个broker上跑两个partition，而另一个是1个。但是如果有3个broker，必然是3个broker上各跑一个partition。

Partition中严格按照消息进入的顺序排序
一个从Producer发送来的消息，只会进入Topic的某一个Partition（除非特殊实现Producer要求消息进入所有Partition）
Consumer可以自己决定从哪个Partition读取数据

### Offset
单个Partition中的消息的顺序ID，例如第一个进入的Offset为0，第二个为1，以此类推。传统的MQ，Offset是由MQ自己维护，而kafka是由client维护

### Replica
Kafka从0.8版本开始，支持消息的HA，通过消息复制的方式。在创建时，我们可以指定一个topic有几个partition，以及每个partition有几个复制。复制的过程有同步和异步两种，根据性能需要选取。 正常情况下，写和读都是访问leader，只有当leader挂掉或者手动要求重新选举，kafka会从几个复制中选举新的leader。

Kafka会统计replica与leader的同步情况。当一个replica与leader数据相差不大，会被认为是一个"in-sync" replica。只有"in-sync" replica才有资格参与重新选举。

### ConsumerGroup
一个或多个Consumer构成一个ConsumerGroup，一个消息应该只能被同一个ConsumerGroup中的一个Consumer消化掉，但是可以同时发送到不同ConsumerGroup。

通常的做法，一个Consumer去对应一个Partition。

传统MQ中有queuing（消息）和publish-subscribe（订阅）模式，Kafka中也支持：

当所有Consumer具有相同的ConsumerGroup时，该ConsumerGroup中只有一个Consumer能收到消息，就是 queuing 模式
当所有Consumer具有不同的ConsumerGroup时，每个ConsumerGroup会收到相同的消息，就是 publish-subscribe 模式

1.producer：
　　消息生产者，发布消息到 kafka 集群的终端或服务。
2.broker：
　　kafka 集群中包含的服务器。
3.topic：
　　每条发布到 kafka 集群的消息属于的类别，即 kafka 是面向 topic 的。
4.partition：
　　partition 是物理上的概念，每个 topic 包含一个或多个 partition。kafka 分配的单位是 partition。
5.consumer：
　　从 kafka 集群中消费消息的终端或服务。
6.Consumer group：
　　high-level consumer API 中，每个 consumer 都属于一个 consumer group，每条消息只能被 consumer group 中的一个 Consumer 消费，但可以被多个 consumer group 消费。
7.replica：
　　partition 的副本，保障 partition 的高可用。
8.leader：
　　replica 中的一个角色， producer 和 consumer 只跟 leader 交互。
9.follower：
　　replica 中的一个角色，从 leader 中复制数据。
10.controller：
　　kafka 集群中的其中一个服务器，用来进行 leader election 以及 各种 failover。
12.zookeeper：
　　kafka 通过 zookeeper 来存储集群的 meta 信息。

------------

## 基本交互原理
每个Topic被创建后，在zookeeper上存放有其metadata，包含其分区信息、replica信息、LogAndOffset等 
默认路径/brokers/topics/<topic_id>/partitions/<partition_index>/state

Producer可以通过zookeeper获得topic的broker信息，从而得知需要往哪写数据。

Consumer也从zookeeper上获得该信息，从而得知要监听哪个partition。

## 创建一个Producer

Kafka提供了java api，Producer特别的简单，举传输byte[] 为例

```java
Properties p = new Properties();
props.put("metadata.broker.list", "10.1.110.21:9092");
ProducerConfig config = new ProducerConfig(props);
Producer producer = new Producer<String, byte[]>(config);
producer.send(byte[] msg);
```
## 创建一个Consumer

Kafka提供了两种java的Consumer API：High Level Consumer和Simple Consumer

如何保证kafka的高容错性？

producer不使用批量接口，并采用同步模型持久化消息。
consumer不采用批量化，每消费一次就更新offset


## Setup Kafka
[Quick Start](https://kafka.apache.org/quickstart "Quick Start")

### Windows
cd F:\Develop\kafka_2.11-1.0.0\
#### 1. Start the server
**start zookeeper**
bin\windows\zookeeper-server-start.bat config\zookeeper.properties
**start kafka**
bin\windows\kafka-server-start.bat config\server.properties

#### 2. Create a topic
Let's create a topic named "test" with a single partition and only one replica:
`bin\windows\kafka-topics.bat --create --zookeeper localhost:2181 --replication-factor 1 --partitions 1 --topic test`

We can now see that topic if we run the list topic command:
` bin\windows\kafka-topics.bat --list --zookeeper localhost:2181`

#### 3. Send some messages

Kafka comes with a command line client that will take input from a file or from standard input and send it out as messages to the Kafka cluster. By default, each line will be sent as a separate message.

Run the producer and then type a few messages into the console to send to the server.

`bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic test`

#### 4. Start a consumer

Kafka also has a command line consumer that will dump out messages to standard output.

`bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic test --from-beginning`




