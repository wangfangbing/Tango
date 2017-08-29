package com.dancing.bigw.lib.adapter.autopager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dancing.bigw.lib.adapter.BaseViewHolder;
import com.dancing.bigw.lib.adapter.ViewHolderActionListener;

import java.util.List;

/**
 * Created by bigw on 27/08/2017.
 */
class FooterViewHolder extends BaseViewHolder<FooterViewItem, ViewHolderActionListener> {
    private FooterSiblingViewHelper mSiblingViewHelper;

    private InternalErrorViewClickListener mInternalErrorViewClickListener;

    public static FooterViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        FooterSiblingViewHelper viewHelper = FooterSiblingViewHelper.create(inflater, parent);
        View itemView = viewHelper.getItemView();
        return new FooterViewHolder(itemView, viewHelper);
    }

    public FooterViewHolder(View itemView, FooterSiblingViewHelper siblingViewHelper) {
        super(itemView);
        this.mSiblingViewHelper = siblingViewHelper;
    }

    @Override
    public void bind(FooterViewItem item, int position) {
        super.bind(item, position);
        if (mSiblingViewHelper == null) {
            return;
        }
Log.d("AutoPager", "bind " + item.getSiblingViewPendingState());
        if (item.isFooterVisible()) {
            mSiblingViewHelper.setFooterViewGenerators(item);
            mSiblingViewHelper.bringViewToFront(item.getSiblingViewPendingState());
            if (mSiblingViewHelper.getErroView() != null) {
                mSiblingViewHelper.getErroView().setOnClickListener(getInternalErrorViewClickListener(item.getErrorViewClickListeners()));
            }
        } else {
            //hide the footerView
            ViewGroup footerViewContainer = mSiblingViewHelper.getContainer();
            ViewGroup.LayoutParams lp = footerViewContainer.getLayoutParams();
            if (lp.height != 0) {
                lp.height = 0;
                footerViewContainer.setLayoutParams(lp);
            }
        }

        //自动加载更多
        if (item.getSiblingViewPendingState() == FooterSiblingViewHelper.PENDING_STATE_LOADING) {
            if (!item.isLoading() && item.getAutoPagerListener() != null) {
                item.setIsLoading(true);
                item.getAutoPagerListener().onAutoPager(item.getNextPage(), item.getPageSize());
            }
        }
    }

    private InternalErrorViewClickListener getInternalErrorViewClickListener(List<AutoPagerAdapter.ErrorViewClickListener> errorViewClickListeners) {
        if (mInternalErrorViewClickListener == null) {
            mInternalErrorViewClickListener = new InternalErrorViewClickListener(errorViewClickListeners);
        }
        return mInternalErrorViewClickListener;
    }

    private static class InternalErrorViewClickListener implements View.OnClickListener {
        private List<AutoPagerAdapter.ErrorViewClickListener> mErrorViewClickListeners = null;

        public InternalErrorViewClickListener(List<AutoPagerAdapter.ErrorViewClickListener> errorViewClickListeners) {
            this.mErrorViewClickListeners = errorViewClickListeners;
        }

        @Override
        public void onClick(View v) {
            if (mErrorViewClickListeners != null) {
                for (AutoPagerAdapter.ErrorViewClickListener listener : mErrorViewClickListeners) {
                    listener.onErrorViewClicked();
                }
            }
        }
    }
}
