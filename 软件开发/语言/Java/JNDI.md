Java命名和目录接口（the Java Naming and Directory Interface，JNDI）是一组在Java应用中访问命名和目录服务的API。命名服务将名称和对象联系起来，使得读者可以用名称访问对象。目录服务是一种命名服务，在这种服务里，对象不但有名称，还有属性。
命名或目录服务使读者可以集中存储共有信息，这一点在网络应用中是非常重要的，因为这使得这样的应用更协调、更容易管理。例如，可以将打印机设置存储在目录服务中，以便被与打印机有关的应用使用。

## 1  什么是JNDI

在一个企业中，命名服务为读者的应用程序在网络上定位对象提供了一种方法。一个命名服务将对象和名称联系在了一起，并且可以通过它们指定的名称找到相应的对象。
JNDI是Java命名和目录接口，是一个为Java应用程序提供命名服务的应用程序编程接口（API）。它为开发人员提供了查找和访问各种命名和目录服务的通用、统一的接口，类似于JDBC都是构建在抽象层上。要使用JNDI，必须要安装jdk 1.3以上版本。
JNDI包含了大量的命名和目录服务，它使用通用接口来访问不同种类的服务，可以同时连接到多个命名或目录服务上并建立起逻辑关联。

### 1.1  命名服务

命名服务是一种服务，它提供了为给定的数据集创建一个标准名字的能力。它允许把名称同Java对象或资源关联起来，而不必指出对象或资源的物理ID。这类似于字典结构（或者是Java的map结构），该结构中键映射到值。例如在Internet上的域名服务（Domain Naming Service，DNS）就是提供将域名映射到IP地址的命名服务，在打开网站时一般都是在浏览器中输入名字，通过DNS找到相应的IP地址，然后打开。

所有的因特网通信都使用TCP、UDP或IP协议。IP地址由4个字节32位二进制数字组成，数字和名字相比，对于人来说名字比数字要容易记忆，但对于计算机来讲，它更善于处理数字。
其实所有的命名服务都提供DNS这种基本功能，即一个系统向命名服务注册，命名服务提供一个值到另一个值的映射。然后，另外一个系统访问命名服务就可以取得映射信息。这种交互关系对分布式企业级应用来讲显得非常重要。
在Java中，基本的名字操作包含在Context接口中。

### 1.2  目录服务

目录服务是一种特殊类型的数据库，与SQL Server、Access、Oracle等 关系数据库管理系统相反，构造目录服务的目的是为了处理基于行为的事务，并且使用一种关系信息模型。目录服务将命名服务的概念进一步引申为提供具有层次结 构的信息库，这一信息库除了包含一对一的关系外，还有信息的层次结构。对目录服务而言，这种层次结构通常用于优化搜索操作，并且也可以按实际情况进行分布 或者跨网络复制。

一个目录服务通常拥有一个名字服务（但是一个名字服务不必具有一个目录服务）。如电话簿就是一个典型的目录服务，一般先在电话簿里找到相关的人名，再找到这个人的电话号码。
每一种目录服务都可以存储有关用户名、用户密码、用户组（如有关访问控制的信息）、以太网地址、IP地址等信息。它所支持的信息和操作会因为所使用的目录服务的不同而不同。遗憾的是，访问不同目录服务的协议也会不同，所以读者需要了解多种API。

这就是JNDI的起源，就像JDBC一样，JNDI充当不同名称和目录服务的通用API或者说是前端，然后使用不同的后端适配器来连接实际服务。如图1显示了JNDI和LDAP如何共同合作，为客户提供一种完美的解决方案。
 
在这里，使用JNDI完成与LDAP服务器之间的通信，对开发者来说他们只担心一个特殊协议（LDAP）和一个API（JNDI），而由开发商给他们自己的各个协议提供LDAP接口。事实上对这些流行的目录服务中来说，都有产品可让开发者通过LDAP与这些目录服务通信。

JNDI是J2EE技术中的一个完整的组件。它支持通过一个单一的方法访问不同的、新的和已经存在的服务的方法。这种支持允许任何服务提供商执行通过标准服务提供商接口（SPI）协定插入JNDI框架。另外，JNDI允许Weblogic服务器上的Java应用程序通过插入适当的服务提供者来访问像LDAP这样的标准化方式的外部目录服务。
在Java中，基本的目录服务操作包含在DirContext接口中。

### 1.3  LDAP的介绍
轻量目录访问协议（lightweight directory access protocol，LDAP）是在20世纪90年代早期作为标准目录协议进行开发的。它是目前最流行的目录协议，与厂商跟平台无关。
LDAP可以追溯到X.500协议，而X.500协议最初是基于OSI网络协议发展起来的。LDAP的第3版协议是在RFC2251中定义的，并且已经非常成熟，它的最新补充部分包含LDAP的XML规范，称为目录服务标记语言。

Java语言通过使用LDAP API，如Netscape Directory 服务器可以直接使用LDAP，或者通过JNDI来使用LDAP。JNDI是J2SE中的标准API，是通用的API，不必绑定到LDAP。
LDAP定义客户应当如何访问服务器中的数据，它并不指定数据应当如何存储在服务器上。大多数情况下，开发者只需要和一个专为LDAP设计的目录服务，或现有目录服务的LDAP前端打交道。LDAP能够成为任何数据存储类型的前端。目前最流行的目录服务有NIS、NDS、Active Directory等都有某种类型的LDAP前端。

LDAP和关系数据库是两种不同层次的概念，后者是存储方式（同一层次如网格数据库，对象数据库），前者是存储模式和访问协议。LDAP是一个比关系数据库抽象层次更高的存储概念，与关系数据库的查询语言SQL属于同一级别。LDAP最基本的形式是一个连接数据库的标准方式，该数据库为读查询作了优化。因此它可以很快地得到查询结果，不过在其他方面，例如更新操作等就慢得多。

从另一个意义上来讲，LDAP是实现了指定的数据结构的存储，它是一种特殊的数据库。但是LDAP和一般的数据库不同，明白这一点是很重要的。LDAP对查询进行了优化，与写性能相比，LDAP的读性能要优秀很多。LDAP服务器也是用来处理查询和更新LDAP目录的。换句话说，LDAP目录也是一种类型的数据库，但不是关系型数据库。要特别注意的是，LDAP通常作为一个hierarchal数据库使用，而不是一个关系数据库。

#### 1. LDAP数据
在LDAP中，数据被组织成一棵树的形式，叫做目录信息树（directory information tree，DIT）。DIT中的第一个“叶子”叫做一个条目（entry），第一个条目叫根条目（root entry）。
一个条目是由一个区分名称DN（distinguished name）和任意一个属性/值对组成。DN是一个条目的名字，它必须是唯一的，它类似于一个关系型数据库的唯一关键字。DN也表明了该条目与DIT树的其他部分之间的关系，它类似于这种方式：一个文件的全路径名表明硬盘上的一个特定文件与系统中的其他文件之间的关系。当从根目录读取文件时，读取系统上的文件路径是从左到右读取的，但是当从根目录读取DN时，是从右到左读DN的。如：
`uid = jordan,ou = nba,o = american`

表示定义了在组织american中的小组为nba的用户jordan的用户。其中一个DN名的最左边部分叫相对区分名称RDN（relative distinguished name），它由一个条目内的属性/值组成，如前面的uid = jordan是RDN，后面的可有可无。

LDAP通常使用简写形式的助记符表示其名称，常用的LDAP属性及其定义如表1所示。
LDAP属性	定义
```
o	Organization：组织
ou	Organization unit：组织单元
uid	Userid：用户id
cn	Common name：常见名称
sn	姓
givenname	首名
dn	Distinguished Name：区分名称
mail	E-mail Address：电子邮件地址
```
其中一个属性可以有一个或多个值，如一个用户可以有多个mail。

#### 2．LDAP的功能

到目前为止，我们已经介绍了LDAP行程注册部分，其实LDAP中还包括很多其他的用途，总的来说LDAP的功能包括远程服务的注册、访问控制、黄页服务器和配置数据。

（1）远程服务器注册
这是本章中使用LDAP时所涉及的内容，前面已经谈到过该项功能。LDAP允许远程服务器注册其可用性，然后允许客户机获得该注册信息，并且可使用该服务器。在某些情况下（如RMI），这可能意味着将可序列化的对象保存在LDAP中，并且在此后可以检索这些已保存的对象。这类似于rmiregistry的使用方法，但它能搜索LDAP，而rmiregistry却不能，并且当停止并重新启动rmiregistry后，原来注册的对象无效，必须重新注册所有对象。

（2）访问控制
许多企业应用程序都会对访问它们服务的用户进行控制。这可能像Web应用登录页面那样简单，也可能像使用数字签名那样复杂。不论是使用哪种方法，那些待检查的信息总需要保存到某个地方，有一种选择是将这些信息保存到LDAP内。例如，可以将用户保存在LDAP中。

（3）黄页服务器
黄页服务器的作用类似于电话目录，因此称为黄页。它提供信息搜索功能，可以使LDAP根据条目的属性内容进行信息搜索。如对于用户姓名、计算机和打印机等，均可以使用LDAP存储公司内所有员工的这些信息，然后根据名字为某个特定名字的用户以及检索他们的用户ID搜索。

（4）配置数据
LDAP可以充当配置信息的资源中心，这些配置信息在应用程序运行时使用。这类似于在Java应用程序中使用属性文件，但是这并不仅仅是每一个应用程序的属性文件，而是作为一个可用来访问并且还必须进行维护的单一的信息库中心。如一些特定应用程序使用的共享数据库名称可以保存到LDAP中。因此，如果数据库名称发生变更时，对于所有需要迁移到新数据库的应用程序来讲仅仅需要维护一个地方。

#### 3．使用LDAP
为了使用LDAP，需要完成以下４个步骤。
（1）连接到LDAP服务器
要使用LDAP，必须首先获取一个到LDAP的连接。为了完成LDAP的连接，需要知道运行LDAP的主机和要连接的端口，这有点类似于将电话线插到墙壁的电话孔后才能够与别人打电话。

（2）绑定到LDAP服务器
对于LDAP，通常至少有两种方法来绑定（登录）到LDAP服务器，匿名（LDAP3以上版本）或者作为特定用户。绑定有些类似于使用电话公司设置的电话线拨打朋友的电话。匿名登录仅仅能够访问到一些公共数据。如果应用程序作为特定用户登录时，就可以访问公共数据和特定为该用户设置的可访问的所有数据。而为某个用户设置可访问的数据是由LDAP服务器的访问控制列表（access control list，ACL）决定。ACL控制哪个用户能够读、写或者修改该ACL所关联的任何数据。一个条目内的不同属性可以与多个不同的ACL相关联。因此，不同的用户就能够看到不同的数据项，除此之外，某些特定的属性可以与多个ACL相关联，如一个ACL可以向一个用户组提供读的权限，但是另一个ACL可以向另一个用户组提供写的权限。

（3）在LDAP服务器上执行所需的任何操作
这些操作主要包括搜索服务器、增加新条目、修改条目、删除条目等。

（4）释放LDAP服务器的连接
当应用程序连接到LDAP并完成其相关工作后，该应用程序应该关闭连接以释放系统资源。

#### 2  使用JNDI
##### 2.1  JNDI服务提供者
不使用服务提供者就不能用JNDI。一个服务提供者就是一组Java类的集合，它支持开发者同目录服务进行通信，其方式类似于JDBC驱动程序与数据库之间的通信方式。能够用于JNDI的服务提供者必须实现Context接口或Context的扩展接口Directory- Context。
在使用JNDI时，读者只需要了解JNDI，而服务提供者才关注实际的网络协议、编码/解码值等细节。

当下载SDK软件开发包时，同时就下载了Sun公司的一些现有的服务提供者。这些服务提供者包括LDAP、NIS、COS（CORBA对象服务）、RMI注册及文件系统的提供者。如：hashtableObj.put(Context.INITIAL_CONTEXT_FACTORY，“com.sun.jndi.ldap.ldapCtx- Fatory” )就是表示使用Sun LDAP服务提供者。当然如果要使用IBM服务提供者时就可以用com.ibm.jndi.LDAPCtxFatory来代替com.sun.jndi.ldap.ldapCtxFatory。

##### 2.2  JNDI的包
JNDI中包括5个包。
-  javax.naming：主要用于命名操作，它包含了命名服务的类和接口，该包定义了Context接口和InitialContext类；
-   javax.naming.directory：主要用于目录操作，它定义了DirContext接口和InitialDir- Context类；
-   javax.naming.event：在命名目录服务器中请求事件通知；
-   javax.naming.ldap：提供LDAP支持；
-   javax.naming.spi：允许动态插入不同实现，为不同命名目录服务供应商的开发人员提供开发和实现的途径，以便应用程序通过JNDI可以访问相关服务。

##### 2.3  常用的JNDI操作

常用的JNDI操作如下：
- void bind(String sName，Object object)，绑定：把名称同对象关联的过程。
-  void rebind(String sName，Object object)，重新绑定：用来把对象同一个已经存在的名称重新绑定。一般使用rebind()而不使用bind()，因为当有重名的时候rebind()不会出现异常，而bind()会报异常。
- void unbind(String sName)，释放：用来把对象从目录中释放出来。
- void lookup(String sName，Object object)，查找：返回目录总的一个对象。
- void rename(String sOldName，String sNewName)，重命名：用来修改对象名称绑定的名称。
- NamingEnumeration listBindings(String sName)，清单：返回绑定在特定上下文中指定属性名对象的清单列表，它返回名字、类和对象本身，它用于那些需要对对象进行实际操作的应用。具体使用如下：
```
//得到初始目录环境的一个引用
Context cntxt = new InitialContext();
//返回绑定在特定上下文中指定属性名对象的清单列表
NamingEnumeration namEnumList = ctxt.listBinding(”cntxtName”);
//循环列出所有名字、类和对象
while ( namEnumList.hasMore() )  {
    Binding bnd = (Binding) namEnumList.next();
    String sObjName = bnd.getName();
    String sClassName = bnd.getClassName();
    //得到对象
    SomeObject objLocal = (SomeObject) bnd.getObject();
}
``` 
- NamingEnumeration list(String sName)与listBindings(String sName)相似，只是它只返回一系列名字/类映射，它主要是用于上下文浏览应用。

##### 2.4  JNDI操作步骤
使用JNDI来访问命名服务或者目录服务，操作步骤如下：
1. 建立一个散列表（hashtable），它包含定义所希望使用的JNDI服务的属性，所希望连接的LDAP服务器IP地址以及工作的端口。
2. 将与认证成为用户登录有关的任何信息添加到散列表中。
3. 创建初始context对象。如果访问命名服务，则使用InitialContext类，如果访问目录服务，则要使用InitialDirContext类。
4. 使用刚才得到的context对象执行所需的操作（如添加新的条目或者搜索条目）。
5. 完成操作后关闭context对象。

##### 2.5  JNDI允许存储的对象类型

JNDI最大的功能是能使用LDAP来存储需要在不同应用之间共享或者留做备用的对象。JNDI允许将下面几种与Java相关的对象类型存储到LDAP服务器内。
1. 串行化的Java对象。这是存储和取回已经串行化的Java对象的能力。也就是说要存储的Java对象必须要实现Referenceable或Serializable接口类，否则该对象不能存储。
2. 标准的LDAP目录条目。它提供了操作标准目录数据的能力。标准目录数据的数据量比较小，可以在不同的语言之间共享它们。保持目录数据与编程语言的无关性对于要使用几种不同语言进行开发的大企业里是非常重要的。
3. 指向RMI Java对象的指针。RMI是用于分布式计算的，通过RMI，一个Java应用可以像本地一样调用一个远程类的方法。我们可以把一个可用的RMI类的引用存储在开发者的LDAP服务器中，而不必在每个装有RMI客户应用的计算机上都保持可用方法的注册。

##### 2.6  JNDI存储查询串行化的Java对象

JNDI的主要目标是在网络上读/写Java对象。下面用具体实例来了解怎么使用JNDI。首先通过一个例子来讲解怎么样在LDAP中保存串行化的Java对象数据，再用一个例子来说明怎么对保存的对象数据进行查询、调用。
###### 1．保存数据
在LDAP中保存数据就是在LDAP服务器中添加使用条目，也就是把条目绑定在服务器中。下面先建立一个基本类，再在另一个类中利用JNDI把这个基本类绑定在服务器中。
例1：  在LDAP中保存数据。
（1）待绑定的基本类
```java
package jndi;
import java.io.serializable;
public class persons  implements Serializable {
    String Name = “”;
    String Age =”"  ;
    public persons () {
    }
    //构造函数,用于给变量赋值
    public persons (String namePara,String age) {
        Name = namePara;
        Age = age;
    }
    //用于返回变量Name的值
    public String getName() {
        return Name;
    }
    //用于返回变量Age的值
    public String getAge () {
    return  Age;
    }
}
```
JNDI定义了一个Serializable接口类来为应用信息的表达提供一种统一的方式。Serializable接口类包含了诸如地址、类型信息等用于访问具体对象的信息。为了能将对象的引用绑定到目录树中，该对象的类必须实现Referenceable接口，其中包含了方法 getReference()。开发者可以在该对象上调用getReference()方法来获得Reference以用于绑定。Serializable接口与Referenceable接口有颇多相似之处，不同在于Referenceable可引用的对象只包含一些用于创建实际对象的信息，而Serializable会包含更多的甚至不适合存储在目录结构中的信息。
（2）绑定保存对象程序
```java
package jndi;
import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;    
import javax.naming.directory.*;
public class ldapDataBind {
   public static void main(String[]args){
       //创建Hashtable以存储JNDI将用于连接目录服务的环境变量
        Hashtable hs = new Hashtable();
        //设置连接LDAP的实现工厂
        hs.put(Context.INITIAL_CONTEXT_FACTORY, 
                      “com.sun.jndi.ldap.LdapCtxFactory”);
        // 指定LDAP服务器的主机名和端口号
        hs.put(Context.PROVIDER_URL, “ldap://localhost:389 “);
        //给环境提供认证方法,有SIMPLE、SSL/TLS和SASL
        hs.put(Context.SECURITY_AUTHENTICATION, “simple”);
        //指定进入的目录识别名DN
        hs.put(Context.SECURITY_PRINCIPAL, “cn=Directory Manager”);
        //进入的目录密码
        hs.put(Context.SECURITY_CREDENTIALS, “password”);
        try {
           // 得到初始目录环境的一个引用
           DirContext ctx = new InitialDirContext(hs);
           // 新建一个对象
           persons perObj = new persons(”jordan”,”40″);
           //绑定对象
           ctx.rebind (”uid = Jordan,ou = Bull,o = NBA “,perObj);
           System.out.println(”bind object object success ” );
             /*实例化一个属性集合*/
             Attributes  attrs =  new BasicAttributes(true);
             /*建立一个属性,其属性名为“mail”*/
            Attribute  personMail  = new BasicAttribute(”mail”);
            //设置属性“mail”的值为“xie@163.com”、“liu@sina.com.cn”、
                 ”xyh@powerise.com.cn”
            personMail.add(”xie@163.com”);
            personMail.add(”liu@sina.com.cn”);
            personMail.add(”xyh@powerise.com.cn”);
             attrs.put(personMail);
             /*建立一个属性,其属性名为“uid”,值为001*/
            attrs.put(”uid”,”001″);
            /*建立一个属性,其属性名为“cn”,值为jordan1*/
            attrs.put(”cn”,”jordan1″);
            /*建立一个属性,其属性名为“sn”,值为NBA */
            attrs.put(”sn”,”NBA”);
            /*建立一个属性,其属性名为“ou”,值为bull */
            attrs.put(”ou”,”bull”);
            System.out.println(”bind object object success ” );
            /* 在识别名为DN的目录中增加一个条目*/
            ctx.createSubcontext(”uid = Jordan, ou = Wizzard,o=NBA”,attrs);
           //关闭初始目录环境
           ctx.close();
        } catch (NamingException ex) {
           System.err.println(”bind object fail: ” + ex.toString());
        }  
   }
}
```
###### 2．使用JNDI查找数据
前面已经介绍了怎么样将对象数据绑定到服务器，现在开始介绍如何取得调用绑定在服务器上的对象数据。
例2：  使用JNDI查找数据。
要调用对象数据，首先就必须用JNDI查找绑定的对象和数据，查找出来后，再调用该对象。程序如下所示。
```java
package jndi;
import java.nutil.Hashtable;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
public class findUseBindObj {
public static void main(String[]args){
         //创建Hashtable以存储JNDI将用于连接目录服务的环境变量
       Hashtable hs = new Hashtable();
        //设置连接Ldap的实现工厂
        hs.put(Context.INITIAL_CONTEXT_FACTORY,
                       “com.sun.jndi.ldap.LdapCtxFactory”);
        // 指定LDAP服务器IP地址为本机及端口号为389
        hs.put(Context.PROVIDER_URL, “ldap://localhost:389″);
        try {
           // 得到初始目录环境的一个引用
           DirContext ctx = new InitialDirContext(hs);
          //利用lookup查找返回指定DN的条目对象
           persons pers =(persons)ctx.lookup(”uid=Jordan,ou=Bull,o=NBA”);
           // 利用远程对象调用远程方法,返回Age变量的值
           String  age    =  pers.getAge();
           // 利用远程对象调用远程方法,返回Name变量的值
           String  name  =  pers.getName();
           //输出Name的值
       System.out.println(”name is :” +  name );
       /*根据结点的DN来查找它的所有属性, 然后再从属性中得到所有的值,注意一个属性可
           以有多个值*/
       Attributes attrs=ctx.getAttributes(”uid=Jordan,ou=Wizzard,o=NBA”);
       //循环获取并输出这个属性的所有属性值
       for(NamingEnumeration ae = attrs.getAll();ae.hasMore();){
           //获取一个属性
           Attribute attr = (Attribute)ae.next();
           System.out.println(”Attribute : ” + attr.getID());
                      //循环取得输出这个属性的所有属性值
            for(NamingEnumeration ve = attr.getAll();ve.hasMore();){
                        System.out.println(”  Value : ” + ve.next());
        }
        }
        //成功打印提示信息
        System.out.println(”find object success ” );
        //调用该对象的函数
       pers.toString();
           
       //关闭初始目录环境
       ctx.close();
    } catch (NamingException ex) {
       System.err.println(ex.toString());
    }     
  }
}
```
对于作为引用绑定在目录树中的对象，JNDI SPI 指定针对引用创建实际的对象。因此，在程序中只需要认为用lookup()方法返回的对象就是实际对象，而不用在调用什么方法来将引用转换为实际对象了，因为所有的工作都由JNDI内部完成了。

##### 2.7  JNDI查询修改LDAP目录条目
前面已经介绍了如何在LDAP服务器里存储一个对象：主要是利用一个DN将对象绑定到LDAP服务器中，然后用lookup(DN)查找定位到绑定的对象，再对该对象进行操作。但是往往使用DN查找非常难，用户很难记住DN，因此我们可以使用其他属性（比如CN=Cherry）来检索包含那个属性的条目。下面来介绍JNDI中相关属性检索的具体使用。
###### 1．修改条目
很多时候可能要对LDAP服务器上的条目进行修改，如修改用户密码，更新应用的配置等。但修改必须由一个已认证过的用户来执行，而且通常只能修改自己的密码而不能修改其他信息，管理助手能够修改电话号码和邮件地址，而修改用户标识这种工作由数据库管理员完成。
例3：  用JNDI修改LDAP条目。
```java
package jndi;
import java.nutil.Hashtable;
import javax.naming.Context;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
public class jndiPropertyModify {
    public static void main(String[] args){
        Hashtable hs = new Hashtable();
        //设置连接LDAP的实现工厂为com.sun.jndi.ldap.LdapCtxFactory
       hs.put(Context.INITIAL_CONTEXT_FACTORY,”com.sun.jndi.ldap.
            LdapCtxFactory”);
        //指定提供服务的服务器IP地址和端口号
        hs.put(Context.PROVIDER_URL,”ldap://localhost:389″);
        //使用简单认证来认证用户
        hs.put(Context.SECURITY_AUTHENTICATION,”simple”); 
        hs.put(Context.SECURITY_PRINCIPAL,”uid=Jordan,ou=Bull,o=NBA”); 
        hs.put(Context.SECURITY_CREDENTIALS,”good”);
        try {
           /*指定了JNDI服务提供者中工厂类（factory class）的名称。Factory负责为其服务创建适当的InitialContext对象。在上面的代码片断中,为文件系统服务提供者指定了工厂类*/
           DirContext ctx = new InitialDirContext(hs);
           System.out.println(”成功创建初始化context对象!”);
              //新建生成一个修改条目类对象,用于存放条目属性
           ModificationItem[] mdi = new ModificationItem[2];
              // 把属性mail的值置为jordan@163.com
           Attribute att0 = new BasicAttribute(”mail”,
               “jordan@163.com”);
              // 把属性call的值置为12745827
           Attribute att1 = new BasicAttribute(”call”,”12745827″);
             //修改指定属性mail
           mdi[0]=new ModificationItem(DirContext.REPLACE_ATTRIBUTE,
               att0); 
            //增加新属性call到条目
           mdi[1]=new ModificationItem(DirContext.ADD_ATTRIBUTE,att1); 
            // 修改指定DN条目的属性
           ctx.modifyAttributes(”uid=Jordan,ou=Bull,o=NBA”,mdi);
        }catch(Exception ex ){
           ex.printStackTrace();
           System.exit(1);
        }
    }
}
```
上面程序的作用是修改前面例子中增加的DN为uid = Jordan，ou = Bull，o = NBA条目的属性。
在程序中用DirContext.REPLACE_ATTRIBUTE来修改条目的mail属性，在这里如果原来的mail属性有多个值时，都会被删掉，取而代之的是新赋的值。用DirContext. REPLACE_ATTRIBUTE时，如果原来的属性（mail）不存在时，就增加一个属性，有则修改。
用DirContext.ADD_ATTRIBUTE来将一个新属性增加到条目。真正起到修改作用的是ctx.modifyAttributes(”uid = Jordan,ou = Bull,o = NBA”,mdi)这条语句。
###### 2．删除条目
有时，当开发者不需要某个条目时，就可以把它从LDAP服务器上删除。这只要通过调用参数为指定DN条目的DirContext接口的destorySubContext()方法来完成。
例4：  用JNDI删除LDAP条目。
```java
package jndi;
import java.nutil.Hashtable;
import javax.naming.Context;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
public class jndiPropertyModify {
    public static void main(String[] args){
        Hashtable hs = new Hashtable();
        //设置连接LDAP的实现工厂为com.sun.jndi.ldap.LdapCtxFactory
        hs.put(Context.INITIAL_CONTEXT_FACTORY,”com.sun.jndi.ldap.
            LdapCtxFactory”);
         //指定提供服务的服务器IP地址和端口号
        hs.put(Context.PROVIDER_URL,”ldap://localhost:389″);
         //使用简单认证来认证用户
        hs.put(Context.SECURITY_AUTHENTICATION,”simple”); 
        // 指定DN
        hs.put(Context.SECURITY_PRINCIPAL,”uid=Jordan,ou=Bull,o=NBA”); 
        // 指定认证密码
        hs.put(Context.SECURITY_CREDENTIALS,”good”);
        try {
           /*指定了JNDI服务提供者中工厂类（factory class）的名称。Factory负
               责为其服务创建适当的InitialContext对象。在上面的代码片断中,为文件
               系统服务提供者指定了工厂类*/
           DirContext ctx = new InitialDirContext(hs);
             //删除指定DN条目
           ctx.destroySubcontext(”uid=Jordan,ou=Bull,o=NBA”);
        }catch(Exception ex ){
           ex.printStackTrace();
           System.exit(1);
        }
    }
}
```
###### 3  小结
JNDI（命名和目录接口）是一个为Java应用程序提供命名服务的应用程序编程接口（API）。为开发人员提供了查找和访问各种命名和目录服务的通用、统一的接口。
命名服务是一种服务，它提供了为给定的数据集创建一个标准名字的能力。允许把名称同Java对象或资源关联起来，而不必指导对象或资源的物理ID。
目录服务是一种特殊类型的数据库，与SQL Server、Access、Oracle等关系数据库管理系统相反，构造目录服务的目的是为了处理基于行为的事务，并且使用一种关系信息模型。目录服务将命名服务的概念进一步引申为提供具有层次结构的信息库。
LDAP是在20世纪90年代早期作为标准目录协议进行开发的，它是目前最流行的目录协议，与厂商跟平台无关。LDAP定义客户应当如何访问服务器中的数据，它并不指定数据应当如何存储在服务器上。不使用服务提供者就不能用JNDI。在使用JNDI时，只需要了解JNDI，而服务提供者才关注实际的网络协议、编码/解码值等细节。一个服务提供者就是一组Java类的集合，SDK中的服务提供者包括LDAP、NIS、COS（CORBA对象服务）、RMI注册及文件系统的提供者等。
JNDI中包括5个包：
javax.naming、javax.naming.directory、javax.naming.event、javax.naming.ldap和javax.naming.spi。
常用的JNDI操作有：
bind、unbind、lookup、rename、NamingEnumeration listBindings和NamingEnumeration list。
使用JNDI来访问命名服务或者目录服务，操作步骤如下：
（1）建立一个散列表（hashtable），它包含定义所希望使用的JNDI服务的属性，所希望连接的LDAP服务器IP地址以及工作的端口。
（2）将与认证成用户登录有关的任何信息添加到散列表中。
（3）创建初始context对象。如果访问命名服务，则使用InitialContext类，如果访问目录服务，则要使用InitialDirContext。
（4）使用刚才得到的context对象执行所需的操作（如添加新的条目或者搜索条目）。
（5）完成操作后关闭context对象。
