package com.dim.topbar

/**
 * Created by dim on 2017/6/6 16:18
 * 邮箱：271756926@qq.com
 */
interface DimTopBarContract {

    interface IKLMTopBarView {
        fun onLeftClick()
        fun onRightClick()
        fun onRightLeftClick()
    }

    interface IKLMTopBarPresent {
        fun subscribe()
        fun unSubscribe()
    }

}