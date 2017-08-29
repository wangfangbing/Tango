package com.dancing.bigw.lib.adapter.autopager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;

import com.dancing.bigw.lib.R;
import com.dancing.bigw.lib.adapter.BaseAdapter;
import com.dancing.bigw.lib.utils.SiblingViewHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigw on 24/08/2017.
 */

public class AutoPagerAdapter extends BaseAdapter {

    private String TAG = getClass().getSimpleName();
    private FooterViewItem mFooterViewItem = new FooterViewItem();
    private List<ErrorViewClickListener> errorViewClickListeners = new ArrayList<>();

    public interface FooterViewGenerator {
        View generate(ViewStub viewStub);
    }

    public interface AutoPagerListener {
        void onAutoPager(int nextPage, int count);
    }

    public interface ErrorViewClickListener {
        void onErrorViewClicked();
    }

    public void addErrorViewClickListener(ErrorViewClickListener errorViewClickListener) {
        mFooterViewItem.addErrorViewClickListener(errorViewClickListener);
    }

    public void setFooterViewVisible(boolean visible) {
        mFooterViewItem.setFooterVisible(visible);
    }

    public void setAutoPagerListener(AutoPagerListener autoPagerListener) {
        mFooterViewItem.setAutoPagerListener(autoPagerListener);
    }

    public void setPageSize(int pageSize) {
        mFooterViewItem.setPageSize(pageSize);
    }

    public AutoPagerAdapter() {
        super();
        register(FooterViewItem.class, FooterViewHolder.class, null);
        registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                Log.d(TAG, "onChanged() " + mFooterViewItem);
                handleDataSetChange(mFooterViewItem, -1);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                Log.d(TAG, "onItemRangeInserted(positionStart, itemCount) " + positionStart + " , " + itemCount + " " + mFooterViewItem);
                handleDataSetChange(mFooterViewItem, itemCount);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                Log.d(TAG, "onItemRangeRemoved(positionStart, itemCount) " + positionStart + " , " + itemCount + " " + mFooterViewItem);
                handleDataSetChange(mFooterViewItem, itemCount);
            }
        });

        mFooterViewItem.setEndViewGenerator(getEndViewGenerator());
        mFooterViewItem.setErrorViewGenerator(getErrorViewGenerator());
        mFooterViewItem.setLoadingViewGenerator(getLoadingViewGenerator());
        mFooterViewItem.addErrorViewClickListener(new ErrorViewClickListener() {
            @Override
            public void onErrorViewClicked() {
                setFooterViewToState(FooterSiblingViewHelper.PENDING_STATE_LOADING);
            }
        });
    }

    private void handleDataSetChange(FooterViewItem footerViewItem, int effectedItemCount) {
        footerViewItem.updateState(effectedItemCount);
        footerViewItem.setIsLoading(false);

        int footerViewIndex = getIndexOfFooterViewWithinAdapter();
        if (footerViewIndex >= 0) {
            if (footerViewItem.shouldRemoveFooterView(getItemCount() == 1)) {
                super.removeItem(getItemCount() - 1);

            } else {
                notifyItemRangeChanged(footerViewIndex, 1);
            }
        }
    }

    /**
     * 当发生错误的时候，主动show
     */
    public void showErrorFooterView() {
        mFooterViewItem.setIsLoading(false);
        setFooterViewToState(FooterSiblingViewHelper.PENDING_STATE_ERROR);
    }

    private void setFooterViewToState(int siblingViewPendingState) {
        int footerViewIndex = getIndexOfFooterViewWithinAdapter();
        if (footerViewIndex >= 0) {
            mFooterViewItem.setSiblingViewPendingState(siblingViewPendingState);
            notifyItemRangeChanged(footerViewIndex, 1);
        }
    }

    /**
     * ok
     */
    @Override
    public void setItem(Object item) {
        checkItemValidity(item);

        mFooterViewItem.setOption(FooterViewItem.OPTION_SET_ITEM);

        List items = new ArrayList();
        items.add(item);
        items.add(mFooterViewItem); //TODO check the footerView enable
        super.setItems(items);
    }

    /**
     * ok
     */
    @Override
    public void setItems(List newItems) {
        checkListValidity(newItems);

        mFooterViewItem.setOption(FooterViewItem.OPTION_SET_ITEMS);

        newItems.add(mFooterViewItem);
        super.setItems(newItems);
    }

    /**
     * ok
     */
    @Override
    public void appendItem(Object item) {
        checkItemValidity(item);

        mFooterViewItem.setOption(FooterViewItem.OPTION_APPEND_ITEM);

        int footerViewIndex = getIndexOfFooterViewWithinAdapter();
        if (footerViewIndex < 0) { //ensure the footer view
            List items = new ArrayList();
            items.add(item);
            items.add(mFooterViewItem);
            super.appendItems(items);
        } else {
            super.insertItem(footerViewIndex, item);
        }
    }

    /**
     * ok
     */
    @Override
    public void appendItems(List newItems) {
        checkListValidity(newItems);

        mFooterViewItem.setOption(FooterViewItem.OPTION_APPEND_ITEMS);

        int footerViewIndex = getIndexOfFooterViewWithinAdapter();
        if (footerViewIndex < 0) {
            newItems.add(mFooterViewItem);
            super.appendItems(newItems);
        } else {
            super.insertItems(footerViewIndex, newItems);
        }
    }

    /**
     * ok
     */
    @Override
    public void insertItem(int startPosition, Object item) {
        checkItemValidity(item);

        mFooterViewItem.setOption(FooterViewItem.OPTION_INSERT_ITEM);

        int footerViewIndex = getIndexOfFooterViewWithinAdapter();
        if (footerViewIndex >= 0 && startPosition > footerViewIndex) {
            startPosition = footerViewIndex;
        }
        super.insertItem(startPosition, item);
    }

    /**
     * ok
     */
    @Override
    public void insertItems(int startPosition, List newItems) {
        checkListValidity(newItems);

        mFooterViewItem.setOption(FooterViewItem.OPTION_INSERT_ITEMS);

        int footerViewIndex = getIndexOfFooterViewWithinAdapter();
        if (footerViewIndex >= 0 && startPosition > footerViewIndex) {
            startPosition = footerViewIndex;
        }
        super.insertItems(startPosition, newItems);
    }

    /**
     * 删除最后一个元素的快捷方法
     * ok
     */
    public final void removeLastItem() { //TODO when there is on footerView
        int startPosition = getItemCount() - 1;
        int footerViewIndex = getIndexOfFooterViewWithinAdapter();
        if (startPosition >= footerViewIndex) {
            startPosition = footerViewIndex - 1;
        }
        if (startPosition >= 0) {
            super.removeItem(startPosition);
        }
    }

    @Override
    public void removeItems(int startPosition, int itemCount) {
        mFooterViewItem.setOption(FooterViewItem.OPTION_REMOVE_ITEMS);
        super.removeItems(startPosition, itemCount);
    }

    private int getIndexOfFooterViewWithinAdapter() {
        List items = getItemList();
        for (int i = getItemCount() - 1; i >= 0; i--) {
            if (items.get(i) == mFooterViewItem) {
                return i;
            }
        }
        return -1;
    }

    protected FooterViewGenerator getEndViewGenerator() {
        return new FooterViewGenerator() {
            @Override
            public View generate(ViewStub viewStub) {
                viewStub.setLayoutResource(R.layout.item_footer_view_end);
                return viewStub.inflate();
            }
        };
    }

    protected FooterViewGenerator getErrorViewGenerator() {
        return new FooterViewGenerator() {
            @Override
            public View generate(ViewStub viewStub) {
                viewStub.setLayoutResource(R.layout.item_footer_view_error);
                return viewStub.inflate();
            }
        };
    }

    protected FooterViewGenerator getLoadingViewGenerator() {
        return new FooterViewGenerator() {
            @Override
            public View generate(ViewStub viewStub) {
                viewStub.setLayoutResource(R.layout.item_footer_view_loading);
                return viewStub.inflate();
            }
        };
    }
}
