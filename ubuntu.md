## 替换源

1.寻找国内镜像源

https://mirrors.tuna.tsinghua.edu.cn/help/ubuntu/

2.配置source  list源

sources.list系统自带的，源是来Ubuntu的官网！安装包比较慢，所以最好切换成国内的

```
linuxidc.com@ubuntu:~$ cd /etc/apt
linuxidc.com@ubuntu:/etc/apt$ sudo cp sources.list sources.list.bak
linuxidc.com@ubuntu:/etc/apt$ vim sources.list                                                 

# 默认注释了源码镜像以提高 apt update 速度，如有需要可自行取消注释
清华大学

# deb cdrom:[Ubuntu 16.04 LTS _Xenial Xerus_ - Release amd64 (20160420.1)]/ xenial main restricted
deb http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial main restricted
deb http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-updates main restricted
deb http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial universe
deb http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-updates universe
deb http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial multiverse
deb http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-updates multiverse
deb http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-backports main restricted universe multiverse
deb http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-security main restricted
deb http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-security universe
deb http://mirrors.tuna.tsinghua.edu.cn/ubuntu/ xenial-security multiverse

阿里云源

# deb cdrom:[Ubuntu 16.04 LTS _Xenial Xerus_ - Release amd64 (20160420.1)]/ xenial main restricted
deb-src http://archive.ubuntu.com/ubuntu xenial main restricted #Added by software-properties
deb http://mirrors.aliyun.com/ubuntu/ xenial main restricted
deb-src http://mirrors.aliyun.com/ubuntu/ xenial main restricted multiverse universe #Added by software-properties
deb http://mirrors.aliyun.com/ubuntu/ xenial-updates main restricted
deb-src http://mirrors.aliyun.com/ubuntu/ xenial-updates main restricted multiverse universe #Added by software-properties
deb http://mirrors.aliyun.com/ubuntu/ xenial universe
deb http://mirrors.aliyun.com/ubuntu/ xenial-updates universe
deb http://mirrors.aliyun.com/ubuntu/ xenial multiverse
deb http://mirrors.aliyun.com/ubuntu/ xenial-updates multiverse
deb http://mirrors.aliyun.com/ubuntu/ xenial-backports main restricted universe multiverse
deb-src http://mirrors.aliyun.com/ubuntu/ xenial-backports main restricted universe multiverse #Added by software-properties
deb http://archive.canonical.com/ubuntu xenial partner
deb-src http://archive.canonical.com/ubuntu xenial partner
deb http://mirrors.aliyun.com/ubuntu/ xenial-security main restricted
deb-src http://mirrors.aliyun.com/ubuntu/ xenial-security main restricted multiverse universe #Added by software-properties
deb http://mirrors.aliyun.com/ubuntu/ xenial-security universe
deb http://mirrors.aliyun.com/ubuntu/ xenial-security multiverse
```

4.其他一些命令
```
sudo apt-get update  更新源
sudo apt-get install package 安装包
sudo apt-get remove package 删除包
sudo apt-cache search package 搜索软件包
sudo apt-cache show package  获取包的相关信息，如说明、大小、版本等
sudo apt-get install package --reinstall  重新安装包
sudo apt-get -f install  修复安装
sudo apt-get remove package --purge 删除包，包括配置文件等
sudo apt-get build-dep package 安装相关的编译环境
sudo apt-get upgrade 更新已安装的包
sudo apt-get dist-upgrade 升级系统
sudo apt-cache depends package 了解使用该包依赖那些包
sudo apt-cache rdepends package 查看该包被哪些包依赖
sudo apt-get source package  下载该包的源代码
sudo apt-get clean && sudo apt-get autoclean 清理无用的包
sudo apt-get check 检查是否有损坏的依赖
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

首先退出你刚才配置好的docker镜像
exit

然后使用以下命令可以看到刚才退出的docker镜像
docker ps –a

再使用以下命令，根据某个”容器ID”来创建一个新的”镜像”：
docker  commit  57c312bbaad1  javaweb:0.1

该容器ID是”57c312bbaad1”，所创建的镜像名是”javaweb”
注意：”57c312bbaad1” 这个ID是使用 docker ps 命令来查看的
提交了新的镜像你可以把这个镜像储存tar包
docker    –o  ~/javaweb.tar  javaweb
docker  save –o  保存的目录  镜像名

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