
1. 	Java 中对于字符串的编码处理可以用　new String(str.getBytes(“utf-8”),”utf-8”);来做处理，getBytes(“utf-8”)是说把str按照utf-8来编码，并以字节方式表示。New String(charset)是用来把字节数组按照charset编码进行组合识别，最后转换为unicode存储。两者组合起来形成一个完全可逆的过程。

2. setCharacterEncoding() 
该函数用来设置http请求或者相应的编码。 
对于request，是指提交内容的编码，指定后可以通过getParameter()则直接获得正确的字符串，如果不指定，则默认使用iso8859-1编码，需要进一步处理。参见下述"表单输入"。值得注意的是在执行setCharacterEncoding()之前，不能执行任何getParameter()。java doc上说明：This method must be called prior to reading request parameters or reading input using getReader()。而且，该指定只对POST方法有效，对GET方法无效。分析原因，应该是在执行第一个getParameter()的时候，java将会按照编码分析所有的提交内容，而后续的getParameter()不再进行分析，所以setCharacterEncoding()无效。而对于GET方法提交表单是，提交的内容在URL中，一开始就已经按照编码分析所有的提交内容，setCharacterEncoding()自然就无效。 
对于response，则是指定输出内容的编码，同时，该设置会传递给浏览器，告诉浏览器输出内容所采用的编码。 

3. 表单输入
User input  *(gbk:d6d0 cec4)  browser  *(gbk:d6d0 cec4)  web server  iso8859-1(00d6 00d 000ce 00c4)  class，需要在class中进行处理：getbytes("iso8859-1")为d6 d0 ce c4，new String("gbk")为d6d0 cec4，内存中以unicode编码则为4e2d 6587。
用户输入的编码方式和页面指定的编码有关，也和用户的操作系统有关，所以是不确定的，上例以gbk为例。 
从browser到web server，可以在表单中指定提交内容时使用的字符集，否则会使用页面指定的编码。而如果在url中直接用?的方式输入参数，则其编码往往是操作系统本身的编码，因为这时和页面无关。上述仍旧以gbk编码为例。
Web server接收到的是字节流，默认时（getParameter）会以iso8859-1编码处理之，结果是不正确的，所以需要进行处理。但如果预先设置了编码（通过request. setCharacterEncoding ()），则能够直接获取到正确的结果。
在页面中指定编码是个好习惯，否则可能失去控制，无法指定正确的编码

4. jsp编译
指定文件的存储编码，很明显，该设置应该置于文件的开头。例如：<%@page pageEncoding="GBK"%>。另外，对于一般class文件，可以在编译的时候指定编码。

5. jsp输出 
指定文件输出到browser是使用的编码，该设置也应该置于文件的开头。例如：<%@ page contentType="text/html; charset= GBK" %>。该设置和response.setCharacterEncoding("GBK")等效。 

6. meta设置 
指定网页使用的编码，该设置对静态网页尤其有作用。因为静态网页无法采用jsp的设置，而且也无法执行response.setCharacterEncoding()。例如：<META http-equiv="Content-Type" content="text/html; charset=GBK" /> 
如果同时采用了jsp输出和meta设置两种编码指定方式，则jsp指定的优先。因为jsp指定的直接体现在response中。 
需要注意的是，apache有一个设置可以给无编码指定的网页指定编码，该指定等同于jsp的编码指定方式，所以会覆盖静态网页中的meta指定。所以有人建议关闭该设置。 
