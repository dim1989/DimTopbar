package com.dim.topbar

import rx.Subscription


/**
 * @author by dim
 * @data 2018/1/31 14:05
 * 邮箱：271756926@qq.com
 */
class DimTopBarPresent : DimTopBarContract.IKLMTopBarPresent {

    private val mSubscription: Subscription? = null

    private var mIklmTopBarView: DimTopBarContract.IKLMTopBarView? = null

    constructor(dimView: DimTopBarContract.IKLMTopBarView?) {
        this.mIklmTopBarView = dimView
    }

    override
    fun subscribe() {

    }

    override
    fun unSubscribe() {
        if (mSubscription != null && !mSubscription.isUnsubscribed) {
            mSubscription.unsubscribe()
        }
    }

    fun doLeftClick() {
        mIklmTopBarView!!.onLeftClick()
    }

    fun doRightClick() {
        mIklmTopBarView!!.onRightClick()
    }

    fun doRightLeftClick() {
        mIklmTopBarView!!.onRightLeftClick()
    }

}