[TOC]

# Java NIO

通常系统运行的性能瓶颈通常在I/O读写，包括对端口和文件的操作上，标准IO操作的步骤是：
1. 在打开一个I/O通道 后
2. read()将一直等待在端口一边读取字节内容，假如没有内容进来，read()会阻塞等待，那么改进做法就是开设线程，让线程去等待，但是这样做相当耗费资源的。

**Java NIO**非堵塞技术实际是采取Reactor模式，或者说是Observer模式来监察I/O端口，假如有内容进来，会自动通知程序，这样，就不必开启多个线程等待，在程序外部来看，实现了流畅的I/O读写，不堵塞了。

Java NIO 由以下几个核心部分组成：

- Channels
- Buffers
- Selectors
虽然Java NIO 中除此之外还有很多类和组件，但在我看来，Channel，Buffer 和 Selector 构成了核心的API。其它组件，如Pipe和FileLock，只不过是与三个核心组件共同使用的工具类。

## Channel 和 Buffer
所有的 IO 在NIO 中都从一个Channel 开始。Channel 有点象流。 数据可以从Channel读到Buffer中，也可以从Buffer 写到Channel中。

Channel和Buffer有好几种类型。下面是JAVA NIO中的一些主要Channel的实现：

- FileChannel
- DatagramChannel
- SocketChannel
- ServerSocketChannel

以下是Java NIO里关键的Buffer实现：

- ByteBuffer
- CharBuffer
- DoubleBuffer
- FloatBuffer
- IntBuffer
- LongBuffer
- ShortBuffer
这些Buffer覆盖了你能通过IO发送的基本数据类型：byte, short, int, long, float, double 和 char

## Selector
Selector允许单线程处理多个 Channel。如果你的应用打开了多个连接（通道），但每个连接的流量都很低，使用Selector就会很方便。例如，在一个聊天服务器中。

这是在一个单线程中使用一个Selector处理3个Channel的图示：
![Selector](./pic/overview-selectors.png "Selector")

要使用Selector，得向Selector注册Channel，然后调用它的select()方法。这个方法会一直阻塞到某个注册的通道有事件就绪。一旦这个方法返回，线程就可以处理这些事件，事件的例子有如新连接进来，数据接收等


### Selector内部原理
实际是在做一个对所注册的channel的轮询访问，不断的轮询(目前就这一个算法)，一旦轮询到一个channel有所注册的事情发生，比如数据来了，他就会站起来报告，交出一把钥匙，通过这把钥匙来读取这个channel的内容。



参考：
1. [并发编程网](http://ifeve.com/channels/ "并发编程网 ")