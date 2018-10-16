## Activity启动流程

### 前言

- 一个App是怎么启动起来的？
- App的程序入口到底是哪里？
- Launcher到底是什么神奇的东西？
- 听说还有个AMS的东西，它是做什么的？
- Binder是什么？他是如何进行IPC通信的？
- Activity生命周期到底是什么时候调用的？被谁调用的？

### 主要对象

- ActivityManagerServices，简称AMS，服务端对象，负责系统中所有Activity的生命周期
- ActivityThread，App的真正入口。当开启App之后，会调用main()开始运行，开启消息循环队列，这就是传说中的UI线程或者叫主线程。与ActivityManagerServices配合，一起完成Activity的管理工作
- ApplicationThread，用来实现ActivityManagerService与ActivityThread之间的交互。在ActivityManagerService需要管理相关Application中的Activity的生命周期时，通过ApplicationThread的代理对象与ActivityThread通讯。
- ApplicationThreadProxy，是ApplicationThread在服务器端的代理，负责和客户端的ApplicationThread通讯。AMS就是通过该代理与ActivityThread进行通信的。
- Instrumentation，每一个应用程序只有一个Instrumentation对象，每个Activity内都有一个对该对象的引用。Instrumentation可以理解为应用进程的管家，ActivityThread要创建或暂停某个Activity时，都需要通过Instrumentation来进行具体的操作。
- ActivityStack，Activity在AMS的栈管理，用来记录已经启动的Activity的先后关系，状态信息等。通过ActivityStack决定是否需要启动新的进程。
- ActivityRecord，ActivityStack的管理对象，每个Activity在AMS对应一个ActivityRecord，来记录Activity的状态以及其他的管理信息。其实就是服务器端的Activity对象的映像。
- TaskRecord，AMS抽象出来的一个“任务”的概念，是记录ActivityRecord的栈，一个“Task”包含若干个ActivityRecord。AMS用TaskRecord确保Activity启动和退出的顺序。如果你清楚Activity的4种launchMode，那么对这个概念应该不陌生。

### 主要流程

#### zygote

Android是基于Linux系统的，而在Linux中，所有的进程都是由init进程直接或者是间接fork出来的，zygote进程也不例外。在Android系统里面，zygote是一个进程的名字。之后所有开启的新进程，都是通过zygote进程fork出来的。

#### SystemServer

SystemServer是由zygote进程fork出来的。在zygote开启的时候，会调用ZygoteInit.main()进行初始化，然后调用startSystemServer() fork一个新的System Server进程。

#### ActivityManagerService (AMS)

ActivityManagerService，简称AMS，服务端对象，负责系统中所有Activity的生命周期。ActivityManagerService进行初始化的时机很明确，就是在SystemServer进程开启的时候，就会初始化ActivityManagerService。

具体流程如下：





### 参考

https://blog.csdn.net/zhaokaiqiang1992/article/details/49428287#%E4%B8%BB%E8%A6%81%E5%AF%B9%E8%B1%A1%E5%8A%9F%E8%83%BD%E4%BB%8B%E7%BB%8D

