---
title: nginx
date: 2019-05-24 22:06:47
categories: 
- 软件开发
- 服务器
---

server {
        listen       6200;
        server_name  localhost;
        location / {
            root   /home/oliver/allbetter;
            # 这是angular生成的dist文件夹存放的位置
            index  index.html;
            access_log  /home/oliver/allbetter/proxy.access.log  main;
    try_files $uri $uri/ /index.html;
            # 注意此句，一定要加上。否则配置的子路由等无法使用
        }

        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }


启动服务：nginx -t
        nginx -c /etc/nginx/nginx.conf
停止服务：nginx -s stop
重新加载：nginx -s reload(配置文件被修改后需要执行它)


/var/log/nginx/access.log