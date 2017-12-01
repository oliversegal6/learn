


Java Web有很多成熟的框架，主要可以分为两类Web Application和Web Services。用于Web Application的框架包括官方的Servlet/JSP, JSTL/JSF以及第三方Struts/Spring MVC(action-based)。Web Services的项目又可以分为基于XML的（SOAP/WSDL）的和基于JSON的，Java Communitiy为这两种方式都定义了标准，Java EE5引入了JAX-WS(Java API for XML Web Services)-JSR224，Java EE6引入了JAX-RS(Java API for RESTful Web Services)-JSR331。RESTful Service由于轻量，好测试，有弹性等特点，越来越受欢迎。Jersey，RESTEasy都是JAX-RS标准的具体实现。

####RESTful架构

 Rest（representational state transfer, 表现层状态转化）是一种渐渐变成Web设计主流的设计理念，最早由Roy Thomas Fielding（HTTP1.0/1.1协议主要设计者之一，Apache作者之一，Apache基金会第一任主席）在2000年的博士论文中提出。

- 资源（Resource）：网络上一个实体（具体信息），每个资源都用一个URI来标识和定位。所有的资源都位于服务器中。
- 表现层（Representation）：资源的表现形式。例如文本信息可以用Txt展现，也可以用HTML，XML，JSON格式表现，甚至是二进制格式。URI只代表资源实体，它的表现形式在Http请求头中用Accept和Content-Type字段指定，这两个字段才是对表现层的描述。客户端见到的所有东西都只是服务器上资源的表现层，客户端和服务器之间传递的也都是表现层（资源请求携带的参数，返回的JSON，TXT，JPG等MIME-TYPE）。
- 状态转换（State Transfer）：客户端所有操作本质上就是用某种方法让服务器中的资源状态发生变化。客户端只能见到资源的表现层，所以服务器上资源状态的转换必然建立在表现层上。客户端让服务器资源发生状态变化的唯一方法就是使用HTTP请求，通过HTTP请求的不同方法（Method）实现对资源的不同的状态更改操作（如增删改查Create,Read,Update,Delete）。HTTP协议中设计的请求方法包括GET(获取)，POST(新增)，PUT(更新)，DELETE(删除)，HEAD，STATUS，OPTIONS等，不同方法代表了不同的操作，但是HTML只实现了GET和POST。
1. 对url进行规范，写RESTful格式的url

　　非REST的url：http://...../queryItems.action?id=001&type=T01
　　REST的url风格：http://..../items/001
　　特点：url简洁，将参数通过url传到服务端

2. http的方法规范

　　　a)不管是删除，添加，更新….使用的url都是一致，那么如果需要删除，就把http的方法设置删除
　　　b) 控制器：通过判断http的方法来执行操作（增删改查）
3. 对http的contentType规范

　　请求时指定contentType，要json数据，设置成json格式的type。

####JAX-RS
Java API for RESTful Web Services，Roy Fielding也参与了JAX-RS的制订，他在自己的博士论文中定义了REST。对于那些想要构建RESTful Web Services的开发者来说，JAX-RS给出了不同于JAX-WS（JSR-224）的另一种解决方案。目前共有4种JAX-RS实现，所有这些实现都支持Spring，Jersey则是JAX-RS的参考实现，也是本文所用的实现

JAX-RS和所有JAVA EE的技术一样，只提供了技术标准，允许各个厂家有自己的实现版本，实现版本有：RESTEasy(JBoss), Jersey(Sun提供的参考实现), Apache CXF, Restlet(最早的REST框架，先于JAX-RS出现), Apache Wink。JAX-RS基于JavaEE的Servlet。标准中定义的注解大大简化资源位置和参数的描述，仅仅使用注解就可以将一个POJO java类封装成一个Web资源。JAX-RS也有类似于Spring依赖注入的方式，减少类之间的耦合度。

```
@Path("/greeter")   
public class GreeterResource {
 @GET
 @Path("/{name}")
 public String sayHello(@PathParam("name") String name) {
     return "Hello, " + name;
 }
 @DELETE
 @Path("/{name}")
 public String sayBye(@PathParam("name") String name) {
     return "Bye, " + name;
 }
}
```

标注包括：
@Path，标注资源类或方法的相对路径
@GET，@PUT，@POST，@DELETE，标注方法是用的HTTP请求的类型
@Produces，标注返回的MIME媒体类型
@Consumes，标注可接受请求的MIME媒体类型
@PathParam，@QueryParam，@HeaderParam，@CookieParam，@MatrixParam，@FormParam,分别标注方法的参数来自于HTTP请求的不同位置，例如@PathParam来自于URL的路径，@QueryParam来自于URL的查询参数，@HeaderParam来自于HTTP请求的头信息，@CookieParam提取Cookie值，不仅可以注入基本类型，还可以注入Cookie对象。也可以用@Context注入HttpHeaders对象获取所有Cookie信息。。@FormParam提取Post请求中的Form参数，其中Content-Type被假设为application/x-www-formurlencoded


![RPC](./pic/rpc.jpg)

参考： 
体系化认识RPC 

Richardson Maturity Model

Spring MVC与JAX-RS比较与分析 - Spring MVC与JAX-RS比较与分析 

