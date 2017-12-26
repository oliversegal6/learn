#Setup Docker

下载Docker：https://www.docker.com

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
