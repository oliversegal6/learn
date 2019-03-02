# Redis

在大并发的情况下，所有的请求直接访问数据库，数据库会出现连接异常。这个时候，就需要使用 Redis 做一个缓冲操作，让请求先访问到 Redis，而不是直接访问数据库。

使用 Redis 的常见问题

- 缓存和数据库双写一致性问题
- 缓存雪崩问题
- 缓存击穿问题
- 缓存的并发竞争问题

Redis 是单线程工作模型。

原因主要是以下三点：

- 纯内存操作
- 单线程操作，避免了频繁的上下文切换
- 采用了非阻塞 I/O 多路复用机制

热数据和冷数据

## 值类型

- Lists: 按插入顺序排序的字符串元素的集合。他们基本上就是链表（linked lists）。
- Sets: 不重复且无序的字符串元素的集合。
- Sorted sets,类似Sets,但是每个字符串元素都关联到一个叫score浮动数值（floating number value）。里面的元素总是通过- score进行着排序，所以不同的是，它是可以检索的一系列元素。（例如你可能会问：给我前面10个或者后面10个元素）。
- Hashes,由field和关联的value组成的map。field和value都是字符串的。这和Ruby、Python的hashes很像。
- Bit arrays (或者说 simply bitmaps): 通过特殊的命令，你可以将 String 值当作一系列 bits 处理：可以设置和清除单独的 - bits，数出所有设为 1 的 bits 的数量，找到最前的被设为 1 或 0 的 bit，等等。
- HyperLogLogs: 这是被用于估计一个 set 中元素数量的概率性的数据结构

## redis持久化

redis支持两种持久化的方式，可以单独使用或者结合起来使用
第一种：RDB方式（redis默认的持久化方式）
第二种：AOF方式

一、RDB
rdb方式的持久化是通过快照完成的，当符合一定条件时redis会自动将内存中的所有数据执行快照操作并存储到硬盘上。默认存储在redis根目录的dump.rdb文件中。(文件名在配置文件中dbfilename)
redis进行快照的时机（在配置文件redis.conf中）
save 900 1：表示900秒内至少一个键被更改则进行快照。
save 300 10
save 60 10000

redis自动实现快照的过程
1：redis使用fork函数复制一份当前进程的副本(子进程)
2：父进程继续接收并处理客户端发来的命令，而子进程开始将内存中的数据写入硬盘中的临时文件
3：当子进程写入完所有数据后会用该临时文件替换旧的RDB文件，至此，一次快照操作完成。  
注意：redis在进行快照的过程中不会修改RDB文件，只有快照结束后才会将旧的文件替换成新的，也就是说任何时候RDB文件都是完整的。 这就使得我们可以通过定时备份RDB文件来实现redis数据库的备份， RDB文件是经过压缩的二进制文件，占用的空间会小于内存中的数据，更加利于传输。

手动执行save或者bgsave命令让redis执行快照。
两个命令的区别在于，save是由主进程进行快照操作，会阻塞其它请求。bgsave是由redis执行fork函数复制出一个子进程来进行快照操作。
文件修复：redis-check-dump

RDB存在哪些优势呢？
1). 一旦采用该方式，那么你的整个Redis数据库将只包含一个文件，这对于文件备份而言是非常完美的。比如，你可能打算每个小时归档一次最近24小时的数据，同时还要每天归档一次最近30天的数据。通过这样的备份策略，一旦系统出现灾难性故障，我们可以非常容易的进行恢复。

2). 对于灾难恢复而言，RDB是非常不错的选择。因为我们可以非常轻松的将一个单独的文件压缩后再转移到其它存储介质上。

3). 性能最大化。对于Redis的服务进程而言，在开始持久化时，它唯一需要做的只是fork出子进程，之后再由子进程完成这些持久化的工作，这样就可以极大的避免服务进程执行IO操作了。

4). 相比于AOF机制，如果数据集很大，RDB的启动效率会更高。

RDB又存在哪些劣势呢？

1). 如果你想保证数据的高可用性，即最大限度的避免数据丢失，那么RDB将不是一个很好的选择。因为系统一旦在定时持久化之前出现宕机现象，此前没有来得及写入磁盘的数据都将丢失。

2). 由于RDB是通过fork子进程来协助完成数据持久化工作的，因此，如果当数据集较大时，可能会导致整个服务器停止服务几百毫秒，甚至是1秒钟。

二、AOF

 aof方式的持久化是通过日志文件的方式。默认情况下redis没有开启aof，可以通过参数appendonly参数开启。
 appendonly yes
 aof文件的保存位置和rdb文件的位置相同，都是dir参数设置的，默认的文件名是appendonly.aof，可以通过      appendfilename参数修改
 appendfilename appendonly.aof
 redis写命令同步的时机
 a ppendfsync always 每次都会执行
 appendfsync everysec 默认 每秒执行一次同步操作（推荐，默认）
 appendfsync no不主动进行同步，由操作系统来做，30秒一次
 aof日志文件重写
 auto-aof-rewrite-percentage 100(当目前aof文件大小超过上一次重写时的aof文件大小的百分之多少时会再次进行重写，如果之前没有重写，则以启动时的aof文件大小为依据)
 auto-aof-rewrite-min-size 64mb
 手动执行bgrewriteaof进行重写

AOF的优势有哪些呢？

1). 该机制可以带来更高的数据安全性，即数据持久性。Redis中提供了3中同步策略，即每秒同步、每修改同步和不同步。事实上，每秒同步也是异步完成的，其效率也是非常高的，所差的是一旦系统出现宕机现象，那么这一秒钟之内修改的数据将会丢失。而每修改同步，我们可以将其视为同步持久化，即每次发生的数据变化都会被立即记录到磁盘中。可以预见，这种方式在效率上是最低的。至于无同步，无需多言，我想大家都能正确的理解它。

2). 由于该机制对日志文件的写入操作采用的是append模式，因此在写入过程中即使出现宕机现象，也不会破坏日志文件中已经存在的内容。然而如果我们本次操作只是写入了一半数据就出现了系统崩溃问题，不用担心，在Redis下一次启动之前，我们可以通过redis-check-aof工具来帮助我们解决数据一致性的问题。

3). 如果日志过大，Redis可以自动启用rewrite机制。即Redis以append模式不断的将修改数据写入到老的磁盘文件中，同时Redis还会创建一个新的文件用于记录此期间有哪些修改命令被执行。因此在进行rewrite切换时可以更好的保证数据安全性。

4). AOF包含一个格式清晰、易于理解的日志文件用于记录所有的修改操作。事实上，我们也可以通过该文件完成数据的重建。

AOF的劣势有哪些呢？

1). 对于相同数量的数据集而言，AOF文件通常要大于RDB文件。RDB 在恢复大数据集时的速度比 AOF 的恢复速度要快。

2). 根据同步策略的不同，AOF在运行效率上往往会慢于RDB。总之，每秒同步策略的效率是比较高的，同步禁用策略的效率和RDB一样高效。

二者选择的标准，就是看系统是愿意牺牲一些性能，换取更高的缓存一致性（aof），还是愿意写操作频繁的时候，不启用备份来换取更高的性能，待手动运行save的时候，再做备份（rdb）。rdb这个就更有些 eventually consistent的意思了。

## 高可用（多副本Replication)

在 Redis 复制的基础上，使用和配置主从复制非常简单，能使得从 Redis 服务器（下文称 slave）能精确得复制主 Redis 服务器（下文称 master）的内容。

- 当一个 master 实例和一个 slave 实例连接正常时， master 会发送一连串的命令流来保持对 slave 的更新，以便于将自身数据集的改变复制给 slave ， ：包括客户端的写入、key 的过期或被逐出等等。
- 当 master 和 slave 之间的连接断开之后，因为网络问题、或者是主从意识到连接超时， slave 重新连接上 master 并会尝试进行部分重同步：这意味着它会尝试只获取在断开连接期间内丢失的命令流。
- 当无法进行部分重同步时， slave 会请求进行全量重同步。这会涉及到一个更复杂的过程，例如 master 需要创建所有数据的快照，将之发送给 slave ，之后在数据集更改时持续发送命令流到 slave 。

### 选举机制

1. Slave of：在slave中指定master，但是如果master不可用，不会自动切换slave
2. 哨兵：监控master和slave，保证master不可用时可以自动切换slave

Redis 的 Sentinel 系统用于管理多个 Redis 服务器（instance）， 该系统执行以下三个任务：

- 监控（Monitoring）： Sentinel 会不断地检查你的主服务器和从服务器是否运作正常。
- 提醒（Notification）： 当被监控的某个 Redis 服务器出现问题时， Sentinel 可以通过 API 向管理员或者其他应用程序发送通知。
- 自动故障迁移（Automatic failover）： 当一个主服务器不能正常工作时， Sentinel 会开始一次自动故障迁移操作， 它会将失效主服务器的其中一个从服务器升级为新的主服务器， 并让失效主服务器的其他从服务器改为复制新的主服务器； 当客户端试图连接失效的主服务器时， 集群也会向客户端返回新主服务器的地址， 使得集群可以使用新主服务器代替失效服务器。

3. redis-cluster

Redis集群搭建的方式有多种，例如使用zookeeper等，但从redis 3.0之后版本支持redis-cluster集群，Redis-Cluster采用无中心结构，每个节点保存数据和整个集群状态,每个节点都和其他所有节点连接

其结构特点：
     1. 所有的redis节点彼此互联(PING-PONG机制),内部使用二进制协议优化传输速度和带宽。
     2. 节点的fail是通过集群中超过半数的节点检测失效时才生效。
     3. 客户端与redis节点直连,不需要中间proxy层.客户端不需要连接集群所有节点,连接集群中任何一个可用节点即可。
     4. redis-cluster把所有的物理节点映射到[0-16383]slot上（不一定是平均分配）,cluster 负责维护node<->slot<->value。
     5. Redis集群预分好16384个桶，当需要在 Redis 集群中放置一个 key-value 时，根据 CRC16(key) mod 16384的值，决定将一个key放到哪个桶中

集群中至少应该有奇数个节点，所以至少有三个节点，每个节点至少有一个备份节点，所以下面使用6节点（主节点、备份节点由redis-cluster集群确定）。

### Redis主从复制原理

Redis虽然读取写入的速度都特别快，但是也会产生读压力特别大的情况。为了分担读压力，Redis支持主从复制，Redis的主从结构可以采用一主多从或者级联结构，Redis主从复制可以根据是否是全量分为全量同步和增量同步。

#### 全量同步

Redis全量复制一般发生在Slave初始化阶段，这时Slave需要将Master上的所有数据都复制一份。具体步骤如下： 
　　1）从服务器连接主服务器，发送SYNC命令； 
　　2）主服务器接收到SYNC命名后，开始执行BGSAVE命令生成RDB文件并使用缓冲区记录此后执行的所有写命令； 
　　3）主服务器BGSAVE执行完后，向所有从服务器发送快照文件，并在发送期间继续记录被执行的写命令； 
　　4）从服务器收到快照文件后丢弃所有旧数据，载入收到的快照； 
　　5）主服务器快照发送完毕后开始向从服务器发送缓冲区中的写命令； 
　　6）从服务器完成对快照的载入，开始接收命令请求，并执行来自主服务器缓冲区的写命令；

#### 增量同步

Redis增量复制是指Slave初始化后开始正常工作时主服务器发生的写操作同步到从服务器的过程。 
增量复制的过程主要是主服务器每执行一个写命令就会向从服务器发送相同的写命令，从服务器接收并执行收到的写命令

#### Redis主从同步策略

主从刚刚连接的时候，进行全量同步；全同步结束后，进行增量同步。当然，如果有需要，slave 在任何时候都可以发起全量同步。redis 策略是，无论如何，首先会尝试进行增量同步，如不成功，要求从机进行全量同步

如果多个Slave断线了，需要重启的时候，因为只要Slave启动，就会发送sync请求和主机全量同步，当多个同时出现的时候，可能会导致Master IO剧增宕机

## 水平扩展(分区Partitioning )

- The normal client communication port (usually 6379) used to communicate with clients to be open to all the clients that need to reach the cluster, plus all the other cluster nodes (that use the client port for keys migrations).
- The cluster bus port (the client port + 10000) must be reachable from all the other cluster nodes.

分区是将你的数据分发到不同redis实例上的一个过程，每个redis实例只是你所有key的一个子集

Redis分区主要有两个目的:

- 分区可以让Redis管理更大的内存，Redis将可以使用所有机器的内存。如果没有分区，你最多只能使用一台机器的内存。
- 分区使Redis的计算能力通过简单地增加计算机得到成倍提升,Redis的网络带宽也会随着计算机和网卡的增加而成倍增长。

### 读写路由，Partitioning的不同实现方式

分区可以在程序的不同层次实现。

- 客户端分区就是在客户端就已经决定数据会被存储到哪个redis节点或者从哪个redis节点读取。大多数客户端已经实现了客户端分区。
- 代理分区 意味着客户端将请求发送给代理，然后代理决定去哪个节点写数据或者读数据。代理根据分区规则决定请求哪些Redis实例，然后根据Redis的响应结果返回给客户端。redis和memcached的一种代理实现就是Twemproxy
- 查询路由(Query routing) 的意思是客户端随机地请求任意一个redis实例，然后由Redis将请求转发给正确的Redis节点。Redis Cluster实现了一种混合形式的查询路由，但并不是直接将请求从一个redis节点转发到另一个redis节点，而是在客户端的帮助下直接redirected到正确的redis节点。

### 写入时Sharding 策略

Redis集群是 query routing 和 client side partitioning的一种混合实现。

Redis 集群没有使用一致性hash, 而是引入了 哈希槽的概念.

Redis 集群有16384个哈希槽,每个key通过CRC16校验后对16384取模来决定放置哪个槽.集群的每个节点负责一部分hash槽,举个例子,比如当前集群有3个节点,那么:

- 节点 A 包含 0 到 5500号哈希槽.
- 节点 B 包含5501 到 11000 号哈希槽.
- 节点 C 包含11001 到 16384号哈希槽.

### 一致性保证

Redis 并不能保证数据的强一致性. 这意味这在实际中集群在特定的条件下可能会丢失写操作.

第一个原因是因为集群是用了异步复制. 写操作过程:

- 客户端向主节点B写入一条命令.
- 主节点B向客户端回复命令状态.
- 主节点将写操作复制给他得从节点 B1, B2 和 B3.

主节点对命令的复制工作发生在返回命令回复之后， 因为如果每次处理命令请求都需要等待复制操作完成的话， 那么主节点处理命令请求的速度将极大地降低 —— 我们必须在性能和一致性之间做出权衡。

### 数据自动均衡

Resharding就是把hash slots从一个实例复制到另一个实例，redis还不能自动reshard，可以用redis-cli来实现

./redis-trib.rb reshard 127.0.0.1:7000

## 缓存穿透和缓存雪崩问题

缓存穿透，即黑客故意去请求缓存中不存在的数据，导致所有的请求都怼到数据库上，从而数据库连接异常。

### 缓存穿透解决方案：

- 利用互斥锁，缓存失效的时候，先去获得锁，得到锁了，再去请求数据库。没得到锁，则休眠一段时间重试。
- 采用异步更新策略，无论 Key 是否取到值，都直接返回。Value 值中维护一个缓存失效时间，缓存如果过期，异步起一个线程去读数据库，更新缓存。需要做缓存预热(项目启动前，先加载缓存)操作。
- 提供一个能迅速判断请求是否有效的拦截机制，比如，利用布隆过滤器，内部维护一系列合法有效的 Key。迅速判断出，请求所携带的 Key 是否合法有效。如果不合法，则直接返回。

### 缓存雪崩

缓存雪崩，即缓存同一时间大面积的失效，这个时候又来了一波请求，结果请求都怼到数据库上，从而导致数据库连接异常。

缓存雪崩解决方案：

- 给缓存的失效时间，加上一个随机值，避免集体失效。
- 使用互斥锁，但是该方案吞吐量明显下降了。
- 双缓存。我们有两个缓存，缓存 A 和缓存 B。缓存 A 的失效时间为 20 分钟，缓存 B 不设失效时间。自己做缓存预热操作。
- 然后细分以下几个小点：从缓存 A 读数据库，有则直接返回；A 没有数据，直接从 B 读数据，直接返回，并且异步启动一个更新线程，更新线程同时更新缓存 A 和缓存 B。

## redis 基本配置

```shell
#what?局域网内本机IP。
#why?只接受外部程序发送到IP 172.17.84.39的数据。 
#resoult:更加安全，因为只有同一局域网内的机器能够访问。当然也可以把bind注释掉，以支持包括外网在内的所有IP。
bind 172.17.84.39
#修改默认端口，避免被恶意脚本扫描。
port 9999
loglevel debug
logfile /usr/local/redis/logs/redis.log.9999
#为服务设置安全密码
requirepass redispass
#以守护进程方式运行
daemonize yes
```

### 主从复制（master-slave）

主从模式的两个重要目的，提升系统可靠性和读写分离提升部分性能

slave机需要修改一下配置

```shell
port 9997
logfile /usr/local/redis/logs/redis.log.9997
#指定master ip port
slaveof 172.17.84.39 9999
#认证master时需要的密码。必须和master配置的requirepass 保持一致
masterauth redispass
protected-mode no
```

### Replication (sentinel模式故障自动迁移)

Master-slave主从复制避免了数据丢失带来的灾难性后果。

但是单点故障仍然存在，在运行期间master宕机需要停机手动切换。

Sentinel很好的解决了这个问题，当Master-slave模式中的Master宕机后，能够自主切换，选择另一个可靠的redis-server充当master角色，使系统仍正常运行

```shell
#服务运行端口号
port 26379
#mymaster为指定的master服务器起一个别名
#master IP和端口号
#2的含义：当开启的sentinel server认为当前master主观下线的（+sdown）数量达到2时，则sentinel server认为当前master客观下线（+odown）系统开始自动迁移。2的计算（建议）：sentinel server数量的大多数，至少为count（sentinel server）/2 向上取整。2>3/2（主观下线与客观下线？）
sentinel monitor mymaster 172.17.84.39 9999 2
#master别名和认证密码。这就提醒了用户，在master-slave系统中，各服务的认证密码应该保持一致。
sentinel auth-pass mymaster redispass
#以守护进程方式运行
daemonize yes
logfile /usr/local/redis/logs/sentinel.log.26379
protected-mode no
sentinel down-after-milliseconds mymaster 6000
sentinel failover-timeout mymaster 18000
```

### Cluster

redis.conf
```
port 7000
cluster-enabled yes
cluster-config-file nodes.conf
cluster-node-timeout 5000
appendonly yes
```

~/Work/Develop/redis-5.0.3/src/redis-server ~/Work/Develop/redis-5.0.3/cluster/17001/redis.conf &
~/Work/Develop/redis-5.0.3/src/redis-server ~/Work/Develop/redis-5.0.3/cluster/17002/redis.conf &
~/Work/Develop/redis-5.0.3/src/redis-server ~/Work/Develop/redis-5.0.3/cluster/17003/redis.conf &
~/Work/Develop/redis-5.0.3/src/redis-server ~/Work/Develop/redis-5.0.3/cluster/17004/redis.conf &
~/Work/Develop/redis-5.0.3/src/redis-server ~/Work/Develop/redis-5.0.3/cluster/17005/redis.conf &
~/Work/Develop/redis-5.0.3/src/redis-server ~/Work/Develop/redis-5.0.3/cluster/17006/redis.conf &

#### 搭建集群

通过向实例发送特殊命令来完成创建新集群

~/Work/Develop/redis-5.0.3/src/redis-cli --cluster create 127.0.0.1:17006 127.0.0.1:17001 127.0.0.1:17002 127.0.0.1:17003 127.0.0.1:17004 127.0.0.1:17005 --cluster-replicas 1

这个命令在这里用于创建一个新的集群, 选项–replicas 1 表示我们希望为集群中的每个主节点创建一个从节点

#### 测试集群

写入数据
```shell
~/Work/Develop/redis-5.0.3/src/redis-cli -c -p 17001
127.0.0.1:17001> set foo bar
-> Redirected to slot [12182] located at 127.0.0.1:17002
OK
127.0.0.1:17002> set hello world
-> Redirected to slot [866] located at 127.0.0.1:17006
OK
```

~/Work/Develop/redis-5.0.3/src/redis-cli -p 17001 cluster nodes 
