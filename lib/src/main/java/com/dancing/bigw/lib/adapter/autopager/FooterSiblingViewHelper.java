package com.dancing.bigw.lib.adapter.autopager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.dancing.bigw.lib.R;
import com.dancing.bigw.lib.utils.SiblingViewHelper;


/**
 * Created by bigw on 27/08/2017.
 */

public class FooterSiblingViewHelper extends SiblingViewHelper {

    public static FooterSiblingViewHelper create(LayoutInflater inflater, ViewGroup parent) {
        ViewGroup itemView = (ViewGroup) inflater.inflate(R.layout.layout_footer_view_item, parent, false);
        return new FooterSiblingViewHelper(itemView);
    }

    private AutoPagerAdapter.FooterViewGenerator mEndViewCreator, mErrorViewCreator, mLoadingViewCreator;
    private View mEndView, mErrorView, mLoadingView;

    private FooterSiblingViewHelper(ViewGroup itemView) {
        super(itemView);
    }

    public View getItemView() {
        return getContainer();
    }

    public void setFooterViewGenerators(FooterViewItem item) {
        mEndViewCreator = item.getEndViewGenerator();
        mErrorViewCreator = item.getErrorViewGenerator();
        mLoadingViewCreator = item.getLoadingViewGenerator();
    }

    public void bringEndViewToFront() {
        ensureEndView();
        bringToFront(mEndView);
    }

    public void bringErrorViewToFront() {
        ensureErrorView();
        bringToFront(mErrorView);
    }

    public void bringLoadingViewToFront() {
        ensureLoadingView();
        bringToFront(mLoadingView);
    }

    private void ensureEndView() {
        if (mEndView == null && mEndViewCreator != null) {
            ViewStub viewStub = (ViewStub) getItemView().findViewById(R.id.vs_end);
            if (viewStub != null) {
                mEndView = mEndViewCreator.generate(viewStub);
                trackView(mEndView);
            }
        }
    }

    private void ensureErrorView() {
        if (mErrorView == null && mErrorViewCreator != null) {
            ViewStub viewStub = (ViewStub) getItemView().findViewById(R.id.vs_error);
            if (viewStub != null) {
                mErrorView = mErrorViewCreator.generate(viewStub);
                trackView(mErrorView);
            }
        }
    }

    private void ensureLoadingView() {
        if (mLoadingView == null && mLoadingViewCreator != null) {
            ViewStub viewStub = (ViewStub) getItemView().findViewById(R.id.vs_loading);
            if (viewStub != null) {
                mLoadingView = mLoadingViewCreator.generate(viewStub);
                trackView(mLoadingView);
            }
        }
    }
}
