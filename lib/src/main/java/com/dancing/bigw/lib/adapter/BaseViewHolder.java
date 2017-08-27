package com.dancing.bigw.lib.adapter;

import android.support.annotation.CallSuper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by bigw on 21/08/2017.
 */

public class BaseViewHolder<T, Listener extends ViewHolderActionListener> extends RecyclerView.ViewHolder {

    private Listener mActionListener;

    private T mItem;

    private int mPosition;

    public BaseViewHolder(View itemView) {
        super(itemView);
    }

    public <T> T getViewById(int id) {
        return (T) itemView.findViewById(id);
    }

    @CallSuper
    public void bind(T item, int position) {
        this.mItem = item;
        this.mPosition = position;
    }

    public T getItem() {
        return this.mItem;
    }

    public int getCurrentPosition() {
        return mPosition;
    }

    public void setActionListener(Listener actionListener) {
        this.mActionListener = actionListener;
    }

    public Listener getActionListener() {
        return this.mActionListener;
    }

    //TODO define a static method named create with ViewHolder return type
}
