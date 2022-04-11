package com.zeyaly.extractor.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatCheckedTextView;

public class CustomCheckedTextview extends AppCompatCheckedTextView {

    public CustomCheckedTextview(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public CustomCheckedTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomCheckedTextview(Context context) {
        super(context);
        init();
    }


    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Gilroy-Light.ttf");
        setTypeface(tf);
    }
}