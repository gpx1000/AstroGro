package com.astrogro.astrogro;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

public class FontButtonView extends Button {
    public FontButtonView(Context context) {
        super(context);
    }

    public FontButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
        UiUtil.setCustomFont(this, context, attrs,
                R.styleable.com_astrogro_astrogro_FontableButton,
                R.styleable.com_astrogro_astrogro_FontableButton_font);
    }

    public FontButtonView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        UiUtil.setCustomFont(this, context, attrs,
                R.styleable.com_astrogro_astrogro_FontableButton,
                R.styleable.com_astrogro_astrogro_FontableButton_font);
    }
}