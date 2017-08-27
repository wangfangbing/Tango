package com.dancing.bigw.tango.ui.test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dancing.bigw.tango.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bigw on 25/08/2017.
 */

public class TestActivity extends AppCompatActivity {

    public static void start(Context context) {
        context.startActivity(new Intent(context, TestActivity.class));
    }

    private Handler mHandler = new Handler();

    private MyAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teset);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        mAdapter = new MyAdapter();
        recyclerView.setAdapter(mAdapter);

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                List<String> list = new ArrayList<>();
                for (int i = 0; i < 30; i++) {
                    list.add("string " + i);
                }
                mAdapter.setItems(list);
            }
        }, 1000);
    }

    private class MyAdapter extends RecyclerView.Adapter<ViewHolder> {

        private List<String> items = new ArrayList<>();

        public void setItems(List<String> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            View itemView = LayoutInflater.from(context).inflate(R.layout.layout_footer_view_item, parent, false);
            return new ViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            String item = items.get(position);
            holder.textView.setText(item);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemViewType(int position) {
            int result = 0;
            Log.d("TestAdapter", "getItemViewType " + position);
            return result;
        }
    };

    private static class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
        }
    }
}
