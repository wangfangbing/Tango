package com.dancing.bigw.lib.adapter.autopager;

/**
 * Created by bigw on 27/08/2017.
 */
class FooterViewItem {
    public static final int OPTION_SET_ITEM = 1;
    public static final int OPTION_SET_ITEMS = 2;
    public static final int OPTION_APPEND_ITEM = 4;
    public static final int OPTION_APPEND_ITEMS = 8;
    public static final int OPTION_INSERT_ITEM = 16;
    public static final int OPTION_INSERT_ITEMS = 32;
    public static final int OPTION_REMOVE_ITEMS = 64;

    public static final int PENDING_STATE_END = 1;
    public static final int PENDING_STATE_ERROR = 4;
    public static final int PENDING_STATE_REMOVE_FOOTER = 8;
    public static final int PENDING_STATE_LOADING = 16;

    private AutoPagerAdapter.FooterViewGenerator mEndViewGenerator, mErrorViewGenerator, mLoadingViewGenerator;

    private AutoPagerAdapter.AutoPagerListener mAutoPagerListener;

    private int mNextPage = 1;
    private int mPageSize;
    private boolean mIsLoading;

    private int mOption;
    private int mPendingState = PENDING_STATE_LOADING;

    public int getNextPage() {
        return mNextPage;
    }

    public int getPageSize() {
        return mPageSize;
    }

    public void setPageSize(int pageSize) {
        this.mPageSize = pageSize;
    }

    public AutoPagerAdapter.AutoPagerListener getAutoPagerListener() {
        return mAutoPagerListener;
    }

    public void setAutoPagerListener(AutoPagerAdapter.AutoPagerListener autoPagerListener) {
        this.mAutoPagerListener = autoPagerListener;
    }

    public boolean isLoading() {
        return mIsLoading;
    }

    public void setIsLoading(boolean isLoading) {
        this.mIsLoading = isLoading;
    }

    public AutoPagerAdapter.FooterViewGenerator getEndViewGenerator() {
        return mEndViewGenerator;
    }

    public void setEndViewGenerator(AutoPagerAdapter.FooterViewGenerator endViewGenerator) {
        this.mEndViewGenerator = endViewGenerator;
    }

    public AutoPagerAdapter.FooterViewGenerator getErrorViewGenerator() {
        return mErrorViewGenerator;
    }

    public void setErrorViewGenerator(AutoPagerAdapter.FooterViewGenerator errorViewGenerator) {
        this.mErrorViewGenerator = errorViewGenerator;
    }

    public AutoPagerAdapter.FooterViewGenerator getLoadingViewGenerator() {
        return mLoadingViewGenerator;
    }

    public void setLoadingViewGenerator(AutoPagerAdapter.FooterViewGenerator loadingViewGenerator) {
        this.mLoadingViewGenerator = loadingViewGenerator;
    }

    public void setOption(int option) {
        this.mOption = option;
    }

    public void setPendingState(int pendingState) {
        this.mPendingState = pendingState;
    }

    public void updateState(int effectedItemCount) {
        if (mOption == OPTION_SET_ITEM || mOption == OPTION_SET_ITEMS) {
            mPendingState = PENDING_STATE_LOADING;
            mNextPage = 1;

        } else if (mOption == OPTION_APPEND_ITEM || mOption == OPTION_INSERT_ITEM || mOption == OPTION_INSERT_ITEMS) {
            //do nothing

        } else if (mOption == OPTION_APPEND_ITEMS) {
            mNextPage += 1;

            if (effectedItemCount > 0) {
                mPendingState = PENDING_STATE_LOADING;
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

    public int getPendingState() {
        return mPendingState;
    }

    @Override
    public String toString() {
        return "option " + mOption;
    }
}
