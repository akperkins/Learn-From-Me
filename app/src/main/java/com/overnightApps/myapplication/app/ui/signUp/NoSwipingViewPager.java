package com.overnightApps.myapplication.app.ui.signUp;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by andre on 4/20/14.
 */
public class NoSwipingViewPager extends ViewPager {

    public NoSwipingViewPager(Context context) {
        super(context);
    }

    public NoSwipingViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent arg0) {
        // Never allow swiping to switch between pages
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // Never allow swiping to switch between pages
        return false;
    }
}
