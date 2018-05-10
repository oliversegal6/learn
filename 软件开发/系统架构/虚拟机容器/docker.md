# Docker基础

## Ubuntu Docker 安装

http://www.docker.org.cn/book/install/install-docker-under-ubuntu-precise-20.html

### 内核要求：

由于LXC的一个bug，Docker在3.8内核下面运行最佳。Ubuntu的Precise版本内置的是3.2版本的内核，因此我们首先需要升级内核。安装下面的步骤可以升级到3.8内核，并内置AUFS的支持。同时还包括了通用头文件，这样我们就可以激活依赖于这些头文件的包，比如ZFS，VirtualBox的增强功能包。

```
# install the backported kernel
sudo apt-get update
sudo apt-get install linux-image-generic-lts-raring linux-headers-generic-lts-raring

# reboot
sudo reboot
```


## 使用Docker

可以下载ubuntu镜像并启动一个镜像来验证安装是否正常。

sudo docker run -i -t ubuntu /bin/bash

成功运行之后，输入exit退出即可。

### 下载镜像

docker search <image>
使用docker pull imagename（镜像名）来下载镜像

下载完成后使用以下命令查看本地所有的镜像：

docker images

### 创建 image 文件

有了 Dockerfile 文件以后，就可以使用docker image build命令创建 image 文件了。

```
    $ docker image build -t koa-demo .
    # 或者
    $ docker image build -t koa-demo:0.0.1 .
```
上面代码中，-t参数用来指定 image 文件的名字，后面还可以用冒号指定标签。如果不指定，默认的标签就是latest。最后的那个点表示 Dockerfile 文件所在的路径，上例是当前路径，所以是一个点。

如果运行成功，就可以看到新生成的 image 文件koa-demo了。

```
    $ docker image ls
```

### 生成容器

docker container run命令会从 image 文件生成容器。

```
    $ docker container run -p 8000:3000 -it koa-demo /bin/bash
    # 或者
    $ docker container run -p 8000:3000 -it koa-demo:0.0.1 /bin/bash
```
上面命令的各个参数含义如下：
```
        -p参数：容器的 3000 端口映射到本机的 8000 端口。
        -it参数：容器的 Shell 映射到当前的 Shell，然后你在本机窗口输入的命令，就会传入容器。
        koa-demo:0.0.1：image 文件的名字（如果有标签，还需要提供标签，默认是 latest 标签）。
        /bin/bash：容器启动以后，内部第一个执行的命令。这里是启动 Bash，保证用户可以使用 Shell。
```
如果一切正常，运行上面的命令以后，就会返回一个命令行提示符。

```
    root@66d80f4aaf1e:/app#
```
这表示你已经在容器里面了，返回的提示符就是容器内部的 Shell 提示符。执行下面的命令。

```
    root@66d80f4aaf1e:/app# node demos/01.js
```
RUN命令与CMD命令的区别在哪里？简单说，RUN命令在 image 文件的构建阶段执行，执行结果都会打包进入 image 文件；CMD命令则是在容器启动后执行。另外，一个 Dockerfile 可以包含多个RUN命令，但是只能有一个CMD命令。

### 启动容器

容器是在镜像的基础上来运行的，一旦容器启动了，我们就可以登录到容器中，安装自己所需的软件或应用程序。

使用进入已经运行的docker
docker  attach  dabfb413d8cf[容器ID]

使用以下命令即可启动容器：
docker run -i -t -v /root/software/:/mnt/software/ --privileged=true 2a392a47afc5
docker run <相关参数> <镜像 ID> <初始命令>

其中相关参数包括：
-i：表示以交互模式运行容器
-t：表示容器启动后会进入其命令行
-v：表示需要将本地哪个目录挂载到容器中，格式-v<宿主机目录>：<容器目录>

假设我们的所有安装程序都放在了宿主机的/root/software/目录下，现在需要将其挂载到容器的/mnt/software/目录下。
这一切做好后你就可以为这个容器安装软件了。

Docker 传送文件命令
docker cp more.log e7de404c00bd:/tmp/  


### 提交Docker镜像

1. 启动python镜像容器
docker run -t -i python:3.5 /bin/bash

2. 安装对应库
```
    $ pip install pandas 
    $ pip install lxml 
    $ pip install bs4
    $ pip install tushare 
```

3. 退出你刚才配置好的docker镜像
exit

4. 使用以下命令可以看到刚才退出的docker镜像
docker ps -a

5. 用以下命令，根据某个”容器ID”来创建一个新的”镜像”：

docker commit -m="Add pandas/lxml/bs4/tushare" -a="oliver" 165364af3418 oliver/python:3.5a

各个参数说明：
    -m:提交的描述信息
    -a:指定镜像作者
    165364af3418
    oliver/python:3.5a:指定要创建的目标镜像名

该容器ID是”57c312bbaad1”，所创建的镜像名是”javaweb”
注意：”57c312bbaad1” 这个ID是使用 docker ps 命令来查看的
提交了新的镜像可以把这个镜像储存tar包
docker save -o  ~/python.tar  oliver/python
docker  save -o  保存的目录  镜像名

### 发布 image 文件

容器运行成功后，就确认了 image 文件的有效性。这时，我们就可以考虑把 image 文件分享到网上，让其他人使用。

首先，去 hub.docker.com 或 cloud.docker.com 注册一个账户。然后，用下面的命令登录。

```
    $ docker login
```

接着，为本地的 image 标注用户名和版本。

```
    $ docker image tag [imageName] [username]/[repository]:[tag]
    # 实例
    $ docker image tag koa-demos:0.0.1 ruanyf/koa-demos:0.0.1
```
也可以不标注用户名，重新构建一下 image 文件。

```
    $ docker image build -t [username]/[repository]:[tag] .
```
最后，发布 image 文件。

```
    $ docker image push [username]/[repository]:[tag]
```

发布成功以后，登录 hub.docker.com，就可以看到已经发布的 image 文件。


### 编写Dockerfile

我使用了Dockerfile来描述开发环境，下面是我写的一个只安装Eclipse的Dockerfile，诸如mysql，jdk什么的比较简单就不再写进来了。

vi Dockerfile
```
RUN echo "deb http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ trusty main restricted universe multiverse" > /etc/apt/sources.list
RUN echo "deb http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ trusty-security main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ trusty-updates main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ trusty-backports main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ trusty-proposed main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb-src http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ trusty main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb-src http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ trusty-security main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb-src http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ trusty-updates main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb-src http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ trusty-backports main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb-src http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ trusty-proposed main restricted universe multiverse" >> /etc/apt/sources.list

RUN apt-get update && apt-get install -y libgtk2.0-0 libcanberra-gtk-module
RUN apt-get install -y eclipse

# Replace 1000 with your user / group id
RUN export uid=1000 gid=1000 && \
    mkdir -p /home/developer && \
    echo "developer:x:${uid}:${gid}:Developer,,,:/home/developer:/bin/bash" >> /etc/passwd && \
    echo "developer:x:${uid}:" >> /etc/group && \
    echo "developer ALL=(ALL) NOPASSWD: ALL" > /etc/sudoers.d/developer && \
    chmod 0440 /etc/sudoers.d/developer && \
    chown ${uid}:${gid} -R /home/developer

USER developer
ENV HOME /home/developer
CMD /usr/bin/eclipse

```

### Docker Build

docker build -t eclipse .

有error
```
error pulling image configuration: Get https://dseasb33srnrn.cloudfront.net/registry-v2/docker/registry/v2/blobs/sha256/e4/e4422b8da209755dd5a8aa201ba79cef0c465003f46f6313f318a0e306e4fe05/data?Expires=1525925417&Signature=VMqDbVQri5CAHgh9WfKsiOgbuEAKi6VpZUjFdjgrA0Q8~XTyXdLYeRDwznGSfPdaMCkcPdRg32r-tdBLJ9~hPXb8QcaAAw0OXCZi86X8xXbv8Bjdjrl4whMB~ooIE0Sd6fXiQBRnlsB8lO5MhTg0lkkb7IPes9XU31-RiovOFrM_&Key-Pair-Id=APKAJECH5M7VWIS5YZ6Q: net/http: TLS handshake timeout


出现这个问题原因为国内网络问题，无法连接到 docker hub。 好在国内已经有  daocloud，docker指定该源即可。

1. systemctl stop docker
2. sudo vi /etc/docker/daemon.json
3. add 
{
  "registry-mirrors": ["https://registry.docker-cn.com"]
}
4. service docker restart


service docker start
如果service命令启动不了用下面的
systemctl start docker.service
```

### Run Docker
```
docker run -ti --rm \
       -e DISPLAY=$DISPLAY \
       -v /tmp/.X11-unix:/tmp/.X11-unix \
       psharkey/eclipse

mkdir -p .eclipse-docker
docker run -ti --rm \
           -e DISPLAY=$DISPLAY \
           -v /tmp/.X11-unix:/tmp/.X11-unix \
           -v `pwd`/.eclipse-docker:/home/developer \
           -v `pwd`:/workspace \
           fgrehm/eclipse:v4.4.1

run: 运行docker
--ti: 伪终端交互模式
--rm: 运行后删除Container
--name: 运行的容器的名称
--v: 将主机的目录和容器的目录做镜像，这样容器在这个目录操作的内容就自动同步保存到主机上
--e: 环境变量设置
iwakoshi/eclipse：镜像的名字，docker pull下来的
--device：可选参数，设备和主机共享
```

# 使用 Docker 搭建开发环境

## Docker 安装 Python
```
docker search python
docker pull python:3.5
```

使用python镜像运行容器
docker run  -v $PWD/myapp:/usr/src/myapp  -w /usr/src/myapp python:3.5 python helloworld.py

命令说明：
-v $PWD/myapp:/usr/src/myapp :将主机中当前目录下的myapp挂载到容器的/usr/src/myapp
-w /usr/src/myapp :指定容器的/usr/src/myapp目录为工作目录
python helloworld.py :使用容器的python命令来执行工作目录中的helloworld.py文件

## Docker 安装 mongo

docker search mongo
docker pull mongo:3.6

1. 使用mongo镜像

运行容器
docker run -p 27017:27017 -v $PWD/db:/data/db -d mongo:3.6

命令说明：

-p 27017:27017 :将容器的27017 端口映射到主机的27017 端口
-v $PWD/db:/data/db :将主机中当前目录下的db挂载到容器的/data/db，作为mongo数据存储目录

2. 查看容器启动情况

docker ps 

3. 使用mongo镜像执行mongo 命令连接到刚启动的容器,主机IP为172.17.0.1

docker run -it mongo:3.6 mongo --host 172.17.0.1