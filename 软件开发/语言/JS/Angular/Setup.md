#### 步骤1. 设置开发环境
`npm install -g @angular/cli@1.4.5`

重新卸载安装

```
npm uninstall -g @angular/cli
npm cache clean
npm install -g @angular/cli@latest

npm install --save-dev @angular/cli@latest
```

#### 步骤2. 创建新项目
运行下列命令来生成一个新项目以及应用的骨架代码：
`ng new markdown-angular5`

#### 步骤3. 启动开发服务器
进入项目目录，并启动服务器。
```
cd my-app
ng serve --open
```