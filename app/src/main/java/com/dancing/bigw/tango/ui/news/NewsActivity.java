package com.dancing.bigw.tango.ui.news;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dancing.bigw.tango.R;
import com.dancing.bigw.tango.entities.Content;
import com.dancing.bigw.tango.entities.Title;
import com.dancing.bigw.tango.ui.base.adapter.BaseAdapter;
import com.dancing.bigw.tango.ui.base.adapter.BaseViewHolder;
import com.dancing.bigw.tango.ui.base.adapter.ViewHolderActionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigw on 21/08/2017.
 */

public class NewsActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();

    public static void start(Context context) {
        context.startActivity(new Intent(context, NewsActivity.class));
    }

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final BaseAdapter mAdapter = new BaseAdapter();
        mAdapter.register(Title.class, TitleViewHolder.class, mTitleViewHolderActionListener);
        mAdapter.register(Content.class, new BaseAdapter.ViewHolderInfoProvider<Content>() {
            @Override
            public Class<? extends BaseViewHolder> provideViewHolderClass(Content item) {
                if ("he".equals(item.content)) {
                    return OtherViewHolder.class;
                }
                return ContentViewHolder.class;
            }

            @Override
            public ViewHolderActionListener provideViewHolderActionListener(Content item) {
                if ("he".equals(item.content)) {
                    return null;
                }
                return mContentActionListener;
            }
        });
        recyclerView.setAdapter(mAdapter);


        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //mAdapter.setItems(generateTitleList());
                mAdapter.setItems(generateDataList());
            }
        }, 1000);
    }

    private TitleViewHolder.ActionListener mTitleViewHolderActionListener = new TitleViewHolder.ActionListener() {
        @Override
        public void onTitleClicked(Title item, View view, int position) {
            String result = "clicked title " + item.name + " , position " + position;
            Toast.makeText(NewsActivity.this, result, Toast.LENGTH_SHORT).show();
            Log.d(TAG, "onTitleClicked " + item.name + ", position " + position);
        }

        @Override
        public void onIdClicked(Title item, View view, int position) {
            String result = "clicked id " + item.id + " , position " + position;
            Log.d(TAG, "onIdClicked " + item.id + ", position " + position);
            Toast.makeText(NewsActivity.this, result, Toast.LENGTH_SHORT).show();
        }
    };

    private ContentViewHolder.ActionListener mContentActionListener = new ContentViewHolder.ActionListener() {
        @Override
        public void onContentClicked(Content content, View view, int position) {
            Toast.makeText(NewsActivity.this, "position " + position + " , " + content.content, Toast.LENGTH_SHORT).show();
        }
    };

    private List<?> generateTitleList() {
        List items = new ArrayList<>();
        for(int i = 0; i < 200; i++) {
            Title title = new Title();
            title.name = "name#" + i;
            title.id = i;
            items.add(title);
        }
        return items;
    }

    private List<?> generateDataList() {
        List items = new ArrayList();
        for(int i = 0; i < 200; i++) {
            Title title = new Title();
            title.name = "name#" + i;
            title.id = i;
            items.add(title);

            if (i % 3 == 0) {
                Content content = new Content();
                content.content = "content#" + i;

                if (i % 6 == 0) {
                    content.content = "he";
                }

                items.add(content);
            }
        }
        return items;
    }
}
