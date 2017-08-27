package com.dancing.bigw.tango.ui.news;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dancing.bigw.tango.R;
import com.dancing.bigw.tango.entities.Title;
import com.dancing.bigw.tango.ui.base.adapter.BaseViewHolder;
import com.dancing.bigw.tango.ui.base.adapter.ViewHolderActionListener;

/**
 * Created by bigw on 21/08/2017.
 */

public class TitleViewHolder extends BaseViewHolder<Title, TitleViewHolder.ActionListener> implements View.OnClickListener {

    private final String TAG = getClass().getSimpleName();

    private final TextView mTextTitle;
    private final TextView mTextId;

    public TitleViewHolder(View itemView) {
        super(itemView);
        mTextTitle = getViewById(R.id.text);
        mTextId = getViewById(R.id.text_id);
    }

    @Override
    public void bind(Title item, int position) {
        super.bind(item, position);

        mTextTitle.setText(item.name);
        mTextId.setText(String.valueOf(item.id));

        mTextTitle.setOnClickListener(this);
        mTextId.setOnClickListener(this);

        Log.d("AutoPager", "TitleViewHolder bind position " + position);
    }

    @Override
    public void onClick(View v) {
        ActionListener actionListener = getActionListener();
        if (actionListener == null) {
            return;
        }

        switch (v.getId()) {
            case R.id.text:
                actionListener.onTitleClicked(getItem(), v, getCurrentPosition());
                break;
            case R.id.text_id:
                actionListener.onIdClicked(getItem(), v, getCurrentPosition());
                break;
        }
    }

    public static TitleViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.layout_item_title, parent, false);
        return new TitleViewHolder(itemView);
    }

    public interface ActionListener extends ViewHolderActionListener {
        void onTitleClicked(Title item, View view, int position);
        void onIdClicked(Title item, View view, int position);
    }
}
