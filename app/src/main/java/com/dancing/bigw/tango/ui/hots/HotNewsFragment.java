package com.dancing.bigw.tango.ui.hots;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.dancing.bigw.tango.R;
import com.dancing.bigw.tango.entities.Title;
import com.dancing.bigw.tango.ui.base.BaseFragment;
import com.dancing.bigw.tango.ui.base.adapter.autopager.AutoPagerAdapter;
import com.dancing.bigw.tango.ui.news.TitleViewHolder;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by bigw on 25/08/2017.
 */

public class HotNewsFragment extends BaseFragment {

    public static HotNewsFragment newInstance() {
        HotNewsFragment f = new HotNewsFragment();
        return f;
    }

    private RecyclerView mRecyclerView;
    private AutoPagerAdapter mAdapter;

    private Handler mHandler = new Handler();

    @Override
    protected View inflateNormalPageView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_hot_news, parent, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mAdapter = new AutoPagerAdapter();
        mAdapter.register(Title.class, TitleViewHolder.class, mTitleActionListener);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setAutoPagerListener(new AutoPagerAdapter.AutoPagerListener() {
            @Override
            public void onAutoPager(int nextPage, int count) {
                Log.d("AutoPager", "onAutoPager page " + nextPage + " size " + count);
                Random random = new Random();
                if (random.nextBoolean()) {
                    emulateAppendItems();
                } else {
                    emulateAppendNone();
                }
            }
        });
        mAdapter.setPageSize(20);
    }

    private TitleViewHolder.ActionListener mTitleActionListener = new TitleViewHolder.ActionListener() {
        @Override
        public void onTitleClicked(Title item, View view, int position) {
            Toast.makeText(getContext(), "name#" + item.name, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onIdClicked(Title item, View view, int position) {
            Toast.makeText(getContext(), "id#" + item.id, Toast.LENGTH_SHORT).show();
        }
    };

    public void emulateSetItem() {
        showSwipeRefreshLayout();
        post(new Runnable() {
            @Override
            public void run() {
                hideSwipeRefreshLayout();
                Title title = generateTitleList(1, 1).get(0);
                mAdapter.setItem(title);
            }
        });
    }

    public void emulateSetItems() {
        showSwipeRefreshLayout();
        post(new Runnable() {
            @Override
            public void run() {
                hideSwipeRefreshLayout();
                List<Title> titles = generateTitleList(1, 30);
                mAdapter.setItems(titles);
            }
        });
    }

    public void emulateAppendItem() {
        showSwipeRefreshLayout();
        post(new Runnable() {
            @Override
            public void run() {
                hideSwipeRefreshLayout();

                Title title = generateTitleList(1, 1).get(0);
                mAdapter.appendItem(title);
            }
        });
    }

    public void emulateAppendItems() {
        showSwipeRefreshLayout();
        post(new Runnable() {
            @Override
            public void run() {
                hideSwipeRefreshLayout();
                List<Title> titles = generateTitleList(1, 30);
                mAdapter.appendItems(titles);
            }
        });
    }

    public void emulateInsertItem() {
        showSwipeRefreshLayout();
        post(new Runnable() {
            @Override
            public void run() {
                hideSwipeRefreshLayout();

                Title title = generateTitleList(1, 1).get(0);
                mAdapter.insertItem(mAdapter.getItemCount(), title);
            }
        });
    }

    public void emulateInsertItems() {
        showSwipeRefreshLayout();
        post(new Runnable() {
            @Override
            public void run() {
                hideSwipeRefreshLayout();

                List<Title> titles = generateTitleList(1, 5);
                mAdapter.insertItems(mAdapter.getItemCount() - 2, titles);
            }
        });
    }

    public void emulateMoveItem() {

    }

    public void emulateRemoveItems() {
        post(new Runnable() {
            @Override
            public void run() {
                mAdapter.removeItems(1, 2);
            }
        });
    }

    public void emulateRemoveItem() {
        post(new Runnable() {
            @Override
            public void run() {
                //mAdapter.removeItem(2);
                mAdapter.removeLastItem();
            }
        });
    }

    public void emulateAppendNone() {
        showSwipeRefreshLayout();
        post(new Runnable() {
            @Override
            public void run() {
                hideSwipeRefreshLayout();
                List<Title> titles = new ArrayList<Title>();
                mAdapter.appendItems(titles);
            }
        });
    }

    public void emulateAppendFailed() {
        showSwipeRefreshLayout();
        post(new Runnable() {
            @Override
            public void run() {
                hideSwipeRefreshLayout();
                mAdapter.showErrorFooterView();
            }
        });
    }

    private void post(Runnable r) {
        mHandler.postDelayed(r, 800);
    }

    private List<Title> generateTitleList(int page, int pageSize) {
        List<Title> titles = new ArrayList<>();

        for(int i = 0; i < pageSize; i++) {
            Title title = new Title();
            title.name = "title#" + (page + i);
            title.id = page + i;
            titles.add(title);
        }
        return titles;
    }

}
