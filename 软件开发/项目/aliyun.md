## 安装 Docker

curl -sSL http://acs-public-mirror.oss-cn-hangzhou.aliyuncs.com/docker-engine/intranet | sh

### 内核要求：

由于LXC的一个bug，Docker在3.8内核下面运行最佳。Ubuntu的Precise版本内置的是3.2版本的内核，因此我们首先需要升级内核。安装下面的步骤可以升级到3.8内核，并内置AUFS的支持。同时还包括了通用头文件，这样我们就可以激活依赖于这些头文件的包，比如ZFS，VirtualBox的增强功能包。

```
# install the backported kernel
sudo apt-get update
sudo apt-get install linux-image-generic-lts-raring linux-headers-generic-lts-raring

# reboot
sudo reboot
```

## Docker 安装 mongo

docker search mongo
docker pull mongo:3.6

1. 使用mongo镜像

运行容器
docker run -p 27017:27017 -v /home/oliver/Work/Develop/mongodb/db:/data/db -d mongo:3.6

aliyun:

docker run -d -p 27017:27017 -v /home/oliver/tools/mongodb/db:/data/db -d mongo:3.6

命令说明：

-p 27017:27017 :将容器的27017 端口映射到主机的27017 端口
-v $PWD/db:/data/db :将主机中当前目录下的db挂载到容器的/data/db，作为mongo数据存储目录

docker run -t -i mongo:3.6 mongo

使用mongo镜像执行mongo 命令连接到刚启动的容器,主机IP为172.17.0.1

docker run -it mongo:3.6 mongo --host 172.17.0.1

mongorestore -h 127.0.0.1 -d stockminingnew  /home/oliver/mongo/dbdump

## docker下安装python

docker search python
docker pull python:3.6

1. 启动python镜像容器

第一次运行下载下来的python进行修改
docker run -t -i python:3.6 /bin/bash

首次后使用自己的image进行修改
docker run -t -i oliver/python:3.6a /bin/bash

2. 安装对应库
```
    pip install pandas 
    pip install lxml 
    pip install bs4
    pip install matplotlib
    pip install requests
    pip install flask
    pip install flask-restplus
    pip install pymongo
    pip install tushare 
```

3. 退出你刚才配置好的docker镜像
exit    

4. 使用以下命令可以看到刚才退出的docker镜像
docker ps -a

5. 用以下命令，根据某个”容器ID”来创建一个新的”镜像”：
docker commit -m="Add pandas/lxml/bs4/matplotlib/tushare/threadpool/Django/flask" -a="oliver" 7d95d4cc8018 oliver/python:3.6a

## 启动Springboot

/home/oliver/tools/jdk1.8.0_191/bin/java -jar /home/oliver/SampleService-1.0-SNAPSHOT.jar >/home/oliver/service.log &


## 启动页面

ng serve --prod --aot --host 0.0.0.0 &

## Cron Job

开机就启动cron进程的设置命令：chkconfig --add crond

把cron加入到启动脚本中：
rc-update add vixie-cron default
crontab -l  #查看你的任务
crontab -e #编辑你的任务
crontab -r #删除用户的crontab的内容

秒 0-59 , - * / 
分 0-59 , - * / 
小时 0-23 , - * / 
日期 1-31 , - * ? / L W C 
月份 1-12 或者 JAN-DEC , - * / 
星期 1-7 或者 SUN-SAT , - * ? / L C # 
年（可选） 留空, 1970-2099 , - * /

"0 15 10 ? * *" 每天上午10:15触发

59 21 * * * python /home/oliver/stockQuant/tuShareService.py >/dev/null 2>&1> /tmp/cronjob.txt