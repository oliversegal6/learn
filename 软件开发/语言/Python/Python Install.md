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
```

3. 退出你刚才配置好的docker镜像
exit

4. 使用以下命令可以看到刚才退出的docker镜像
docker ps -a

5. 用以下命令，根据某个”容器ID”来创建一个新的”镜像”：
docker commit -m="Add pandas/lxml/bs4/matplotlib/tushare/pymongo/Django/flask" -a="oliver" ee71f0491df0 oliver/python:3.5a

该容器ID是”57c312bbaad1”，所创建的镜像名是”javaweb”
注意：”57c312bbaad1” 这个ID是使用 docker ps 命令来查看的
提交了新的镜像可以把这个镜像储存tar包
docker save -o  ~/python.tar  oliver/python
docker  save -o  保存的目录  镜像名


## Django

### 创建 Django 项目：
docker run -v /home/oliver/Work/Develop/python/:/usr/src/python  -w /usr/src/python oliver/python:3.5a django-admin.py startproject testdj

### 启动服务

cd testdj # 切换到我们创建的项目
docker run -p 8000:8000 -v /home/oliver/Work/Develop/python/testdj:/usr/src/python/testdj -w /usr/src/python/testdj oliver/python:3.5a python manage.py runserver 0.0.0.0:8000

### Create app
docker run -v /home/oliver/Work/Develop/python/testdj:/usr/src/python/testdj  -w /usr/src/python/testdj oliver/python:3.5a python manage.py startapp stockmining

### Migrate the default database

This is for the admin default setup

docker run -v /home/oliver/Work/Develop/python/testdj:/usr/src/python/testdj  -w /usr/src/python/testdj oliver/python:3.5a python manage.py migrate

### create superuser

python manage.py createsuperuser 

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