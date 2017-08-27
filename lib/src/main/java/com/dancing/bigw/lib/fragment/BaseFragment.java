package com.dancing.bigw.lib.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.TextView;

import com.dancing.bigw.lib.R;

/**
 * Created by bigw on 21/08/2017.
 */

public abstract class BaseFragment extends Fragment {

    public static final boolean DEBUG = true;

    private static final int SWIPE_REFRESH_TIME_INTERVAL = 500;

    private boolean mLastUserVisibleHint;
    private boolean mFragmentIsHidden;
    private boolean mShouldNotifyUserVisibleHintWhenActivityCreated;

    private BaseFragmentSiblingViewHelper mSiblingViewHelper;

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private long mLastRefreshingStartTime;

    protected SwipeRefreshLayout generateSwipeRefreshLayout(Context context) {
        SwipeRefreshLayout swipeRefreshLayout = new SwipeRefreshLayout(context);
        swipeRefreshLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        return swipeRefreshLayout;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        View rootView = getView();
        return (SwipeRefreshLayout) rootView;
    }

    public void showSwipeRefreshLayout() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.post(mShowSwipeRefreshLayoutTask);
        }
    }

    public void hideSwipeRefreshLayout() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.removeCallbacks(mShowSwipeRefreshLayoutTask);

            long delta = System.currentTimeMillis() - mLastRefreshingStartTime;
            if (0 <= delta && delta < SWIPE_REFRESH_TIME_INTERVAL) {
                mSwipeRefreshLayout.postDelayed(mHideSwipeRefreshLayoutTask, SWIPE_REFRESH_TIME_INTERVAL - delta);
            } else {
                mSwipeRefreshLayout.post(mHideSwipeRefreshLayoutTask);
            }
        }
    }

    public void showNormalPageView() {
        if (mSiblingViewHelper != null) {
            mSiblingViewHelper.bringNormalViewToFront();
        }
    }

    public void showEmptyPageView() {
        if (mSiblingViewHelper != null) {
            mSiblingViewHelper.bringEmptyViewToFront();
        }
    }

    public void showEmptyPageView(CharSequence tips,@DrawableRes int drawableRes) {
        if (mSiblingViewHelper != null) {
            mSiblingViewHelper.bringEmptyViewToFront(tips, drawableRes);
        }
    }

    public void showErrorPageView() {
        if (mSiblingViewHelper != null) {
            mSiblingViewHelper.bringErrorViewToFront();
        }
    }

    public void showErrorPageView(CharSequence tips, @DrawableRes int drawableRes) {
        if (mSiblingViewHelper != null) {
            mSiblingViewHelper.bringErrorViewToFront(tips, drawableRes);
        }
    }

    protected abstract View inflateNormalPageView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    /**
     * ------------------------
     *-- SwipeRefreshLayout
     *   -- FrameLayout
     *      -- ViewStub (error view)
     *      -- ViewStub (empty view)
     *      -- normalView
     * ------------------------
     */
    @Nullable
    @Override
    public final View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mSwipeRefreshLayout = generateSwipeRefreshLayout(getContext());
        //TODO set theme background color for mSwipeRefreshLayout

        mSiblingViewHelper = BaseFragmentSiblingViewHelper.create(inflater, mSwipeRefreshLayout);
        View normalPageView = inflateNormalPageView(inflater, mSiblingViewHelper.getParentView(), savedInstanceState);

        mSiblingViewHelper.setNormalView(normalPageView);
        mSiblingViewHelper.setEmptyPageGenerator(getEmptyPageGenerator());
        mSiblingViewHelper.setErrorPageGenerator(getErrorPageGenerator());

        return mSwipeRefreshLayout;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mFragmentIsHidden = isHidden();
        if (mShouldNotifyUserVisibleHintWhenActivityCreated) {
            mShouldNotifyUserVisibleHintWhenActivityCreated = false;
            onFragmentVisibilityChanged(mLastUserVisibleHint);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        mLastUserVisibleHint = isVisibleToUser;
        if (isAdded()) {
            onFragmentVisibilityChanged(isVisibleToUser);
        } else {
            mShouldNotifyUserVisibleHintWhenActivityCreated = true;
        }
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);

        if (mFragmentIsHidden != hidden) {
            mFragmentIsHidden = hidden;
            onFragmentVisibilityChanged(!mFragmentIsHidden);
        }
    }

    protected void onFragmentVisibilityChanged(boolean visible) {
    }

    protected PageGenerator getErrorPageGenerator() {
        return new PageGenerator() {
            private View mErrorPageView;
            @Override
            public View generate(ViewStub viewStub) {
                viewStub.setLayoutResource(R.layout.layout_error_page);
                mErrorPageView = viewStub.inflate();
                return mErrorPageView;
            }

            @Override
            public void setTips(CharSequence tips) {
                if (mErrorPageView != null) {
                    TextView textView = (TextView) mErrorPageView.findViewById(R.id.text1);
                    textView.setText(tips);
                }
            }

            @Override
            public void setImageSrc(@DrawableRes int drawableRes) {
                if (mErrorPageView != null) {
                    ImageView imageView = (ImageView) mErrorPageView.findViewById(R.id.image);
                    imageView.setImageResource(drawableRes);
                }
            }
        };
    }

    protected PageGenerator getEmptyPageGenerator() {
        return new PageGenerator() {
            private View mEmptyPageView;

            @Override
            public View generate(ViewStub viewStub) {
                viewStub.setLayoutResource(R.layout.layout_empty_page);
                mEmptyPageView = viewStub.inflate();
                return mEmptyPageView;
            }

            @Override
            public void setTips(CharSequence tips) {
                if (mEmptyPageView != null) {
                    TextView text = (TextView) mEmptyPageView.findViewById(R.id.text1);
                    text.setText(tips);
                }
            }

            @Override
            public void setImageSrc(@DrawableRes int drawableRes) {
                if (mEmptyPageView != null) {
                    ImageView imageView = (ImageView) mEmptyPageView.findViewById(R.id.image);
                    imageView.setImageResource(drawableRes);
                }
            }
        };
    }

    public interface PageGenerator {
        View generate(ViewStub viewStub);
        void setTips(CharSequence tips);
        void setImageSrc(@DrawableRes int drawableRes);
    }

    private Runnable mShowSwipeRefreshLayoutTask = new Runnable() {
        @Override
        public void run() {
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(true);
            }
            mLastRefreshingStartTime = System.currentTimeMillis();
        }
    };

    private Runnable mHideSwipeRefreshLayoutTask = new Runnable() {
        @Override
        public void run() {
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }
    };
}
