# Redis

## Redis主从复制原理

Redis虽然读取写入的速度都特别快，但是也会产生读压力特别大的情况。为了分担读压力，Redis支持主从复制，Redis的主从结构可以采用一主多从或者级联结构，Redis主从复制可以根据是否是全量分为全量同步和增量同步。

### 全量同步

Redis全量复制一般发生在Slave初始化阶段，这时Slave需要将Master上的所有数据都复制一份。具体步骤如下： 
　　1）从服务器连接主服务器，发送SYNC命令； 
　　2）主服务器接收到SYNC命名后，开始执行BGSAVE命令生成RDB文件并使用缓冲区记录此后执行的所有写命令； 
　　3）主服务器BGSAVE执行完后，向所有从服务器发送快照文件，并在发送期间继续记录被执行的写命令； 
　　4）从服务器收到快照文件后丢弃所有旧数据，载入收到的快照； 
　　5）主服务器快照发送完毕后开始向从服务器发送缓冲区中的写命令； 
　　6）从服务器完成对快照的载入，开始接收命令请求，并执行来自主服务器缓冲区的写命令；

### 增量同步

Redis增量复制是指Slave初始化后开始正常工作时主服务器发生的写操作同步到从服务器的过程。 
增量复制的过程主要是主服务器每执行一个写命令就会向从服务器发送相同的写命令，从服务器接收并执行收到的写命令

### Redis主从同步策略

主从刚刚连接的时候，进行全量同步；全同步结束后，进行增量同步。当然，如果有需要，slave 在任何时候都可以发起全量同步。redis 策略是，无论如何，首先会尝试进行增量同步，如不成功，要求从机进行全量同步

如果多个Slave断线了，需要重启的时候，因为只要Slave启动，就会发送sync请求和主机全量同步，当多个同时出现的时候，可能会导致Master IO剧增宕机

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




