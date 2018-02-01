package com.dim.topbar

import android.content.Context
import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.RelativeLayout
import org.jetbrains.anko.backgroundColor
import org.jetbrains.anko.doAsync
import org.jetbrains.anko.textColor
import org.jetbrains.anko.uiThread


/**
 * 通用的头部导航栏，包括了标题，左边一个按钮，右边一个按钮，右边按钮的左边还有一个按钮；
 * XML中使用的时候，需要配置一些属性址，如：app:appType="1"，类型1表示顶部背景默认为黑色，字体颜色是白色的，2为蓝底黑字；3为白底黑字
 * 其他具体属性见attes-Topbar；
 * 如果需要监听点击事件，
 * Activity等调用的地方，需要实现一个KLMTopBarContract.IKLMTopBarView，为按钮的点击事件，
 * 需要传入一个KLMTopBarContract.IKLMTopBarView，方法：setKLMTopBarPresent（），来接受点击事件
 * 当调用View销毁的时候，调用KLMTopBarView的onViewDestory（）来销毁一些事件等，
 *
 * Created by dim on 2017/6/6 14:52
 * 邮箱：271756926@qq.com
 */
class DimTopBarView : RelativeLayout {

    /**
     * 类型1表示顶部背景默认为黑色，字体颜色是白色的，2为蓝底黑字；3为白底黑字
     */
    companion object {
        val TOP_BAR_TYPE_BLACK = 1
        val TOP_BAR_TYPE_BLUE = 2
        val TOP_BAR_TYPE_KLM_WHITE = 3
    }

    private var defaultTextSize = 26F

    private var mContext: Context? = null
    private var mDimTopBarPresent: DimTopBarPresent? = null
    private var mDimTopBarView: DimTopBarContract.IKLMTopBarView? = null

    private var layout: RelativeLayout? = null
    private var mLeftView: DimEduSohoIconTextView? = null
    private var mRightView: DimEduSohoIconTextView? = null
    private var mTitleView: DimEduSohoIconTextView? = null
    private var mRightLeftView: DimEduSohoIconTextView? = null
    private var topBarBag: RelativeLayout? = null

    private var titleViewStr: String? = null
    private var titleViewSize: Float = 0F
    private var titleViewColor: Int = 0
    private var iconViewSize: Float = 0F
    private var iconViewColor: Int = 0
    private var topBarColor: Int = 0

    private var leftViewStr: String? = null
    private var rightViewStr: String? = null
    private var rightLeftViewStr: String? = null

    private var leftViewSize: Float = 0F
    private var rightViewSize: Float = 0F
    private var rightLeftViewSize: Float = 0F

    private var leftViewColor: Int = 0
    private var rightViewColor: Int = 0
    private var rightLeftViewColor: Int = 0

    private var appType: Int = 1

    private var commonTextColor: Int = 0

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        getConfig(context, attrs)
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        getConfig(context, attrs)
        initView(context)
    }

    fun setKLMTopBarPresent(topBarView: DimTopBarContract.IKLMTopBarView?) {
        mDimTopBarView = topBarView
        mDimTopBarPresent = DimTopBarPresent(mDimTopBarView)
    }

    fun onViewDestory() {
        mDimTopBarView = null
        mDimTopBarPresent!!.unSubscribe()
        mDimTopBarPresent = null
    }

    /**
     * 从xml中获取配置信息
     */
    private fun getConfig(context: Context, attrs: AttributeSet) {
        // TypedArray是一个数组容器用于存放属性值
        val ta = context.obtainStyledAttributes(attrs, R.styleable.Topbar)

        val count = ta.indexCount
        for (i in 0..count - 1) {
            val attr = ta.getIndex(i)
            when (attr) {
            // app类型
                R.styleable.Topbar_appType -> appType = ta.getInt(R.styleable.Topbar_appType, 1)

                R.styleable.Topbar_commonColor ->
                    // 默认颜色设置为白色
                    commonTextColor = ta.getColor(attr, Color.WHITE)
            // 标题内容
                R.styleable.Topbar_titleText -> titleViewStr = ta.getString(R.styleable.Topbar_titleText)
            // 右边图标内容
                R.styleable.Topbar_rightText -> rightViewStr = ta.getString(R.styleable.Topbar_rightText)
            // 右边左边图标内容
                R.styleable.Topbar_rightLeftText -> rightLeftViewStr = ta.getString(R.styleable.Topbar_rightLeftText)
            // 左边图标内容
                R.styleable.Topbar_leftText -> leftViewStr = ta.getString(R.styleable.Topbar_leftText)
            // 标题颜色
                R.styleable.Topbar_titleColor ->
                    // 默认颜色设置为白色
                    titleViewColor = ta.getColor(attr, Color.WHITE)
            // 标题大小
                R.styleable.Topbar_titleSize ->
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    titleViewSize = ta.getDimensionPixelSize(attr,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 36f,
                                    resources.displayMetrics).toInt()).toFloat()
            // 整个的背景色
                R.styleable.Topbar_barBgColor ->
                    // 默认颜色设置为白色
                    topBarColor = ta.getColor(attr, Color.WHITE)
            // icon的大小
                R.styleable.Topbar_iconSize ->
                    iconViewSize = ta.getDimensionPixelSize(attr,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16f,
                                    resources.displayMetrics).toInt()).toFloat()
            // icon的颜色
                R.styleable.Topbar_iconColor ->
                    // 默认颜色设置为白色
                    iconViewColor = ta.getColor(attr, Color.WHITE)
            // 左边icon颜色
                R.styleable.Topbar_leftColor ->
                    // 默认颜色设置为白色
                    leftViewColor = ta.getColor(attr, Color.WHITE)
            // 左边icon大小
                R.styleable.Topbar_leftSize ->
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    leftViewSize = ta.getDimensionPixelSize(attr,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 36f,
                                    resources.displayMetrics).toInt()).toFloat()
            // 右边icon颜色
                R.styleable.Topbar_rightColor ->
                    // 默认颜色设置为白色
                    rightViewColor = ta.getColor(attr, Color.WHITE)
            // 右边icon大小
                R.styleable.Topbar_rightSize ->
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    rightViewSize = ta.getDimensionPixelSize(attr,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 36f,
                                    resources.displayMetrics).toInt()).toFloat()
            // 右边左边icon颜色
                R.styleable.Topbar_rightLeftColor ->
                    // 默认颜色设置为白色
                    rightLeftViewColor = ta.getColor(attr, Color.WHITE)
            // 右边左边icon大小
                R.styleable.Topbar_rightLeftSize ->
                    // 默认设置为16sp，TypeValue也可以把sp转化为px
                    rightLeftViewSize = ta.getDimensionPixelSize(attr,
                            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 36f,
                                    resources.displayMetrics).toInt()).toFloat()
            }
        }
        // 用完务必回收容器
        ta.recycle()
    }

    private fun initView(context: Context) {
        this.mContext = context
        val layout = LayoutInflater.from(context).inflate(R.layout.layout_custom_head, this, true)
        mLeftView = layout.findViewById(R.id.klm_topbar_left) as DimEduSohoIconTextView
        mTitleView = layout.findViewById(R.id.klm_topbar_title) as DimEduSohoIconTextView
        mRightView = layout.findViewById(R.id.klm_topbar_right) as DimEduSohoIconTextView
        mRightLeftView = layout.findViewById(R.id.klm_topbar_right_left) as DimEduSohoIconTextView
        topBarBag = layout.findViewById(R.id.klm_topbar_bg) as RelativeLayout
        if (commonTextColor == 0) {
            when (appType) {
                1 -> {
                    commonTextColor = Color.WHITE
                }
                2 -> {
                    commonTextColor = Color.WHITE
                }
                3 -> {
                    commonTextColor = R.color.klm_666
                }
            }
        }
        // 左边的按钮
        if (TextUtils.isEmpty(leftViewStr)) {
            mLeftView!!.visibility = View.GONE
        } else {
            mLeftView!!.text = leftViewStr
            mLeftView!!.visibility = View.VISIBLE
        }
        mLeftView!!.setOnClickListener { mDimTopBarPresent?.doLeftClick() }
        // 右边的按钮
        if (TextUtils.isEmpty(rightViewStr)) {
            mRightView!!.visibility = View.GONE
        } else {
            mRightView!!.text = rightViewStr
            mRightView!!.visibility = View.VISIBLE
        }
        mRightView!!.setOnClickListener { mDimTopBarPresent?.doRightClick() }
        // 右边的左边的按钮
        if (TextUtils.isEmpty(rightLeftViewStr)) {
            mRightLeftView!!.visibility = View.GONE
        } else {
            mRightLeftView!!.text = rightLeftViewStr
            mRightLeftView!!.visibility = View.VISIBLE
        }
        mRightLeftView!!.setOnClickListener { mDimTopBarPresent?.doRightLeftClick() }
        showTypeForColor()
    }

    /**
     * 设置标题内容, 如果不需要设置大小，大小传null
     */
    fun setTopBarTitle(title: String, size: Float?) {
        if (size != null) {
            titleViewSize = size
        }
        setTopBarTitle(title)
    }

    /**
     * 设置标题内容
     */
    fun setTopBarTitle(title: String) {
        doAsync {
            uiThread {
                if (!TextUtils.isEmpty(title)) {
                    titleViewStr = title
                    if (mTitleView != null) {
                        mTitleView!!.text = titleViewStr
                        mTitleView!!.visibility = View.VISIBLE
                        if (titleViewSize > 0)
                            mTitleView!!.textSize = titleViewSize
                    }
                }
            }
        }
    }

    /**
     * 获取右边的控件
     *
     * @author dim.
     * @time 2018/1/3 14:25.
     */
    fun getTopbarRightView(): View {
        return mRightView!!
    }

    /**
     * 设置左边按钮内容, 如果不需要设置大小，大小传null
     */
    fun setTopBarLeft(title: String, size: Float?) {
        if (size != null) {
            leftViewSize = size
        }
        setTopBarLeft(title)
    }

    /**
     * 设置左边按钮内容
     */
    fun setTopBarLeft(title: String) {
        doAsync {
            uiThread {
                if (!TextUtils.isEmpty(title)) {
                    leftViewStr = title
                    if (mLeftView != null) {
                        mLeftView!!.text = leftViewStr
                        mLeftView!!.visibility = View.VISIBLE
                        if (leftViewSize > 0)
                            mLeftView!!.textSize = leftViewSize
                    }
                }
            }
        }
    }

    fun setTopBarLeftVisiable(vis: Boolean) {
        doAsync {
            uiThread {
                if (vis) {
                    mLeftView!!.visibility = View.VISIBLE
                } else {
                    mLeftView!!.visibility = View.INVISIBLE
                }
            }
        }
    }

    /**
     * 设置右边按钮内容, 如果不需要设置大小，大小传null
     */
    fun setTopBarRight(title: String, size: Float?) {
        if (size != null) {
            rightViewSize = size
        }
        setTopBarRight(title)
    }

    fun setTopBarRight(title: String, size: Float?, color: Int) {
        if (size != null) {
            rightViewSize = size
        }
        if (color > 0) {
            rightViewColor = color
        }
        setTopBarRight(title)
    }

    fun setTopBarRightVisiable(vis: Boolean) {
        doAsync {
            uiThread {
                if (vis) {
                    mRightView!!.visibility = View.VISIBLE
                } else {
                    mRightView!!.visibility = View.INVISIBLE
                }
            }
        }
    }

    /**
     * 设置右边按钮内容
     */
    fun setTopBarRight(title: String) {
        doAsync {
            uiThread {
                if (!TextUtils.isEmpty(title)) {
                    rightViewStr = title
                    if (mRightView != null) {
                        mRightView!!.text = rightViewStr
                        mRightView!!.visibility = View.VISIBLE
                        if (rightViewSize > 0)
                            mRightView!!.textSize = rightViewSize
                    }
                }
            }
        }
    }

    /**
     * 设置右边左边按钮内容, 如果不需要设置大小，大小传null
     */
    fun setTopBarRightLeft(title: String, size: Float?) {
        if (size != null) {
            rightLeftViewSize = size
        }
        setTopBarRightLeft(title)
    }

    fun setTopBarRightLeft(title: String, size: Float?, color: Int) {
        if (size != null) {
            rightLeftViewSize = size
        }
        if (color > 0) {
            rightLeftViewColor = color
        }
        setTopBarRightLeft(title)
    }

    /**
     * 设置右边左边按钮内容
     */
    fun setTopBarRightLeft(title: String) {
        doAsync {
            uiThread {
                if (!TextUtils.isEmpty(title)) {
                    rightLeftViewStr = title
                    if (mRightLeftView != null) {
                        mRightLeftView!!.text = rightLeftViewStr
                        mRightLeftView!!.visibility = View.VISIBLE
                        if (rightLeftViewSize > 0)
                            mRightLeftView!!.textSize = rightLeftViewSize
                    }
                }
            }
        }
    }

    fun setTopBarType(type: Int) {
        this@DimTopBarView.appType = type
        showTypeForColor()
    }

    fun showTypeForColor() {
        // 如果是卡乐猫的
        if (appType == 1) {
            topBarBag!!.background = resources.getDrawable(R.color.com_bg_klm)
        } else if (appType == 2) {
            topBarBag!!.background = resources.getDrawable(R.color.com_bg_mm)
        } else if (appType == 3) {
            topBarBag!!.background = resources.getDrawable(R.color.white)
        }
        mTitleView!!.textColor = commonTextColor
        mLeftView!!.textColor = commonTextColor
        mRightView!!.textColor = commonTextColor
        mRightLeftView!!.textColor = commonTextColor
        mLeftView!!.textSize = defaultTextSize
        mRightView!!.textSize = defaultTextSize
        mRightLeftView!!.textSize = defaultTextSize
        mTitleView!!.textSize = 18F
        // 标题颜色
        if (titleViewColor != 0) {
            mTitleView!!.textColor = titleViewColor
        }
        // 标题大小
        if (titleViewSize > 0) {
            mTitleView!!.textSize = titleViewSize
        }
        // 整个的背景色
        if (topBarColor != 0) {
            topBarBag!!.backgroundColor = topBarColor
        }
        // icon的大小
        if (iconViewSize > 0) {
            mLeftView!!.textSize = iconViewSize
            mRightView!!.textSize = iconViewSize
            mRightLeftView!!.textSize = iconViewSize
        }
        // icon的颜色
        if (iconViewColor != 0) {
            mLeftView!!.textColor = iconViewColor
            mRightView!!.textColor = iconViewColor
            mRightLeftView!!.textColor = iconViewColor
        }
        // title的
        if (!TextUtils.isEmpty(titleViewStr)) {
            mTitleView!!.text = titleViewStr
        }
        if (titleViewSize > 0) {
            mTitleView!!.textSize = titleViewSize
        }
        if (leftViewColor != 0) {
            mLeftView!!.textColor = leftViewColor
        }
        if (rightViewColor != 0) {
            mRightView!!.textColor = rightViewColor
        }
        if (rightLeftViewColor != 0) {
            mRightLeftView!!.textColor = rightLeftViewColor
        }
        if (leftViewSize > 0) {
            mLeftView!!.textSize = leftViewSize
        }
        if (rightViewSize > 0) {
            mRightView!!.textSize = rightViewSize
        }
        if (rightLeftViewSize > 0) {
            mRightLeftView!!.textSize = rightLeftViewSize
        }
    }

    fun setTopBarLeftSizeDifferent(title: String) {
        if (TextUtils.isEmpty(title) || title.length == 1) {
            setTopBarLeft(title)
            return
        }
        leftViewStr = title
        doAsync {
            uiThread {
                val textSpan = SpannableString(leftViewStr)
                textSpan.setSpan(AbsoluteSizeSpan(70), 0, 1, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                textSpan.setSpan(AbsoluteSizeSpan(35), 1, leftViewStr!!.length, Spannable.SPAN_INCLUSIVE_INCLUSIVE)
                mLeftView!!.text = textSpan
            }
        }
    }
}