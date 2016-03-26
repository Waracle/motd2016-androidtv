package com.waracle.motd2016;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.animation.AccelerateInterpolator;
import android.widget.ImageView;


public class SlidesActivity extends FragmentActivity implements ViewPager.OnPageChangeListener {

    private static final String TAG = SlidesActivity.class.getSimpleName();

    static final int[] SLIDES = new int[] {
            R.layout.slide_intro,
            R.layout.slide_code_kitchen1,
            R.layout.slide_keynote,
            R.layout.slide_skyscanner,
            R.layout.slide_honor,
            R.layout.slide_xda_nfc,
            R.layout.slide_firetv,
            R.layout.slide_honor_community,
            R.layout.slide_last,
    };

    private ImageView mLogo;
    private ViewPager mPager;
    private int mCurrentPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.slides_activity);

        mLogo = (ImageView)findViewById(R.id.logo);

        mPager = (ViewPager)findViewById(R.id.slides_view_pager);
        mPager.setAdapter(new SlidesPagerAdapter(getSupportFragmentManager()));
        mPager.addOnPageChangeListener(this);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        mLogo.setRotation(360 * positionOffset);
    }

    @Override
    public void onPageSelected(int position) {
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d(TAG, "onKeyUp: " + event);

        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                nextAction();
                return true;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                previousSlide(true);
                return true;

            case KeyEvent.KEYCODE_MEDIA_REWIND:
                previousSlide(false);
                break;

            case KeyEvent.KEYCODE_MEDIA_FAST_FORWARD:
                nextSlide(false);
                break;
        }

        return super.onKeyUp(keyCode, event);
    }

    private void nextAction() {

        SlideFragment fragment = getCurrentFragment();
        if (!fragment.nextAction()) {
             nextSlide(true);
        }

    }

    private SlideFragment getCurrentFragment() {
        // http://stackoverflow.com/a/18611036/73479
        return (SlideFragment)getSupportFragmentManager().findFragmentByTag("android:switcher:" + R.id.slides_view_pager + ":" + mPager.getCurrentItem());
    }

    private void previousSlide(boolean smooth) {
        Log.d(TAG, "nextSlide: " + mCurrentPage);

        if (mCurrentPage > 0) {
            mCurrentPage--;
            if (smooth) {
                animatePagerTransition(mPager, false);
            } else {
                mPager.setCurrentItem(mCurrentPage, false);
            }
        }

    }

    private void nextSlide(boolean smooth) {
        Log.d(TAG, "nextSlide: " + mCurrentPage);

        if (mCurrentPage + 1 < SLIDES.length) {
            resetCurrentFragmentLater();
            mCurrentPage++;

            if (smooth) {
                animatePagerTransition(mPager, true);
            } else {
                mPager.setCurrentItem(mCurrentPage, false);
            }
        }
    }

    private void resetCurrentFragmentLater() {
        final SlideFragment currentFragment = getCurrentFragment();
        mPager.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentFragment.resetActions();
            }
        }, 1000);
    }

    // http://stackoverflow.com/a/30976741/73479
    private void animatePagerTransition(final ViewPager viewPager, final boolean forward) {
        ValueAnimator animator = ValueAnimator.ofInt(0, viewPager.getWidth() - ( forward ? viewPager.getPaddingLeft() : viewPager.getPaddingRight() ));
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                endFrakeDrag();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                endFrakeDrag();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            private void endFrakeDrag() {
                if (viewPager.isFakeDragging()) {
                    viewPager.endFakeDrag();
                }
            }
        });

        animator.setInterpolator(new AccelerateInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            private int oldDragPosition = 0;

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int dragPosition = (Integer) animation.getAnimatedValue();
                int dragOffset = dragPosition - oldDragPosition;
                oldDragPosition = dragPosition;
                if (viewPager.isFakeDragging()) {
                    viewPager.fakeDragBy(dragOffset * (forward ? -1 : 1));
                }
            }
        });

        animator.setDuration(500);
        viewPager.beginFakeDrag();
        animator.start();
    }

    static class SlidesPagerAdapter extends FragmentPagerAdapter {

        public SlidesPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return SLIDES.length;
        }


        @Override
        public Fragment getItem(int position) {
            return SlideFragment.newInstance(SLIDES[position]);
        }

    }

}
