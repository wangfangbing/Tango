package com.dancing.bigw.tango;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.dancing.bigw.tango.ui.account.AccountActivity;
import com.dancing.bigw.tango.ui.hots.HotNewsActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //AccountActivity.start(this);
        ///NewsActivity.start(this);
        HotNewsActivity.start(this);
        //TestActivity.start(this);
    }
}
