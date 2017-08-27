package com.dancing.bigw.lib.adapter.autopager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dancing.bigw.lib.adapter.BaseViewHolder;
import com.dancing.bigw.lib.adapter.ViewHolderActionListener;

/**
 * Created by bigw on 27/08/2017.
 */
class FooterViewHolder extends BaseViewHolder<FooterViewItem, ViewHolderActionListener> {
    private FooterSiblingViewHelper mSiblingViewHelper;

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
        mSiblingViewHelper.setFooterViewGenerators(item);

        switch (item.getPendingState()) {
            case FooterViewItem.PENDING_STATE_END:
                mSiblingViewHelper.bringEndViewToFront();
                break;
            case FooterViewItem.PENDING_STATE_ERROR:
                mSiblingViewHelper.bringErrorViewToFront();
                break;
            case FooterViewItem.PENDING_STATE_LOADING:
                mSiblingViewHelper.bringLoadingViewToFront();
                if (!item.isLoading() && item.getAutoPagerListener() != null) {
                    item.setIsLoading(true);
                    item.getAutoPagerListener().onAutoPager(item.getNextPage(), item.getPageSize());
                }
                break;
        }
    }

    public static FooterViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        FooterSiblingViewHelper viewHelper = FooterSiblingViewHelper.create(inflater, parent);
        View itemView = viewHelper.getItemView();
        return new FooterViewHolder(itemView, viewHelper);
    }
}
