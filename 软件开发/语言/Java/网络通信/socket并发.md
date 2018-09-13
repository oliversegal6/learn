在客户/服务器通信模式中，服务器端需要创建监听特定端口的ServerSocket，ServerSocket负责接收客户连接请求。
提供线程池的一种实现方式。线程池包括一个工作队列和若干工作线程。服务器程序向工作队列中加入与客户通信的任务，工作线程不断从工作队列中取 出任务并执行它。还介绍了java.util.concurrent包中的线程池类的用法，在服务器程序中可以直接使用它们。
## 3.1  构造ServerSocket

ServerSocket的构造方法有以下几种重载形式：
- ServerSocket()throws IOException 
- ServerSocket(int port) throws IOException 
- ServerSocket(int port, int backlog) throws IOException
- ServerSocket(int port, int backlog, InetAddress bindAddr) throws IOException   

在以上构造方法中，参数port指定服务器要绑定的端口（服务器要监听的端口），参数backlog指定客户连接请求队列的长度，参数bindAddr指定服务器要绑定的IP地址。 

### 3.1.1  绑定端口

除了第一个不带参数的构造方法以外，其他构造方法都会使服务器与特定端口绑定，该端口由参数port指定。例如，以下代码创建了一个与80端口绑定的服务器：

`ServerSocket serverSocket=new ServerSocket(80);`

如果运行时无法绑定到80端口，以上代码会抛出IOException，更确切地说，是抛出BindException，它是IOException的子类。BindException一般是由以下原因造成的：

- 端口已经被其他服务器进程占用；
- 在某些操作系统中，如果没有以超级用户的身份来运行服务器程序，那么操作系统不允许服务器绑定到1~1023之间的端口。

如果把参数port设为0，表示由操作系统来为服务器分配一个任意可用的端口。由操作系统分配的端口也称为匿名端口。对于多数服务器，会使用明确的 端口，而不会使用匿名端口，因为客户程序需要事先知道服务器的端口，才能方便地访问服务器。在某些场合，匿名端口有着特殊的用途

### 3.1.2  设定客户连接请求队列的长度

当服务器进程运行时，可能会同时监听到多个客户的连接请求。例如，每当一个客户进程执行以下代码：
`Socket socket=new Socket(www.javathinker.org, 80);`
就意味着在远程www.javathinker.org主 机的80端口上，监听到了一个客户的连接请求。管理客户连接请求的任务是由操作系统来完成的。操作系统把这些连接请求存储在一个先进先出的队列中。许多操 作系统限定了队列的最大长度，一般为50。当队列中的连接请求达到了队列的最大容量时，服务器进程所在的主机会拒绝新的连接请求。只有当服务器进程通过 ServerSocket的accept()方法从队列中取出连接请求，使队列腾出空位时，队列才能继续加入新的连接请求。

对于客户进程，如果它发出的连接请求被加入到服务器的队列中，就意味着客户与服务器的连接建立成功，客户进程从Socket构造方法中正常返回。如果客户进程发出的连接请求被服务器拒绝，Socket构造方法就会抛出ConnectionException。

ServerSocket构造方法的backlog参数用来显式设置连接请求队列的长度，它将覆盖操作系统限定的队列的最大长度。值得注意的是，在以下几种情况中，仍然会采用操作系统限定的队列的最大长度：

- backlog参数的值大于操作系统限定的队列的最大长度；
- backlog参数的值小于或等于0；
- 在ServerSocket构造方法中没有设置backlog参数。

以下例程3-1的Client.java和例程3-2的Server.java用来演示服务器的连接请求队列的特性。
例程3-1  Client.java
```java
import java.net.*;
public class Client {
public static void main(String args[])throws Exception{
final int length=100;
String host="localhost";
int port=8000;
Socket[] sockets=new Socket[length];
for(int i=0;i<length;i++){     ="" 试图建立100次连接="">
sockets[i]=new Socket(host, port);
System.out.println("第"+(i+1)+"次连接成功");
}
Thread.sleep(3000);
for(int i=0;i
sockets[i].close();      //断开连接
} 
}
}
```
例程3-2  Server.java
```java
import java.io.*;
import java.net.*;
public class Server {
private int port=8000;
private ServerSocket serverSocket;
public Server() throws IOException {
serverSocket = new ServerSocket(port,3);    //连接请求队列的长度为3
System.out.println("服务器启动");
}
public void service() {
while (true) {
Socket socket=null;
try {
socket = serverSocket.accept();     //从连接请求队列中取出一个
连接
System.out.println("New connection accepted " +
socket.getInetAddress() + ":" +socket.getPort());
}catch (IOException e) {
e.printStackTrace();
}finally {
try{
if(socket!=null)socket.close();
}catch (IOException e) {e.printStackTrace();}
}
}
}
public static void main(String args[])throws Exception {
Server server=new Server();
Thread.sleep(60000*10);      //睡眠10分钟
//server.service();
}
}
```
Client试图与Server进行100次连接。在Server类中，把连接请求队列的长度设为3。这意味着当队列中有了3个连接请求时，如果Client再请求连接，就会被Server拒绝。下面按照以下步骤运行Server和Client程序。

（1）把Server类的main()方法中的“server.service();”这行程序代码注释掉。这使得服务器与8000端口绑定后，永 远不会执行serverSocket.accept()方法。这意味着队列中的连接请求永远不会被取出。先运行Server程序，然后再运行Client 程序，Client程序的打印结果如下：
第1次连接成功
第2次连接成功
第3次连接成功
Exception in thread "main" java.net.ConnectException: Connection refused: connect
at java.net.PlainSocketImpl.socketConnect(Native Method)
at java.net.PlainSocketImpl.doConnect(Unknown Source)
at java.net.PlainSocketImpl.connectToAddress(Unknown Source)
at java.net.PlainSocketImpl.connect(Unknown Source)
at java.net.SocksSocketImpl.connect(Unknown Source)
at java.net.Socket.connect(Unknown Source)
at java.net.Socket.connect(Unknown Source)
at java.net.Socket.(Unknown Source)
at java.net.Socket.(Unknown Source)
at Client.main(Client.java:10)
从以上打印结果可以看出，Client与Server在成功地建立了3个连接后，就无法再创建其余的连接了，因为服务器的队列已经满了。

（2）把Server类的main()方法按如下方式修改：
```java
public static void main(String args[])throws Exception {
Server server=new Server();
//Thread.sleep(60000*10);  //睡眠10分钟
server.service();
}
```

作了以上修改，服务器与8 000端口绑定后，就会在一个while循环中不断执行serverSocket.accept()方法，该方法从队列中取出连接请求，使得队列能及时腾 出空位，以容纳新的连接请求。先运行Server程序，然后再运行Client程序，Client程序的打印结果如下：
第1次连接成功
第2次连接成功
第3次连接成功
…
第100次连接成功
从以上打印结果可以看出，此时Client能顺利与Server建立100次连接。

### 3.1.3  设定绑定的IP地址

如果主机只有一个IP地址，那么默认情况下，服务器程序就与该IP地址绑定。ServerSocket的第4个构造方法 ServerSocket(int port, int backlog, InetAddress bindAddr)有一个bindAddr参数，它显式指定服务器要绑定的IP地址，该构造方法适用于具有多个IP地址的主机。假定一个主机有两个网卡， 一个网卡用于连接到Internet， IP地址为222.67.5.94，还有一个网卡用于连接到本地局域网，IP地址为192.168.3.4。如果服务器仅仅被本地局域网中的客户访问，那 么可以按如下方式创建ServerSocket：
`ServerSocket serverSocket=new ServerSocket(8000,10,InetAddress.getByName ("192.168.3.4"));`

### 3.1.4  默认构造方法的作用

ServerSocket有一个不带参数的默认构造方法。通过该方法创建的ServerSocket不与任何端口绑定，接下来还需要通过bind()方法与特定端口绑定。
这个默认构造方法的用途是，允许服务器在绑定到特定端口之前，先设置ServerSocket的一些选项。因为一旦服务器与特定端口绑定，有些选项就不能再改变了。
在以下代码中，先把ServerSocket的SO_REUSEADDR选项设为true，然后再把它与8000端口绑定：
```java
ServerSocket serverSocket=new ServerSocket();
serverSocket.setReuseAddress(true);      //设置ServerSocket的选项
serverSocket.bind(new InetSocketAddress(8000));   //与8000端口绑定
```
如果把以上程序代码改为：
```java
ServerSocket serverSocket=new ServerSocket(8000);
serverSocket.setReuseAddress(true);      //设置ServerSocket的选项
那么serverSocket.setReuseAddress(true)方法就不起任何作用了，因为SO_ REUSEADDR选项必须在服务器绑定端口之前设置才有效。
```
## 3.2  接收和关闭与客户的连接

ServerSocket的accept()方法从连接请求队列中取出一个客户的连接请求，然后创建与客户连接的Socket对象，并将它返回。如果队列中没有连接请求，accept()方法就会一直等待，直到接收到了连接请求才返回。

接下来，服务器从Socket对象中获得输入流和输出流，就能与客户交换数据。当服务器正在进行发送数据的操作时，如果客户端断开了连接，那么服务器端会抛出一个IOException的子类SocketException异常：
java.net.SocketException: Connection reset by peer
这只是服务器与单个客户通信中出现的异常，这种异常应该被捕获，使得服务器能继续与其他客户通信。

以下程序显示了单线程服务器采用的通信流程：
```java
public void service() {
while (true) {
Socket socket=null;
try {
socket = serverSocket.accept();    //从连接请求队列中取出一个连接
System.out.println("New connection accepted " +
socket.getInetAddress() + ":" +socket.getPort());
//接收和发送数据
…
}catch (IOException e) {
//这只是与单个客户通信时遇到的异常，可能是由于客户端过早断开连接引起的
//这种异常不应该中断整个while循环
e.printStackTrace();
}finally {
try{
if(socket!=null)socket.close();    //与一个客户通信结束后，要关闭
Socket
}catch (IOException e) {e.printStackTrace();}
}
}
}
```
与单个客户通信的代码放在一个try代码块中，如果遇到异常，该异常被catch代码块捕获。try代码块后面还有一个finally代码块，它保证不管与客户通信正常结束还是异常结束，最后都会关闭Socket，断开与这个客户的连接。

### 3.3  关闭ServerSocket

ServerSocket的close()方法使服务器释放占用的端口，并且断开与所有客户的连接。当一个服务器程序运行结束时，即使没有执行 ServerSocket的close()方法，操作系统也会释放这个服务器占用的端口。因此，服务器程序并不一定要在结束之前执行 ServerSocket的close()方法。

在某些情况下，如果希望及时释放服务器的端口，以便让其他程序能占用该端口，则可以显式调用ServerSocket的close()方法。例如， 以下代码用于扫描1~65535之间的端口号。如果ServerSocket成功创建，意味着该端口未被其他服务器进程绑定，否者说明该端口已经被其他进 程占用：
```java
for(int port=1;port<=65535;port++){
try{
ServerSocket serverSocket=new ServerSocket(port);
serverSocket.close();   //及时关闭ServerSocket
}catch(IOException e){
System.out.println("端口"+port+" 已经被其他服务器进程占用");
}
}
```
以上程序代码创建了一个ServerSocket对象后，就马上关闭它，以便及时释放它占用的端口，从而避免程序临时占用系统的大多数端口。

ServerSocket的isClosed()方法判断ServerSocket是否关闭，只有执行了ServerSocket的close() 方法，isClosed()方法才返回true；否则，即使ServerSocket还没有和特定端口绑定，isClosed()方法也会返回 false。
ServerSocket的isBound()方法判断ServerSocket是否已经与一个端口绑定，只要ServerSocket已经与一个端口绑定，即使它已经被关闭，isBound()方法也会返回true。

如果需要确定一个ServerSocket已经与特定端口绑定，并且还没有被关闭，则可以采用以下方式：
`boolean isOpen=serverSocket.isBound() && !serverSocket.isClosed();`

## 3.4  获取ServerSocket的信息

ServerSocket的以下两个get方法可分别获得服务器绑定的IP地址，以及绑定的端口：
- public InetAddress getInetAddress()
- public int getLocalPort()

前面已经讲到，在构造ServerSocket时，如果把端口设为0，那么将由操作系统为服务器分配一个端口（称为匿名端口），程序只要调用 getLocalPort()方法就能获知这个端口号。如例程3-3所示的RandomPort创建了一个ServerSocket，它使用的就是匿名端 口。

例程3-3  RandomPort.java
```java
import java.io.*;
import java.net.*;
public class RandomPort{
	public static void main(String args[])throws IOException{
		ServerSocket serverSocket=new ServerSocket(0);
		System.out.println("监听的端口为:"+serverSocket.getLocalPort());
	}
}
```
多次运行RandomPort程序，可能会得到如下运行结果：
C:\chapter03\classes>java RandomPort
监听的端口为:3000
C:\chapter03\classes>java RandomPort
监听的端口为:3004
C:\chapter03\classes>java RandomPort
监听的端口为:3005

多数服务器会监听固定的端口，这样才便于客户程序访问服务器。匿名端口一般适用于服务器与客户之间的临时通信，通信结束，就断开连接，并且ServerSocket占用的临时端口也被释放。

FTP（文件传输）协议就使用了匿名端口。FTP使用两个并行的TCP连接：一个是控制连接，一个是数据连接。控制连接用于在客户和服务器之间发送控制信息，如用户名和口令、改变远程目录的 命令或上传和下载文件的命令。数据连接用于传送文件。TCP服务器在21端口上监听控制连接，如果有客户要求上传或下载文件，就另外建立一个数据连接，通 过它来传送文件。数据连接的建立有两种方式。
1. TCP服务器在20端口上监听数据连接，TCP客户主动请求建立与该端口的连接。
1. 如图3-3所示，首先由TCP客户创建一个监听匿名端口的ServerSocket，再把这个ServerSocket监听的端口号（调用 ServerSocket的getLocalPort()方法就能得到端口号）发送给TCP服务器，然后由TCP服务器主动请求建立与客户端的连接。
 
以上第二种方式就使用了匿名端口，并且是在客户端使用的，用于和服务器建立临时的数据连接。在实际应用中，在服务器端也可以使用匿名端口。

## 3.5  ServerSocket选项

ServerSocket有以下3个选项。
- SO_TIMEOUT：表示等待客户连接的超时时间。
- SO_REUSEADDR：表示是否允许重用服务器所绑定的地址。
- SO_RCVBUF：表示接收数据的缓冲区的大小。

### 3.5.1  SO_TIMEOUT选项

- 设置该选项：public void setSoTimeout(int timeout) throws SocketException
- 读取该选项：public int getSoTimeout () throws IOException

SO_TIMEOUT表示ServerSocket的accept()方法等待客户连接的超时时间，以毫秒为单位。如果SO_TIMEOUT的值为0，表示永远不会超时，这是SO_TIMEOUT的默认值。

当服务器执行ServerSocket的accept()方法时，如果连接请求队列为空，服务器就会一直等待，直到接收到了客户连接才从 accept()方法返回。如果设定了超时时间，那么当服务器等待的时间超过了超时时间，就会抛出SocketTimeoutException，它是 InterruptedException的子类。

如例程3-4所示的TimeoutTester把超时时间设为6秒钟。
例程3-4  TimeoutTester.java
```java
import java.io.*;
import java.net.*;
public class TimeoutTester{
public static void main(String args[])throws IOException{
ServerSocket serverSocket=new ServerSocket(8000);
serverSocket.setSoTimeout(6000); //等待客户连接的时间不超过6秒
Socket socket=serverSocket.accept(); 
socket.close();
System.out.println("服务器关闭");
}
}
```
运行以上程序，过6秒钟后，程序会从serverSocket.accept()方法中抛出Socket- TimeoutException：
C:\chapter03\classes>java TimeoutTester
Exception in thread "main" java.net.SocketTimeoutException: Accept timed out
at java.net.PlainSocketImpl.socketAccept(Native Method)
at java.net.PlainSocketImpl.accept(Unknown Source)
at java.net.ServerSocket.implAccept(Unknown Source)
at java.net.ServerSocket.accept(Unknown Source)
at TimeoutTester.main(TimeoutTester.java:8)
如果把程序中的“serverSocket.setSoTimeout(6000)”注释掉，那么serverSocket. accept()方法永远不会超时，它会一直等待下去，直到接收到了客户的连接，才会从accept()方法返回。
Tips：服务器执行serverSocket.accept()方法时，等待客户连接的过程也称为阻塞。

### 3.5.2  SO_REUSEADDR选项

- 设置该选项：public void setResuseAddress(boolean on) throws SocketException
- 读取该选项：public boolean getResuseAddress() throws SocketException
这个选项与Socket的SO_REUSEADDR选项相同，用于决定如果网络上仍然有数据向旧的ServerSocket传输数据，是否允许新的 ServerSocket绑定到与旧的ServerSocket同样的端口上。SO_REUSEADDR选项的默认值与操作系统有关，在某些操作系统中， 允许重用端口，而在某些操作系统中不允许重用端口。

当ServerSocket关闭时，如果网络上还有发送到这个ServerSocket的数据，这个ServerSocket不会立刻释放本地端口，而是会等待一段时间，确保接收到了网络上发送过来的延迟数据，然后再释放端口。

许多服务器程序都使用固定的端口。当服务器程序关闭后，有可能它的端口还会被占用一段时间，如果此时立刻在同一个主机上重启服务器程序，由于端口已经被占用，使得服务器程序无法绑定到该端口，服务器启动失败，并抛出BindException：
Exception in thread "main" java.net.BindException: Address already in use: JVM_Bind

为了确保一个进程关闭了ServerSocket后，即使操作系统还没释放端口，同一个主机上的其他进程还可以立刻重用该端口，可以调用ServerSocket的setResuse- Address(true)方法：
if(!serverSocket.getResuseAddress())serverSocket.setResuseAddress(true);
值得注意的是，serverSocket.setResuseAddress(true)方法必须在ServerSocket还没有绑定到一个本地 端口之前调用，否则执行serverSocket.setResuseAddress(true)方法无效。此外，两个共用同一个端口的进程必须都调用 serverSocket.setResuseAddress(true)方法，才能使得一个进程关闭ServerSocket后，另一个进程的 ServerSocket还能够立刻重用相同端口。

### 3.5.3  SO_RCVBUF选项

- 设置该选项：public void setReceiveBufferSize(int size) throws SocketException
- 读取该选项：public int getReceiveBufferSize() throws SocketException

SO_RCVBUF表示服务器端的用于接收数据的缓冲区的大小，以字节为单位。一般说来，传输大的连续的数据块（基于HTTP或FTP协议的数据传 输）可以使用较大的缓冲区，这可以减少传输数据的次数，从而提高传输数据的效率。而对于交互式的通信（Telnet和网络游戏），则应该采用小的缓冲区， 确保能及时把小批量的数据发送给对方。

SO_RCVBUF的默认值与操作系统有关。例如，在Windows 2000中运行以下代码时，显示SO_RCVBUF的默认值为8192：
```java
ServerSocket serverSocket=new ServerSocket(8000);
System.out.println(serverSocket.getReceiveBufferSize());    //打印8192
```

无论在ServerSocket绑定到特定端口之前或之后，调用setReceiveBufferSize()方法都有效。例外情况下是如果要设置 大于64K的缓冲区，则必须在ServerSocket绑定到特定端口之前进行设置才有效。例如，以下代码把缓冲区设为128K：
```java
ServerSocket serverSocket=new ServerSocket();
int size=serverSocket.getReceiveBufferSize();
if(size<131072) serverSocket.setReceiveBufferSize(131072);  //把缓冲区的大小设为128K
serverSocket.bind(new InetSocketAddress(8000));     //与8 000端口绑定
执行serverSocket.setReceiveBufferSize()方法，相当于对所有由serverSocket.accept()方法返回的Socket设置接收数据的缓冲区的大小。
```
### 3.5.4  设定连接时间、延迟和带宽的相对重要性

`public void setPerformancePreferences(int connectionTime,int latency,int bandwidth)`
该方法的作用与Socket的setPerformancePreferences()方法的作用相同，用于设定连接时间、延迟和带宽的相对重要性，参见本书第2章的2.5.10节（设定连接时间、延迟和带宽的相对重要性）。

## 3.6  创建多线程的服务器

EchoServer中，其service()方法负责接收客户连接，以及与客户通信。service()方法的处理流程如下：
```java
while (true) {
Socket socket=null;
try {
socket = serverSocket.accept();     //接收客户连接
//从Socket中获得输入流与输出流，与客户通信
…

}catch (IOException e) {
e.printStackTrace();
}finally {
try{
if(socket!=null)socket.close();    //断开连接
}catch (IOException e) {e.printStackTrace();}
}
} 
```
EchoServer接收到一个客户连接，就与客户进行通信，通信完毕后断开连接，然后再接收下一个客户连接。假如同时有多个客户请求连接，这些客户就必须排队等候EchoServer的响应。EchoServer无法同时与多个客户通信。

许多实际应用要求服务器具有同时为多个客户提供服务的能力。HTTP服务器就是最明显的例子。任何时刻，HTTP服务器都可能接收到大量的客户请求，每个客户都希望能快速得到HTTP服务器的响应。如果长时间让客户等待，会使网站失去信誉，从而降低访问量。

可以用并发性能来衡量一个服务器同时响应多个客户的能力。一个具有好的并发性能的服务器，必须符合两个条件：
- 能同时接收并处理多个客户连接；
- 对于每个客户，都会迅速给予响应。

服务器同时处理的客户连接数目越多，并且对每个客户作出响应的速度越快，就表明并发性能越高。

用多个线程来同时为多个客户提供服务，这是提高服务器的并发性能的最常用的手段。本节将按照3种方式来重新实现EchoServer，它们都使用了多线程。
- 为每个客户分配一个工作线程。
- 创建一个线程池，由其中的工作线程来为客户服务。
- 利用JDK的Java类库中现成的线程池，由它的工作线程来为客户服务。

### 3.6.1  为每个客户分配一个线程

服务器的主线程负责接收客户的连接，每次接收到一个客户连接，就会创建一个工作线程，由它负责与客户的通信。以下是EchoServer的service()方法的代码：
```java
public void service() {
while (true) {
Socket socket=null;
try {
socket = serverSocket.accept();      //接收客户连接
Thread workThread=new Thread(new Handler(socket));   //创建一个工作线程
workThread.start();        //启动工作线程
}catch (IOException e) {
e.printStackTrace();
}
}
}
```
以上工作线程workThread执行Handler的run()方法。Handler类实现了Runnable接口，它的run()方法负责与单 个客户通信，与客户通信结束后，就会断开连接，执行Handler的run()方法的工作线程也会自然终止。如例程3-5所示是EchoServer类及 Handler类的源程序。

例程3-5  EchoServer.java（为每个任务分配一个线程）
```java
package multithread1;
import java.io.*;
import java.net.*;
public class EchoServer {
private int port=8000;
private ServerSocket serverSocket;
public EchoServer() throws IOException {
serverSocket = new ServerSocket(port);
System.out.println("服务器启动");
}
public void service() {
while (true) {
Socket socket=null;
try {
socket = serverSocket.accept();      //接收客户连接
Thread workThread=new Thread(new Handler(socket));   //创建一个工作线程
workThread.start();        //启动工作线程
}catch (IOException e) {
e.printStackTrace();
}
}
}
public static void main(String args[])throws IOException {
new EchoServer().service();
}
}
class Handler implements Runnable{       //负责与单个客户的通信
private Socket socket;
public Handler(Socket socket){
this.socket=socket;
}
private PrintWriter getWriter(Socket socket)throws IOException{…}
private BufferedReader getReader(Socket socket)throws IOException{…}
public String echo(String msg) {…}
public void run(){
try {
System.out.println("New connection accepted " +
socket.getInetAddress() + ":" +socket.getPort());
BufferedReader br =getReader(socket);
PrintWriter pw = getWriter(socket);

String msg = null;
while ((msg = br.readLine()) != null) {     //接收和发送数据，直到通信结束
System.out.println(msg);
pw.println(echo(msg));
if (msg.equals("bye"))
break;
}
}catch (IOException e) {
e.printStackTrace();
}finally {
try{
if(socket!=null)socket.close();       //断开连接
}catch (IOException e) {e.printStackTrace();}
}
}
}
```

### 3.6.2  创建线程池

在3.6.1节介绍的实现方式中，对每个客户都分配一个新的工作线程。当工作线程与客户通信结束，这个线程就被销毁。这种实现方式有以下不足之处。

- 服务器创建和销毁工作线程的开销（包括所花费的时间和系统资源）很大。如果服务器需要与许多客户通信，并且与每个客户的通信时间都很短，那么有可能服务器为客户创建新线程的开销比实际与客户通信的开销还要大。

- 除了创建和销毁线程的开销之外，活动的线程也消耗系统资源。每个线程本身都会占用一定的内存（每个线程需要大约1M内存），如果同时有大量客户连接服务器，就必须创建大量工作线程，它们消耗了大量内存，可能会导致系统的内存空间不足。

- 如果线程数目固定，并且每个线程都有很长的生命周期，那么线程切换也是相对固定的。不同操作系统有不同的切换周期，一般在20毫秒左右。这里所说 的线程切换是指在Java虚拟机，以及底层操作系统的调度下，线程之间转让CPU的使用权。如果频繁创建和销毁线程，那么将导致频繁地切换线程，因为一个 线程被销毁后，必然要把CPU转让给另一个已经就绪的线程，使该线程获得运行机会。在这种情况下，线程之间的切换不再遵循系统的固定切换周期，切换线程的 开销甚至比创建及销毁线程的开销还大。
线程池为线程生命周期开销问题和系统资源不足问题提供了解决方案。线程池中预先创建了一些工作线程，它们不断从工作队列中取出任务，然后执行该任务。当工作线程执行完一个任务时，就会继续执行工作队列中的下一个任务。线程池具有以下优点：
- 减少了创建和销毁线程的次数，每个工作线程都可以一直被重用，能执行多个任务。
- 可以根据系统的承载能力，方便地调整线程池中线程的数目，防止因为消耗过量系统资源而导致系统崩溃。

如例程3-6所示，ThreadPool类提供了线程池的一种实现方案。
例程3-6  ThreadPool.java
```java
package multithread2;
import java.util.LinkedList;
public class ThreadPool extends ThreadGroup {
private boolean isClosed=false;     //线程池是否关闭
private LinkedList workQueue;   //表示工作队列
private static int threadPoolID;     //表示线程池ID
private int threadID;      //表示工作线程ID
public ThreadPool(int poolSize) {    //poolSize指定线程池中的工作线程数目
super("ThreadPool-" + (threadPoolID++));
setDaemon(true);
workQueue = new LinkedList();   //创建工作队列
for (int i=0; i
new WorkThread().start();     //创建并启动工作线程
}

/** 向工作队列中加入一个新任务，由工作线程去执行该任务 */
public synchronized void execute(Runnable task) {
if (isClosed) {      //线程池被关则抛出
IllegalStateException异常
throw new IllegalStateException();
}
if (task != null) {
workQueue.add(task);
notify();       //唤醒正在getTask()方法中等待任务的工作线程
}
}
/** 从工作队列中取出一个任务，工作线程会调用此方法 */
protected synchronized Runnable getTask()throws InterruptedException{
while (workQueue.size() == 0) {
if (isClosed) return null;
wait();       //如果工作队列中没有任务，就等待任务
}
return workQueue.removeFirst();
}
/** 关闭线程池 */
public synchronized void close() {
if (!isClosed) {
isClosed = true;
workQueue.clear();     //清空工作队列
interrupt();       //中断所有的工作线程，该方法继承自ThreadGroup类
}
}
/** 等待工作线程把所有任务执行完 */
public void join() {
synchronized (this) {
isClosed = true;
notifyAll();       //唤醒还在getTask()方法中等待任务
的工作线程
}
Thread[] threads = new Thread[activeCount()];
//enumerate()方法继承自ThreadGroup类，获得线程组中当前所有活着的工作线程
int count = enumerate(threads);  
for (int i=0; i
      try {
threads[i].join();     //等待工作线程运行结束
}catch(InterruptedException ex) { }
}
}
/**  内部类：工作线程  */
private class WorkThread extends Thread {
public WorkThread() {
//加入到当前ThreadPool线程组中
super(ThreadPool.this,"WorkThread-" + (threadID++));
}
public void run() {
while (!isInterrupted()) {  //isInterrupted()方法继承自Thread类，判断线程是否被中断
Runnable task = null;
try {       //取出任务
task = getTask();
}catch (InterruptedException ex){}
// 如果getTask()返回null或者线程执行getTask()时被中断，则结束此线程
if (task == null) return;

try { //运行任务，异常在catch代码块中捕获
task.run();
} catch (Throwable t) {
t.printStackTrace();
}
}      //#while
}       //#run()
}       //#WorkThread类
}
```
在ThreadPool类中定义了一个LinkedList类型的workQueue成员变量，它表示工作队列，用来存放线程池要执行的任务，每个 任务都是Runnable实例。ThreadPool类的客户程序（利用ThreadPool来执行任务的程序）只要调用ThreadPool类的 execute (Runnable task)方法，就能向线程池提交任务。在ThreadPool类的execute()方法中，先判断线程池是否已经关闭。如果线程池已经关闭，就不再接 收任务，否则就把任务加入到工作队列中，并且唤醒正在等待任务的工作线程。

在ThreadPool类的构造方法中，会创建并启动若干工作线程，工作线程的数目由构造方法的参数poolSize决定。WorkThread类 表示工作线程，它是ThreadPool类的内部类。工作线程从工作队列中取出一个任务，接着执行该任务，然后再从工作队列中取出下一个任务并执行它，如 此反复。

工作线程从工作队列中取任务的操作是由ThreadPool类的getTask()方法实现的，它的处理逻辑如下：
- 如果队列为空并且线程池已关闭，那就返回null，表示已经没有任务可以执行了；
- 如果队列为空并且线程池没有关闭，那就在此等待，直到其他线程将其唤醒或者中断；
- 如果队列中有任务，就取出第一个任务并将其返回。

线程池的join()和close()方法都可用来关闭线程池。join()方法确保在关闭线程池之前，工作线程把队列中的所有任务都执行完。而close()方法则立即清空队列，并且中断所有的工作线程。

ThreadPool类是ThreadGroup类的子类。ThreadGroup类表示线程组，它提供了一些管理线程组中线程的方法。例 如，interrupt()方法相当于调用线程组中所有活着的线程的interrupt()方法。线程池中的所有工作线程都加入到当前 ThreadPool对象表示的线程组中。ThreadPool类在close()方法中调用了interrupt()方法：

```java
/** 关闭线程池 */
public synchronized void close() {
if (!isClosed) {
isClosed = true;
workQueue.clear();   //清空工作队列
interrupt();     //中断所有的工作线程，该方法继承自ThreadGroup类
}
}
以上interrupt()方法用于中断所有的工作线程。interrupt()方法会对工作线程造成以下影响：
◆如果此时一个工作线程正在ThreadPool的getTask()方法中因为执行wait()方法而阻塞，则会抛出InterruptedException；
◆如果此时一个工作线程正在执行一个任务，并且这个任务不会被阻塞，那么这个工作线程会正常执行完任务，但是在执行下一轮while (!isInterrupted()) {…}循环时，由于isInterrupted()方法返回true，因此退出while循环。
如例程3-7所示，ThreadPoolTester类用于测试ThreadPool的用法。
例程3-7  ThreadPoolTester.java
package multithread2;
public class ThreadPoolTester {
public static void main(String[] args) {
if (args.length != 2) {
System.out.println(
"用法: java ThreadPoolTest numTasks poolSize");
System.out.println(
"  numTasks - integer: 任务的数目");
System.out.println(
"  numThreads - integer: 线程池中的线程数目");
return;
}
int numTasks = Integer.parseInt(args[0]);
int poolSize = Integer.parseInt(args[1]);
ThreadPool threadPool = new ThreadPool(poolSize);    //创建线程池
// 运行任务
for (int i=0; i
threadPool.execute(createTask(i));

threadPool.join();        //等待工作线程完成所有的任务
// threadPool.close();       //关闭线程池
}//#main()

/**  定义了一个简单的任务(打印ID)   */
private static Runnable createTask(final int taskID) {
return new Runnable() {
public void run() {
System.out.println("Task " + taskID + ": start");
try {
Thread.sleep(500);       //增加执行一个任务的时间
} catch (InterruptedException ex) { }
System.out.println("Task " + taskID + ": end");
}
};
}
}
```
ThreadPoolTester类的createTask()方法负责创建一个简单的任务。ThreadPoolTester类的main()方 法读取用户从命令行输入的两个参数，它们分别表示任务的数目和工作线程的数目。main()方法接着创建线程池和任务，并且由线程池来执行这些任务，最后 调用线程池的join()方法，等待线程池把所有的任务执行完毕。
运行命令“java multithread2.ThreadPoolTester 5 3”，线程池将创建3个工作线程，由它们执行5个任务。程序的打印结果如下：
Task 0: start
Task 1: start
Task 2: start
Task 0: end
Task 3: start
Task 1: end
Task 4: start
Task 2: end
Task 3: end
Task 4: end
从打印结果看出，主线程等到工作线程执行完所有任务后，才结束程序。如果把main()方法中的“threadPool.join()”改为“threadPool.close()”，再运行程序，则会看到，尽管有一些任务还没有执行，程序就运行结束了。

如例程3-8所示，EchoServer利用线程池ThreadPool来完成与客户的通信任务。
例程3-8  EchoServer.java（使用线程池ThreadPool类）
```java

package multithread2;
import java.io.*;
import java.net.*;
public class EchoServer {
private int port=8000;
private ServerSocket serverSocket;
private ThreadPool threadPool;      //线程池
private final int POOL_SIZE=4;      //单个CPU时线程池中工作线程的数目
public EchoServer() throws IOException {
serverSocket = new ServerSocket(port);
//创建线程池
//Runtime的availableProcessors()方法返回当前系统的CPU的数目
//系统的CPU越多，线程池中工作线程的数目也越多 
threadPool= new ThreadPool( 
Runtime.getRuntime().availableProcessors() * POOL_SIZE);
System.out.println("服务器启动");
}
public void service() {
while (true) {
Socket socket=null;
try {
socket = serverSocket.accept();
threadPool.execute(new Handler(socket));   //把与客户通信的任务交给线程池
}catch (IOException e) {
e.printStackTrace();
}
}
}
public static void main(String args[])throws IOException {
new EchoServer().service();
}
}
```

/** 负责与单个客户通信的任务，代码与3.6.1节的例程3-5的Handler类相同 */
class Handler implements Runnable{…}  
在以上EchoServer的service()方法中，每接收到一个客户连接，就向线程池ThreadPool提交一个与客户通信的任务。ThreadPool把任务加入到工作队列中，工作线程会在适当的时候从队列中取出这个任务并执行它。

### 3.6.3  使用JDK类库提供的线程池

java.util.concurrent包提供了现成的线程池的实现，它比3.6.2节介绍的线程池更加健壮，而且功能也更强大。

Executor接口表示线程池，它的execute(Runnable task)方法用来执行Runnable类型的任务。Executor的子接口ExecutorService中声明了管理线程池的一些方法，比如用于关 闭线程池的shutdown()方法等。Executors类中包含一些静态方法，它们负责生成各种类型的线程池ExecutorService实例

Executors类的静态方法	创建的ExecutorService线程池的类型
- newCachedThreadPool()	在有任务时才创建新线程，空闲线程被保留60秒
- newFixedThreadPool(int nThreads)	线程池中包含固定数目的线程，空闲线程会一直保留。参数nThreads设定线程池中线程的数目
- newSingleThreadExecutor()	线程池中只有一个工作线程，它依次执行每个任务
- newScheduledThreadPool(int corePoolSize)	线程池能按时间计划来执行任务，允许用户设定计划执行任务的时间。参数corePoolSize设定线程池中线程的最小数目。当任务较多时，线程池可能会创建更多的工作线程来执行任务
- newSingleThreadScheduledExecutor()	线程池中只有一个工作线程，它能按时间计划来执行任务

如例程3-9所示，EchoServer就利用上述线程池来负责与客户通信的任务。
例程3-9  EchoServer.java（使用java.util.concurrent包中的线程池类）
```java
package multithread3;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
public class EchoServer {
private int port=8000;
private ServerSocket serverSocket;
private ExecutorService executorService;    //线程池
private final int POOL_SIZE=4;      //单个CPU时线程池中工作线程的数目
public EchoServer() throws IOException {
serverSocket = new ServerSocket(port);
//创建线程池
//Runtime的availableProcessors()方法返回当前系统的CPU的数目
//系统的CPU越多，线程池中工作线程的数目也越多 
executorService= Executors.newFixedThreadPool( 
Runtime.getRuntime().availableProcessors() * POOL_SIZE);
System.out.println("服务器启动");
}
public void service() {
while (true) {
Socket socket=null;
try {
socket = serverSocket.accept();
executorService.execute(new Handler(socket));
}catch (IOException e) {
e.printStackTrace();
}
}
}
public static void main(String args[])throws IOException {
new EchoServer().service();
}
}
```

class Handler implements Runnable{…}
在EchoServer的构造方法中，调用Executors.newFixedThreadPool()创建了具有固定工作线程数目的线程池。在 EchoServer的service()方法中，通过调用executor- Service.execute()方法，把与客户通信的任务交给了ExecutorService线程池来执行。

### 3.6.4  使用线程池的注意事项

虽然线程池能大大提高服务器的并发性能，但使用它也会存在一定风险。与所有多线程应用程序一样，用线程池构建的应用程序容易产生各种并发问题，如对 共享资源的竞争和死锁。此外，如果线程池本身的实现不健壮，或者没有合理地使用线程池，还容易导致与线程池有关的死锁、系统资源不足和线程泄漏等问题。
1. 死锁
任何多线程应用程序都有死锁风险。造成死锁的最简单的情形是，线程A持有对象X的锁，并且在等待对象Y的锁，而线程B持有对象Y的锁，并且在等待对象X的锁。线程A与线程B都不释放自己持有的锁，并且等待对方的锁，这就导致两个线程永远等待下去，死锁就这样产生了。
虽然任何多线程程序都有死锁的风险，但线程池还会导致另外一种死锁。在这种情形下，假定线程池中的所有工作线程都在执行各自任务时被阻塞，它们都在 等待某个任务A的执行结果。而任务A依然在工作队列中，由于没有空闲线程，使得任务A一直不能被执行。这使得线程池中的所有工作线程都永远阻塞下去，死锁 就这样产生了。

2. 系统资源不足
如果线程池中的线程数目非常多，这些线程会消耗包括内存和其他系统资源在内的大量资源，从而严重影响系统性能。

3. 并发错误
线程池的工作队列依靠wait()和notify()方法来使工作线程及时取得任务，但这两个方法都难于使用。
如果编码不正确，可能会丢失通知，导致工作线程一直保持空闲状态，无视工作队列中需要处理的任务。因此使用这些方法时，必须格外小心，即便是专家也 可能在这方面出错。最好使用现有的、比较成熟的线程池。例如，直接使用java.util.concurrent包中的线程池类。 

4. 线程泄漏
使用线程池的一个严重风险是线程泄漏。对于工作线程数目固定的线程池，如果工作线程在执行 任务时抛出RuntimeException 或Error，并且这些异常或错误没有被捕获，那么这个工作线程就会异常终止，使得线程池永久失去了一个工作线程。如果所有的工作线程都异常终止，线程池 就最终变为空，没有任何可用的工作线程来处理任务。
导致线程泄漏的另一种情形是，工作线程在执行一个任务时被阻塞，如等待用户的输入数据，但是由于用户一直不输入数据（可能是因为用户走开了），导致 这个工作线程一直被阻塞。这样的工作线程名存实亡，它实际上不执行任何任务了。假如线程池中所有的工作线程都处于这样的阻塞状态，那么线程池就无法处理新 加入的任务了。

5. 任务过载
当工作队列中有大量排队等候执行的任务时，这些任务本身可能会消耗太多的系统资源而引起系统资源缺乏。
综上所述，线程池可能会带来种种风险，为了尽可能避免它们，使用线程池时需要遵循以下原则。
（1）如果任务A在执行过程中需要同步等待任务B的执行结果，那么任务A不适合加入到线程池的工作队列中。如果把像任务A一样的需要等待其他任务执行结果的任务加入到工作队列中，可能会导致线程池的死锁。
（2）如果执行某个任务时可能会阻塞，并且是长时间的阻塞，则应该设定超时时间，避免工作线程永久的阻塞下去而导致线程泄漏。在服务器程序中，当线程等待客户连接，或者等待客户发送的数据时，都可能会阻塞。可以通过以下方式设定超时时间：
- 调用ServerSocket的setSoTimeout(int timeout)方法，设定等待客户连接的超时时间，参见本章3.5.1节（SO_TIMEOUT选项）；
- 对于每个与客户连接的Socket，调用该Socket的setSoTimeout(int timeout)方法，设定等待客户发送数据的超时时间，参见本书第2章的2.5.3节（SO_TIMEOUT选项）。
（3）了解任务的特点，分析任务是执行经常会阻塞的I/O操作，还是执行一直不会阻塞的运算操作。前者时断时续地占用CPU，而后者对CPU具有更高的利用率。预计完成任务大概需要多长时间？是短时间任务还是长时间任务？
根据任务的特点，对任务进行分类，然后把不同类型的任务分别加入到不同线程池的工作队列中，这样可以根据任务的特点，分别调整每个线程池。
（4）调整线程池的大小。线程池的最佳大小主要取决于系统的可用CPU的数目，以及工作队列中任务的特点。假如在一个具有 N 个CPU的系统上只有一个工作队列，并且其中全部是运算性质（不会阻塞）的任务，那么当线程池具有 N 或 N+1 个工作线程时，一般会获得最大的 CPU 利用率。
如果工作队列中包含会执行I/O操作并常常阻塞的任务，则要让线程池的大小超过可用CPU的数目，因为并不是所有工作线程都一直在工作。选择一个典 型的任务，然后估计在执行这个任务的过程中，等待时间（WT）与实际占用CPU进行运算的时间（ST）之间的比例WT/ST。对于一个具有N个CPU的系 统，需要设置大约N×(1+WT/ST)个线程来保证CPU得到充分利用。
当然，CPU利用率不是调整线程池大小过程中唯一要考虑的事项。随着线程池中工作线程数目的增长，还会碰到内存或者其他系统资源的限制，如套接字、打开的文件句柄或数据库连接数目等。要保证多线程消耗的系统资源在系统的承载范围之内。
（5）避免任务过载。服务器应根据系统的承载能力，限制客户并发连接的数目。当客户并发连接的数目超过了限制值，服务器可以拒绝连接请求，并友好地告知客户：服务器正忙，请稍后再试

## 3.7  关闭服务器

前面介绍的EchoServer服务器都无法关闭自身，只有依靠操作系统来强行终止服务器程序。这种强行终止服务器程序的方式尽管简单方便，但是会 导致服务器中正在执行的任务被突然中断。如果服务器处理的任务不是非常重要，允许随时中断，则可以依靠操作系统来强行终止服务器程序；如果服务器处理的任 务非常重要，不允许被突然中断，则应该由服务器自身在恰当的时刻关闭自己。

本节介绍的EchoServer服务器就具有关闭自己的功能。它除了在8000端口监听普通客户程序EchoClient的连接外，还会在8001 端口监听管理程序AdminClient的连接。当EchoServer服务器在8001端口接收到了AdminClient发送的“shutdown” 命令时，EchoServer就会开始关闭服务器，它不会再接收任何新的EchoClient进程的连接请求，对于那些已经接收但是还没有处理的客户连 接，则会丢弃与该客户的通信任务，而不会把通信任务加入到线程池的工作队列中。另外，EchoServer会等到线程池把当前工作队列中的所有任务执行 完，才结束程序。

如例程3-10所示是EchoServer的源程序，其中关闭服务器的任务是由shutdown- Thread线程来负责的。
例程3-10  EchoServer.java(具有关闭服务器的功能) 
```java
package multithread4;
import java.io.*;
import java.net.*;
import java.util.concurrent.*;
public class EchoServer {
private int port=8000;
private ServerSocket serverSocket;
private ExecutorService executorService;     //线程池
private final int POOL_SIZE=4;       //单个CPU时线程池中工作线程的数目

private int portForShutdown=8001;      //用于监听关闭服务器命令的
端口
private ServerSocket serverSocketForShutdown;
private boolean isShutdown=false;     //服务器是否已经关闭
private Thread shutdownThread=new Thread(){     //负责关闭服务器的线程
public void start(){
this.setDaemon(true);       //设置为守护线程（
也称为后台线程）
super.start(); 
}
public void run(){
while (!isShutdown) {
Socket socketForShutdown=null;
try {
socketForShutdown= serverSocketForShutdown.accept();
BufferedReader br = new BufferedReader(
new InputStreamReader(socketForShutdown.getInputStream()));
String command=br.readLine();
if(command.equals("shutdown")){
long beginTime=System.currentTimeMillis(); 
socketForShutdown.getOutputStream().write("服务器正在关闭\r\n".getBytes());
isShutdown=true;
//请求关闭线程池
//线程池不再接收新的任务，但是会继续执行完工作队列中现有的任务
executorService.shutdown();  

//等待关闭线程池，每次等待的超时时间为30秒
while(!executorService.isTerminated())
executorService.awaitTermination(30,TimeUnit.SECONDS); 

serverSocket.close(); //关闭与EchoClient客户通信的ServerSocket 
long endTime=System.currentTimeMillis(); 
socketForShutdown.getOutputStream().write(("服务器已经关闭，"+
"关闭服务器用了"+(endTime-beginTime)+"毫秒\r\n").getBytes());
socketForShutdown.close();
serverSocketForShutdown.close();

}else{
socketForShutdown.getOutputStream().write("错误的命令\r\n".getBytes());
socketForShutdown.close();
}  
}catch (Exception e) {
e.printStackTrace();
} 
} 
}
};
public EchoServer() throws IOException {
serverSocket = new ServerSocket(port);
serverSocket.setSoTimeout(60000);    //设定等待客户连接的超过时间为60秒
serverSocketForShutdown = new ServerSocket(portForShutdown);
//创建线程池
executorService= Executors.newFixedThreadPool( 
Runtime.getRuntime().availableProcessors() * POOL_SIZE);

shutdownThread.start();     //启动负责关闭服务器的线程
System.out.println("服务器启动");
}

public void service() {
while (!isShutdown) {
Socket socket=null;
try {
socket = serverSocket.accept();
//可能会抛出SocketTimeoutException和SocketException
socket.setSoTimeout(60000);     //把等待客户发送数据的超时时间设为60秒 
executorService.execute(new Handler(socket));
//可能会抛出RejectedExecutionException
}catch(SocketTimeoutException e){
//不必处理等待客户连接时出现的超时异常
}catch(RejectedExecutionException e){
try{
if(socket!=null)socket.close();
}catch(IOException x){}
return;
}catch(SocketException e) {
//如果是由于在执行serverSocket.accept()方法时，
//ServerSocket被ShutdownThread线程关闭而导致的异常，就退出service()方法
if(e.getMessage().indexOf("socket closed")!=-1)return;
}catch(IOException e) {
e.printStackTrace();
}
}
}
public static void main(String args[])throws IOException {
new EchoServer().service();
}
}
```

class Handler implements Runnable{…}
shutdownThread线程负责关闭服务器。它一直监听8001端口，如果接收到了AdminClient发送的“shutdown”命令， 就把isShutdown变量设为true。shutdownThread线程接着执行executorService.shutdown()方法，该方 法请求关闭线程池，线程池将不再接收新的任务，但是会继续执行完工作队列中现有的任务。shutdownThread线程接着等待线程池关闭：
while(!executorService.isTerminated())
executorService.awaitTermination(30,TimeUnit.SECONDS);  //等待30秒
当线程池的工作队列中的所有任务执行完毕，executorService.isTerminated()方法就会返回true。
shutdownThread线程接着关闭监听8000端口的ServerSocket，最后再关闭监听8001端口的ServerSocket。
shutdownThread线程在执行上述代码时，主线程正在执行EchoServer的service()方法。
shutdownThread线程一系列操作会对主线程造成以下影响。
- 如果shutdownThread线程已经把isShutdown变量设为true，而主线程正准备执行service()方法的下一轮while(!isShutdown){…}循环时，由于isShutdown变量为true，就会退出循环。
- 如果shutdownThread线程已经执行了监听8 000端口的ServerSocket的close()方法，而主线程正在执行该ServerSocket的accept()方法，那么该方法会抛出 SocketException。EchoServer的service()方法捕获了该异常，在异常处理代码块中退出service()方法。
- 如果shutdownThread线程已经执行了executorService.shutdown()方法，而主线程正在执行 executorService.execute(…)方法，那么该方法会抛出Rejected- ExecutionException。EchoServer的service()方法捕获了该异常，在异常处理代码块中退出service()方法。
- 如果shutdownThread线程已经把isShutdown变量设为true，但还没有调用监听8 000端口的ServerSocket的close()方法，而主线程正在执行ServerSocket的accept()方法，主线程阻塞60秒后会抛 出SocketTimeoutException。在准备执行service()方法的下一轮while(!isShutdown){…}循环时，由于 isShutdown变量为true，就会退出循环。

由此可见，当shutdownThread线程开始执行关闭服务器的操作时，主线程尽管不会立即终止，但是迟早会结束运行。

如例程3-11所示是AdminClient的源程序，它负责向EchoServer发送“shutdown”命令，从而关闭EchoServer。
例程3-11  AdminClient.java
```java
package multithread4;
import java.net.*;
import java.io.*;
public class AdminClient{
public static void main(String args[]){
Socket socket=null;
try{
socket=new Socket("localhost",8001);
//发送关闭命令
OutputStream socketOut=socket.getOutputStream();
socketOut.write("shutdown\r\n".getBytes());

//接收服务器的反馈
BufferedReader br = new BufferedReader(
new InputStreamReader(socket.getInputStream()));
String msg=null;
while((msg=br.readLine())!=null)
System.out.println(msg);
}catch(IOException e){
e.printStackTrace();
}finally{
try{
if(socket!=null)socket.close();
}catch(IOException e){e.printStackTrace();}
}
}
}
```
下面按照以下方式运行EchoServer、EchoClient和AdminClient，以观察EchoServer服务器的关闭过程。EchoClient类的源程序参见本书第1章的1.5.2节的例程1-3。

（1）先运行EchoServer，然后运行AdminClient。EchoServer与AdminClient进程都结束运行，并且在AdminClient的控制台打印如下结果：
服务器正在关闭
服务器已经关闭，关闭服务器用了60毫秒    
（2）先运行EchoServer，再运行EchoClient，然后再运行AdminClient。EchoServer程序不会立即结束，因为 它与EchoClient的通信任务还没有结束。在EchoClient的控制台中输入“bye”， EchoServer、EchoClient和AdminClient进程都会结束运行。
（3）先运行EchoServer，再运行EchoClient，然后再运行AdminClient。EchoServer程序不会立即结束，因为 它与EchoClient的通信任务还没有结束。不要在EchoClient的控制台中输入任何字符串，过60秒后，EchoServer等待 EchoClient的发送数据超时，结束与EchoClient的通信任务，EchoServer和AdminClient进程结束运行。如果在 EchoClient的控制台再输入字符串，则会抛出“连接已断开”的SocketException。

## 3.8  小结
在EchoServer的构造方法中可以设定3个参数。
- 参数port：指定服务器要绑定的端口。
- 参数backlog：指定客户连接请求队列的长度。
- 参数bindAddr：指定服务器要绑定的IP地址。 
ServerSocket的accept()方法从连接请求队列中取出一个客户的连接请求，然后创建与客户连接的Socket对象，并将它返回。如 果队列中没有连接请求，accept()方法就会一直等待，直到接收到了连接请求才返回。SO_TIMEOUT选项表示ServerSocket的 accept()方法等待客户连接请求的超时时间，以毫秒为单位。如果SO_TIMEOUT的值为0，表示永远不会超时，这是SO_TIMEOUT的默认 值。可以通过ServerSocket的setSo- Timeout()方法来设置等待连接请求的超时时间。如果设定了超时时间，那么当服务器等待的时间超过了超时时间后，就会抛出 SocketTimeoutException，它是Interrupted- Exception的子类。
许多实际应用要求服务器具有同时为多个客户提供服务的能力。用多个线程来同时为多个客户提供服务，这是提高服务器的并发性能的最常用的手段。本章采用3种方式来重新实现EchoServer，它们都使用了多线程：

（1）为每个客户分配一个工作线程；
（2）创建一个线程池，由其中的工作线程来为客户服务；
（3）利用java.util.concurrent包中现成的线程池，由它的工作线程来为客户服务。

第一种方式需要频繁地创建和销毁线程，如果线程执行的任务本身很简短，那么有可能服务器在创建和销毁线程方面的开销比在实际执行任务上的开销还要 大。线程池能很好地避免这一问题。线程池先创建了若干工作线程，每个工作线程执行完一个任务后就会继续执行下一个任务，线程池减少了创建和销毁线程的次 数，从而提高了服务器的运行性能。