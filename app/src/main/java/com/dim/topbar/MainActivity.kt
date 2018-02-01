package com.dim.topbar

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.toast

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // topbar.setTopBarLeft(resources.getString(R.string.icon_topbar_modify))

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
    }
}
