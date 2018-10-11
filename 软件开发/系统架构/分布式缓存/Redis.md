# Redis

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




