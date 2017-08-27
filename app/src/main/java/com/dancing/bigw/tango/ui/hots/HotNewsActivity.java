package com.dancing.bigw.tango.ui.hots;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.dancing.bigw.tango.R;

/**
 * Created by bigw on 25/08/2017.
 */

public class HotNewsActivity extends AppCompatActivity implements View.OnClickListener {

    public static void start(Context context) {
        context.startActivity(new Intent(context, HotNewsActivity.class));
    }

    private HotNewsFragment mFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot_news);

        if (savedInstanceState == null) {
            mFragment = HotNewsFragment.newInstance();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, mFragment).commit();
        }

        findViewById(R.id.setItem).setOnClickListener(this);
        findViewById(R.id.setItems).setOnClickListener(this);

        findViewById(R.id.appendItem).setOnClickListener(this);
        findViewById(R.id.appendItems).setOnClickListener(this);

        findViewById(R.id.insertItem).setOnClickListener(this);
        findViewById(R.id.insertItems).setOnClickListener(this);

        findViewById(R.id.removeItem).setOnClickListener(this);
        findViewById(R.id.removeItems).setOnClickListener(this);

        findViewById(R.id.moveItem).setOnClickListener(this);

        findViewById(R.id.append_none).setOnClickListener(this);
        findViewById(R.id.append_failed).setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setItem:
                mFragment.emulateSetItem();
                break;
            case R.id.setItems:
                mFragment.emulateSetItems();
                break;
            case R.id.appendItem:
                mFragment.emulateAppendItem();
                break;
            case R.id.appendItems:
                mFragment.emulateAppendItems();
                break;
            case R.id.insertItem:
                mFragment.emulateInsertItem();
                break;
            case R.id.insertItems:
                mFragment.emulateInsertItems();
                break;
            case R.id.removeItem:
                mFragment.emulateRemoveItem();
                break;
            case R.id.removeItems:
                mFragment.emulateRemoveItems();
                break;
            case R.id.moveItem:
                mFragment.emulateMoveItem();
                break;
            case R.id.append_none:
                mFragment.emulateAppendNone();
                break;
            case R.id.append_failed:
                mFragment.emulateAppendFailed();
                break;
        }
    }
}
