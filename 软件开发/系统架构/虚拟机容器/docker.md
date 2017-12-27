#Setup Docker

下载Docker：https://www.docker.com

## Linux
要安装最新的 Docker 版本，首先需要安装 apt-transport-https 支持，之后通过添加源来安装。
```
$ sudo apt-get install apt-transport-https
$ sudo apt-key adv --keyserver hkp://keyserver.ubuntu.com:80 --recv-keys 36A1D7869245C8950F966E92D8576A8BA88D21E9
$ sudo bash -c "echo deb https://get.docker.io/ubuntu docker main > /etc/apt/sources.list.d/docker.list"
$ sudo apt-get update
$ sudo apt-get install lxc-docker

sudo apt-get install -y docker.io
sudo apt-get install docker.io
```

## Windows
Hyper-V要打开 
具体就是，首先要打开cpu虚拟化功能，这个在BIOS里面改，可能会不成功，回复初始设置之后再改应该就可以了。 
然后就是在 程序与功能>启用或关闭windows功能 中将Hyper-v打开就可以了。