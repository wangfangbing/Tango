package com.dancing.bigw.tango.ui.account;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dancing.bigw.tango.R;
import com.dancing.bigw.tango.ui.base.BaseFragment;

import java.util.Calendar;

/**
 * Created by bigw on 21/08/2017.
 */

public class AccountFragment extends BaseFragment {

    public static AccountFragment newInstance(String title) {
        AccountFragment f = new AccountFragment();
        return f;
    }

    private int mErrorCount = 0;
    private int mEmptyCount = 0;

    private Handler mHandler = new Handler();

    private TextView mTextView;

    @Override
    protected View inflateNormalPageView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mTextView = (TextView) view.findViewById(R.id.text);
    }

    public void emulateFailedRequest() {
        showSwipeRefreshLayout();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideSwipeRefreshLayout();
                if (mErrorCount++ % 3 == 0) {
                    showErrorPageView("error1 " + mErrorCount, R.drawable.ic_bangumi_index_drawer_exit);
                } else {
                    showErrorPageView("error2 ", R.drawable.img_tips_error_banner_tv);
                }
            }
        }, 800);
    }

    public void emulateEmptyRequst() {
        showSwipeRefreshLayout();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideSwipeRefreshLayout();
                if (mEmptyCount++ % 3 == 0) {
                    showEmptyPageView("empty1 " + mEmptyCount, R.drawable.img_tips_error_banner_tv);
                } else {
                    showEmptyPageView("ooops " + mEmptyCount, R.drawable.ic_bangumi_index_drawer_exit);
                }
            }
        }, 800);
    }

    public void emulateNormalRequest() {
        refreshCurrentPage();
    }

    @Override
    protected void onFragmentVisibilityChanged(boolean visible) {
        super.onFragmentVisibilityChanged(visible);
        if (visible) {
            refreshCurrentPage();
        }
    }

    private void refreshCurrentPage() {
        showSwipeRefreshLayout();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                hideSwipeRefreshLayout();
                showNormalPageView();
                Calendar calendar = Calendar.getInstance();
                mTextView.setText("refresh at " + calendar.get(Calendar.HOUR_OF_DAY) + " : " + calendar.get(Calendar.MINUTE) +" : " + calendar.get(Calendar.SECOND));
            }
        }, 800);
    }
}
