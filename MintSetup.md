### Setup ENV

安装 JDK
```
    sudo add-apt-repository ppa:webupd8team/java  
    sudo apt-get update  
    sudo apt-get install oracle-java8-installer  
    sudo apt-get install oracle-java8-set-default  
```

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

类似于Java中的java.lang.Object类，所有Java类都继承自该类。在Maven中也存在一个特殊的POM，被称为Super POM。任何Maven项目的POM都继承自Super POM

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
- spring-boot-dependencies：包含所有boot依赖包的版本号，方便管理版本
- spring-cloud-dependencies：包含所有cloud依赖包的版本号，方便管理版本

bootstrap.yml里面的配置 —>链接config server 加载远程配置 —>加载application.yml里的配置,所以我们要使用bootstrap.yml
本地同名配置会被远程的覆盖

spring官方建议我们在bootstrap中放置不更改的属性.

bootstrap.yml文件中的内容不能放到application.yml中，否则config部分无法被加载，因为config部分的配置先于application.yml被加载，而bootstrap.yml中的配置会先于application.yml加载，

Bootstrap.yml（bootstrap.properties）在application.yml（application.properties）之前加载，就像application.yml一样，但是用于应用程序上下文的引导阶段。它通常用于“使用Spring Cloud Config Server时，应在bootstrap.yml中指定spring.application.name和spring.cloud.config.server.git.uri”以及一些加密/解密信息。技术上，bootstrap.yml由父Spring ApplicationContext加载。父ApplicationContext被加载到使用application.yml的之前。

### Discovery Server
SpringCLoud中的“Discovery Service”有多种实现，比如：eureka, consul, zookeeper。

1，@EnableDiscoveryClient注解是基于spring-cloud-commons依赖，并且在classpath中实现；
2，@EnableEurekaClient注解是基于spring-cloud-netflix依赖，只能为eureka作用；

如果你的classpath中添加了eureka，则它们的作用是一样的。

### Config Server

获取git上的资源信息遵循如下规则
/{application}/{profile}[/{label}]
/{application}-{profile}.yml
/{label}/{application}-{profile}.yml
/{application}-{profile}.properties
/{label}/{application}-{profile}.properties

### Swagger

API接口缺乏维护和管理
- 文档需要更新的时候，需要再次发送一份给前端，也就是文档更新交流不及时。
- 接口返回结果不明确
- 不能直接在线测试接口，通常需要使用工具，比如postman
- 接口文档太多，不好管理

#### 依赖
```
<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger2</artifactId>
	<version>2.7.0</version>
</dependency>

<dependency>
	<groupId>io.springfox</groupId>
	<artifactId>springfox-swagger-ui</artifactId>
	<version>2.7.0</version>
</dependency>
```
#### Swagger配置类

```
@Configuration
public class Swagger2 {

    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.xyz.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Sample Project API")
                .description("简单优雅的restfun风格")
                .version("1.0")
                .build();
    }
}
```
#### Controller示例

```
@RestController
public class SampleController {

    @GetMapping("/service")
    @ApiOperation(value="getUserById", notes="根据id的id来获取用户详细信息")
    @ApiImplicitParam(name = "id", value = "用户ID", required = true, dataType = "Integer", paramType = "path")
    public String getUserById(String id){
        return "service" + id;
    }
}
```

#### Swagger注解

swagger通过注解表明该接口会生成文档，包括接口名、请求方法、参数、返回信息的等等。

- @Api：修饰整个类，描述Controller的作用
- @ApiOperation：描述一个类的一个方法，或者说一个接口
- @ApiParam：单个参数描述
- @ApiModel：用对象来接收参数
- @ApiProperty：用对象接收参数时，描述对象的一个字段
- @ApiResponse：HTTP响应其中1个描述
- @ApiResponses：HTTP响应整体描述
- @ApiIgnore：使用该注解忽略这个API
- @ApiError ：发生错误返回的信息
- @ApiImplicitParam：一个请求参数
- @ApiImplicitParams：多个请求参数