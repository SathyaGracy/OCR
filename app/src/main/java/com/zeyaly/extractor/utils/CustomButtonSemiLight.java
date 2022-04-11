package com.zeyaly.extractor.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatButton;

public class CustomButtonSemiLight extends AppCompatButton {

	public CustomButtonSemiLight(Context context, AttributeSet attrs, int defStyle) {
	      super(context, attrs, defStyle);
	      init();
	  }

	 public CustomButtonSemiLight(Context context, AttributeSet attrs) {
	      super(context, attrs);
	      init();
	  }

	 public CustomButtonSemiLight(Context context) {
	      super(context);
	      init();
	 }


	public void init() {
	    Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/Gilroy-Medium.ttf");
	    setTypeface(tf);
	}
}