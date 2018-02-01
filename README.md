# DimTopbar
通用的头部导航栏，包括了标题，左边一个按钮，右边一个按钮，右边按钮的左边还有一个按钮；

## 
 * XML中使用的时候，需要配置一些属性址，如：app:appType="1"，类型1表示顶部背景默认为黑色，字体颜色是白色的，2为蓝底黑字；3为白底黑字
 * 其他具体属性见attes-Topbar；
 * 如果需要监听点击事件，
 * Activity等调用的地方，需要实现一个KLMTopBarContract.IKLMTopBarView，为按钮的点击事件，
 * 需要传入一个KLMTopBarContract.IKLMTopBarView，方法：setKLMTopBarPresent（），来接受点击事件
 * 当调用View销毁的时候，调用KLMTopBarView的onViewDestory（）来销毁一些事件等，
 * 显示图标使用了Iconfont（http://www.iconfont.cn）

## XML中的引用：
```java
  <com.dim.topbar.DimTopBarView
        android:id="@+id/topbar2"
        android:layout_width="match_parent"
        android:layout_height="50dip"
        app:appType="2"
        app:leftColor="@color/buy_red"
        app:leftText="@string/icon_topbar_back"
        app:rightLeftText="@string/icon_topbar_msg"
        app:rightText="@string/icon_topbar_me"
        app:titleText="title" />
```

## 设置监听：
```
 topbar1.setKLMTopBarPresent(object : DimTopBarContract.IKLMTopBarView {
            override fun onLeftClick() {
                toast("onLeftClick")
            }

            override fun onRightClick() {
                toast("onRightClick")
            }

            override fun onRightLeftClick() {
                toast("onRightLeftClick")
            }
        })
 ```
 
 ## XML中设置的属性：
 ```
         <!-- 设置背景色 -->
        <attr name="barBgColor" format="color|reference"/>
        <!-- 设置标题内容 -->
        <attr name="titleText" format="string|reference"/>
        <!-- 设置标题大小 -->
        <attr name="titleSize" format="dimension|reference"/>
        <!-- 设置标题颜色 -->
        <attr name="titleColor" format="color|reference"/>
        <!-- 设置左边内容 -->
        <attr name="leftText" format="string|reference"/>
        <!-- 设置右边内容 -->
        <attr name="rightText" format="string|reference"/>
        <!-- 设置右边靠左的内容 -->
        <attr name="rightLeftText" format="string|reference"/>
        <!-- 设置通用的icon的大小 -->
        <attr name="iconSize" format="dimension|reference"/>
        <!-- 设置通用的icon的颜色 -->
        <attr name="iconColor" format="color|reference"/>
        <!-- 当前的类型，类型1表示顶部背景默认为黑色，字体颜色是白色的，2为蓝底黑字；3为白底黑字 -->
        <attr name="appType" format="integer|reference"/>
        <!-- 设置左边内容的颜色 -->
        <attr name="leftColor" format="color|reference"/>
        <!-- 设置右边内容的颜色 -->
        <attr name="rightColor" format="color|reference"/>
        <!-- 设置右边靠左的内容颜色 -->
        <attr name="rightLeftColor" format="color|reference"/>
        <!-- 设置左边内容大小 -->
        <attr name="leftSize" format="dimension|reference"/>
        <!-- 设置右边内容大小 -->
        <attr name="rightSize" format="dimension|reference"/>
        <!-- 设置右边靠左内容大小 -->
        <attr name="rightLeftSize" format="dimension|reference"/>
        <!-- 设置通用字体颜色 -->
        <attr name="commonColor" format="color|reference"/>
 ```
 ## 在JAVA中设置属性：
  ```
  可以调用如“setTopBarTitle”，“setTopBarLeft”，“setTopBarType”等对应方法设置对应的属性。
   ```
