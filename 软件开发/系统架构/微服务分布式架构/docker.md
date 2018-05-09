# 使用 Docker 搭建 Java 开发环境

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