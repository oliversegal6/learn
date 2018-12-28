#### 步骤1. 设置开发环境
`npm install -g @angular/cli@6.0.0`
重新卸载安装

```
npm uninstall -g @angular/cli
npm cache clean --force
npm install -g @angular/cli@6.0.0

npm install typescript@2.6.2
```

如果npm 比较满可以使用 cnpm
npm install cnpm -g --registry=https://registry.npm.taobao.org

或加registry
npm i --save-dev @angular-devkit/build-angular --registry=https://registry.npm.taobao.org

以后所有的安装都用 cnpm install

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


####  启动开发服务器
进入项目目录，并启动服务器。
```
cd my-app
ng serve --open

ng build --prod 
http-server -c-1 ./dist/toBeBetter
```