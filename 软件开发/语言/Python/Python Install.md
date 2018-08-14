## 安装 python

### remove python

1、卸载python3.6

sudo apt-get remove python3.6

2、卸载python3.6及其依赖

sudo apt-get remove --auto-remove python3.6

3、清除python3.6

sudo apt-get purge python3.6
or
sudo apt-get purge --auto-remove python3.6

### Install python

sudo apt-get install python2.7 python2.7-dev

sudo apt-get install python3.6 python3.6-dev

### 安装build依赖包
sudo apt-get install build-essential libssl-dev libevent-dev libjpeg-dev libxml2-dev libxslt-dev

### 安装pip

pip是Python的包管理工具，建议Python的所有包都用pip进行管理，命令如下：

sudo apt-get install python-pip
sudo apt-get install python3-pip

## docker下安装python

1. 启动python镜像容器
docker run -t -i oliver/python:3.5a /bin/bash

2. 安装对应库
```
    $ pip install pandas 
    $ pip install lxml 
    $ pip install bs4
    $ pip install matplotlib
    $ pip install requests
    $ pip install tushare 
    $ pip install pymongo
    $ pip install Django
    $ pip install flask 

    pip install sklearn
    pip install tensorflow
    pip install keras
    pip install corenlp
```

3. 退出你刚才配置好的docker镜像
exit

4. 使用以下命令可以看到刚才退出的docker镜像
docker ps -a

5. 用以下命令，根据某个”容器ID”来创建一个新的”镜像”：
docker commit -m="Add pandas/lxml/bs4/matplotlib/tushare/pymongo/Django/flask" -a="oliver" be16caa12af3 oliver/python:3.5a

该容器ID是”57c312bbaad1”，所创建的镜像名是”javaweb”
注意：”57c312bbaad1” 这个ID是使用 docker ps 命令来查看的
提交了新的镜像可以把这个镜像储存tar包
docker save -o  ~/python.tar  oliver/python
docker  save -o  保存的目录  镜像名


## Flask

### Flask

```
 from flask import Flask 
 app = Flask(__name__) 
 
 @app.route('/') 
 
 def hello_world(): 
    return 'Hello World!' 
    
if __name__ == '__main__': 
    app.run(host="0.0.0.0", port=5000)
```

host一定不要用默认的"127.0.0.1"，不然容器启动，即使映射了端口，在浏览器中也仍然是无法访问服务的。
将host设置为"0.0.0.0"，这样Flask容器可以接受到宿主的请求。

#### 启动示例

docker run -p 5000:5000 -v /home/oliver/Work/github/stockQuant:/usr/src/stockmining  -w /usr/src/stockmining oliver/python:3.5a python flaskMain.py 0.0.0.0:5000