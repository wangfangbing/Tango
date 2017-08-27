package com.dancing.bigw.tango.ui.pages;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dancing.bigw.tango.R;
import com.dancing.bigw.tango.ui.base.BaseFragment;

/**
 * Created by bigw on 21/08/2017.
 */

public class SimpleFragment extends BaseFragment {

    public static SimpleFragment newInstance(String title) {
        SimpleFragment f = new SimpleFragment();
        Bundle data = new Bundle();
        data.putString("title", title);
        f.setArguments(data);
        return f;
    }

    private String mTitle;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mTitle = getArguments().getString("title");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView textView = (TextView) view.findViewById(R.id.text1);
        textView.setText(mTitle);
    }

    @Override
    protected void onFragmentVisibilityChanged(boolean visible) {
        super.onFragmentVisibilityChanged(visible);

        if (DEBUG) {
            Log.d(TAG, mTitle + " onFragmentVisibilityChanged visible = " + visible);
        }
    }
}
