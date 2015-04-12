package com.astrogro.astrogro;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class FontTextView extends TextView {

    public FontTextView(Context context) {
        super(context);
    }

    public FontTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        UiUtil.setCustomFont(this,context,attrs,
                R.styleable.com_astrogro_astrogro_FontableTextView,
                R.styleable.com_astrogro_astrogro_FontableTextView_font);
    }

    public FontTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        UiUtil.setCustomFont(this,context,attrs,
                R.styleable.com_astrogro_astrogro_FontableTextView,
                R.styleable.com_astrogro_astrogro_FontableTextView_font);
    }
}