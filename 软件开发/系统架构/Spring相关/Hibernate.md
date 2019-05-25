---
title: Hibernate
date: 2019-05-24 22:06:47
categories: 
- 软件开发
- 系统架构
- Spring
---

[TOC]

既然说到了Hibernate就把JDBC（Java DataBase Connection）顺带说一下好了，它做为SUN提供的一个数据库接口标准，基本上也是现行的java连接数据库的标准接口，Hiberante也是建立在它的基础之上。简单的说JDBC就是一个标准接口，看过它的源代码就知道其中提供了大量的接口，可以通过这些接口得到connection,执行SQL，返回结果集等数据库操作。再有一点就是数据库驱动是什么？都知道不同的数据库有不同的数据库驱动，打开数据库驱动的包，你就可以看到其实它实现了JDBC中定义的一些接口，例如Connection类就是由数据库驱动包提供的实现类，因为不同的数据库connect的方法一定不同，所以要提供不同的实现，数据库驱动就像一个适配器，在数据库与JDBC之间充当转换的角色。


下面我们就来看一下Hibernate，先解释一下持久化（Persistence）简单的说，就是把数据保存到掉电式存储设备中供之后所用（如硬盘，光盘等）。大多数情况下，特别是企业级应用，数据持久化也就意味着将内存中的数据保存到磁盘上加以“固化”，而持久化的实现过程则大多通过各种关系型数据库来完成。
Hibernate一个O/R Mapping 框架，为了消除关系数据库与面向对象语言之间的阻抗。
Hibernate的关键点
1. 数据库操作
就是一些Hibernate提供的方法用来访问数据库使用的，功能和JDBC的SQL语句类似，里面细节狂多，在这里就不一一说明了。如save,update,load等方法
2. 实体对象的3种状态
这个特性与数据库操作相关紧密，也就是与session相关联，因为在Hibernate中session就类似于connection。因为Hibernate做为一个O/R Mapping框架从数据库中取数据的数据都是对象形式的，所以说三种对象的关系一定要搞清楚。
3. 关联关系
就是用来配置数据库中的1:N,1:1,M:N关系如何在面向对象的实现的，涉及到级联操作、延迟加载等内容
4. 对象映射
也叫做继承映射，面向对象下面向关系最大的不同就是类的继承方面，这一特性就是用来把数据库中的表如何映射到类的继承关系中的。
5. Cache管理
Cache分为一级缓存与二级缓存，一级缓存在Hibernate中就是session，二级缓存是第二方提供的，作用都是为提高性能。
6. 延迟加载
也是为了提高性能，对于暂时不需要的数据不进行加载，到使用时再加载，特别是对于存在1：N关系时很有用。
以上几点以下都有更详细的说明。

## Hibernate 配置
Hibernate同时支持xml 格式的配置文件，以及传统的properties文件配置方式，不过这
里建议采用xml 型配置文件。xml配置文件提供了更易读的结构和更强的配置能力，可以直
接对映射文件加以配置，而在properties文件中则无法配置，必须通过代码中的Hard Coding
加载相应的映射文件。
配置文件名默认为“hibernate.cfg.xml”（或者hibernate.properties），Hibernate 初始化期
间会自动在CLASSPATH 中寻找这个文件，并读取其中的配置信息，为后期数据库操作做好
准备。
配置文件应部署在CLASSPATH 中，对于Web 应用而言，配置文件应放置在在
\WEB-INF\classes 目录下。

## Hibernate基础语义

### Configuration
正如其名，Configuration 类负责管理Hibernate 的配置信息。Hibernate 运行时需要
获取一些底层实现的基本信息，其中几个关键属性包括：
1． 数据库URL
2． 数据库用户
3． 数据库用户密码
4． 数据库JDBC驱动类
5． 数据库dialect，用于对特定数据库提供支持，其中包含了针对特定数据库特性
的实现，如Hibernate数据类型到特定数据库数据类型的映射等。
使用Hibernate 必须首先提供这些基础信息以完成初始化工作，为后继操作做好准
备。这些属性在hibernate配置文件（hibernate.cfg.xml 或hibernate.properties）中加以设
定
Configuration 类一般只有在获取SessionFactory时需要涉及，当获取SessionFactory 之后，由于配置信息已经由Hibernate 维护并绑定在返回的SessionFactory之上，因此一般情况下无需再对其进行操作。

### SessionFactory
SessionFactory 负责创建Session 实例。我们可以通过Configuation 实例构建
SessionFactory：
Configuration实例config会根据当前的配置信息，构造SessionFactory实例并返回。
SessionFactory 一旦构造完毕，即被赋予特定的配置信息。也就是说，之后config 的任
何变更将不会影响到已经创建的SessionFactory 实例（sessionFactory）。如果需要
使用基于改动后的config 实例的SessionFactory，需要从config 重新构建一个
SessionFactory实例。

### Session
Session是持久层操作的基础，相当于JDBC中的Connection。
Session实例通过SessionFactory实例构建：

### 数据检索
数据查询与检索是Hibernate中的一个亮点。相对其他ORM实现而言，Hibernate
提供了灵活多样的查询机制。其中包括：
1. Criteria Query
2. Hibernate Query Language (HQL)
3. SQL

### 关于unsaved-value
在非显示数据保存时，Hibernate将根据这个值来判断对象是否需要保存。一般为null就OK，用在级联中，未保存的记录id是null,与unsaved-value值对比相等就做为新的记录插入，不相等说明是原有的记录做更新操作，做为PO操作

### Inverse和Cascade
Inverse，直译为“反转”。在Hibernate语义中，Inverse指定了关联关系中的
方向。
关联关系中，inverse=”false”的为主动方，由主动方负责维护关联关系。具体可
参见一对多关系中的描述。
而Cascade，译为“级联”，表明对象的级联关系，如TUser的Cascade设为all，
就表明如果发生对user对象的操作，需要对user所关联的对象也进行同样的操作。如对
user对象执行save操作，则必须对user对象相关联的address也执行save操作。

### 延迟加载（Lazy Loading）
为了避免一些情况下，关联关系所带来的无谓的性能开销。Hibernate引入了延迟加载的
概念。
所谓延迟加载，就是在需要数据的时候，才真正执行数据加载操作。对于我们这里的user对象的加载过程，也就意味着，加载user对象时只针对其本身的属性，而当我们需要获取user对象所关联的address信息时（如执行user.getAddresses时），才真正从数据库中加载address数据并返回。

Hibernate.initialize方法可以通过强制加载关联对象实现这一功能：强制加载一个对象，可以在session关闭后继续读取
为了实现透明化的延迟加载机制，hibernate进行了大量努力。其中包括JDKCollection接口的独立实现。

如果我们尝试用HashSet强行转化Hibernate返回的Set型对象：
`Set hset = (HashSet)user.getAddresses();`
就会在运行期得到一个java.lang.ClassCastException,实际上，此时返回的是一个Hibernate的特定Set实现“net.sf.hibernate.collection.Set”对象，而非传统意义上的JDK Set实现。
这也正是我们为什么在编写POJO时，必须用JDK Collection接口（如Set,Map）,而非特定的JDK Collection实现类（如HashSet、HashMap）申明Collection属性的原因。
由于拥有自身的Collection实现，Hibernate就可以在Collection层从容的实现延迟加载特性。只有程序真正读取这个Collection时，才激发底层实际的数据库操作。

### Cache管理

Cache对于大量倚赖数据读取操作的系统而言（典型的，如114查号系统）尤为重要，
在大并发量的情况下，如果每次程序都需要向数据库直接做查询操作，所带来的性能开销
显而易见，频繁的网络传输、数据库磁盘的读写操作（大多数数据库本身也有Cache，但
即使如此，访问数据库本身的开销也极为可观），这些都大大降低了系统的整体性能。

引入Cache机制的难点是如何保证内存中数据的有效性，否则脏数据的出现将给系统
带来难以预知的严重后果。

Hibernate 中实现了良好的Cache 机制，我们可以借助Hibernate 内部的Cache
迅速提高系统数据读取性能。

**需要注意的是：Hibernate做为一个应用级的数据访问层封装，只能在其作用范围内
保持Cache中数据的的有效性，也就是说，在我们的系统与第三方系统共享数据库的情况
下，Hibernate的Cache机制可能失效。**

Hibernate 在本地JVM 中维护了一个缓冲池，并将从数据库获得的数据保存到池中
以供下次重复使用（如果在Hibernate中数据发生了变动，Hibernate同样也会更新池
中的数据版本）。
外部系统的定义，并非限于本系统之外的第三方系统，即使在本系统中，如果出现了
绕过Hibernate数据存储机制的其他数据存取手段，那么Cache的有效性也必须细加考
量。如，在同一套系统中，基于Hibernate和基于JDBC的两种数据访问方式并存，那么
通过JDBC更新数据库的时候，Hibernate同样无法获知数据更新的情况，从而导致脏数
据的出现。
用EHCache作为其默认的第二级Cache实现。相对JCS，EHCache更加稳定，并具备更好的缓存调度性能，缺陷是目前还无法做到分布式缓存，如果我们的系统需要在多台设备上部署，并共享同一个数据库，必须使用支持分布式缓存的Cache实现（如JCS、JBossCache）以避免出现不同系统实例之间缓存不一致而导致脏数据的情况。


Spring与hibernate结合使用

```java
public class UserDAO extends HibernateDaoSupport implements IUserDAO
{
public void insertUser(User user) {
getHibernateTemplate().saveOrUpdate(user);
}
}
```

上面的UserDAO实现了自定义的IUserDAO接口，并扩展了抽象类：HibernateDaoSupport
HibernateDaoSupport实现了HibernateTemplate和SessionFactory实例的关联。HibernateTemplate对Hibernate Session操作进行了封装，而HibernateTemplate.execute方法则是一封装机制的核心，感兴趣可以研究一下其实现机制。

借助HibernateTemplate我们可以脱离每次数据操作必须首先获得Session实例、启
动事务、提交/回滚事务以及烦杂的try/catch/finally等繁琐操作。从而获得以上代码
中精干集中的逻辑呈现效果。

### 数据库操作
1. 通过get(),load()方法都可以得到特定对象，它的区别是，get从一级缓存和数据库中查找，找不到返回NULL，load从一级缓存，二级缓存和数据库中查找，找不到抛出ObjectNotFindException.
2. Session.createQuery().find()和Session.createQuery().iterator(),
a)	前者是直接从数据库中查询放入缓存中，对缓存只写不读，就算是相同的SQL语句也会多次执行，不会从缓存中查找。
b)	后者是很执行一次SQL把ID取出，再根据ID在缓存中找，缓存中找不到就执行相当于get的SQL语句，它利用到了缓存，但也可能产生N＋1SQL问题。

### 实体对象的3种状态
实体对象可以分为三个阶段：
1．只是一个对象。2.对象与数据库中记录有对应关系。3.对象与数据库有对应关系并且也保存在session缓存中，也就是说与session也有关系。分别对应下面三种状态。

1. Transient(自由状态)
2. Detached（游离状态）
3. Persistent（持久状态）
数据库的insert操作是立即做的，update操作在commit时才做，因为只是缓存中状态变化，session可以把几条语句合成一条来执行，以提高性能。调用flush()时执行SQL操作。

## 关联关系

在做映射时，你需要关心两类对象关系。第一类是基于多重性(multiplicity)的，包含三种类型： 

- 一对一关系(One-to-one relationships)。这是一种两端多重性(multiplicity)最大值都为1的关系(译注：两端最多只有一个对象) 。举个例子来说就像Employee与Position之间的拥有(holds)关系。每个雇员拥有且仅拥有一个职位，每个职位可能拥有一个雇员（有些职位还可能空缺）。 有两种形式：1。主键关联，用<one-to-one>2。唯一外键关联,用<many-to-one>它是多对一的一种特殊形式。
-  一对多关系(One-to-many relationships)。也被称作多对一关系(many-to-one relationship)，这种关系产生于一端多重性(multiplicity)最大为1，而另一端大于1的场合。例如 Employee与Division之间的隶属(work in)关系。每个雇员在一个部门工作，任何给定部门都有一个或者多个雇员在里面工作。 尽量使用双向一对多关系，单向性能低，还会出现外键违例。
- 多对多关系(Many-to-many relationships)。这是一种两端多重性(multiplicity)最大值均大于1的关系。例如Employee与Task之间的分派(assigned)关系。每个雇员可以被分派一个或多个任务，每个任务可以被指派给0个或多个雇员。 

第二类是基于方向（directionality）的，包含两种类型：单向关系和双向关系。 
- 单向关系(Uni-directional relationships)。单向关系是指一个对象知道与其关联的其他对象，但是其他对象不知道该对象。例如 Employee 和Position之间的拥有(holds)关系。Employee对象知道其所拥有的职位，而Position对象不知道拥有它的雇员是谁（没有需要知道的必要）。不久你就会看到，单向关系要比双向关系容易实现。 
- 双向关系(Bi-directional relatinships)。双向关系是指，关联两端的对象都彼此知道对方。例如Employee与Division之间的隶属(work in)关系。Employee对象知道自己工作的部门，而Division对象也知道有哪些雇员在本部门工作。 
 



## 对象映射
### 1.	映射组成关系

### 2.	映射继承关系
#### a)	Table per hierarchy(每个类分层结构一张表)
**该策略的优点：**
SINGLE_TABLE 映射策略在所有继承策略中是最简单的，同时也是执行效率最高的。他仅需对一个表进行管理及操作，持久化引掣在载入entiry或多态连接时不需要进行任何的关联，联合或子查询，因为所有数据都存储在一个表。
**该策略的缺点：**
这种策略最大的一个缺点是需要对关系数据模型进行非常规设计，在数据库表中加入额外的区分各个子类的字段，此外，不能为所有子类的属性对应的字段定义not null约束，此策略的关系数据模型完全不支持对象的继承关系。
选择原则：查询性能要求高，子类属性不是非常多时，优先选择该策略。

#### b)	Table per subclass(每个子类一张表)
**该策略的优点：**
这种映射方式支持多态关联和多态查询，而且符合关系数据模型的常规设计规则。在这种策略中你可以对子类的属性对应的字段定义not null约束。
**该策略的缺点：**
它的查询性能不如上面介绍的映射策略。在这种映射策略下，必须通过表的内连接或左外连接来实现多态查询和多态关联。
选择原则：子类属性非常多，需要对子类某些属性对应的字段进行not null约束，且对性能要求不是很严格时，优先选择该策略。

#### c)	Table per concrete class(每个具体类一张表)
**该策略的优点：**
在这种策略中你可以对子类的属性对应的字段定义not null约束。
**该策略的缺点：**
不支持多态关联和多态查询，不符合关系数据模型的常规设计规则，每个表中都存在属于基类的多余的字段。同时，为了支持策略的映射，持久化管理者需要决定使用什么方法，一种方法是在entity载入或多态关联时，容器使用多次查询去实现，这种方法需要对数据库做几次来往查询，非常影响执行效率。另一种方法是容器通过使用SQL UNIOU查询来实现这种策略。
选择原则：除非你的现实情况必须使用这种策略，一般情况下不要选择 
 
 

