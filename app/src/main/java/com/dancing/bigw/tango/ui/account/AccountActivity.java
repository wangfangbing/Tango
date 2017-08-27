package com.dancing.bigw.tango.ui.account;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.dancing.bigw.tango.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigw on 21/08/2017.
 */

public class AccountActivity extends AppCompatActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, AccountActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        final PagesAdpater mAdapter = new PagesAdpater(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);

        findViewById(R.id.text).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AccountFragment firstFragment = mAdapter.getFirstFragment();
                firstFragment.emulateNormalRequest();
            }
        });

        findViewById(R.id.text1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AccountFragment firstFragment = mAdapter.getFirstFragment();
                firstFragment.emulateEmptyRequst();
            }
        });

        findViewById(R.id.text2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AccountFragment firstFragment = mAdapter.getFirstFragment();
                firstFragment.emulateFailedRequest();
            }
        });
    }

    private static class PagesAdpater extends FragmentPagerAdapter {
        private List<String> mTitle = new ArrayList<>();
        private AccountFragment mFirstFragment;

        public PagesAdpater(FragmentManager fm) {
            super(fm);
            mTitle.add("page0");
            mTitle.add("page1");
            mTitle.add("page2");
            mTitle.add("page3");
        }

        @Override
        public Fragment getItem(int position) {
            Log.d("MainActivity", "ViewPagerAdapter.getItem position = " + position);
            AccountFragment f =  AccountFragment.newInstance(mTitle.get(position));
            if (position == 0) {
                mFirstFragment = f;
            }
            return f;
        }

        @Override
        public int getCount() {
            return mTitle.size();
        }

        public AccountFragment getFirstFragment() {
            return mFirstFragment;
        }
    }
}
