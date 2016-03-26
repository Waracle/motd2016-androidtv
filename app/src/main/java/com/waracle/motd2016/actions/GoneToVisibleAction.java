package com.waracle.motd2016.actions;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * A wrapper around an element which changes from {@link View#GONE} to {@link View#VISIBLE} when it is executed.
 */
public class GoneToVisibleAction extends FrameLayout implements SlideAction {

    public GoneToVisibleAction(Context context) {
        super(context);
    }

    public GoneToVisibleAction(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GoneToVisibleAction(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public GoneToVisibleAction(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        if (!isInEditMode()) {
            reset();
        }

    }

    @Override
    public void performAction() {
        setVisibility(View.VISIBLE);
    }

    @Override
    public void reset() {
        setVisibility(View.GONE);
    }

}
