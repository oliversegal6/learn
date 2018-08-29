# Maven

Apache Maven是一个软件项目管理和综合工具。基于项目对象模型（POM）的概念，Maven可以从一个中心资料片管理项目构建，报告和文件。

## Maven POM

POM代表项目对象模型。它是 Maven 中工作的基本单位，这是一个 XML 文件。它始终保存在该项目基本目录中的 pom.xml 文件。

POM 包含的项目是使用 Maven 来构建的，它用来包含各种配置信息。也包含了目标和插件。在执行任务或目标时，Maven 会使用当前目录中的 POM。它读取POM得到所需要的配置信息，然后执行目标。部分的配置可以在 POM 使用如下：

- project dependencies
- plugins
- goals
- build profiles
- project version
- developers
- mailing list

创建一个POM之前，应该要先决定项目组(groupId)，它的名字(artifactId)和版本，因为这些属性在项目仓库是唯一标识的

```maven
<project xmlns="http://maven.apache.org/POM/4.0.0"
   xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
   xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
   http://maven.apache.org/xsd/maven-4.0.0.xsd">
   <modelVersion>4.0.0</modelVersion>
   <groupId>com.yiibai.project-group</groupId>
   <artifactId>project</artifactId>
   <version>1.0</version>
<project>
```

## Maven资源库

在 Maven 术语里存储库是一个目录，即目录中保存所有项目的 jar 库，插件或任何其他项目特定文件，并可以容易由 Maven 使用。

Maven库中有三种类型

- local - 本地库: Maven 本地存储库是一个在本地计算机上的一个文件夹位置。当你第一次运行 maven 命令的时候它就被创建了，当运行 Maven 构建，那么 Maven 会自动下载所有依赖的jar到本地存储库中。
- central - 中央库： Maven中央存储库是由Maven社区提供的资源库，当Maven没有在本地存储库找到任何依赖，就会开始搜索在中央存储库
- remote - 远程库: Maven不能从依赖中央存储库找到上述库，那么它停下构建过程并输出错误消息到控制台。为了防止这种情况，Maven提供远程仓库概念，这是开发商的自定义库包含所需的库文件或其他项目 jar 文件

```
<repositories>
      <repository>
         <id>companyname.lib1</id>
         <url>http://download.companyname.org/maven2/lib1</url>
      </repository>
      <repository>
         <id>companyname.lib2</id>
         <url>http://download.companyname.org/maven2/lib2</url>
      </repository>
   </repositories>
```

### Maven 依赖搜索序列

执行 Maven 构建命令，Maven 依赖库按以下顺序进行搜索：

- 第1步 - 搜索依赖本地资源库，如果没有找到，跳到第2步，否则，如果找到那么会做进一步处理。
- 第2步 - 搜索依赖中央存储库，如果没有找到，则从远程资源库/存储库中，然后移动到步骤4，否则如果找到，那么它下载到本地存储库中，以备将来参考使用。
- 第3步 - 如果没有提到远程仓库，Maven 则会停止处理并抛出错误（找不到依赖库）。
- 第4步 - 远程仓库或储存库中的搜索依赖，如果找到它会下载到本地资源库以供将来参考使用，否则 Maven 停止处理并抛出错误（找不到依赖库）。

如果dependencies里的dependency自己没有声明version元素，那么maven就会倒dependencyManagement里面去找有没有对该artifactId和groupId进行过版本声明，如果有，就继承它，如果没有就会报错，告诉你必须为dependency声明一个version

```
<dependencyManagement>  
      <dependencies>  
            <dependency>  
                <groupId>org.springframework</groupId>  
                <artifactId>spring-core</artifactId>  
                <version>3.2.7</version>  
            </dependency>  
    </dependencies>  
</dependencyManagement>  
  
//会实际下载jar包  
<dependencies>  
       <dependency>  
                <groupId>org.springframework</groupId>  
                <artifactId>spring-core</artifactId>  
       </dependency>  
</dependencies>  
```

## Maven 的插件

Maven 是一个执行插件的框架，每一个任务都是由插件完成的。Maven 插件通常用于：

- 创建 jar 文件
- 创建 war 文件
- 编译代码文件
- 进行代码单元测试
- 创建项目文档
- 创建项目报告

插件执行语法：
`mvn [plugin-name]:[goal-name]`
Java项目编译
`mvn compiler:compile`

### 插件类型

两种类型插件

- 构建插件: 在生成过程中执行，并在 pom.xml 中的<build/> 元素进行配置
- 报告插件: 在网站生成期间执行，在 pom.xml 中的 <reporting/> 元素进行配置

常见的插件有：

- clean: 编译后的清理目标，删除目标目录
- compiler: 编译 Java 源文件
- surefile: 运行JUnit单元测试，创建测试报告
- jar: 从当前项目构建 JAR 文件
- war: 从当前项目构建 WAR 文件
- javadoc: 产生用于该项目的 Javadoc
- antrun: 从构建所述的任何阶段运行一组 Ant 任务

