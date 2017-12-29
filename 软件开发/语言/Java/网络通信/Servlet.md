## URL
#### 1. 一致资源定位器URL
URL(Uniform Resource Locator)是一致资源定位器的简称，它表示Internet上某一资源的地址。通过URL我们可以访问Internet上的各种网络资源，比如最常见的WWW，FTP站点。浏览器通过解析给定的URL可以在网络上查找相应的文件或其他资源。
#### 2.  URL的组成
protocol://resourceName协议名（protocol）指明获取资源所使用的传输协议，如http、ftp、gopher、file等，资源名（resourceName）则应该是资源的完整地址，包括主机名、端口号、文件名或文件内部的一个引用。例如：
http://www.sun.com/ 协议名://主机名
http://home.netscape.com/home/welcome.html 协议名://机器名＋文件名
http://www.gamelan.com:80/Gamelan/network.html#BOTTOM 协议名://机器名＋端口号＋文件名＋内部引用.

#### 3. 创建一个URL
为了表示URL， java.net中实现了类URL。我们可以通过下面的构造方法来初始化一个URL对象：
　　（1） public URL (String spec);
　　　　　通过一个表示URL地址的字符串可以构造一个URL对象。
　　　　　URL urlBase=new URL("http://www. 263.net/") 
　　（2） public URL(URL context, String spec);
　　　　　通过基URL和相对URL构造一个URL对象。
　　　　　URL net263=new URL ("http://www.263.net/");
　　　　　URL index263=new URL(net263, "index.html")
　　（3） public URL(String protocol, String host, String file);
　　　　　new URL("http", "www.gamelan.com", "/pages/Gamelan.net. html");
　　（4） public URL(String protocol, String host, int port, String file);
URL gamelan=new URL("http", "www.gamelan.com", 80, "Pages/Gamelan.network.html");
注意：类URL的构造方法都声明抛弃非运行时例外（MalformedURLException），因此生成URL对象时，我们必须要对这一例外进行处理，通常是用try-catch语句进行捕获。格式如下：
try{
　　　　　URL myURL= new URL(…)
　　}catch (MalformedURLException e){
　　…　　}

#### 4、 解析一个URL
一个URL对象生成后，其属性是不能被改变的，但是我们可以通过类URL所提供的方法来获取这些属性：
　　　public String getProtocol() 获取该URL的协议名。
　　　public String getHost() 获取该URL的主机名。
　　　public int getPort() 获取该URL的端口号，如果没有设置端口，返回-1。
　　　public String getFile() 获取该URL的文件名。
　　　public String getRef() 获取该URL在文件中的相对位置。
　　　public String getQuery() 获取该URL的查询信息。
　　　public String getPath() 获取该URL的路径
　　  public String getAuthority() 获取该URL的权限信息
　　　public String getUserInfo() 获得使用者的信息
　　　public String getRef() 获得该URL的锚

#### 5、从URL读取WWW网络资源
当我们得到一个URL对象后，就可以通过它读取指定的WWW资源。这时我们将使用URL的方法openStream()，其定义为：
　　　　　　　　　InputStream openStream();
　　方法openSteam()与指定的URL建立连接并返回InputStream类的对象以从这一连接中读取数据。
  
```java
public class URLReader {
　　public static void main(String[] args) throws Exception { 
　　　　//声明抛出所有例外
　　　　URL tirc = new URL("http://www.tirc1.cs.tsinghua.edu.cn/"); 
　　　　//构建一URL对象
　　　　BufferedReader in = new BufferedReader(new InputStreamReader(tirc.openStream()));
　　　　//使用openStream得到一输入流并由此构造一个BufferedReader对象
　　　　String inputLine;
　　　　while ((inputLine = in.readLine()) != null) 
　　　　　　　//从输入流不断的读数据，直到读完为止
　　　　　　　System.out.println(inputLine); //把读入的数据打印到屏幕上
　　　　in.close(); //关闭输入流
　　}
　　}
```

#### 6、通过URLConnetction连接WWW

通过URL的方法openStream()，我们只能从网络上读取数据，如果我们同时还想输出数据，例如向服务器端的CGI程序发送一些数据，我们必须先与URL建立连接，然后才能对其进行读写，这时就要用到类URLConnection了。CGI是公共网关接口（Common Gateway Interface）的简称，它是用户浏览器和服务器端的应用程序进行连接的接口，有关CGI程序设计，请读者参考有关书籍。
　　类URLConnection也在包java.net中定义，它表示Java程序和URL在网络上的通信连接。当与一个URL建立连接时，首先要在一个URL对象上通过方法openConnection()生成对应的URLConnection对象。例如下面的程序段首先生成一个指向地址http://edu.chinaren.com/index.shtml的对象，然后用openConnection（）打开该URL对象上的一个连接，返回一个URLConnection对象。如果连接过程失败，将产生IOException.
  
```java
　Try{
　　　　URL netchinaren = new URL ("http://edu.chinaren.com/index.shtml");
　　　　URLConnectonn tc = netchinaren.openConnection();
　　}catch(MalformedURLException e){ //创建URL()对象失败
　　…
　　}catch (IOException e){ //openConnection()失败
　　…
　　}
```
　　类URLConnection提供了很多方法来设置或获取连接参数，程序设计时最常使用的是getInputStream()和getOurputStream(),其定义为：
　　　　　InputSteram getInputSteram();
　　　　　OutputSteram getOutputStream();
　　通过返回的输入/输出流我们可以与远程对象进行通信。看下面的例子：
```java
　URL url =new URL ("http://www.javasoft.com/cgi-bin/backwards"); 
　　//创建一URL对象
　　URLConnectin con=url.openConnection(); 
　　//由URL对象获取URLConnection对象
　　DataInputStream dis=new DataInputStream (con.getInputSteam()); 
　　//由URLConnection获取输入流，并构造DataInputStream对象
　　PrintStream ps=new PrintSteam(con.getOutupSteam());
　　//由URLConnection获取输出流，并构造PrintStream对象
　　String line=dis.readLine(); //从服务器读入一行
　　ps.println("client…"); //向服务器写出字符串 "client…"
　　
```
其中backwards为服务器端的CGI程序。实际上，类URL的方法openSteam（）是通过URLConnection来实现的。它等价于
　　　　`openConnection().getInputStream();`
　　基于URL的网络编程在底层其实还是基于下面要讲的Socket接口的。WWW，FTP等标准化的网络服务都是基于TCP协议的，所以本质上讲URL编程也是基于TCP的一种应用.

## Tomcat

1. 是Servlet和Jsp的容器，源代码完全公开。
2. 具有Web服务器的基本功能。
3. 提供数据库连接池、SSL、Proxy等许多通用组件

Web服务器做为一个TCP程序必须有端口号

在看Tomcat启动方式以前先看一下Java类的两种基本启动工：
1. Java <各种命令选项> <java启动类>
2. Java –jar <各种命令选项> <启动类所在的jar包>
**注意：要在启动类所在的jar包的MANIFEST.MF文件中增加一个名称为Main-Class的属性，设置其值为启动类。**

Tomcat就是用第二种启动
	Java –jar bootstrap.jar start
其中bootstrap.jar是tomcat启动类所在的jar包，start是传给启动类的参数

Tomcat.exe只是一个启动BootStrap类的window外壳包装程序，运行哪个程序与window注册表有关系，startup.bat是用来检查与设置环境变量的，真正启动tomcat的是catalina.bat文件。

### Tomcat虚拟目录

	将Web服务器的本地文件系统中的其个目录映射成一个虚拟目录的过程叫做发布。一个文件系统可以被映射成多个虚拟目录。
	一个Web站点必须有且只有一个虚拟根目录，其他虚拟目录都以其子目录形式出现，子目录可以是多级目录形式，tomcat按照最长路径匹配原则进行处理，也就是从最深的子目录向上匹配。如果子虚拟目录与上级虚拟目录有相同名称的文件，服务器就会加载子虚拟目录的，而不是上级虚拟目录的

设置Web站点的虚拟子目录的方法：
1. 修改/conf/server.xml文件中的<Context>元素，必须嵌套在<Host>元素之中，
<Context path=”/it” docBase=”d:\test” debug=”0”/>虚拟子目录可以是一个文件夹也可以是一个war包。
2. 如果<Host>元素指定appBase目录下的子目录中包含WEB-INF/web.xml，则该目录被自动设置成web应用程序。
3. 如果<Host>元素指定appBase目录下包含war文件，这些war文件被自动发布，war包中可以不包含WEB-INF/web.xml。
4. 如果<Host>元素指定的appBase目录中的xml文件包含<context>元素，这些元素与server.xml中的<Context>效果相同。




## HTTP协议

### HTTP1.1与1.0的比较

1. 1.1支持一次TCP连接传递多个HTTP请求和响应，减少了建立和关闭连接的消耗和延迟。1.0每次请求都要打开和关闭一个TCP连接
2. 1.1还允许客户端不用等待上一次请求的返回结果就可以发出下一次请求，但服务器端必须按请求的次序来回送结果。这样显著减少了整个下载过程所需要的时间，这个功能称为Pipeline。
3. 1.1中增加Host请求头字段，使浏览器可以使用主机名来明确表示要访问服务器上的哪个Web站点，这才实现了在一台Web服务器上可以在同一个IP地址和端口号上使用不同的主机名来创建多个虚拟Web站点
4. 1.1的持续连接也需要增加新的请求头来帮助实现，如Connection请求头的值为Keep-Alive时，客户端通知服务器返回本次请求后保持连接；当Connection的值为Close时才关闭连接。另上还提供了与身份论证、状态管理和Cache等机制相关的请求头与响应头。

### HTTP消息的格式
	浏览器发出请求信息和Web服务器回送的响应信息都叫HTTP消息，它有一定严格规定的格式。
	一个完整的消息包括：一个请求行，若干消息头，以及实体内容。其中的一些消息和实体内容都是可选的，消息头和实体内容之间要用空行隔开


## Servlet基础

Servlet和jsp写成的动态网页都需要一个引擎来解释执行，tomcat就是这样一个引擎，只有引擎才会与浏览器直接进行信息交换，动态网页程序不与浏览器进行交换，动态网页只与引擎进行信息交换，由引擎将浏览器的信息传递给动态网页程序，并将动态网页程序生成的结果回送给浏览器。Servlet 与普通的java程序相比，只是输入信息的来源和输出结果的目标不一样。

- Servlet引擎与Servlet程序之间采用Servlet　API进行通信。
- 一个Servlet程序就是一个在Web服务器端运行的java类，它必须实现javax.servlet.Servlet接口，Servlet接口定义了Servlet引擎与Servlet程序之间通信的协议约定。
- 	Servlet API中提供了一个实现Servlet接口的最简单的Servlet类，GenericServlet实现了Servlet程序的基本特征和功能。HttpServlet是GenericServlet的子类，在其基础上进行了一些针对HTTP的扩充。HttpServlet中有一个service方法，servlet引擎调用这个方法进行处理，自己编写程序时可以覆盖这个方法。
- 	HTML文件可以直接用浏览器打开并查看运行效果，但是，Servlet程序必须通过Web服务器和Servlet引擎来启动运行，Servlet程序必须在Web应用程序的Web.xml文件中进行注册和映射其访问路径，才可以被Servlet引擎加载和被外界访问。客户端将使用Servlet所映射的对外访问路径来访问Servlet，而不是使用Servlet名称来访问。
- 	Serlvet激活器：一个Web应用程序的部署描述符除了包含该应用程序内部的Web.xml文件的设置信息外，还包含<Tomcat>/conf目录下的Web.xml文件中设置的全局信息。Tomcat的Web.xml中就有一个invoker的Servlet类的注册信息，它的作用就是去激活和调用任何其他Servlet，在tomcat启动时加载。

### Servlet类装载器
- 	Java虚拟机使用每一个类的第一件事情就是将该类的字节码装载进来，装载类字节码的功能是由类加载器完成的，类装载器负责根据一个类的名称来定位和生成类的字节码数据后返回给java虚拟机，不管类装载器采用什么方式，只要能够在内存中制造出给java虚拟机调用的类字节码即可，把类装载器描述为字节码的制造器更容易让人理解。
- 	当一个类被加载后，java虚拟机将其编译为可执行代码存储在内存中，并将索引信息存储进一个HashTable中，其索引关键字就是这个类的完整类名。当java虚拟机需要用到某个类时，它就使用类名在HashTable中查找相应的信息，如果该可执行代码已经存在，就执行，不存在就调用类加载器加载。类装载器装载某个类的字节码的过程实际上就是在创建Class类的一个实例对象
- ServletConfig对象，代表的是在Web.xml中配置的Servlet信息，可以通过它得到参数名，参数值，servlet名，以及servletContext等
- 	项目中类路径中包含了servlet-api.jar编写servlet时不会出现编译问题了，但是运行时还是不能脱离serlvet容器，因为像HttpServletRequest、HttpServletResponse这样的类在servlet-api.jar中只是定义了一个接口，具体的实现是servlet容器来做的。
- 	Servlet引擎为每个web应用程序都创建一个对应的ServletContext对象，它被包含在ServletConfig对象中，在Servlet容器初始化Servlet对象时随着ServletConfig对象提供给Servlet。与其它接口一样，ServletContext接口的实现类也是由Servlet引擎提供的。修改它可以改变整个应用范围内的一些参数信息。
1．可以在Tomcat的server.xml文件的<Context>元素中增加<Parameter>元素来设置初始化参数。
2．可以在web.xml中<web-app>根目录中增加<context-param>元素来设置

### 访问资源文件
- 	ServletContext接口定义了一些用于访问Web应用程序的内部资源文件的方法，它可以访问web项目下的各种形式的文件，包括Web-INF目录中不能被外界访问的文件。
- 	GetResourcePaths(“/”)：得到/下包含的目录和文件的路径，是相对于WebRoot的相对路径。
- GetResource ：返回映射到某个资源上的URL对象。
- GetResourceAsStream：返回连接到某个资源上的InputStream对象。
- getRealPath：获得虚拟路径所映射的本地路径。

在Servlet程序中为什么不适合用FileInputStream访问文件
1．一个Web应用程序在本地文件系统中的安装位置是可以变化的，所以Servlet程序不应该用绝对路径的形式来访问Web应用程序中的某个文件。
2．在某个java类中使用的相对路径是相对于当前的工作目录而言的，这个工作目录通常是执行java命令的目录，而不是当前正在执行的java类所在的目录，这一特性导致很难在java类中直接使用相对目录。
ClassLoader和ServletContext访问资源的不同点
1．ClassLoader类专门提供了getResource等方法去装载资源文件，它们使用与查找java类文件同样的方式去查找资源文件，即在类装载器所搜索的目录中查找，由于web应用程序的类装载器会搜索WEB-INF/classes目录，所以ClassLoader.getResourcesStream方法也可以访问该目录中的资源文件，但它不能访问Web应用程序内的其他目录中的资源。
2．ServletContext类中的访问资源的方法是通过Servlet容器来获得资源文件的，它使得ServletContext可以访问Web应用内部的任意文件。

### HttpServletResponse
	浏览器接收到的中文字符并不是中文字符本身，而是它的某种字符集编码数据。中文字符乱码产生有两种原因：
1．Servlet程序输出给浏览器的内容不是任何一种正确的中文字符编码；
2．浏览网页文档时所采用的字符集编码与它所接收到的中文字符本身的字符集编码不一致。
ServletResponse接口中定义了setCharacterEncoding、setContentType和setLocale等方法来指定servletResponse.getWriter方法返回的PrintWriter对象所使用的字符集。
setCharacterEncoding设置的字符集会覆盖另外两个，一般应用的话最好是用setContentType方法，因为通过这个方法浏览器会自动识别并设置字符集类型。用setCharacterEncoding方法的话浏览器不会自动识别设置的字符集，需要用户手动去改浏览器的默认字符集。

	Response.setHeader(“Refresh”,”2;URL=…”);两秒后自动跳到指定页面
	禁止浏览器缓存当前文档内容：
		Response.setDateHeader(“Expires”,0);
		Response.setHeader(“Cache-Control”,”no-cache”);
		Response.setHeader(“Pragma”,”no-cache”);

	<meta>标签是用来在html文件中像JSP文件中以编程方式实现的响应头功能。

getOutputStream方法用于返回Servlet引擎创建的字符输出流对象。
getWriter方法用于返回servlet引擎创建的字符输出流对象。
Servlet程序向ServletoutputStream或PrintWriter对象中写入数据将被Servlet引擎获取，Servlet引擎将这些数据当做响应消息的正文，然后再与响应状态行和各响应头组合后输出到客户端。

### 请求重定向与转发
在Servlet程序中，如需要调用另外一个资源对浏览器的请求进行响应，可以用两种方式来实现
1．调用RequestDispatcher.forward方法实现的请求转发
2．调用HttpServletResponse.sendRedirect方法实现请求重定向

RequestDispatcher实例对象是由Servlet引擎创建，它用于包装一个要被其他资源调用的资源（如Servlet、HTML文件、JSP文件等）并可以通过其中的方法将客户端的请求转发给所包装的资源。它有两个方法：forward和include方法，分别用于将请求转发到RequestDispatcher对象封装的资源和将RequestDispatcher对象封装的资源作为当前响应内容的一部分包含进来。
- ServletContext接口中定义了两个方法来得到ServletDispatcher对象：getRequestDispatcher和getNamedDispatcher方法。ServletRequest接口中也定义了一个getRequestDispatcher方法。RequestDispatcher对象只能包装当前Web应用程序中的资源，所以forward方法和include方法只能在同一个Web应用内的资源之间转发请求和实现资源的包含。
- Forward：调用者与被调用者的响应头与状态码都不会被忽略
- Inlude：被调用者的响应头与状态码会被忽略
- sendRedirect只是生成302响应码和Location响应头，从而通知客户端去重新访问Location响应头中指定的URL，它可以使用相对URL，Serlvet引擎会自动将相对URL翻译成绝对URL，再生成Location字段，它还会自动创建包含重定向URL的超连接的文本正文，该文本正文显示在不支持自动重定向的旧浏览器中，以便用户可以手工进入该URL所指向的页面

### 两者区别：
1．Forward只能将请求转发给同一个Web应用中的组件，而sendRedirect可以重定向到任何URL，如果传给sendRedirect方法的URL以“/”开头，它是相对于整个Web站点的根目录，如传给forward方法的URL以以“/”开头，它是相对于Web应用程序的根目录
2．sendRedirect使URL地址栏中的URL变为重定向的目标地址，forward不变
3．sendRedirect相当于两次请求不共享request,response，forward算一次请求，共享request,response