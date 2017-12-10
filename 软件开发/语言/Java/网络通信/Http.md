
## Persistent HTTP connections

早先的WEB网站都是由一些文字页面组成，一个页面可能就是一个简单的http请求，里面也不会嵌套css，gif类似的资源文件，所以在1.0标准实现的时候没有去考虑连接的效率（用完就丢弃），现在的页面很多都非常复杂，在里面嵌套二三十个资源文件是很正常的事情,创建二三十个tcp连接去下载这些资源，对于tcp连接的利用率非常低。

另外一方面，tcp连接打开之后，首先采用slow start算法，开始先传输几个小的packet探测网络情况来确定传输速率，而且页面上嵌入的资源文件往往都比较小，也就是说如果采用http 1.0,就意味着很多的资源传输都是在slow start下完成的，效率也很低。

Persistent HTTP connections就是让连接重复利用，不需要为每个资源文件都建立一个tcp连接。也不存在slow start问题。

http规范里面对于持久连接的数量建议为2（firefox和iexplorer缺省都是采用该值），增加该值会增加服务器的压力（需要创建更多的连接），而且对于网络状况不好的情况只会导致更大的阻塞，所以不建议修改该值

## Pipelining

在HTTP 1.0的时候，客户端的请求都是等到前一个请求完成之后，再发送另一个请求，一个packet里面只能请求一个资源。Pipelining可以让你在一个packet里面请求多个资源，减少了packet的数量，具体请参考下面示例。

 -  firefox Pipelining设置
    network.http.pipelining             （缺省为false，推荐打开）
    network.http.pipelining.maxrequests  (缺省为4，最大为8，建议设置为8）
 - iexplorer设置
    ie没有实现pipelining

## Chunked

http/1.0不支持chunked packet（当sender无法确定传输数据大小的时候使用）,keep-alive对于http/1.0同样有效，但是不支持chuncked packet
