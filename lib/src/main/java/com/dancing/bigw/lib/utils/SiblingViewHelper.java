package com.dancing.bigw.lib.utils;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by bigw on 24/08/2017.
 */

public class SiblingViewHelper {

    private SparseArray<View> mSiblings = new SparseArray<>(5);

    private ViewGroup mContainer;

    public SiblingViewHelper(ViewGroup parent) {
        mContainer = parent;
    }

    public ViewGroup getContainer() {
        return this.mContainer;
    }

    public void addView(View childView) {
        if (trackView(childView)) {
            mContainer.addView(childView);
        }
    }

    public boolean trackView(View childView) {
        if (childView == null) {
            return false;
        }
        int key = childView.hashCode();
        if (mSiblings.get(key) == null) {
            mSiblings.put(key, childView);
        }
        return true;
    }

    public void bringToFront(View childView) {
        if (childView == null) {
            return;
        }

        int key = childView.hashCode();
        for (int i = 0; i < mSiblings.size(); i++) {
            int childKey = mSiblings.keyAt(i);
            if (childKey == key) {
                continue;
            }

            View siblingView = mSiblings.get(childKey);
            if (siblingView != null) {
                siblingView.setVisibility(View.GONE);
            }
        }

        childView.setVisibility(View.VISIBLE);
        childView.bringToFront();
    }
}
