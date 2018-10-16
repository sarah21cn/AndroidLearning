## Android应用启动

> 安卓应用的启动方式分为三种：冷启动、暖启动、热启动，不同的启动方式决定了应用UI对用户可见所需要花费的时间长短。顾名思义，冷启动消耗的时间最长。基于冷启动方式的优化工作也是最考验产品用户体验的地方。

### 冷启动

> 当启动应用时，后台没有该应用的进程，这时系统会重新创建一个新的进程分配给该应用，这个启动方式就是冷启动。

冷启动因为系统会重新创建一个新的进程分配给它，所以会先创建和初始化Application类，再创建和初始化MainActivity类（包括一系列的测量、布局、绘制），最后显示在界面上。只有当应用完成第一次绘制，系统当前展示的空白背景才会消失，才会被 Activity 的内容视图替换掉。也就是这个时候，用户才能和我们的应用开始交互。

![Cold Launch](http://ocq7gtgqu.bkt.clouddn.com/1479177555.png)

#### 冷启动流程

当点击app的启动图标时，安卓系统会从Zygote进程中fork创建出一个新的进程分配给该应用，之后会依次创建和初始化Application类、创建MainActivity类、加载主题样式Theme中的windowBackground等属性设置给MainActivity以及配置Activity层级上的一些属性、再inflate布局、当onCreate/onStart/onResume方法都走完了后最后才进行contentView的measure/layout/draw显示在界面上。所以直到这里，应用的第一次启动才算完成，这时候我们看到的界面也就是所说的第一帧。所以，总结一下，应用的启动流程如下：

**Application的构造器方法——>attachBaseContext()——>onCreate()——>Activity的构造方法——>onCreate()——>配置主题中背景等属性——>onStart()——>onResume()——>测量布局绘制显示在界面上。**

**大致流程如下：**

1、点击桌面图标，Launcher会启动程序默认的Acticity，之后再按照程序的逻辑启动各种Activity

2、启动Activity都需要借助应用程序框架层的ActivityManagerService服务进程(Service也是由ActivityManagerService进程来启动的)；在Android应用程序框架层中，ActivityManagerService是一个非常重要的接口，它不但负责启动Activity和Service，还负责管理Activity和Service。

Step 1. 无论是通过Launcher来启动Activity，还是通过Activity内部调用startActivity接口来启动新的Activity，都通过Binder进程间通信进入到ActivityManagerService进程中，并且调用ActivityManagerService.startActivity接口；

Step 2. ActivityManagerService调用ActivityStack.startActivityMayWait来做准备要启动的Activity的相关信息；

Step 3. ActivityStack通知ApplicationThread要进行Activity启动调度了，这里的ApplicationThread代表的是调用ActivityManagerService.startActivity接口的进程，对于通过点击应用程序图标的情景来说，这个进程就是Launcher了，而对于通过在Activity内部调用startActivity的情景来说，这个进程就是这个Activity所在的进程了；

Step 4. ApplicationThread不执行真正的启动操作，它通过调用ActivityManagerService.activityPaused接口进入到ActivityManagerService进程中，看看是否需要创建新的进程来启动Activity；

Step 5. 对于通过点击应用程序图标来启动Activity的情景来说，ActivityManagerService在这一步中，会调用startProcessLocked来创建一个新的进程，而对于通过在Activity内部调用startActivity来启动新的Activity来说，这一步是不需要执行的，因为新的Activity就在原来的Activity所在的进程中进行启动；

Step 6. ActivityManagerServic调用ApplicationThread.scheduleLaunchActivity接口，通知相应的进程执行启动Activity的操作；

Step 7. ApplicationThread把这个启动Activity的操作转发给ActivityThread，ActivityThread通过ClassLoader导入相应的Activity类，然后把它启动起来。

### 温启动

> 当应用中的 Activities 被销毁，但在内存中常驻时，应用的启动方式就会变为暖启动。相比冷启动，暖启动过程减少了对象初始化、布局加载等工作，启动时间更短。但启动时，系统依然会展示一个空白背景，直到第一个 Activity 的内容呈现为止。

### 热启动

> 相比暖启动，热启动时应用做的工作更少，启动时间更短。热启动产生的场景很多，常见如：用户使用返回键退出应用，然后马上又重新启动应用。

### 启动时间

从 Android 4.4 (API 19) 开始，Logcat 自动帮我们打印出应用的启动时间。这个时间值从应用启动 (创建进程) 开始计算，到完成视图的第一次绘制 (即 Activity 内容对用户可见) 为止。**搜索Displayed可以看到每个activity的启动时长**

### 优化方案

应用的冷启动总是无法避免的，也就是说冷启动时用户总要经历一个启动等待时间。开发人员唯一能做的就是在 Application 和 第一个 Activity 中，减少 onCreate() 方法的工作量，从而缩短冷启动的时间。

1、一般应用中嵌入的一些第三方 SDK，都建议在 Application 中做一些初始化工作，开发人员不妨采取懒加载的形式移除这部分代码，而在真正需要用到第三方 SDK 时再进行初始化。

2、 为MainActivity在Manifest中设置一个主题，可以是透明的，也可以设置background作为启动页，也可以和MainActivity的UI类似，让用户以为已经进入了MainActivity。然后在MainActivity OnCreate中将主题还原即可。还可以适度结合 Activity 内容视图使用动画过渡效果。

```xml
<?xml version="1.0" encoding="utf-8"?>
<layer-list 
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:opacity="opaque">

    <item android:drawable="@color/grey"/>
    <item>
        <bitmap
            android:gravity="center"
            android:src="@drawable/img_pizza"/>
    </item>
</layer-list>
```

```xml
<style name="LaunchStyle" parent="AppTheme">
	<item name="android:windowBackground">@drawable/shape_launch</item>
</style>
```

```xml
<activity 
    android:name=".MainActivity"
    android:theme="@style/LaunchStyle">
    <intent-filter>
        <action android:name="android.intent.action.MAIN" />
        <category android:name="android.intent.category.LAUNCHER" />
    </intent-filter>
</activity>
```

```java
@Override
protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setTheme(R.style.AppTheme);
    setContentView(R.layout.activity_onboarding_center);
}

@Override
public void onWindowFocusChanged(boolean hasFocus) {
    if (!hasFocus || animationStarted) {
        return;
    }
    animate(); //冷启动完成，oncreate后，加载过渡动画
    super.onWindowFocusChanged(hasFocus);
}
```

### 参考

https://www.jianshu.com/p/1d2e55f1d393

https://yifeng.studio/2016/11/15/android-optimize-for-cold-start/

http://saulmm.github.io/avoding-android-cold-starts

https://github.com/saulmm/onboarding-examples-android （启动衔接动画）