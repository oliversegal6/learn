
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
