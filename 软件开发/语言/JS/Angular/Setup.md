#### 步骤1. 设置开发环境
`npm install -g @angular/cli@6.0.0`

重新卸载安装

```
npm uninstall -g @angular/cli
npm cache clean --force
npm install -g @angular/cli@6.0.0

npm install typescript@2.6.2
```

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

####  启动开发服务器
进入项目目录，并启动服务器。
```
cd my-app
ng serve --open
```