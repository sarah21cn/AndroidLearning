## Binder机制

### Binder是什么？

![å®ä¹](http://upload-images.jianshu.io/upload_images/944365-45db4df339348b9b.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### Binder驱动

![ç¤ºæå¾](http://upload-images.jianshu.io/upload_images/944365-82d6a0658e55e9d7.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 原理

![ç¤ºæå¾](http://upload-images.jianshu.io/upload_images/944365-c10d6032f91a103f.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

说明：

![ç¤ºæå¾](http://upload-images.jianshu.io/upload_images/944365-135560c87c983e43.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 工作流程

![ç¤ºæå¾](http://upload-images.jianshu.io/upload_images/944365-65a5b17426aed424.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 详细工作流程

![ç¤ºæå¾](http://upload-images.jianshu.io/upload_images/944365-d3c78b193c3e8a38.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### 线程管理

- `Server`进程会创建很多线程来处理`Binder`请求
- `Binder`模型的线程管理 采用`Binder`驱动的线程池，并由`Binder`驱动自身进行管理 
- 一个进程的`Binder`线程数默认最大是16，超过的请求会被阻塞等待空闲的Binder线程。

#### 具体实现





### 参考

https://blog.csdn.net/carson_ho/article/details/73560642