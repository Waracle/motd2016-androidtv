package com.waracle.motd2016;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Disable standard interaction with the view pager... we'll control moving to different
 *  pages via the controller (using {@link android.view.KeyEvent}s).
 */
public class DisabledViewPager extends ViewPager {
    public DisabledViewPager(Context context) {
        super(context);
    }

    public DisabledViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return false;
    }

    @Override
    public boolean arrowScroll(int direction) {
        return false;
    }

}
