package com.waracle.motd2016;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.waracle.motd2016.actions.SlideAction;

/**
 * Inflates the specified layout id and Looks for {@link SlideAction} views with tags in the
 * format of ACTION_0 (where 0 will increment) and executes them in turn every
 * time {@link #nextAction()} is called.
 */
public class SlideFragment extends Fragment {

    private static final String TAG = SlideFragment.class.getSimpleName();

    private static final String ARG_LAYOUT = "LAYOUT_ID";

    private int mCurrentAction;
    private View mRootView;

    public static SlideFragment newInstance(@LayoutRes int layoutId) {

        Log.d(TAG, "newInstance: " + layoutId);

        Bundle args = new Bundle();
        args.putInt(ARG_LAYOUT, layoutId);

        SlideFragment fragment = new SlideFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView");
        return inflater.inflate(getArguments().getInt(ARG_LAYOUT), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
        mRootView = view;
    }

    public void resetActions() {
        mCurrentAction = 0;

        int i = 0;
        SlideAction slideAction;
        while(null != (slideAction = getSlideAction(i++))) {
            slideAction.reset();
        }
    }

    public boolean nextAction() {
        Log.d(TAG, "nextAction");

        SlideAction slideAction = getSlideAction(mCurrentAction++);
        if (null != slideAction) {
            slideAction.performAction();
            return true;
        }

        return false;
    }

    private SlideAction getSlideAction(int i) {
        String tag = "ACTION_" + i;
        Fragment fragment = getChildFragmentManager().findFragmentByTag(tag);
        if (null != fragment) {
            return (SlideAction)fragment;
        }

        View view = mRootView.findViewWithTag(tag);
        if (null != view) {
            return (SlideAction)view;
        }

        return null;
    }

}
