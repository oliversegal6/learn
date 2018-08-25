# 项目介绍

MongoDB, Flask Service在Quant项目中已经创建好。


## 启动

### MongoDB 

运行MongoDB docker容器
docker run -p 27017:27017 -v /home/oliver/Work/Develop/mongodb/db:/data/db -d mongo:3.6

### Flask Restful Service 

docker run -p 5000:5000 -v /home/oliver/Work/github/stockQuant:/usr/src/stockmining  -w /usr/src/stockmining oliver/python:3.5a python flaskMain.py 0.0.0.0:5000