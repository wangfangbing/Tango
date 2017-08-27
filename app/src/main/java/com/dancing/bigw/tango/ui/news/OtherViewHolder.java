package com.dancing.bigw.tango.ui.news;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.dancing.bigw.tango.R;
import com.dancing.bigw.tango.entities.Content;
import com.dancing.bigw.tango.ui.base.adapter.BaseViewHolder;
import com.dancing.bigw.tango.ui.base.adapter.ViewHolderActionListener;

/**
 * Created by bigw on 22/08/2017.
 */

public class OtherViewHolder extends BaseViewHolder<Content, OtherViewHolder.ActionListener> {

    public OtherViewHolder(View itemView) {
        super(itemView);
    }

    @Override
    public void bind(Content item, int position) {
        super.bind(item, position);
        //
    }

    public static OtherViewHolder create(LayoutInflater inflater, ViewGroup parent) {
        View itemView = inflater.inflate(R.layout.layout_item_other_content, parent, false);
        return new OtherViewHolder(itemView);
    }

    public interface ActionListener extends ViewHolderActionListener {
        //
    }
}
