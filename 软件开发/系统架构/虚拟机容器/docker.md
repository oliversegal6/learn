#Setup Docker

下载Docker：https://www.docker.com

## Linux
要安装最新的 Docker 版本，首先需要安装 apt-transport-https 支持，之后通过添加源来安装。
```
```

## Windows
Hyper-V要打开 
具体就是，首先要打开cpu虚拟化功能，这个在BIOS里面改，可能会不成功，回复初始设置之后再改应该就可以了。 
然后就是在 程序与功能>启用或关闭windows功能 中将Hyper-v打开就可以了。

## 下载镜像
`docker search ubuntu`

使用docker pull ubuntu（镜像名）来下载镜像

下载完成后使用以下命令查看本地所有的镜像：
`docker images`

## 启动容器并修改镜像
容器是在镜像的基础上来运行的，一旦容器启动了，我们就可以登录到容器中，安装自己所需的软件或应用程序。
镜像下载到本地以后，就可以使用Docker运行，通过下面的命令参数启动容器，
docker run <相关参数> <镜像 ID> <初始命令>

其中相关参数包括：
-i：表示以交互模式运行容器
-t：表示容器启动后会进入其命令行
-v：表示需要将本地哪个目录挂载到容器中，格式-v<宿主机目录>：<容器目录>
假设我们的所有安装程序都放在了宿主机的/root/software/目录下，现在需要将其挂载到容器的/mnt/software/目录下。
这一切做好后你就可以为这个容器安装软件了。

使用以下命令即可启动容器，相关程序都在当前机器的~/Work/目录下，并且想把它挂载到容器的相同目录下

`docker run -i -t -v ~/Work/:/Work/ --privileged=true 00fd29ccc6f1`

使用进入已经运行的docker
`docker  attach  dabfb413d8cf[容器ID]`

## 安装JDK

配置环境变量

vi  ~/.bashrc
vi  /etc/profile
```
JAVA_HOME=/Work/Develop/jdk1.8.0_131

PATH=${PATH}:${JAVA_HOME}/bin
export JAVA_HOME PATH
```

source ~/.bashrc
source /etc/profile

## 退出容器

当以上步骤全部完成后，可使用exit命令，退出容器。
随后，可使用如下命令查看正在运行的容器：
docker ps

此时，您应该看不到任何正在运行的程序，因为刚才已经使用exit命令退出的容器，此时容器处于停止状态，可使用如下命令查看所有容器：
docker ps -a

输出如下内容：
```
CONTAINER ID        IMAGE               COMMAND             CREATED             STATUS                        PORTS               NAMES
81722f66a2f3        00fd29ccc6f1        "/bin/bash"         6 minutes ago       Exited (127) 19 seconds ago                       stupefied_leavitt
```
记住以上CONTAINER ID（容器 ID），随后我们将通过该容器，创建一个可运行 Java Web 的镜像。

##  创建 Java镜像

使用以下命令，根据某个“容器 ID”来创建一个新的“镜像”：
docker commit 81722f66a2f3 oliver/java:0.1
该容器的 ID 是“81722f66a2f3”，所创建的镜像名是“oliver/java:0.1”，随后可使用镜像来启动 Java Web 容器。

## 启动Java Web 容器

有必要首先使用docker images命令，查看当前所有的镜像：

REPOSITORY          TAG                 IMAGE ID            CREATED             SIZE
oliver/java         0.1                 f04d94101ae0        22 seconds ago      111MB

可见，此时已经看到了最新创建的镜像“oliver/java:0.1”，其镜像 ID 是“f04d94101ae0”。正如上面所描述的那样，我们可以通过“镜像名”或“镜像 ID”来启动容器，与上次启动容器不同的是，我们现在不再进入容器的命令行，而是直接启动容器内部的 Tomcat 服务。此时，需要使用以下命令：

docker run -d -p 58080:8080 --name java oliver/java:0.1
docker run -i -t --privileged=true oliver/java:0.1
稍作解释：

    -d：表示以“守护模式”执行/root/run.sh脚本，此时 Tomcat 控制台不会出现在输出终端上。
    -p：表示宿主机与容器的端口映射，此时将容器内部的 8080 端口映射为宿主机的 58080 端口，这样就向外界暴露了 58080 端口，可通过 Docker 网桥来访问容器内部的 8080 端口了。
    --name：表示容器名称，用一个有意义的名称命名即可。
