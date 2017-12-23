 [Kafka](https://kafka.apache.org "Kafka") is a distributed, partitioned, replicated commit log service. ”

它提供了一个非常特殊的消息机制，不同于传统的mq

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




