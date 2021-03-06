---
title: Spring读书笔记
date: 2019-05-24 22:06:47
categories: 
- 软件开发
- 系统架构
- Spring
---

[TOC]

# Spring

## Spring核心

Spring 从核心而言，是一个DI容器，其设计哲学是提供一种无侵入式的高扩展性框架。即无需代码中涉及Spring专有类，即可将其纳入Spring容器进行管理。
作为对比，EJB则是一种高度侵入性的框架规范，它制定了众多的接口和编码规范，要求实现者必须遵从。侵入性的后果就是，一旦系统基于侵入性框架设计开发，那么之后任何脱离这个框架的企图都将付出极大的代价。为了避免这种情况，实现无侵入性的目标。Spring 大量引入了Java 的Reflection机制，通过动态调用的方式避免硬编码方式的约束，并在此基础上建立了其核心组件BeanFactory，以此作为其依赖注入机制的实现基础。

Spring 可以分为三大部分：

- IOC容器：一个或多个容器的实例将应用程序和框架对象配置和装配到一起，管理对象之间的依赖关系及其生命周期。主要是用来创建和管理对象及用DI做对象关系管理，否则应用中会充满工厂或singleton模式不同程度地引入一些难于测试或者程式化代码重复的问题，给对象管理带来复杂性。
- AOP
- 企业级服务

### IOC

#### 1．依赖注入的几种形式

有设置器注入和构造函数注入两种常用形式，一般来说前者更常用一点。
还有一种比较少见的方式----方法注入，这个涉及到spring线程模型的应用，当单例服务依赖于多例DAO的情况下使用。
另外DI是推(push)模式，容器在运行时间将依赖“推”入应用程序对象，这与传统的拉(pull)模式正好相反，在拉模式中应用程序对象从它的环境中把依赖“拉”出来。

#### 2．容器bean工厂及ApplicationContext

org.springframework.beans包中包括了这些核心组件的实现类，核心中的核心为BeanWrapper和BeanFactory类。这两个类从技术角度而言并不复杂，但对于Spring 框架而言，却是关键所在，关于beans包，有三个重要的概念。

1. 它提供了设置/读取Javabeans属性功能的BeanWrapper接口。
2. Bean工厂。BeanFactory是一个泛化工厂，具有实例化对象和管理不同对象间关系的能力。BeanFactory可以管理几种不同类型的bean，并且支持串行化及其他生命周期方法。BeanFactory是按照bean定义（BeanDefinition）来实例化对象的。
3. BeanDefinition，顾名思义，是你对bean的定义。BeanDefinition不仅定义了bean的类结构、实例化的方式，还定义了bean在运行时的合作者。

##### Bean Factory

顾名思义，负责创建并维护Bean实例。
Bean Factory负责根据配置文件创建Bean实例，可以配置的项目有：

1. Bean属性值及依赖关系（对其他Bean的引用）
2. Bean创建模式（是否Singleton模式，即是否只针对指定类维持全局唯一的实例）
3. Bean初始化和销毁方法
4. Bean的依赖关系

BeanFactory接口不仅仅能管理bean，实际上它能管理你所期望的任意类。很多人在使用Spring时喜欢用BeanFactory管理“真正”的bean（仅有一个默认构造器、私有属性及其读取器和设置器）,事实上它也可以持有非bean形式的类。比如一个从以前系统遗留下来的连接池类――它完全不符合bean规范，不用担心，Spring同样可以很好的管理它。

Spring已经提供了一组现成的BeanFactory实现。XmlBeanFactory类支持在XML文件中指定bean定义，ListableBeanFactory类则支持从properties文件中获得bean定义。

BeanWrapper实现了针对单个Bean的属性设定操作。而BeanFactory则是针对多个Bean的管理容器，根据给定的配置文件，BeanFactory从中读取类名、属性名/值，然后通过Reflection机制进行Bean加载和属性设定

##### ApplicationContext

BeanFactory提供了针对Java Bean的管理功能，而ApplicationContext提供了一个更为框架化的实现（从上面的示例中可以看出，BeanFactory的使用方式更加类似一个API，而非Framework style）。

ApplicationContext覆盖了BeanFactory的所有功能，并提供了更多的特性。此外，ApplicationContext为与现有应用框架相整合，提供了更为开放式的实现（如对于Web应用，我们可以在web.xml中对ApplicationContext进行配置）。提供了更多功能MessageSource支持，事件支持，ResourceLoader支持。

取得资源实际Resource类型取决于位置字符串具有的可选前缀的组合以及应用程序上下文的实际类型。前缀有file: http: classpath:等，没有前缀的情况下取决于实际ApplicationContext的实现

- ClasspathXmlApplicationContext：位置被解释成一个类路径位置，返回ClasspathReource类
- FilesystemXmlApplicationContext：位置被 解释成一个相对的文件系统位置，返回FilesystemResource类
- XmlWebApplicationContext：位置被解释成相对于WEB应用程序目录的位置，返回ServletContextResource类

##### Bean Wrapper

所谓依赖注入，即在运行期由容器将依赖关系注入到组件之中。讲的通俗点，就是在运行期，由Spring根据配置文件，将其他对象的引用通过组件的提供的setter方法进行设定。

一个对于benas包非常重要的概念是BeanWrapper接口及与之对应的实现（BeanWrapperImpl类）。如JavaDoc中所载，BeanWrapper接口提供了设置和读取属性值、获得属性描述以及查询属性是否可读写的功能。BeanWrapper也提供了嵌套属性的支持，允许设置无限深度的子属性，BeanWrapper实现了针对单个Bean的属性设定操作。而BeanFactory则是针对多个Bean的管理容器，根据给定的配置文件，BeanFactory从中读取类名、属性名/值，然后通过Reflection机制进行Bean加载和属性设定。也可以用Bean Wrapper来包装任意的类来操作其中的属性。

**使用PropertyEditors包转换属性** 
有时候，为了使用方便，我们需要以另一种形式展现对象的属性。例如，日期可以以一种更容易阅读的形式表现出来，同时我们也会将人们熟悉的格式转换回原始的日期格式（或者使用一个更好的办法：将所有用户偏好形式转换回统一的java.util.Date对象）。为了达到这一目的，我们可以编写自定义编辑器，使其继承java.beans.PropertyEditor类型，并将自定义编辑器注册到BeanWrapper上。通过注册， BeanWrapper组装bean时将根据属性类型对XML文件中的字符串做相应的转换。请阅读Sun公司提供的java.beans包中PropertyEditors的JavaDoc获得进一步信息。 

##### Bean Definitions

Bean definitions是你的bean的详细描述，对应XML中`<bean>`配置。Bean就是一些提供某些特定功能的普通类，BeanFactory 接口如何管理你的bean以及它们是怎样配置的,都是在一个bean definition中规定的。
另外还有大量的工厂bean类可供使用，主要用来提取抽象返回所需要的对象。

#### 3．Bean定义及依赖、

主要是XML文件的配置用来定义信赖关系

#### 4．Bean的Scope

Singleton：每个Spring容器中一个bean定义对应一个实例
Prototype：一个bean定义对应多个实例
Request：在一个HTTP请求中一个bean定义对应一个实例，仅在基于WEB的ApplicationContext情形下有效
Session：在一个HTTP Session中一个bean定义对应一个实例，仅在基于WEB的ApplicationContext情形下有效
Global session：用于portlet应用中

#### 5．Bean生命周期

一些bean的生命周期回调方法，像beanFactoryAware和ApplicationContextAware接口的setBeanFactory()和setApplicationContext()方法就可以用来得到工厂对象

#### 6．资源

Spring的Resource接口扩充了java访问底层资源的能力及功能，可以通过classpath和servletContext相对路径来访问资源。
ResourceLoader接口用来返回Resource实例对象的实现。

研读代码而写出的调用过程：

1. XmlBeanFactory调用XmlBeanDefinitionReader的loadBeanDefinitions()把Resource转换为Document对象
2. 由XmlBeanDefinitionReader调用DefaultXmlBeanDefinitionParser的registerBeanDefinitions来注册beandefinition
3. 在DefaultXmlBeanDefinitionParser中把xml文件解析并转换来生成beandefinition，并调用beanfactory的registerBeanDefinition向beanfactory的beanDefinitionMap中增加传入的bean definition

### AOP

AOP和OOP有比较大的不通。OOP集中于对象的建模和行为的封装，而AOP考虑的是在流程上如何建模一个比较单独的功能。用OOP来构建一个核心系统是非常好的，但是，如果需要很多其他相关功能，这个时候AOP就有其用武之处了。

AOP关 注的Cross-cutting concerns, 从这些关注面中，我们抽取出一些可识别的Aspects，然后我们把这些Aspects具体设计为Advices，这些Advices根据我们的要求 PointCut，将这些Advices具体Weave到应用的这些可以插入的JointPoint中去。

Aspects 的 具体设计 Advices，它对应到Spring当中，就是那些具体执行操作的类。比如说BeforeAdivce之类的这些表示Aspect的类。
PointCut就是一堆配置信息，它描述了有那些Advices使用到那些JointPoint当中去。它对应到Spring当中，就是对应的 ProxyFactoryBean当中的配置信息。Weave就是ProxyFactoryBean，从名字上也知道它是一个代理工厂，生成原方法的代理 对象，然后将Aspect定义的这些功能动态增加到对象中去。
JointPoint：其实就是插入点，一般对象在执行方法的时候可以被插入其他的功能。在Spring中，只支持在Method层次上进行插入，而不支 持在Field层次上进行插入，因为这样的话实际上已经对对象的状态产生影响了，它连原来的对象都不是了，不适合用来构建应用系统。

其实静态代理和动态代理的了解对Spring AOP的理解真的是有非常大的帮助，因为Spring AOP就是靠动态代理技术以及动态字节码生成技术来达到目标的：通过构建对象的代理对象来完成功能的插入。

Spring2.0的AOP有两种方式一种是@AspactJ声明式的一种是Schema式的，一个advisor是一个仅仅包含一个通知对象和与之关联的切入点表达式的切面。

#### 一、AOP 概念 

- Joinpoint：它定义在哪里加入你的逻辑功能，对于Spring AOP，Jointpoint指的就是Method。 
- Advice：特定的Jointpoint处运行的代码，对于Spring AOP 来讲，有Before advice、AfterreturningAdvice、ThrowAdvice、AroundAdvice(MethodInteceptor)等。 
- Pointcut：一组Joinpoint，就是说一个Advice可能在多个地方织入， 
- Aspect：这个我一直迷惑，它实际是Advice和Pointcut的组合，但是Spring AOP 中的Advisor也是这样一个东西，但是Spring中为什么叫Advisor而不叫做Aspect。 
- Weaving：将Aspect加入到程序代码的过程，对于Spring AOP，由ProxyFactory或者ProxyFactoryBean负责织入动作。 
- Target：这个很容易理解，就是需要Aspect功能的对象。 
- Introduction：引入，就是向对象中加入新的属性或方法，一般是一个实例一个引用对象。当然如果不引入属性或者引入的属性做了线程安全性处理或 者只读属性，则一个Class一个引用也是可以的（自己理解）。Per-class lifecycle or per-instance life cycle 

#### 二、AOP 种类 

1. 静态织入：指在编译时期就织入Aspect代码，AspectJ好像是这样做的。 
2. 动态织入：在运行时期织入，Spring AOP属于动态织入，动态织入又分静动两种，静则指织入过程只在第一次调用时执行；动则指根据代码动态运行的中间状态来决定如何操作，每次调用Target的时候都执行（性能较差）。 

#### 三、Spring AOP 代理原理 

Spring AOP 是使用代理来完成的，Spring 会使用下面两种方式的其中一种来创建代理：

1. JDK动态代理，特点只能代理接口，性能相对较差，需要设定一组代理接口。 
2. CGLIB 代理，可代理接口和类（final method除外），性能较高(生成字节码)。 

#### 四、Spring AOP 通知类型 

1. BeforeAdvice：前置通知需实现MethodBeforeAdvice，但是该接口的Parent是BeforeAdvice，致 于什么用处我想可能是扩展性需求的设计吧。或者Spring未来也并不局限于Method的JoinPoint（胡乱猜测）。BeforeAdvice可 以修改目标的参数，也可以通过抛出异常来阻止目标运行。 
2. AfterreturningAdvice：实现AfterreturningAdvice，我们无法修改方法的返回值，但是可以通过抛出异常阻止方法运行。 
3. AroundAdvice：Spring 通过实现MethodInterceptor(aopalliance)来实现包围通知，最大特点是可以修改返回值，当然它在方法前后都加入了自己的逻辑代码，因此功能异常强大。通过MethodInvocation.proceed()来调用目标方法（甚至可以不调用）。 
4. ThrowsAdvice：通过实现若干afterThrowing()来实现。
5. IntroductionInterceptor：Spring 的默认实现为DelegatingIntroductionInterceptor 

#### 五、Spring AOP Pointcut 

以上只是Advice，如果不指定切入点，Spring 则使用所有可能的Jointpoint进行织入（当然如果你在Advice中进行方法检查除外）。因此切入点在AOP中扮演一个十分重要的角色。Spring 2.0 推荐使用AspectJ的Annocation的切入点表达式来定义切入点，或者使用<aop:xxx/>来定义AOP，这方面本篇不做考虑。 

1. Pointcut：它是Spring AOP Pointcut的核心，定义了getClassFilter()和getMethodMatcher()两个方法。 
2. ClassFilter：定义了matches(Class cls)一个方法。 
3. MethodMatcher() 定义了matches(Method，Class)，isRuntime()，matches(Mathod，Class，Object[])三个方法， 如果isRuntime()返回true则表示为动态代理（实际是动态代理的动态代理），则调用第三个方法（每访问一次调用一次），否则调用第一个方法 （并且只调用一次） 
4. Spring AOP　静态切入点的几个实现。 
- ComposablePointcut 太复杂一个切入点无法表达,union MethodMatcher和ClassFilter或者intersection MethodMatcher、ClassFilter和Pointcut。为什么不实现union Pointcut? 而只能通过Pointcuts类对Pointcut进行union操作。 
- ControlFlowPointcut 对程序的运行过程进行追踪 
- DynamicMatchMatcherPointcut 动态AOP 
- JdkRegexpMethodPointcut 使用正则表达式
- Perl5RegexpMethodPointcut 
- NameMatchMethodPointcut 用方法名字来匹配
- StaticMethodMatcherPointcut 静态切入点 

#### 六、Spring AOP 中的Advisor其实就是Aspect 

##### 1、 PointcutAdvisor 

其实一般使用DefaultPointcutAdvisor就足够了，给它Advice和Pointcut。 
当然如果想少写那么几行代码也可以使用NameMatchMethodPointcutAdvisor，RegexpMethodPointcutAdvisor等。 
更多Advisor可以查看API文档。 
##### 2、 IntroductionAdvisor 
默认实现为DefaultIntroductionAdvisor。 

在Spring里有可能在同一个AOP代理里模糊advisor和通知类型。例如，你可以在一个代理配置里使用一个interception环绕通知，一个异常通知：Spring将负责自动创建所需的拦截器链。在Spring中创建一个AOP代理的基本方法是使用ProxyFactoryBean类，它对应用的切入点和通知提供了完整的控制能力

#### 七、AOP ProxyFactory 
使用代码实现AOP 可使用ProxyFactory 
声明式AOP 可使用ProxyFactoryBean 
ProxyFactoryBean 需要设定 target，interceptorNames（可以是Advice或者Advisor，注意顺序） 
对接口代理需设置proxyInterfaces

**AOP编程的适用领域**
- Authentication 权限
- Caching 缓存
- Context passing 内容传递
- Error handling 错误处理
- Lazy loading　懒加载
- Debugging　　调试
- logging, tracing, profiling and monitoring　记录跟踪　优化　校准
- Performance optimization　性能优化
- Persistence　　持久化
- Resource pooling　资源池
- Synchronization　同步
- Transactions 事务


Web应用中对于异常的处理方式与其他形式的应用并没有太大的不同――通过try/catch语句针对不同的异常进行相应处理。
但是在具体实现中，由于异常层次、种类繁杂，我们往往很难在Servlet、JSP层妥善的处理好所有异常情况，代码中大量的try/catch代码显得尤为凌乱。
我们通常面对下面两个主要问题：

**1． 对异常实现集中式处理**
典型情况：对数据库异常记录错误日志。一般处理方法无外两种，
一是在各处数据库访问代码的异常处理中，加上日志记录语句。
二是将在数据访问代码中将异常向上抛出，并在上层结构中进行集中的日志记录处理

第一种处理方法失之繁琐、并且导致系统难以维护，假设后继需求为“对于数据库异
常，需记录日志，并发送通知消息告知系统管理员”。我们不得不对分散在系统中的各
处代码进行整改，工作量庞大。
第二种处理方法实现了统一的异常处理，但如果缺乏设计，往往使得上层异常处理过
于复杂。
这里，我们需要的是一个设计清晰、成熟可靠的集中式异常处理方案。

**2． 对未捕获异常的处理**
对于Unchecked Exception而言，由于代码不强制捕获，往往被程序员所忽略，如果
运行期产生了Unchecked Exception，而代码中又没有进行相应的捕获和处理，则我
们可能不得不面对尴尬的500服务器内部错误提示页面。这里，我们需要一个全面而有效的异常处理机制。

**自己总结：**
Spring核心也就是IOC和DI，它实现了松散耦合，使业务逻辑代码不必依赖于Spring的接口，提高了可移植性。还有就是它的AOP是实现很多功能的基础（如事务等），使其可以做出大量的横切模块。

### Spring中事务的包装

#### 事务的四个关键属性(ACID)

- 原子性(atomicity):事务是一个原子操作，有一系列动作组成。事务的原子性确保动作要么全部完成，要么完全不起作用
- 一致性(consistency):一旦所有事务动作完成，事务就被提交。数据和资源就处于一种满足业务规则的一致性状态中
- 隔离性(isolation):可能有许多事务会同时处理相同的数据，因此每个事物都应该与其他事务隔离开来，防止数据损坏
- 持久性(durability):一旦事务完成，无论发生什么系统错误，它的结果都不应该受到影响。通常情况下，事务的结果被写到持久化存储器中

#### 隔离级别

隔离级别	含义
ISOLATION_DEFAULT	使用后端数据库默认的隔离级别
ISOLATION_READ_UNCOMMITTED	最低的隔离级别，允许读取尚未提交的数据变更，可能会导致脏读、幻读或不可重复读
ISOLATION_READ_COMMITTED	允许读取并发事务已经提交的数据，可以阻止脏读，但是幻读或不可重复读仍有可能发生
ISOLATION_REPEATABLE_READ	对同一字段的多次读取结果都是一致的，除非数据是被本身事务自己所修改，可以阻止脏读和不可重复读，但幻读仍有可能发生
ISOLATION_SERIALIZABLE	最高的隔离级别，完全服从ACID的隔离级别，确保阻止脏读、不可重复读以及幻读，也是最慢的事务隔离级别，因为它通常是通过完全锁定事务相关的数据库表来实现的

1. 首先来看一般的事务包装，TransactionTemplate类中的excute(TransactionCallBack)方法用来作事务操作，该方法中前后会做事务的处理工作，主要的业务是可以写在TransactionCallBack.doInTransaction()中，这是一个回调方法。
2. 再看一下Hibernate的事务包装，HibernateTemplate类中也有excute方法，它与一般的jdbc事务差不多是在其中调用HibernateCallBack类的回调函数的，另外它还把Hibernate中的操作数据库的方法重写了如find,save等，调用的也是excute()方法，其实与一般性操作方法是一样的，只是多包装了几个通用方法。

先用datasource包装一下数据源，再把datasource传给transactionManagement完成事务包装，再可以把transactionManagement传给transactionTemplate来完成手工事务管理，传给动态代理类可以完成声明式事务管理。