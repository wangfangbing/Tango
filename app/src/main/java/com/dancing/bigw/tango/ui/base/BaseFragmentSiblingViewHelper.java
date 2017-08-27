package com.dancing.bigw.tango.ui.base;

import android.support.annotation.DrawableRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.dancing.bigw.tango.R;

/**
 * Created by bigw on 24/08/2017.
 */

public class BaseFragmentSiblingViewHelper extends SiblingViewHelper {

    public static BaseFragmentSiblingViewHelper create(LayoutInflater inflater, ViewGroup parent) {
        ViewGroup frameLayout = (ViewGroup) inflater.inflate(R.layout.fragment_base, parent, false);
        parent.addView(frameLayout);
        return new BaseFragmentSiblingViewHelper(frameLayout);
    }

    private View mNormalView, mErrorView, mEmptyView;
    private BaseFragment.PageGenerator mErrorPageGenerator, mEmptyPageGenerator;

    private BaseFragmentSiblingViewHelper(ViewGroup parent) {
        super(parent);
    }

    public void setErrorPageGenerator(BaseFragment.PageGenerator errorPageGenerator) {
        this.mErrorPageGenerator = errorPageGenerator;
    }

    public void setEmptyPageGenerator(BaseFragment.PageGenerator emptyPageGenerator) {
        this.mEmptyPageGenerator = emptyPageGenerator;
    }

    public void setNormalView(View normalView) {
        this.mNormalView = normalView;
        if (mNormalView.getParent() == null) {
            addView(mNormalView);
        } else {
            trackView(mNormalView);
        }
    }

    public void bringNormalViewToFront() {
        bringToFront(mNormalView);
    }

    public void bringErrorViewToFront() {
        ensureErrorView();
        bringToFront(mErrorView);
    }

    private void ensureErrorView() {
        if (mErrorView == null && mErrorPageGenerator != null) {
            ViewStub viewStub = (ViewStub) getParentView().findViewById(R.id.vs_error);
            if (viewStub != null) {
                mErrorView = mErrorPageGenerator.generate(viewStub);
                if (mErrorView.getParent() != null) {
                    trackView(mErrorView);
                } else {
                    addView(mErrorView);
                }
            }
        }
    }

    public void bringErrorViewToFront(CharSequence tips, @DrawableRes int drawableRes) {
        ensureErrorView();
        if (mErrorPageGenerator != null) {
            mErrorPageGenerator.setTips(tips);
            mErrorPageGenerator.setImageSrc(drawableRes);
        }
        bringToFront(mErrorView);
    }

    public void bringEmptyViewToFront() {
        ensureEmptyView();
        bringToFront(mEmptyView);
    }

    private void ensureEmptyView() {
        if (mEmptyView == null && mEmptyPageGenerator != null) {
            ViewStub viewStub = (ViewStub) getParentView().findViewById(R.id.vs_empty);
            if (viewStub != null) {
                mEmptyView = mEmptyPageGenerator.generate(viewStub);
                if (mEmptyView.getParent() != null) {
                    trackView(mEmptyView);
                } else {
                    addView(mEmptyView);
                }
            }
        }
    }

    public void bringEmptyViewToFront(CharSequence tips, @DrawableRes int drawableRes) {
        ensureEmptyView();
        if (mEmptyPageGenerator != null) {
            mEmptyPageGenerator.setImageSrc(drawableRes);
            mEmptyPageGenerator.setTips(tips);
        }
        bringToFront(mEmptyView);
    }
}
