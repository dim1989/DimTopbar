package com.dim.topbar;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

/**
 * android中使用iconfont
 */
public class DimEduSohoIconButton extends AppCompatButton {

    public DimEduSohoIconButton(Context context) {
        super(context);
        initView(context);
    }

    public DimEduSohoIconButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public DimEduSohoIconButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        Typeface iconfont = Typeface.createFromAsset(context.getAssets(), "iconfont.ttf");
        setTypeface(iconfont);
    }
}
