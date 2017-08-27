package com.dancing.bigw.tango.ui.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dancing.bigw.lib.adapter.BaseViewHolder;
import com.dancing.bigw.lib.adapter.ViewHolderActionListener;
import com.dancing.bigw.tango.R;
import com.dancing.bigw.tango.entities.Content;

/**
 * Created by bigw on 21/08/2017.
 */

public class ContentViewHolder extends BaseViewHolder<Content, ContentViewHolder.ActionListener> implements View.OnClickListener {

    private final TextView mTextContent;

    public ContentViewHolder(View itemView) {
        super(itemView);
        mTextContent = getViewById(R.id.content);
    }

    @Override
    public void bind(Content item, int position) {
        super.bind(item, position);

        mTextContent.setText(item.content);
        mTextContent.setOnClickListener(this);
    }

    public static ContentViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.layout_item_content, parent, false);
        return new ContentViewHolder(itemView);
    }

    @Override
    public void onClick(View v) {
        ActionListener listener = getActionListener();
        if (listener == null) {
            return;
        }
        switch (v.getId()) {
            case R.id.content:
                listener.onContentClicked(getItem(), v, getCurrentPosition());
                break;
        }
    }

    public interface ActionListener extends ViewHolderActionListener {
        void onContentClicked(Content content, View view, int position);
    }
}
