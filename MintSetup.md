### Setup ENV

```
JAVA_HOME=/home/oliversegal/Work/jdk1.8.0_151
GRADLE_HOME=/home/oliversegal/Work/gradle-4.2.1
MAVEN_HOME=/home/oliversegal/Work/apache-maven-3.5.2

PATH=${PATH}:${JAVA_HOME}/bin:${GRADLE_HOME}/bin:${MAVEN_HOME}/bin
export JAVA_HOME GRADLE_HOME MAVEN_HOME PATH
```

### Add Ali Miror
```
<mirror>
      <!--This sends everything else to /public -->
      <id>nexus</id>
      <mirrorOf>*</mirrorOf> 
      <url>http://maven.aliyun.com/nexus/content/groups/public/</url>
    </mirror>
    <mirror>
      <!--This is used to direct the public snapshots repo in the 
          profile below over to a different nexus group -->
      <id>nexus-public-snapshots</id>
      <mirrorOf>public-snapshots</mirrorOf> 
      <url>http://maven.aliyun.com/nexus/content/repositories/snapshots/</url>
    </mirror>
```

### POM 

#### <parent>
在子项目中，能够继承父项目的如下配置：

- dependencies
- developers
- contributors
- plugin lists
- reports lists
- plugin executions with matching ids
- plugin configuration
    
```    
      <parent>  
         <groupId>com.mycompany.jcat</groupId>  
         <artifactId>jcat-bundle</artifactId>  
         <version>2.0</version>  
         <relativePath>../jcat-bundle</relativePath>  
       </parent>  
```

类似于Java中的java.lang.Object类，所有Java类都继承自该类。在Maven中也存在一个特殊的POM，被称为Super POM。任何Maven项目的POM都继承自Super POM。

在Super POM中，设置如下：

- Maven的central库
- Maven的central插件库
- build的基本参数和4个插件（maven-antrun-plugin、maven-assembly-plugin、maven-dependency-plugin和maven-release-plugin）
- reporting的基本目录
- 一个profile（id=release-profile）

### dependencyManagement
dependencyManagement元素，既能让子模块继承到父模块的依赖配置，又能保证子模块依赖使用的灵活性。在dependencyManagement元素下得依赖声明不会引入实际的依赖，不过它能够约束dependencies下的依赖使用.
父POM使用dependencyManagement能够统一项目范围中依赖的版本，当依赖版本在父POM中声明后，子模块在使用依赖的时候就无须声明版本，也就不会发生多个子模块使用版本不一致的情况，帮助降低依赖冲突的几率。如果子模块不声明依赖的使用，即使该依赖在父POM中的dependencyManagement中声明了，也不会产生任何效果   

## Spring Cloud
spring-boot-dependencies
spring-cloud-dependencies



SpringCLoud中的“Discovery Service”有多种实现，比如：eureka, consul, zookeeper。

1，@EnableDiscoveryClient注解是基于spring-cloud-commons依赖，并且在classpath中实现；
2，@EnableEurekaClient注解是基于spring-cloud-netflix依赖，只能为eureka作用；

如果你的classpath中添加了eureka，则它们的作用是一样的。
