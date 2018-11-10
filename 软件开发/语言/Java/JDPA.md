## 什么是JPDA 

　　Java Platform Debugger Architecture(JPDA:Java平台调试架构) 由Java虚拟机后端和调试平台前端组成 
　　1. Java虚拟机提供了Java调试的功能 
　　2. 调试平台通过调试交互协议向Java虚拟机请求服务以对在虚拟机中运行的程序进行调试
  
##  JPDA的构架 

　　JPDA通过两个接口和协议来完成如上的说明，分别是JVMTI（Java虚拟机工具接口）、JDWP（Java调试连线协议）和JDI（Java调试接口）。 
1. JVMTI定义了虚拟机应该提供的调试服务，包括调试信息（Information譬如栈信息）、调试行为（Action譬如客户端设置一个断点）和通知（Notification譬如到达某个断点时通知客户端），该接口由虚拟机实现者提供实现，并结合在虚拟机中 
2. JDWP定义调试服务和调试器之间的通信，包括定义调试信息格式和调试请求机制 
3. JDI在语言的高层次上定义了调试者可以使用的调试接口以能方便地与远程的调试服务进行交互，Java语言实现，调试器实现者可直接使用该接口访问虚拟机调试服务。

### 运行方式 

　　当虚拟机的调试服务运行时，虚 拟机作为调试的服务提供端，监听一个连接，而调试器通过该连接与虚拟机进行交互。目前，Windows平台的JVM提供了两种方式的连接：共享内存和 Socket连接，共享内存的服务提供端和调试端只能位于同一台机，而Socket连接则支持不同异机调试，即远程调试。

### 　　虚拟机参数设置 

　　1．启用调试服务 
  
    ```
　　　　-Xdebug 启用调试 
　　　　-Xrunjdwp:<sub-options> 加载JVM的JPDA参考实现库 
    ```
    
　　2．Xrunjdwp子参数（sub-options）配置 
　　
    ```
    Xrunjdwp子参数的配置格式如下 
　　　　-Xrunjdwp:<name1>[=<value1>],<name2>[=<value2>]...
　　几个例子 
　　-Xrunjdwp:transport=dt_socket,server=y,address=8000
　　在8000端口监听Socket连接，挂起VM并且不加载运行主函数直到调试请求到达 
　　-Xrunjdwp:transport=dt_shmem,server=y,suspend=n
　　选择一个可用的共享内存（因为没有指address）并监听该内存连接，同时加载运行主函数 
　　-Xrunjdwp:transport=dt_socket,address=myhost:8000
　　连接到myhost:8000提供的调试服务（server=n，以调试客户端存在），挂起VM并且不加载运行主函数 
　　-Xrunjdwp:transport=dt_shmem,address=mysharedmemory 
　　通过共享内存的方式连接到调试服务，挂起VM并且不加载运行主函数 
　　-Xrunjdwp:transport=dt_socket,server=y,address=8000, 
　　onthrow=java.io.IOException,launch=/usr/local/bin/debugstub 
　　等待java.io.IOException被抛出，然后挂起VM并监听8000端口连接，在接到调试请求后以命令/usr/local/bin/debugstub dt_socket myhost:8000执行 
　　-Xrunjdwp:transport=dt_shmem,server=y,onuncaught=y,launch=d:\bin\debugstub.exe 
　　等待一个RuntimeException被抛出，然后挂起VM并监听一个可用的共享内存，在接到调试请求后以命令d:\bin\debugstub.exe dt_shmem <address>执行,<address>是可用的共享内存
    ```
 ### 　　启动tomcat 

`-Xdebug -Xrunjdwp:transport=%JPDA_TRANSPORT%,address=%JPDA_ADDRESS%,server=y,suspend=n`

以上两行是tomcat5.5.12的catalina.bat文件中的一句，可以看出 tomcat在JPDA方式下是怎么启动的，启动tomcat要用catalina jpda start来启动，不能用startup.bat启动，启动前设置好JPDA_TRANSPORT，JPDA_ADDRESS就OK了

### 客户端使用jdb(java debugger)来连接调试

1. jdb classname用来新启一个VM做调试
2. jdb –attach ..用来附入一个已开的VM来做调试，当然VM要用debug方式启动
所有的java IDE调试都是基于JPDA，也都是使用这种方式来做的

