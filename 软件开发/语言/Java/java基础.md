本笔记主要有三个方面：
1．	JDK、JRE及JVM
2．	Java动态特性及ClassLoader
3．	Package及Import

本笔记较为底层一点，正所谓知其然，知其所以然。了解了这些在写程序的过程中就不太会遇到一些莫明其妙的问题了，也对Java更深一步了解打下基础。

## JDK、JRE及JVM

当我們在命令列輸入  java XXX的時候，java.exe 的工作就是找到合适的JRE来执行类。java.exe 依照底下逻辑来找JRE:
1. 自己的目录下有没有JRE 目录。(這个部分這樣說並不是非常精确，原因
请见JDK 源代码，在此不特別说明)
2. 父目录底下JRE 子目录。
3. 查询Windows
Registry(HKEY_LOCAL_MACHINE\Software\JavaSoft\Java
Runtime Environment\)。

所以，java.exe 的执行结果和电脑里面哪一個java.exe 被執行，然后哪一
套JRE 被拿來执行Java应用程序有莫大的关系。打开d:\j2sdk1.4.0\jre\bin 这个目录，会看到client 和server 两个目录，里面都会分別看到jvm.dll，这就是一般我们所谓的Java虚拟机之所在。
当输入的指令为java –server 时， 就会使java.exe 自动选择d:\j2sdk1.4.0\jre\bin\server 这个目录底下的JVM；而输入java –classic时，就会使java.exe 自动选择d:\jdk1.3.1\jre\bin\classic 这个目录底下的JVM。从这里我们就可以解释之前为何设定了path=c:\jdk1.3.1\bin 之后，会在画面中突然出现两个之前不曾出現的选项， 也可以说明为何设定了path=d:\j2sdk1.4.0\bin 之后，java –server 的指令会从原本的错误信息变成可以正常使用。当开发Java 程序或是执行Java 程序的时候，一定要记得两件事:
1. 那一个java.exe 被执行。
2. java.exe 找到哪一套JRE。

只要这两件事都确定了，就知道问题发生的來龙去脉，也可以很容易地解決很多
貌似灵异的怪问题。

## 深入类加载器ClassLoader

动态性：它使应用程序可以在不全部重新编译的情况下更新系统，或在不用停止主程序运行的情况下除去系统中原有的bug，或原来不具有的功能。学过C的应该都知道程序运行以前不仅要编译，还要链接，这里的动态特性就省去了链接这一步。
Java通过类加载器来实现动态特性，因为只有在调用某个类时java虚拟机才会去装载它没有所谓的链接，c与c++并不具有动态特性，，为了让这些本身不具有动态性的程式语言具有某种程度的动态性，就必须依赖底层的操作系统提供一些机制来实现动态性，Windows操作系统底下的动态联接库(Dynamic Linking Library)和Unix 底下的共享对象(Share Object)就是这样的例子。但是，要运用这些底层操作系统所提供的机制，程式设计师必须多费一些功夫來写额外的程式码(例如Windows 平台上需要使用LoadLibrary()与GetProcAddress()两个Win32 API 來完成动态性的需求)，这些额外的程序码也会因为作业平台的不同而不同，毕竟这些额外的程序码与程序本身的运作逻辑甚少关联。

Java 提供两种方式来完成动态性。
1．隐式的(implicit)，
2．显示的(explicit)。

这两种方式底层用到的机制完全相同，差异只有在程式设计师所使用的程序码有所不同，隐式的方法可以让你在不知不觉的情况下就使用，而显式的方法必须加入一些额外的程序码。你可以把这两种方法和Win32应用程序呼叫动态链接库(Dynamic Linking Library)时的两种方法(implicit explicit)来类比，意思几乎相同。隐式的(implicit)方法我们已经谈过了，也就是当程序设计师用到new这个Java关键字时，会让类加载器依需求载入你需要的类别，这种方式使用了隐式的(implicit)方法，在前面提到『一般使用Java 程式语言来开发应用程序的工程师眼中，很少有机会能够察觉Java 因为具有了动态性之後所带来的优点和特性，甚至根本不曾利用过这个Java 先天就具有的特性。这不是我们的错，而是因为这个动态的本质被巧妙地隐藏起來，使得使用Java 的程式设计师在不知不觉中用到了动态性而不自知』，就是因为如此。但是，隐式的(implicit)方法仍有其限制，无法达成更多的弹性，遇到这种情況，我们就必须动用显式的(explicit)方法来完成。

显式的方法，又分成两种方式，一种是藉由java.lang.Class里的forName()方法，另一种則是藉由java.lang.ClassLoader里的loadClass()方法。您可以任意选 用其中一种方法。
- 	forName();
不管您使用的是new来产生某类的实例、或是使用只有一个参数的forName()方法，內部都隐含了”载入类+呼叫静态初始化区块”的动作。而使用具有三个参数的forName()方法时，如果第二个参数给定的是false，那么就只会命令类加载器载入该类，但不会调用其静态初始化区块，只有等到整个程序第一次实例化某个类时，静态初始化区块才会被调用。

- 	classLoader();
在Java 之中，每个类都是由某个类加载器(ClassLoader 的实例)来加载，因此，Class 类的实例中，都会有引用记录着加载它的ClassLoader 的实例(注意:如果该引用是null，并不代表它不是由类加载器所加载，而是代表这个类由根加载器(bootstrap loader,也有人称oot loader)所加载的，只不过因为这个加载器并不是用Java 所写成，所以逻辑上没有实例)。

虚拟机一启动，会先做一些初始化的动作，比方说读取系统参数等。一旦初始化动作完成之后，就会产生第一个类加载器，即所谓的Bootstrap Loader，Bootstrap Loader 是由C++所写成的(所以前面我们说，以Java的观点来看，逻辑上并不存在Bootstrap Loader 的类实例，所以在Java 程序里试图印出其內容的时候，我们会看到输出为null)，这个Bootstrap Loader 所做的初始工作中，除了也做一些基本的初始化动作之外，最重要的就是加载定义在sun.misc 命名空间下的Launcher.java 之中的ExtClassLoader( 因为是inner class ， 所以编译之后会变成Launcher$ExtClassLoader.class)，并设定其Parent为null，代表其父加载器为Bootstrap Loader。然后Bootstrap Loader 再要求加载定义于sun.misc 命名空间底下的Launcher.java 之中的AppClassLoader(因为是inner class，所以编译之后会变成Launcher$AppClassLoader.class)，并设定其Parent为之前产生的ExtClassLoader实例。这里要请大家注意的是，Launcher$ExtClassLoader.class与Launcher$AppClassLoader.class 都是由Bootstrap Loader 所加载，所以Parent 和由哪个类加载器加载没有关系。这个由Bootstrap Loader ?? ExtClassLoader ?? AppClassLoader，就是我们所谓「类加载器的价层体系」。AppClassLoader 和ExtClassLoader 都是URLClassLoader 的子类。由于它们都是URLClassLoader 的子类，所以它们也应该有URL 作为查找类的方法，由源代码中我们可以知道，AppClassLoader 所参考的URL 是从系统参数java.class.path 取出的字串所决定，而java.class.path则是由我们在执行java.exe时，利用 –cp 或-classpath 或CLASSPATH 环境变量所决定，至于ExtClassLoader 也有相同的情形，不过其查找路径是参考系统参数java.ext.dirs系统参数java.ext.dirs 的內容，会指向java.exe 所选择的JRE 所在位置下的\lib\ext 子目录，也可以更改最后一个类加载器是Bootstrap Loader，我们可以通过查询由系统参数sun.boot.class.path 得知Bootstrap Loader用来查找类的路径。AppClassLoader与Bootstrap Loader会查找它们所指定的位置(或JAR)，如果找不到就找不到了，AppClassLoader与Bootstrap Loader 不会递归的查找这些位置下的其它路径及其它没有被指定的JAR。反观ExtClassLoader，所参考的系统参数是java.ext.dirs，意思是说，他会查找底下的所有JAR以及classes目录，作为其查找路径(所以你会发现上面我们在测试的时候，如果加入 -Dsun.boot.class.path=c:\winnt选项时，程序的起始速度会慢了些，这是因为c:\winnt 目录下的文件很多，必须花额外的时间来列举JAR 文件)。
在命令行下使用–classpath / -cp / 环境变量CLASSPATH 来更改AppClassLoader 的查找路径， 或者用 –Djava.ext.dirs 来改变ExtClassLoader 的查找目录， 两者都是有意义的。可是用–Dsun.boot.class.path 来改变Bootstrap Loader 的查找路径是无效。这是因为AppClassLoader 与ExtClassLoader 都是各自参考这两个系统参数的内容而建立，当你在命令行下变更这两个系统参数之后， AppClassLoader与ExtClassLoader 在建立实例的时候会参考这两个系统参数，因而改变了它们查找类的路径;而系统参数sun.boot.class.path 则是预设与Bootstrap Loader 的查找路径相同，就算你更改该系统参与，与Bootstrap Loader 完全无关。
「类加载器可以看到Parent所加载的所有类别，但是Parent却看不到其下的类加载器可以载入的类。


## Package与Import机制
结论一:
如果您的类属于某个package，那么就应该将它至于该package所对应的相对路径之下。举例来说，如果您有个类叫做C，属于xyz.pqr.abc 包，那么就必须建立一个三层的目录 xyz\pqr\abr，然后将C.java 或是C.class放置到这个目录下，才能个让javac.exe 或是java.exe 顺利执行。

结论二:
当使用javac.exe 编译的時候，类的源代码的位置一定要根据结论一所说来放置，如果该源代码出现在不该出现的地方(如上述放在xyz\pqr 之下)，除了很容易造成混淆不清，而且有时候抓不出编译为何发生错误，因为javac.exe 输出的的信息根本无法找出问题到底出在哪里。

  
### 类名与Java文件名的关系
一个 .java 文件，可以有多个类，但是文件名只能有一个，用哪一个类名来充当文件名呢？
其实文件名是无所谓的，因为将来用 javac 进行编译的时候，最终会生成多个 .class 文件，每一个类对应一个 .class 文件。运行时只要运行对应的 .class 文件就可以了，这时 .java 文件就显得没什么用了( 运行时不需要 )，那还管它叫什么名字。
但是当你的类被 public 修饰时，此时文件名必须和该类保持一致（例如 public class A{} 只能放在 A.java 文件中，不然在用 javac 编译时会提示错误：类 A 是公共的，应在名为 A.java 的文件中声明）。也就是说在同一个 .java 文件中，不应该出现2个或2个以上的 public class 。如果有一个public class 类，就应该让文件名和此类名相同；那如果没有呢，即所有的 class 都没有修饰符，那么可以给该文件随便起名字，甚至可以不和任意一个类同名，哪怕是汉字名称都可以，但是后缀还是要有 .java 的 ( 哈哈，不信就试试 ) 。

注：有很多初学者 ( 我也是 ) 可能会认为文件名应该也和 main 方法所在类有关系，其实不然。main 方法只是程序的一个入口，老师讲“一个程序只能有一个入口”，好象是在说只能有一个 main 方法，其实，只要你高兴，就可以在所有类里都写上 main 方法，给程序提供N多个入口，但是最后你运行程序的时候也只能从其中的一个入口进去，这就是 main 的作用( 程序入口 )。（这也就是为什么你会发现，程序员在做单元测试时，会往自己做的很多类里面添加 main 方法，因为他要为自己做的东西添加运行入口，从而能方便测试。）

- Class对象
	Class对象内含与class相关的各种信息，事实上Class对象正是被用来产生你的一般性class对象。你的程序中的每一个class都有一个相应的Class对象。也就是说每当你写一个新的class并编译完成，就会产生一个Class对象存储于相同的.class文件内。执行期间当你想要产生该class对象时，Java虚拟机就会先检查该型别的Class对象是否已被装载。如果尚未装载，JVM就会根据名称找到.class文件并装载它。因此Java程序启动时并不会将整个程序都装载。一旦某个类的Class对象已被装载到内存，就可以被用来产生该类的所有对象
	
- Class literals
	类名.class也可以用来得到指向Class对象的reference。这么做不仅比较简单，也更安全，因为检查动作在编译期就进行了。

### JAR包
JAR 文件就是 Java Archive File，顾名思意，它的应用是与 Java 息息相关的，是 Java 的一种文档格式。JAR 文件非常类似 ZIP 文件——准确的说，它就是 ZIP 文件，所以叫它文件包。JAR 文件与 ZIP 文件唯一的区别就是在 JAR 文件的内容中，包含了一个 META-INF/MANIFEST.MF 文件，这个文件是在生成 JAR 文件的时候自动创建的。

创建可执行的 JAR 文件包.
制作一个可执行的 JAR 文件包来发布你的程序是 JAR 文件包最典型的用法。 
Java 程序是由若干个 .class 文件组成的。这些 .class 文件必须根据它们所属的包不同而分级分目录存放；运行前需要把所有用到的包的根目录指定给 CLASSPATH 环境变量或者 java 命令的 -cp 参数；运行时还要到控制台下去使用 java 命令来运行，如果需要直接双击运行必须写 Windows 的批处理文件 (.bat) 或者 Linux 的 Shell 程序。因此，许多人说，Java 是一种方便开发者苦了用户的程序设计语言。
其实不然，如果开发者能够制作一个可执行的 JAR 文件包交给用户，那么用户使用起来就方便了。在 Windows 下安装 JRE (Java Runtime Environment) 的时候，安装文件会将 .jar 文件映射给 javaw.exe 打开。那么，对于一个可执行的 JAR 文件包，用户只需要双击它就可以运行程序了，和阅读 .chm 文档一样方便 (.chm 文档默认是由 hh.exe 打开的)。那么，现在的关键，就是如何来创建这个可执行的 JAR 文件包。 
创建可执行的 JAR 文件包，需要使用带 cvfm 参数的 jar 命令，同样以上述 test 目录为例，命令如下： 
`jar cvfm test.jar manifest.mf test `
这里 test.jar 和 manifest.mf 两个文件，分别是对应的参数 f 和 m，其重头戏在 manifest.mf。因为要创建可执行的 JAR 文件包，光靠指定一个 manifest.mf 文件是不够的，因为 MANIFEST 是 JAR 文件包的特征，可执行的 JAR 文件包和不可执行的 JAR 文件包都包含 MANIFEST。关键在于可执行 JAR 文件包的 MANIFEST，其内容包含了 Main-Class 一项。这在 MANIFEST 中书写格式如下： 
Main-Class: 可执行主类全名(包含包名) 
例如，假设上例中的 Test.class 是属于 test 包的，而且是可执行的类 (定义了 public static void main(String[]) 方法)，那么这个 manifest.mf 可以编辑如下： 
Main-Class: test.Test <回车>; 
这个 manifest.mf 可以放在任何位置，也可以是其它的文件名，只需要有 Main-Class: test.Test 一行，且该行以一个回车符结束即可。
`jar cvfm test.jar manifest.mf test `
需要注意的是，创建的 JAR 文件包中需要包含完整的、与 Java 程序的包结构对应的目录结构，就像上例一样。而 Main-Class 指定的类，也必须是完整的、包含包路径的类名，如上例的 test.Test；而且在没有打成 JAR 文件包之前可以使用 java <类名>; 来运行这个类，即在上例中 java test.Test 是可以正确运行的 (当然要在 CLASSPATH 正确的情况下)。 

###  jar 命令详解 
jar 是随 JDK 安装的，在 JDK 安装目录下的 bin 目录中，Windows 下文件名为 jar.exe，Linux 下文件名为 jar。它的运行需要用到 JDK 安装目录下 lib 目录中的 tools.jar 文件。不过我们除了安装 JDK 什么也不需要做，因为 SUN 已经帮我们做好了。我们甚至不需要将 tools.jar 放到 CLASSPATH 中。 
使用不带任何的 jar 命令我们可以看到 jar 命令的用法如下： 
jar {ctxu}[vfm0M] [jar-文件] [manifest-文件] [-C 目录] 文件名 ... 
其中 {ctxu} 是 jar 命令的子命令，每次 jar 命令只能包含 ctxu 中的一个，它们分别表示： 
-c 创建新的 JAR 文件包 
-t 列出 JAR 文件包的内容列表 
-x 展开 JAR 文件包的指定文件或者所有文件 
-u 更新已存在的 JAR 文件包 (添加文件到 JAR 文件包中) 

[vfm0M] 中的选项可以任选，也可以不选，它们是 jar 命令的选项参数 
-v 生成详细报告并打印到标准输出 
-f 指定 JAR 文件名，通常这个参数是必须的 
-m 指定需要包含的 MANIFEST 清单文件 
-0 只存储，不压缩，这样产生的 JAR 文件包会比不用该参数产生的体积大，但速度更快 
-M 不产生所有项的清单（MANIFEST〕文件，此参数会忽略 -m 参数 
[jar-文件] 即需要生成、查看、更新或者解开的 JAR 文件包，它是 -f 参数的附属参数 
[manifest-文件] 即 MANIFEST 清单文件，它是 -m 参数的附属参数 
[-C 目录] 表示转到指定目录下去执行这个 jar 命令的操作。它相当于先使用 cd 命令转该目录下再执行不带 -C 参数的 jar 命令，它只能在创建和更新 JAR 文件包的时候可用。 
文件名 ... 指定一个文件/目录列表，这些文件/目录就是要添加到 JAR 文件包中的文件/目录。如果指定了目录，那么 jar 命令打包的时候会自动把该目录中的所有文件和子目录打入包中。 
下面举一些例子来说明 jar 命令的用法： 
1)	jar cf test.jar test 
该命令没有执行过程的显示，执行结果是在当前目录生成了 test.jar 文件。如果当前目录已经存在 test.jar，那么该文件将被覆盖。 
2)	jar cvf test.jar test 
该命令与上例中的结果相同，但是由于 v 参数的作用，显示出了打包过程，如下： 
标明清单(manifest) 
增加：test/(读入= 0) (写出= 0)(存储了 0%) 
增加：test/Test.class(读入= 7) (写出= 6)(压缩了 14%) 
3)	jar cvfM test.jar test 

该命令与 2) 结果类似，但在生成的 test.jar 中没有包含 META-INF/MANIFEST 文件，打包过程的信息也略有差别： 
增加：test/(读入= 0) (写出= 0)(存储了 0%) 
增加：test/Test.class(读入= 7) (写出= 6)(压缩了 14%) 
4) jar cvfm test.jar manifest.mf test 
运行结果与 2) 相似，显示信息也相同，只是生成 JAR 包中的 META-INF/MANIFEST 内容不同，是包含了 manifest.mf 的内容 
在 test.jar 中添加了文件 manifest.mf，此使用 jar tf 来查看 test.jar 可以发现 test.jar 中比原来多了一个 manifest。这里顺便提一下，如果使用 -m 参数并指定 manifest.mf 文件，那么 manifest.mf 是作为清单文件 MANIFEST 来使用的，它的内容会被添加到 MANIFEST 中；但是，如果作为一般文件添加到 JAR 文件包中，它跟一般文件无异。 
10) jar uvf test.jar manifest.mf 
与 9) 结果相同，同时有详细信息显示，如： 
增加：manifest.mf(读入= 17) (写出= 19)(压缩了 -11%) 

### 关于 JAR 文件包的一些技巧 
1) 使用 unzip 来解压 JAR 文件 
在介绍 JAR 文件的时候就已经说过了，JAR 文件实际上就是 ZIP 文件，所以可以使用常见的一些解压 ZIP 文件的工具来解压 JAR 文件，如 Windows 下的 WinZip、WinRAR 等和 Linux 下的 unzip 等。使用 WinZip 和 WinRAR 等来解压是因为它们解压比较直观，方便。而使用 unzip，则是因为它解压时可以使用 -d 参数指定目标目录。 

在解压一个 JAR 文件的时候是不能使用 jar 的 -C 参数来指定解压的目标的，因为 -C 参数只在创建或者更新包的时候可用。那么需要将文件解压到某个指定目录下的时候就需要先将这具 JAR 文件拷贝到目标目录下，再进行解压，比较麻烦。如果使用 unzip，就不需要这么麻烦了，只需要指定一个 -d 参数即可。如： 

`unzip test.jar -d dest/ `

2) 使用 WinZip 或者 WinRAR 等工具创建 JAR 文件 
上面提到 JAR 文件就是包含了 META-INF/MANIFEST 的 ZIP 文件，所以，只需要使用 WinZip、WinRAR 等工具创建所需要 ZIP 压缩包，再往这个 ZIP 压缩包中添加一个包含 MANIFEST 文件的 META-INF 目录即可。对于使用 jar 命令的 -m 参数指定清单文件的情况，只需要将这个 MANIFEST 按需要修改即可。 

3) 使用 jar 命令创建 ZIP 文件 
有些 Linux 下提供了 unzip 命令，但没有 zip 命令，所以需要可以对 ZIP 文件进行解压，即不能创建 ZIP 文件。如要创建一个 ZIP 文件，使用带 -M 参数的 jar 命令即可，因为 -M 参数表示制作 JAR 包的时候不添加 MANIFEST 清单，那么只需要在指定目标 JAR 文件的地方将 .jar 扩展名改为 .zip 扩展名，创建的就是一个不折不扣的 ZIP 文件了，如将上一节的第 3) 个例子略作改动：

## Annotation

1. 基础	
Jdk5 已经定义好了三种类型的Annotation，分别是Override, Deprecated, SupressWarnings。
- Override 是用来指示有一个method，它override掉它自己的superclass的method .
- Deprecated指出某一个method或是element类型的使用是被阻止的。
- SupressWarnings会关掉class, method, field 与 variable初始化的编译器警告。

2. 自定义
可以用 @interface来自定义Annotation，定义时有四个meta-annotation用来修饰自定义的Annotation分别是Target, Retention,Documented,Inherited


3. 应用
Annotation可以在运行时通过反射使用，Class, Field,Method,Constructor 等JDK API都实现了AnnotatedElement用来能过反射得到Annotation使得可以在运行时动态的得到它。

现在很多基于Annotation的Annotation库应该都是使用反射来用的，像EJB3.0，Hibernate中的Annotation应该都是自己写的一套Annotation库来使用。
使用Annotation减少了XML配置文件，使java开发也更倾向于动态语言如Ruby之类的特性了（因为Ruby更相信约定也就是Annotation更强于XML配置文件）


## 异常处理

### 基本原理：
对于异常最重要的是对其观念的了解，以及面对异常你所应该采取的行动。

异常处理其实就是用new产生一个用以表示错误状态的对象。

### 语法方面：
当在函数中用throw来抛出一个异常时，你可以在本函数中进行捕捉（try,catch）也可以抛出到上层调用模块用try,catch来处理，在本函数中不做处理时你必须在函数声明处标识抛出的异常类型（java语法要求，RunTimeException类型异常除外）。

通常用法，如果你不做处理抛出，程序会在抛出点中断。如果你想要函数内程序继续执行，就用try,catch块来捕捉异常情况。那么try,catch块以后的代码用继续执行。

### 通用作法：
1. 运行时异常（unchecked Exception)，被调用函数抛出，调用函数也可以不做处理，语法上不会有错误。但按一般应有地方做catch或处理，如把错误写入log日志中
2. checked　Exception一般为要进行处理的异常，也就是说可修复的异常。

应用例子：像持久用Ibatis,业务层用spring，表示层用struts
	这样的话用Spring包装Dao来操作数据库，Spring用DataAccessException把数据库产生的异常包装了起来
DataAccessException是uncheckedException,可以不捕捉该异常因为一般这种异常都是不可修复的，一般来说要有一个地方把异常记录到日志中
以方便找错，当然也可以把它包装到自己写的Exception中把指定的错误消息显示，等等。
