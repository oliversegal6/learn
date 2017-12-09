Java　NIO非 堵塞应用通常适用用在I/O读写等方面，我们知道，系统运行的性能瓶颈通常在I/O读写，包括对端口和文件的操作上，过去，在打开一个I/O通道 后，read()将一直等待在端口一边读取字节内容，假如没有内容进来，read()也是傻傻的等，这会影响我们程序继续做其他事情，那么改进做法就是开 设线程，让线程去等待，但是这样做也是相当耗费资源的。

Java NIO非堵塞技术实际是采取Reactor模式，或者说是Observer模式为我们监察I/O端口，假如有内容进来，会自动通知我们，这样，我们就不必开启多个线程死等，从外界看，实现了流畅的I/O读写，不堵塞了。

Java NIO出现不只是一个技术性能的提高，你会发现网络上到处在介绍它，因为它具有里程碑意义，从JDK1.4开始，Java开始提高性能相关的功能，从而使得Java在底层或者并行分布式计算等操作上已经可以和C或Perl等语言并驾齐驱。
假如你至今还是在怀疑Java的性能，说明你的思想和观念已经完全落伍了，Java一两年就应该用新的名词来定义。从JDK1.5开始又要提供关于线程、并发等新性能的支持，Java应用在游戏等适时领域方面的机会已经成熟，Java在稳定自己中间件地位后，开始蚕食传统C的领域。

## NIO主要原理和适用

NIO 有一个主要的类Selector,这个类似一个观察者，只要我们把需要探知的socketchannel告诉Selector,我们接着做别的事情，当有 事件发生时，他会通知我们，传回一组SelectionKey,我们读取这些Key,就会获得我们刚刚注册过的socketchannel,然后，我们从 这个Channel中读取数据，放心，包准能够读到，接着我们可以处理这些数据。

Selector内部原理实际是在做一个对所注册的channel的轮询访问，不断的轮询(目前就这一个算法)，一旦轮询到一个channel有所注册的事情发生，比如数据来了，他就会站起来报告，交出一把钥匙，让我们通过这把钥匙来读取这个channel的内容。
如果有socket的编程基础，应该会接触过堵塞socket和非堵塞socket，堵塞socket就是在accept、read、write等 IO操作的的时候，如果没有可用符合条件的资源，不马上返回，一直等待直到有资源为止。而非堵塞socket则是在执行select的时候，当没有资源的 时候堵塞，当有符合资源的时候，返回一个信号，然后程序就可以执行accept、read、write等操作，这个时候，这些操作是马上完成，并且马上返 回。而windows的winsock则有所不同，可以绑定到一个EventHandle里，也可以绑定到一个HWND里，当有资源到达时，发出事件，这 时执行的io操作也是马上完成、马上返回的。一般来说，如果使用堵塞socket，通常我们时开一个线程accept socket，当有socket链接的时候，开一个单独的线程处理这个socket；如果使用非堵塞socket，通常是只有一个线程，一开始是 select状态，当有信号的时候马上处理，然后继续select状态。
　　按照大多数人的说法，堵塞socket比非堵塞socket的性能要好。不过也有小部分人并不是这样认为的，例如Indy项目（Delphi 一个比较出色的网络包），它就是使用多线程＋堵塞socket模式的。另外，堵塞socket比非堵塞socket容易理解，符合一般人的思维，编程相对 比较容易。 
　　nio其实也是类似上面的情况。在JDK1.4，sun公司大范围提升Java的性能，其中NIO就 是其中一项。Java的IO操作集中在java.io这个包中，是基于流的阻塞API（即BIO，Block IO）。对于大多数应用来说，这样的API使用很方便，然而，一些对性能要求较高的应用，尤其是服务端应用，往往需要一个更为有效的方式来处理IO。从 JDK 1.4起，NIO API作为一个基于缓冲区，并能提供非阻塞O操作的API（即NIO，non-blocking IO）被引入。 
　　BIO与NIO一个比较重要的不同，是我们使用BIO的时候往往会引入多线程，每个连接一个单独的线程；而NIO则是使用单线程或者只使用少量的多线程，每个连接共用一个线程。
　　这个时候，问题就出来了：我们非常多的java应用是使用ThreadLocal的，例如JSF的FaceContext、 Hibernate的session管理、Struts2的Context的管理等等，几乎所有框架都或多或少地应用ThreadLocal。如果存在冲 突，那岂不惊天动地？ 
　　后来终于在Tomcat6的文档（http://tomcat.apache.org/tomcat-6.0-doc/aio.html）找到答案。根据上面说明，应该Tomcat6应用nio只是用在处理发送、接收信息的时候用到，也就是说，tomcat6还是传统的多线程Servlet，我画了下面两个图来列出区别：
  
　　tomcat5：客户端连接到达 -> 传统的SeverSocket.accept接收连接 -> 从线程池取出一个线程 -> 在该线程读取文本并且解析HTTP协议 -> 在该线程生成ServletRequest、ServletResponse，取出请求的Servlet -> 在该线程执行这个Servlet -> 在该线程把ServletResponse的内容发送到客户端连接 -> 关闭连接。 
　　我以前理解的使用nio后的tomcat6：客户端连接到达 -> nio接收连接 -> nio使 用轮询方式读取文本并且解析HTTP协议（单线程） -> 生成ServletRequest、ServletResponse，取出请求的Servlet -> 直接在本线程执行这个Servlet -> 把ServletResponse的内容发送到客户端连接 -> 关闭连接。 
　　实际的tomcat6：客户端连接到达 -> nio接收连接 -> nio使 用轮询方式读取文本并且解析HTTP协议（单线程） -> 生成ServletRequest、ServletResponse，取出请求的Servlet -> 从线程池取出线程，并在该线程执行这个Servlet -> 把ServletResponse的内容发送到客户端连接 -> 关闭连接。 
  
　　从上图可以看出，BIO与NIO的不同，也导致进入客户端处理线程的时刻有所不同：tomcat5在接受连接后马上进入客户端线程，在客户端线程里解析HTTP协议，而tomcat6则是解析完HTTP协议后才进入多线程，另外，tomcat6也比5早脱离客户端线程的环境。
  
　　实际的tomcat6与我之前猜想的差别主要集中在如何处理servlet的问题上。实际上即使抛开ThreadLocal的问题，我之前理 解tomcat6只使用一个线程处理的想法其实是行不同的。大家都有经验：servlet是基于BIO的，执行期间会存在堵塞的，例如读取文件、数据库操 作等等。tomcat6使用了nio，但不可能要求servlet里面要使用nio，而一旦存在堵塞，效率自然会锐降。
　　所以，最终的结论当然是tomcat6的servlet里面，ThreadLocal照样可以使用，不存在冲突。