### 安装最新版node和npm

npm 命令是报错：
Error: CERT_UNTRUSTED

**解决办法**
只需要关掉HTTPS就好了！只需要执行如下命令：

npm config set strict-ssl false
或者
npm config set registry=”http://registry.npmjs.org/”


1.安装python-software-properties

sudo apt-get install python-software-properties

2.添加ppa

curl -sL https://deb.nodesource.com/setup_8.x | sudo -E bash -

3.安装nodejs和npm

sudo apt-get install nodejs

4.查看版本

$ node -v
v8.11.1
$ npm -v
5.6.0

5.配置npm仓库

sudo npm install -g nrm

$ nrm ls

* npm ---- https://registry.npmjs.org/
  cnpm --- http://r.cnpmjs.org/
  taobao - https://registry.npm.taobao.org/
  nj ----- https://registry.nodejitsu.com/
  rednpm - http://registry.mirror.cqupt.edu.cn/
  npmMirror  https://skimdb.npmjs.com/registry/
  edunpm - http://registry.enpmjs.org/

$ nrm use taobao
Registry has been set to: https://registry.npm.taobao.org/

5、查看版本

```shell
sudo node -v
sudo npm -v
```

#### 步骤1. 设置开发环境


`npm install -g @angular/cli@6.0.0`
重新卸载安装

```
npm uninstall -g @angular/cli
npm cache clean --force
npm install -g @angular/cli@6.0.0

npm install typescript@2.6.2
```

如果下载速度太慢，使用阿里巴巴在国内的镜像服务器
npm config set registry http://registry.npm.taobao.org

#### 步骤2. 创建新项目
运行下列命令来生成一个新项目以及应用的骨架代码：
`ng new lifeblog`

"styles": [
              "src/styles.css",
              "assets/editor.md/css/editormd.css"
            ],
            "scripts": ["../node_modules/jquery/dist/jquery.js",
              "assets/editor.md/editormd.min.js"]
          },

#### Create new Component

ng generate component docTree

#### Create new Service

ng generate service hero

#### 添加PWA支持

ng add @angular/pwa

@angular/cli内置的Server在–prod 编译的时候还不支持service-worker，所以上面装了一个第三方的http-server

npm install http-server --global

#### 添加ng-zorro

Ant Design Angular

`ng add ng-zorro-antd`


#### 添加日期框架date-fns

npm install date-fns --save

####  启动开发服务器
进入项目目录，并启动服务器。
```
cd my-app
ng serve --open

ng build --prod 
http-server -c-1 ./dist/toBeBetter
```

#### 使用nginx

增加虚拟服务器
vi /etc/nginx/nginx.conf
server {
        listen       5200;
        server_name  localhost;
        location / {
            root   /home/oliver/stockmining; // 这是angular生成的dist文件夹存放的位置
            index  index.html;
    try_files $uri $uri/ /index.html; // 注意此句，一定要加上。否则配置的子路由等无法使用
        }
 
        error_page   500 502 503 504  /50x.html;
        location = /50x.html {
            root   html;
        }
    }

修改默认nginx端口号： vi /etc/nginx/sites-enabled/default


start nginx : 启动nginx
nginx -s reload  ：修改配置后重新加载生效
nginx -s reopen  ：重新打开日志文件
nginx -t -c /path/to/nginx.conf 测试nginx配置文件是否正确，也可以直接使用 nginx -t
 
关闭nginx：
nginx -s stop  :快速停止nginx
