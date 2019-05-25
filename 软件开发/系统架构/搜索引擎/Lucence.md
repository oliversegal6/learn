---
title: Lucence
date: 2019-05-24 22:06:47
categories: 
- 软件开发
- 系统架构
- 搜索引擎
---

[TOC]

## 一、环境
需要导入lucene.jar包（在lucene.apache.org下载）
## 二、基本概念
### 1．Lucene的工作流程：
1.  使用IndexWriter，在指定的目录建立索引的文件
2. 将需要检索的数据转换位Document的Filed对象，然后将Document用IndexWriter添加倒索引的文件中
3. 处理索引信息，关闭IndexWriter流
4. 创建搜索的Query
5. 给IndexSearcher

### 2．Lucene的字段类型
Lucene有四种不同的字段类型：Keyword，UnIndexed，UnStored和Text，用于指定建立最佳索引。
- Keyword字段是指不需要分析器解析但需要被编入索引并保存到索引中的部分。JavaSourceCodeIndexer类使用该字段来保存导入类的声明。
- UnIndexed字段是既不被分析也不被索引，但是要被逐字逐句的将其值保存到索引中。由于我们一般要存储文件的位置但又很少用文件名作为关键字来搜索，所以用该字段来索引Java文件名。
- UnStored字段和UnIndexed字段相反。该类型的Field要被分析并编入索引，但其值不会被保存到索引中。由于存储方法的全部源代码需要大量的空间。所以用UnStored字段来存储被索引的方法源代码。可以直接从Java源文件中取出方法的源代码，这样作可以控制我们的索引的大小。
- Text字段在索引过程中是要被分析、索引并保存的。类名是作为Text字段来保存。下表展示了JavaSourceCodeIndexer类使用Field字段的一般情况。

### 3．基本概念（与传统表的对比）：
Lucene	传统表	说明
IndexWriter	table	 
Document	一条记录	 
Field	每个字段	分为可被索引的，可切分的，不可被切分的，不可被索引的几种组合类型
Hits	RecoreSet	结果集
 
IndexWriter提供了一些参数可供设置，列表如下
 	属性	默认值	说明
mergeFactor	org.apache.lucene.mergeFactor	10	控制index的大小和频率,两个作用
1. 一个段有多少document
2. 多少个段合成一个大段 

- maxMergeDocs	org.apache.lucene.maxMergeDocs	Integer.MAX_VALUE	限制一个段中的document数目
- minMergeDocs	org.apache.lucene.minMergeDocs	10	缓存在内存中的document数目，超过他以后会写入到磁盘
- maxFieldLength	 	1000	一个Field中最大Term数目，超过部分忽略，不会index到field中，所以自然也就搜索不到

这些参数的的详细说明比较复杂：mergeFactor有双重作用
1. 设置每mergeFactor个document写入一个段，比如每10个document写入一个段
2. 设置每mergeFacotr个小段合并到一个大段，比如10个document的时候合并为1小段，以后有10个小段以后合并到一个大段，有10个大段以后再合并，实际的document数目会是mergeFactor的指数

简单的来说mergeFactor 越大，系统会用更多的内存，更少磁盘处理，如果要打批量的作index，那么把mergeFactor设置大没错， mergeFactor 小了以后， index数目也会增多，searhing的效率会降低，但是mergeFactor增大一点一点，内存消耗会增大很多(指数关系),所以要留意不要”out of memory”
把maxMergeDocs设置小，可以强制让达到一定数量的document写为一个段，这样可以抵消部分mergeFactor的作用.
minMergeDocs相当于设置一个小的cache,第一个这个数目的document会留在内存里面，不写入磁盘。这些参数同样是没有最佳值的，必须根据实际情况一点点调整。
maxFieldLength可以在任何时刻设置，设置后，接下来的index的Field会按照新的length截取，之前已经index的部分不会改变。可以设置为Integer.MAX_VALUE
 
### 4．几种查询方式       
查询方式	说明
TermQuery	条件查询
例如：`TermQuery tquery=new TermQuery(new Term("name","jerry"));`
name:字段名
jerry:要搜索的字符串
```java
MultiTermQuery	多个字段进行同一关键字的查询
Query query= null;
Query =MultiFieldQueryParser.parse("我",new String[] {"title","content"},analyzer);
Searcher searcher=new IndexSearcher(indexFilePath);
 Hits hits=searcher.search(query);
BooleanQuery	例如：BooleanQuery bquery=new BooleanQuery();
 bquery.add(query,true,false);
   bquery.add(mquery,true,false);
   bquery.add(tquery,true,false);
   Searcher searcher=new IndexSearcher(indexFilePath);
```
    Hits hits=searcher.search(bquery);
	
WildcardQuery	语义查询（通配符查询）
例：`Query query= new WildcardQuery(new Term("sender","*davy*"));`
- PhraseQuery	短语查询
- PrefixQuery	前缀查询
- PhrasePrefixQuery	短语前缀查询
- FuzzyQuery	模糊查询
- RangeQuery	范围查询
- SpanQuery	范围查询
在全文检索时建议大家先采用语义时的搜索，先搜索出有意义的内容，之后再进行模糊之类的搜索

(1)联合两个索引查询，已解决： 
```java
IndexSearcher[] searchers = new IndexSearcher[2];  
   
searchers[0] = new IndexSearcher(m_indexpath); 
searchers[1] = new IndexSearcher(m_outindexpath); 

MultiSearcher multiSearcher = new MultiSearcher(searchers); 
```

(2)还有个进行多条件搜索 and 与 or 的操作————
```java
用 MultiFieldQueryParser 
建议重新封装 
MultiFieldQueryParser.Parser(p[],d[],f[],analyer)  成or 与 and操作合一 
或者 
BooleanQuery m_BooleanQuery = new BooleanQuery(); 
Query query = QueryParser.Parse(m_SearchText, "INSTRUMENT_NAME", analyzer); 
Query query2 = QueryParser.Parse(m_SearchText2, "INSTRUMENT_NAME2", analyzer); 
m_BooleanQuery.Add(query, true, false); 
m_BooleanQuery.Add(query2, true, false); 
```
(3)复合查询（多种查询条件的综合查询）
```java
Query query=MultiFieldQueryParser.parse("索引”,new String[] {"title","content"},analyzer);
Searcher searcher=new IndexSearcher(indexFilePath);
Hits hits=searcher.search(query);
for (int i = 0; i < hits.length(); i++)  {
            System.out.println(hits.doc(i).get("name"));
}
```
 
### 5. 为查询优化索引(index)
Indexwriter.optimize()方法可以为查询优化索引（index），之前提到的参数调优是为indexing过程本身优化，而这里是为查询优化，优化主要是减少index文件数，这样让查询的时候少打开文件，优化过程中，lucene会拷贝旧的index再合并，合并完成以后删除旧的index，所以在此期间，磁盘占用增加， IO符合也会增加，在优化完成瞬间，磁盘占用会是优化前的2倍,在optimize过程中可以同时作search。
 
 
 
org.apache.lucene.document.Field
        即上文所说的“字段”，它是Document的片段section。
        Field的构造函数：
       Field(String name, String string, boolean store, boolean index, boolean token)。
        Indexed：如果字段是Indexed的，表示这个字段是可检索的。
        Stored：如果字段是Stored的，表示这个字段的值可以从检索结果中得到。
        Tokenized：如果一个字段是Tokenized的，表示它是有经过Analyzer转变后成为一个tokens序列，在这个转变过程tokenization中， Analyzer提取出需要进行索引的文本，而剔除一些冗余的词句（例如：a，the,they等，详见 org.apache.lucene.analysis.StopAnalyzer.ENGLISH_STOP_WORDS和 org.apache.lucene.analysis.standard.StandardAnalyzer(String[] stopWords)的API）。Token是索引时候的.
 
类型	Analyzed	Indexed	Stored	说明
Field.Keyword(String,String/Date)	N	Y	Y	这个Field用来储存会直接用来检索的比如(编号,姓名,日期等)
Field.UnIndexed(String,String)	N	N	Y	不会用来检索的信息,但是检索后需要显示的,比如,硬件序列号,文档的url地址
Field.UnStored(String,String)	Y	Y	N	大段文本内容,会用来检索,但是检索后不需要从index中取内容,可以根据url去load真实的内容
Field.Text(String,String)	Y	Y	Y	检索,获取都需要的内容,直接放index中,不过这样会增大index 
Field.Text(String,Reader)	Y	Y	N	如果是一个Reader, lucene猜测内容比较多,会采用Unstored的策略.
 
 
Lucene 的检索结果排序
        Lucene的排序主要是对org.apache.lucene.search.Sort的使用。Sort可以直接根据字段Field生成，也可以根据标准的SortField生成，但是作为Sort的字段，必须符合以下的条件：唯一值以及Indexed。可以对Integers, Floats, Strings三种类型排序。
        对整数型的ID检索结果排序只要进行以下的简单操作：
Sort sort = new Sort("id");
 Hits hits = searcher.search(query, sort);
用户还可以根据自己定义更加复杂的排序，详细请参考API。
 
### 6．分析器
Lucene使用分析器来处理被索引的文本。在将其存入索引之前，分析器用于将文本标记化、摘录有关的单词、丢弃共有的单词、处理派生词（把派生词还原到词根形式，意思是把bowling、bowler和bowls还原为bowl）和完成其它要做的处理。Lucene提供的通用分析器是：
        SimpleAnalyzer：用字符串标记一组单词并且转化为小写字母。
        StandardAnalyzer：用字符串标记一组单词，可识别缩写词、email地址、主机名称等等。并丢弃基于英语的stop words (a, an, the, to)等、处理派生词。
   ChineseAnalyzer.class，它是一个单字分析法，它把句子中的词全部分成一个一个的字符，以单个字为单位存储。
CJKAnalyzer.class，它是双字分析法，它把中文以双字为单位拆分得到结果，从而建立词条。当然这些得到的双字词中会有很多不符合中文语义单位的双字被送进索引。
 
十、需要注意的问题：
1. IndexWriter在添加新的document后，需要重新建立Index，则需要调用writer.optimize();方法
2. Lucene没有update索引的方法，需要删除后重新建立，参考remove方法
3. 用IndexReader删除Document后，需要重新用IndexWriter进行整理，否则无法在进行搜索（不知道是不是我设置问题）
4. Lucene先在内存中进行索引操作，并根据一定的批量进行文件的写入。这个批次的间隔越大，文件的写入次数越少，但占用内存会很多。反之占用内存少，但文件IO操作频繁，索引速度会很慢。在IndexWriter中有一个MERGE_FACTOR参数可以帮助你在构造索引器后根据应用环境的情况充分利用内存减少文件的操作。根据我的使用经验：缺省Indexer是每20条记录索引后写入一次，每将MERGE_FACTOR增加50倍，索引速度可以提高1倍左右。
5.  并发操作Lucene
(1)所有只读操作都可以并发
(2)在index被修改期间，所有只读操作都可以并发
(3)对index修改操作不能并发，一个index只能被一个线程占用
(4)ndex的优化，合并，添加都是修改操作
(5)但需要注意的是,在创建搜索的时候用: 
searcher = new IndexSearcher(IndexReader.open("E:\\lucene\\test4\\index"));
searcher.close();
这时候是不能关闭searcher的.
如果想让searcher能关闭,就不要用IndexReader了:
  searcher = new IndexSearcher("E:\\lucene\\test4\\index"); 
6. Locking机制
 lucence内部使用文件来locking，默认的locking文件放在java.io.tmpdir,可以通过-Dorg.apache.lucene.lockDir=xxx指定新的dir，有write.lock commit.lock两个文件，lock文件用来防止并行操作index，如果并行操作， lucene会抛出异常，可以通过设置-DdisableLuceneLocks=true来禁止locking，这样做一般来说很危险，除非你有操作系统或者物理级别的只读保证，比如把index文件刻盘到CDROM上。

 
 
 
 
```java
实例：
1.判断索引文件是否存在:
/**
     * 检查索引是否存在.
     * @param indexDir
     * @return
     */
    public static boolean indexExist(String indexDir)
    {
        return IndexReader.indexExists(indexDir);
    }
 private IndexWriter getWriter(String indexFilePath) throws Exception {
        boolean append=true;
        File file=new File(indexFilePath+File.separator+"segments");
        if(file.exists())
            append=false; 
        return new IndexWriter(indexFilePath,analyzer,append);
    }
 
2.删除索引
/**
     * 删除索引.
     * @param aTerm 索引删除条件
     * @param indexDir 索引目录
     */
    public static void deleteIndex(Term aTerm, String indexDir)
    {
        List aList = new ArrayList();
        aList.add(aTerm);
        deleteIndex(aList, indexDir);
    } 
    /**
     * 删除索引. 
     * @param aTerm 索引删除条件.
     * @param indexDir 索引目录     *  
     */
    public static void deleteIndex(List terms, String indexDir)
    {
        if (null == terms) {
            return;
        }
        
        if(!indexExist(indexDir)) { return; }
        IndexReader reader = null;
        try {
            reader = IndexReader.open(indexDir);
            for (int i = 0; i < terms.size(); i++){
                Term aTerm = (Term) terms.get(i);
                if (null != aTerm){
                    reader.delete(aTerm);
                }
            }
        } catch (IOException e){
            LogMan.warn("Error in Delete Index", e);
        } finally {
            try{
                if (null != reader){
                    reader.close();
                }
            }catch (IOException e){
                LogMan.warn("Close reader Error");
            }
        }
    } 
 删除索引需要一个条件,类似数据库中的字段条件,例如删除一条新闻的代码如下:
     public static void deleteNewsInfoIndex(int nid)
     {
         Term aTerm = new Term("nid", String.valueOf(nid));
         deleteIndex(aTerm,indexDir);
     }   
```

Lucene几个重要部分：
1、分词
2、索引
3、搜索

分词方式对数据搜索的性能影响特别大，在基于数据库的查询时对单字段可做二元，四元等分词，这样主要是为了减少clause数量，它是索引和搜索的关键。
索引的问题就是新建和更新，批量新建时可以用RAMDirectory先写，最后再并入FSDirectory中去，可以加快速度，更新时要先删除一个再新增一个。
	搜索有Query子类声明和QueryParser转换两种方式，最终都会变为BooleanQuery来进行搜索。