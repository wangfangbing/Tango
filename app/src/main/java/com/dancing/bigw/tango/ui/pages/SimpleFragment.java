package com.dancing.bigw.tango.ui.pages;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dancing.bigw.lib.fragment.BaseFragment;
import com.dancing.bigw.tango.R;

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

    @Override
    protected View inflateNormalPageView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_simple, parent, false);
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
    }
}
