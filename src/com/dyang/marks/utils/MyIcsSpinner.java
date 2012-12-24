package com.dyang.marks.utils;

import android.content.Context;
import android.util.AttributeSet;

import com.actionbarsherlock.internal.widget.IcsSpinner;

public class MyIcsSpinner extends IcsSpinner {
    public MyIcsSpinner(Context context, AttributeSet attrs) {
        super(context, attrs, com.actionbarsherlock.R.attr.actionDropDownStyle);
      }

      public MyIcsSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
      }
}
