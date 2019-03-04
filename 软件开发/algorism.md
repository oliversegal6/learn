## 二叉查找树

二叉查找树也称为有序二叉查找树,满足二叉查找树的一般性质,是指一棵空树具有如下性质:

- 任意节点左子树不为空,则左子树的值均小于根节点的值.
- 任意节点右子树不为空,则右子树的值均大于于根节点的值.
- 任意节点的左右子树也分别是二叉查找树.

某些情况,二叉查找树会退化成一个有n个节点的线性链

## AVL树

AVL树是带有平衡条件的二叉查找树,一般是用平衡因子差值判断是否平衡并通过旋转来实现平衡,左右子树树高不超过1,和红黑树相比,它是严格的平衡二叉树,平衡条件必须满足(所有节点的左右子树高度差不超过1).不管我们是执行插入还是删除操作,只要不满足上面的条件,就要通过旋转来保持平衡,而旋转是非常耗时的,由此我们可以知道AVL树适合用于插入删除次数比较少，但查找多的情况。



## B+树

       B+树是B-树的变体，也是一种多路搜索树：

       1.其定义基本与B-树同，除了：

       2.非叶子结点的子树指针与关键字个数相同；

       3.非叶子结点的子树指针P[i]，指向关键字值属于[K[i], K[i+1])的子树

（B-树是开区间）；

       5.为所有叶子结点增加一个链指针；

       6.所有关键字都在叶子结点出现；

- B树：二叉树，每个结点只存储一个关键字，等于则命中，小于走左结点，大于
走右结点；
- B-树：多路搜索树，每个结点存储M/2到M个关键字，非叶子结点存储指向关键
字范围的子结点；所有关键字在整颗树中出现，且只出现一次，非叶子结点可以命中；
- B+树：在B-树基础上，为叶子结点增加链表指针，所有关键字都在叶子结点中出现，非叶子结点作为叶子结点的索引；B+树总是到叶子结点才命中；

B/B+树是为了磁盘或其它存储设备而设计的一种平衡多路查找树(相对于二叉,B树每个内节点有多个分支),与红黑树相比,在相同的的节点的情况下,一颗B/B+树的高度远远小于红黑树的高度(在下面B/B+树的性能分析中会提到).B/B+树上操作的时间通常由存取磁盘的时间和CPU计算时间这两部分构成,而CPU的速度非常快,所以B树的操作效率取决于访问磁盘的次数,关键字总数相同的情况下B树的高度越小，磁盘I/O所花的时间越少

B+树是应文件系统所需而产生的一种B树的变形树(文件的目录一级一级索引,只有最底层的叶子节点(文件)保存数据.),非叶子节点只保存索引,不保存实际的数据,数据都保存在叶子节点中

## 红黑树

     红黑树（Red-Black Tree）是二叉搜索树（Binary Search Tree）的一种改进。我们知道二叉搜索树在最坏的情况下可能会变成一个链表（当所有节点按从小到大的顺序依次插入后）。而红黑树在每一次插入或删除节点之后都会花O（log N）的时间来对树的结构作修改，以保持树的平衡。也就是说，红黑树的查找方法与二叉搜索树完全一样；插入和删除节点的的方法前半部分节与二叉搜索树完全一样，而后半部分添加了一些修改树的结构的操作。
     红黑树确保没有一条路径会比其它路径长出两倍.它是一种弱平衡二叉树(由于是若平衡,可以推出,相同的节点情况下,AVL树的高度低于红黑树),相对于要求严格的AVL树来说,它的旋转次数变少,所以对于搜索,插入,删除操作多的情况下,我们就用红黑树

     红黑树的每个节点上的属性除了有一个key、3个指针：parent、lchild、rchild以外，还多了一个属性：color。它只能是两种颜色：红或黑。而红黑树除了具有二叉搜索树的所有性质之外，还具有以下4点性质：
1. 根节点是黑色的。
2. 空节点是黑色的（红黑树中，根节点的parent以及所有叶节点lchild、rchild都不指向NULL，而是指向一个定义好的空节点）。
3. 红色节点的父、左子、右子节点都是黑色。
4. 在任何一棵子树中，每一条从根节点向下走到空节点的路径上包含的黑色节点数量都相同

## 查找

### 二分查找

```java
import java.util.Scanner;

/*

 * 顺序查找

 */

public class SequelSearch {
public static void main(String[] arg) {  
    int[] a={4,6,2,8,1,9,0,3};
    Scanner input=new Scanner(System.in);
    System.out.println("请输入你要查找的数：");
    //存放控制台输入的语句
    int num=input.nextInt();
    //调用searc()方法，将返回值保存在result中
    int result=search(a, num);
    if(result==-1){
         System.out.println("你输入的数不存在与数组中。");
    }
    else
         System.out.println("你输入的数字存在，在数组中的位置是第："+(result+1)+"个");
}
public static int search(int[] a, int num) {        
    for(int i = 0; i < a.length; i++) {
        if(a[i] == num){//如果数据存在
            return i;//返回数据所在的下标，也就是位置
        }
    } 
    return -1;//不存在的话返回-1
}
}
```

```java 
import java.util.Scanner;
/*
 * 二分查找
 */
public class BinarySearch {
    public static void main(String[] args) {
        int[] arr={5,3,6,1,9,8,2,4,7};
        //先打印输出原始数组数据
        System.out.println("原始数组数据如下：");
        for (int n : arr) {
            System.out.print(n+" ");
        }
        System.out.println();
        //首先对数组进行排序，这里用冒泡排序
        for(int i=0;i<arr.length-1;i++){
            for(int j=0;j<arr.length-1-i;j++){
                if(arr[j]>arr[j+1]){
                    int temp=arr[j];
                    arr[j]=arr[j+1];
                    arr[j+1]=temp;
                }
            }
        }
        //遍历输出排序好的数组
        System.out.println("经过冒泡排序后的数组：");
        for(int n:arr){
            System.out.print(n+" ");
        }
        System.out.println();//换行
        Scanner input=new Scanner(System.in);
        System.out.println("请输入你要查找的数：");
        int num=input.nextInt();
        int result=binarySearch(arr, num);
        if(result==-1){
            System.out.println("你要查找的数不存在……");
        }
        else{
            System.out.println("你要查找的数存在，在数组中的位置是："+result);
        }
    }
    //二分查找算法
    public static int binarySearch(int[] arr,int num){
    
        int low=0;
        int upper=arr.length-1;
        while(low<=upper){
            int mid=(upper+low)/2;
            if(arr[mid]<num){
                low=mid+1;
            }
            else if(arr[mid]>num){
                upper=mid-1;
            }
            else
                return mid;
        }
        return -1;
    }
}
```

## 排序

### 选择 

初始时在序列中找到最小（大）元素，放到序列的起始位置作为已排序序列；然后，再从剩余未排序元素中继续寻找最小（大）元素，放到已排序序列的末尾。以此类推，直到所有元素均排序完毕

```java
public void selectSort(int[]a){
        int len=a.length;
        for(int i=0;i<len;i++){//循环次数
            int value=a[i];
            int position=i;
            for(int j=i+1;j<len;j++){//找到最小的值和位置
                if(a[j]<value){
                    value=a[j];
                    position=j;
                }
            }
            a[position]=a[i];//进行交换
            a[i]=value;
        }
    }
```
### 冒泡

它重复地走访过要排序的元素，依次比较相邻两个元素，如果他们的顺序错误就把他们调换过来，直到没有元素再需要交换，排序完成。这个算法的名字由来是因为越小(或越大)的元素会经由交换慢慢“浮”到数列的顶端

```java
public void bubbleSort(int []a){
           int len=a.length;
           for(int i=0;i<len;i++){
               for(int j=0;j<len-i-1;j++){//注意第二重循环的条件
                   if(a[j]>a[j+1]){
                       int temp=a[j];
                       a[j]=a[j+1];
                       a[j+1]=temp;
                   }
               }
           }
       }
       
```

### 插入

对于未排序数据(右手抓到的牌)，在已排序序列(左手已经排好序的手牌)中从后向前扫描，找到相应位置并插入

插入排序在实现上，通常采用in-place排序（即只需用到O(1)的额外空间的排序），因而在从后向前扫描过程中，需要反复把已排序元素逐步向后挪位，为最新元素提供插入空间。

插入排序不适合对于数据量比较大的排序应用。但是，如果需要排序的数据量很小，比如量级小于千，那么插入排序还是一个不错的选择

```java
public void insertSort(int [] a){
        int len=a.length;//单独把数组长度拿出来，提高效率
        int insertNum;//要插入的数
        for(int i=1;i<len;i++){//因为第一次不用，所以从1开始
            insertNum=a[i];
            int j=i-1;//序列元素个数
            while(j>=0&&a[j]>insertNum){//从后往前循环，将大于insertNum的数向后移动
                a[j+1]=a[j];//元素向后移动
                j--;
            }
            a[j+1]=insertNum;//找到位置，插入当前元素
        }
    }
```

### 希尔排序
      针对直接插入排序的下效率问题，有人对次进行了改进与升级，这就是现在的希尔排序。希尔排序，也称递减增量排序算法，是插入排序的一种更高效的改进版本。希尔排序是非稳定排序算法。

希尔排序是基于插入排序的以下两点性质而提出改进方法的：

插入排序在对几乎已经排好序的数据操作时， 效率高， 即可以达到线性排序的效率
但插入排序一般来说是低效的， 因为插入排序每次只能将数据移动一位

```java
public void sheelSort(int [] a){
        int len=a.length;//单独把数组长度拿出来，提高效率
        while(len!=0){
            len=len/2;
            for(int i=0;i<len;i++){//分组
                for(int j=i+len;j<a.length;j+=len){//元素从第二个开始
                    int k=j-len;//k为有序序列最后一位的位数
                    int temp=a[j];//要插入的元素
                    /*for(;k>=0&&temp<a[k];k-=len){
                        a[k+len]=a[k];
                    }*/
                    while(k>=0&&temp<a[k]){//从后往前遍历
                        a[k+len]=a[k];
                        k-=len;//向后移动len位
                    }
                    a[k+len]=temp;
                }
            }
        }
    }
```
### 快速

在平均状况下，排序n个元素要O(nlogn)次比较。在最坏状况下则需要O(n^2)次比较，但这种状况并不常见。事实上，快速排序通常明显比其他O(nlogn)算法更快，因为它的内部循环可以在大部分的架构上很有效率地被实现出来。

快速排序使用分治策略(Divide and Conquer)来把一个序列分为两个子序列

```java
public void quickSort(int[]a,int start,int end){
           if(start<end){
               int baseNum=a[start];//选基准值
               int midNum;//记录中间值
               int i=start;
               int j=end;
               do{
                   while((a[i]<baseNum)&&i<end){
                       i++;
                   }
                   while((a[j]>baseNum)&&j>start){
                       j--;
                   }
                   if(i<=j){
                       midNum=a[i];
                       a[i]=a[j];
                       a[j]=midNum;
                       i++;
                       j--;
                   }
               }while(i<=j);
                if(start<j){
                    quickSort(a,start,j);
                }       
                if(end>i){
                    quickSort(a,i,end);
                }
           }
       }
```

### 归并

归并排序是创建在归并操作上的一种有效的排序算法，效率为O(nlogn)

归并排序的实现分为递归实现与非递归(迭代)实现。递归实现的归并排序是算法设计中分治策略的典型应用，我们将一个大问题分割成小问题分别解决，然后用所有小问题的答案来解决整个大问题

```java
public  void mergeSort(int[] a, int left, int right) {  
           int t = 1;// 每组元素个数  
           int size = right - left + 1;  
           while (t < size) {  
               int s = t;// 本次循环每组元素个数  
               t = 2 * s;  
               int i = left;  
               while (i + (t - 1) < size) {  
                   merge(a, i, i + (s - 1), i + (t - 1));  
                   i += t;  
               }  
               if (i + (s - 1) < right)  
                   merge(a, i, i + (s - 1), right);  
           }  
        }  
       
        private static void merge(int[] data, int p, int q, int r) {  
           int[] B = new int[data.length];  
           int s = p;  
           int t = q + 1;  
           int k = p;  
           while (s <= q && t <= r) {  
               if (data[s] <= data[t]) {  
                   B[k] = data[s];  
                   s++;  
               } else {  
                   B[k] = data[t];  
                   t++;  
               }  
               k++;  
           }  
           if (s == q + 1)  
               B[k++] = data[t++];  
           else  
               B[k++] = data[s++];  
           for (int i = p; i <= r; i++)  
               data[i] = B[i];  
        }
```

### 堆

堆排序是指利用堆这种数据结构所设计的一种选择排序算法。堆是一种近似完全二叉树的结构（通常堆是通过一维数组来实现的），并满足性质：以最大堆（也叫大根堆、大顶堆）为例，其中父结点的值总是大于它的孩子节点

```java
public  void heapSort(int[] a){
           int len=a.length;
           //循环建堆  
           for(int i=0;i<len-1;i++){
               //建堆  
               buildMaxHeap(a,len-1-i);
               //交换堆顶和最后一个元素  
               swap(a,0,len-1-i);
           }
       }
        //交换方法
       private  void swap(int[] data, int i, int j) {
           int tmp=data[i];
           data[i]=data[j];
           data[j]=tmp;
       }
       //对data数组从0到lastIndex建大顶堆  
       private void buildMaxHeap(int[] data, int lastIndex) {
           //从lastIndex处节点（最后一个节点）的父节点开始  
           for(int i=(lastIndex-1)/2;i>=0;i--){
               //k保存正在判断的节点  
               int k=i;
               //如果当前k节点的子节点存在  
               while(k*2+1<=lastIndex){
                   //k节点的左子节点的索引  
                   int biggerIndex=2*k+1;
                   //如果biggerIndex小于lastIndex，即biggerIndex+1代表的k节点的右子节点存在  
                   if(biggerIndex<lastIndex){
                       //若果右子节点的值较大  
                       if(data[biggerIndex]<data[biggerIndex+1]){
                           //biggerIndex总是记录较大子节点的索引  
                           biggerIndex++;
                       }
                   }
                   //如果k节点的值小于其较大的子节点的值  
                   if(data[k]<data[biggerIndex]){
                       //交换他们  
                       swap(data,k,biggerIndex);
                       //将biggerIndex赋予k，开始while循环的下一次循环，重新保证k节点的值大于其左右子节点的值  
                       k=biggerIndex;
                   }else{
                       break;
                   }
               }
           }
       }
```
### 桶排序
### 基数
用于大量数，很长的数进行排序时。

将所有的数的个位数取出，按照个位数进行排序，构成一个序列。

将新构成的所有的数的十位数取出，按照十位数进行排序，构成一个序列。

```java
public void baseSort(int[] a) {
               //首先确定排序的趟数;    
               int max = a[0];
               for (int i = 1; i < a.length; i++) {
                   if (a[i] > max) {
                       max = a[i];
                   }
               }
               int time = 0;
               //判断位数;    
               while (max > 0) {
                   max /= 10;
                   time++;
               }
               //建立10个队列;    
               List<ArrayList<Integer>> queue = new ArrayList<ArrayList<Integer>>();
               for (int i = 0; i < 10; i++) {
                   ArrayList<Integer> queue1 = new ArrayList<Integer>();
                   queue.add(queue1);
               }
               //进行time次分配和收集;    
               for (int i = 0; i < time; i++) {
                   //分配数组元素;    
                   for (int j = 0; j < a.length; j++) {
                       //得到数字的第time+1位数;  
                       int x = a[j] % (int) Math.pow(10, i + 1) / (int) Math.pow(10, i);
                       ArrayList<Integer> queue2 = queue.get(x);
                       queue2.add(a[j]);
                       queue.set(x, queue2);
                   }
                   int count = 0;//元素计数器;    
                   //收集队列元素;    
                   for (int k = 0; k < 10; k++) {
                       while (queue.get(k).size() > 0) {
                           ArrayList<Integer> queue3 = queue.get(k);
                           a[count] = queue3.get(0);
                           queue3.remove(0);
                           count++;
                       }
                   }
               }
        }
```

### 8.总结：
一、稳定性:

　   稳定：冒泡排序、插入排序、归并排序和基数排序

　　不稳定：选择排序、快速排序、希尔排序、堆排序

二、平均时间复杂度

　　O(n^2):直接插入排序，简单选择排序，冒泡排序。

　　在数据规模较小时（9W内），直接插入排序，简单选择排序差不多。当数据较大时，冒泡排序算法的时间代价最高。性能为O(n^2)的算法基本上是相邻元素进行比较，基本上都是稳定的。

　　O(nlogn):快速排序，归并排序，希尔排序，堆排序。

　　其中，快排是最好的， 其次是归并和希尔，堆排序在数据量很大时效果明显。

三、排序算法的选择

　　1.数据规模较小

  　　（1）待排序列基本序的情况下，可以选择直接插入排序；

  　　（2）对稳定性不作要求宜用简单选择排序，对稳定性有要求宜用插入或冒泡

　　2.数据规模不是很大

　　（1）完全可以用内存空间，序列杂乱无序，对稳定性没有要求，快速排序，此时要付出log（N）的额外空间。

　　（2）序列本身可能有序，对稳定性有要求，空间允许下，宜用归并排序

　　3.数据规模很大

   　　（1）对稳定性有求，则可考虑归并排序。

    　　（2）对稳定性没要求，宜用堆排序

　　4.序列初始基本有序（正序），宜用直接插入，冒泡
  
## 高级算法

### 贪婪
### 回溯
### 减枝

### 递归

将原问题分解为若干个规模较小但类似于原问题的子问题（Divide），「递归」的求解这些子问题（Conquer），然后再合并这些子问题的解来建立原问题的解。

因为在求解大问题时，需要递归的求小问题，因此一般用「递归」的方法实现，即自顶向下。


### 动态规划

动态规划其实和分治策略是类似的，也是将一个原问题分解为若干个规模较小的子问题，递归的求解这些子问题，然后合并子问题的解得到原问题的解。  
区别在于这些子问题会有重叠，一个子问题在求解后，可能会再次求解，于是我们想到将这些子问题的解存储起来，当下次再次求解这个子问题时，直接拿过来就是。  
其实就是说，动态规划所解决的问题是分治策略所解决问题的一个子集，只是这个子集更适合用动态规划来解决从而得到更小的运行时间。  
即用动态规划能解决的问题分治策略肯定能解决，只是运行时间长了。因此，分治策略一般用来解决子问题相互对立的问题，称为标准分治，而动态规划用来解决子问题重叠的问题。

与「分治策略」「动态规划」概念接近的还有「贪心算法」「回溯算法」

将「动态规划」的概念关键点抽离出来描述就是这样的：
1.动态规划法试图只解决每个子问题一次
2.一旦某个给定子问题的解已经算出，则将其记忆化存储，以便下次需要同一个子问题解之时直接查表。
