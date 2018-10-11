# MongoDB

## MongoDB文档模型的好处

MongoDB是文档型数据库，是Schema Free的

1、json形式
在MongoDB中，开发人员可以直接将一个json数据存储进MongoDB，这对于开发人员来说是非常友好额；

2、读写性能高
在关系型数据库中，我们经常会进行join、子查询等关联性需求，这时候往往会带来较多的随机IO，而在MongoDB中，我们可以通过合理的数据模型设计来将很多的关联需求通过内嵌、反范式的方式实现，减少了随机IO；

3、schema free
MongoDB的数据模型是灵活的，无需为了Online DDL而操心，不同的document也可以有不同的结构。

MongoDB的可以通过内嵌来减少联合查询的需求，可以通过反范式来减少随机IO

## Document Validation

MongoDB 3.2推出了Document Validation，Document Validation, MongoDB官方想表达”schema free but you may need some rules

phone字段为string类型或者email字段要匹配”@mongodb.com”结尾，或者status为”Unknown”或者"Incomplete"

``` mongodb
db.createCollection("contacts", 
    { validator: {$or:
        [
            { phone: {$type:"string"}},
            { email: {$regex:/@mongodb\.com$/}},
            { status: {$in:["Unknown","Incomplete"]}}
        ]}})
```

多了一个validationLevel参数，我们可以在设置validation的时候指定我们的validationLevel级别：

- 默认级别是strict，对该collection已有的和以后新增的document都进行validation验证；

- 可以设置为moderate，仅对已经存在的document进行validation限定；

同时还有validationAction参数来指定当有不符合validation规则的数据进行update或者insert的时候， 我们mongodb实例如何进行处理。

- 默认级别为error，mongodb将拒绝这些不符合validation规则的insert和update。

- 可以设置为warn，mongodb会在日志中记录，但是允许这类insert和update操作。

## MongoDB 集群

mongodb的集群搭建方式主要有三种，主从模式，Replica set模式，sharding模式, 三种模式各有优劣，适用于不同的场合，属Replica set应用最为广泛，主从模式现在用的较少，sharding模式最为完备，但配置维护较为复杂。本文我们来看下Replica Set模式的搭建方法。

Mongodb的Replica Set即副本集方式主要有两个目的，一个是数据冗余做故障恢复使用，当发生硬件故障或者其它原因造成的宕机时，可以使用副本进行恢复。另一个是做读写分离，读的请求分流到副本上，减轻主（Primary）的读压力

构建一个 mongoDB Sharding Cluster 需要三种角色:shard 服务器(ShardServer)、配置服务器(config Server)、路由进程(Route Process)

### Shard 服务器

shard 服务器即存储实际数据的分片,每个 shard 可以是一个 mongod 实例, 也可以是一组 mongod 实例构成的 Replica Sets.为了实现每个 Shard 内部的故障 自动转换,MongoDB 官方建议每个 shard 为一组 Replica Sets.

### 配置服务器

存储所有数据库元信息（路由、分片）的配置。mongos本身没有物理存储分片服务器和数据路由信息，只是缓存在内存里，配置服务器则实际存储这些数据。mongos第一次启动或者关掉重启就会从 config server 加载配置信息，以后如果配置服务器信息变化会通知到所有的 mongos 更新自己的状态，这样 mongos 就能继续准确路由。在生产环境通常有多个 config server 配置服务器，因为它存储了分片路由的元数据，这个可不能丢失！就算挂掉其中一台，只要还有存货， mongodb集群就不会挂掉

### 路由进程

数据库集群请求的入口，所有的请求都通过mongos进行协调，不需要在应用程序添加一个路由选择器，mongos自己就是一个请求分发中心，它负责把对应的数据请求请求转发到对应的shard服务器上。在生产环境通常有多mongos作为请求的入口，防止其中一个挂掉所有的mongodb请求都没有办法操作

### replica set

高可用性的分片架构还需要对于每一个分片构建 replica set 副本集保证分片的可靠性。生产环境通常是 2个副本 + 1个仲裁。
