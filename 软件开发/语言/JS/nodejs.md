### 安装nodejs

下载Nodejs https://nodejs.org

```
cd /home/oliversegal/Work/node-v8.9.0-linux-x64

sudo cp -r bin/* /usr/local/bin/
sudo cp -r lib/* /usr/local/lib/
sudo cp -r include/* /usr/local/include/

sudo ln -s /usr/local/bin/node /usr/bin/node
sudo ln -s /usr/local/bin/npm /usr/bin/npm

```
 #### 方法2
 sudo apt-get install nodejs
 sudo apt-get install npm

sudo apt-get remove nodejs
sudo apt-get remove npm

如果node不是最新的，node有一个模块叫n，是专门用来管理node.js的版本的。使用npm（NPM是随同nodejs一起安装的包管理工具）安装n模块


    $ sudo npm install -g n  

然后，升级node.js到最新稳定版


    $ sudo n stable  

旧版本的 npm，也可以很容易地通过 npm 命令来升级，命令如下：

$ sudo npm install npm -g

### 加快下载速度
#### (1)、关闭npm的https 
npm config set strict-ssl false

#### (2)、设置npm的获取地址 
npm config set registry “http://registry.npmjs.org/” 

#### (3)、设置npm获取的代理服务器地址： 
npm config set proxy=http://代理服务器ip:代理服务器端口 

清除npm的代理命令如下：

**npm config delete http-proxy 
 npm config delete https-proxy ** 
也可以单独为这次npm下载定义代理 
npm install -g pomelo –proxy http://代理服务器ip:代理服务器端口 
