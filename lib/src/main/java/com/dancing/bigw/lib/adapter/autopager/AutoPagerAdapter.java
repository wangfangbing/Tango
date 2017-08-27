package com.dancing.bigw.lib.adapter.autopager;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewStub;

import com.dancing.bigw.lib.R;
import com.dancing.bigw.lib.adapter.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigw on 24/08/2017.
 */

public class AutoPagerAdapter extends BaseAdapter {

    private String TAG = getClass().getSimpleName();

    private boolean mEnable;

    private FooterViewItem mFooterViewItem = new FooterViewItem();

    public void setAutoPagerListener(AutoPagerListener autoPagerListener) {
        mFooterViewItem.setAutoPagerListener(autoPagerListener);
    }

    public void setPageSize(int pageSize) {
        mFooterViewItem.setPageSize(pageSize);
    }

    //TODO set the generators

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
        mFooterViewItem.setPendingState(FooterViewItem.PENDING_STATE_ERROR);
        int footerViewIndex = getIndexOfFooterViewWithinAdapter();
        if (footerViewIndex >= 0) {
            notifyItemRangeChanged(footerViewIndex, 1);
        }
    }

    public static class FooterViewItem {
        private static final int OPTION_SET_ITEM = 1;
        private static final int OPTION_SET_ITEMS = 2;
        private static final int OPTION_APPEND_ITEM = 4;
        private static final int OPTION_APPEND_ITEMS = 8;
        private static final int OPTION_INSERT_ITEM = 16;
        private static final int OPTION_INSERT_ITEMS = 32;
        private static final int OPTION_REMOVE_ITEMS = 64;

        private static final int PENDING_STATE_END = 1;
        private static final int PENDING_STATE_ERROR = 4;
        private static final int PENDING_STATE_REMOVE_FOOTER = 8;

        private FooterViewGenerator mEndViewGenerator, mErrorViewGenerator, mLoadingViewGenerator;

        private int mNextPage = 1;
        private int mPageSize;

        public int getNextPage() {
            return mNextPage;
        }

        public int getPageSize() {
            return mPageSize;
        }

        public void setPageSize(int pageSize) {
            this.mPageSize = pageSize;
        }

        public AutoPagerListener getAutoPagerListener() {
            return mAutoPagerListener;
        }

        public void setAutoPagerListener(AutoPagerListener autoPagerListener) {
            this.mAutoPagerListener = autoPagerListener;
        }

        private AutoPagerListener mAutoPagerListener;

        public boolean isLoading() {
            return mIsLoading;
        }

        public void setIsLoading(boolean isLoading) {
            this.mIsLoading = isLoading;
        }

        private boolean mIsLoading;

        public FooterViewGenerator getEndViewGenerator() {
            return mEndViewGenerator;
        }

        public void setEndViewGenerator(FooterViewGenerator endViewGenerator) {
            this.mEndViewGenerator = endViewGenerator;
        }

        public FooterViewGenerator getErrorViewGenerator() {
            return mErrorViewGenerator;
        }

        public void setErrorViewGenerator(FooterViewGenerator errorViewGenerator) {
            this.mErrorViewGenerator = errorViewGenerator;
        }

        public FooterViewGenerator getLoadingViewGenerator() {
            return mLoadingViewGenerator;
        }

        public void setLoadingViewGenerator(FooterViewGenerator loadingViewGenerator) {
            this.mLoadingViewGenerator = loadingViewGenerator;
        }



        private int mOption;
        private int mPendingState = com.dancing.bigw.lib.adapter.autopager.FooterViewItem.PENDING_STATE_LOAD_MORE;

        public void setOption(int option) {
            this.mOption = option;
        }

        public void setPendingState(int pendingState) {
            this.mPendingState = pendingState;
        }

        public void updateState(int effectedItemCount) {
            if (mOption == OPTION_SET_ITEM || mOption == OPTION_SET_ITEMS) {
                mPendingState = com.dancing.bigw.lib.adapter.autopager.FooterViewItem.PENDING_STATE_LOAD_MORE;
                mNextPage = 1;

            } else if (mOption == OPTION_APPEND_ITEM || mOption == OPTION_INSERT_ITEM || mOption == OPTION_INSERT_ITEMS) {
                //do nothing

            } else if (mOption == OPTION_APPEND_ITEMS) {
                mNextPage += 1;

                if (effectedItemCount > 0) {
                    mPendingState = com.dancing.bigw.lib.adapter.autopager.FooterViewItem.PENDING_STATE_LOAD_MORE;
                } else {
                    mPendingState = PENDING_STATE_END;
                }

            } else if (mOption == OPTION_REMOVE_ITEMS) {
                mPendingState = PENDING_STATE_REMOVE_FOOTER;
            }
        }

        public boolean shouldRemoveFooterView(boolean onlyFooterViewWithinAdapter) {
            return onlyFooterViewWithinAdapter && mPendingState == PENDING_STATE_REMOVE_FOOTER;
        }

        @Override
        public String toString() {
            return "option " + mOption;
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


    public interface FooterViewGenerator {
        View generate(ViewStub viewStub);
    }

    public interface AutoPagerListener {
        void onAutoPager(int nextPage, int count);
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
