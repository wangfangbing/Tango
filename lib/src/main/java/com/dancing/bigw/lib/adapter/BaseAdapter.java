package com.dancing.bigw.lib.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by bigw on 21/08/2017.
 */

public class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    private static final String CREATE_VIEW_HOLDER_METHOD = "create";
    private List mItems = new ArrayList();

    private SparseArray<ViewHolderInfo> mViewHolderInfos = new SparseArray<>();
    private SparseArray<ViewTypeProvider> mViewTypeProviders = new SparseArray<>();

    public BaseAdapter register(Class<?> itemClass, final Class<? extends BaseViewHolder> viewHolderClass, final ViewHolderActionListener actionListener) {
        int viewType = itemClass.hashCode();
        ViewHolderInfo info = new ViewHolderInfo(viewHolderClass, actionListener);
        mViewHolderInfos.put(viewType, info);
        return this;
    }

    public BaseAdapter register(Class<?> itemClass, @NonNull ViewHolderInfoProvider viewHolderInfoProvider) {
        int key = itemClass.hashCode();
        ViewTypeProvider provider = new ViewTypeProvider(itemClass, viewHolderInfoProvider);
        mViewTypeProviders.put(key, provider);
        return this;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolderInfo viewHolderInfo = mViewHolderInfos.get(viewType);
        Class<? extends BaseViewHolder> viewHolderClass = viewHolderInfo.mViewHolderClass;
        //TODO remove this code
        if (viewHolderClass == null) {
            throw new RuntimeException("没有register过的数据类型");
        }

        try {
            Method method = viewHolderClass.getMethod(CREATE_VIEW_HOLDER_METHOD, LayoutInflater.class, ViewGroup.class);
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            BaseViewHolder viewHolder = (BaseViewHolder) method.invoke(null, inflater, parent);
            viewHolder.setActionListener(viewHolderInfo.mViewHolderActionListener);

            return viewHolder;
        } catch (NoSuchMethodException noMethodException) {
            throw new RuntimeException("请在ViewHolder中定义 static BaseViewHolder create(LayoutInflater.class, ViewGroup.class) 方法");
        } catch (IllegalAccessException illegalAccessException) {
            throw new RuntimeException(illegalAccessException);
        } catch (InvocationTargetException ite) {
            throw new RuntimeException(ite);
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Object item = mItems.get(position);
        holder.bind(item, position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public int getItemViewType(int position) {
        Object item = mItems.get(position);

        int viewType = item.getClass().hashCode();
        ViewHolderInfo viewHolderInfo = mViewHolderInfos.get(viewType);
        if (viewHolderInfo != null) {
            return viewType;
        }

        int viewTypeProviderKey = viewType;
        ViewTypeProvider viewHolderInfoProvider = mViewTypeProviders.get(viewTypeProviderKey);
        if (viewHolderInfoProvider == null) {
            throw new RuntimeException("没有注册过的数据类型 " + item);
        }

        int generatedViewType = viewHolderInfoProvider.getViewType(item);
        //TODO check if the generateViewType has exits int the mViewholderInfos

        Class<? extends BaseViewHolder> viewHolderClass = viewHolderInfoProvider.getViewHolderClass(item);
        ViewHolderActionListener actionListener = viewHolderInfoProvider.getActionListener(item);
        viewHolderInfo = new ViewHolderInfo(viewHolderClass, actionListener);
        mViewHolderInfos.put(generatedViewType, viewHolderInfo);

        return generatedViewType;
    }

    private static class ViewTypeProvider {
        private final Class<?> itemClass;
        private final ViewHolderInfoProvider viewHolderInfoProvider;

        public ViewTypeProvider(Class<?> itemClass, ViewHolderInfoProvider viewHolderInfoProvider) {
            this.itemClass = itemClass;
            this.viewHolderInfoProvider = viewHolderInfoProvider;
        }

        public int getViewType(Object item) {
            Class<?> viewHolderClass = viewHolderInfoProvider.provideViewHolderClass(item);
            String mergedName = itemClass.getCanonicalName() + viewHolderClass.getCanonicalName();
            int viewType = mergedName.hashCode();
            return viewType;
        }

        public ViewHolderActionListener getActionListener(Object item) {
            return viewHolderInfoProvider.provideViewHolderActionListener(item);
        }

        public Class<? extends BaseViewHolder> getViewHolderClass(Object item) {
            return viewHolderInfoProvider.provideViewHolderClass(item);
        }
    }

    private static class ViewHolderInfo {
        public final Class<? extends BaseViewHolder> mViewHolderClass;
        public final ViewHolderActionListener mViewHolderActionListener;

        public ViewHolderInfo(Class<? extends BaseViewHolder> viewHolderClass, ViewHolderActionListener viewHolderActionListener) {
            this.mViewHolderClass = viewHolderClass;
            this.mViewHolderActionListener = viewHolderActionListener;
        }
    }

    public interface ViewHolderInfoProvider<T> {
        Class<? extends BaseViewHolder> provideViewHolderClass(T item);
        ViewHolderActionListener provideViewHolderActionListener(T item);
    }

    public List getItemList() {
        return this.mItems;
    }

    /**
     * ok
     */
    public void setItem(Object item) {
        checkItemValidity(item);
        List newItems = new ArrayList();
        newItems.add(item);
        setItems(newItems);
    }

    /**
     * ok
     */
    public void setItems(List newItems) {
        checkListValidity(newItems);
        mItems = newItems;
        notifyDataSetChanged();
    }

    /**
     * ok
     */
    public void appendItem(Object item) {
        checkItemValidity(item);

        mItems.add(item);
        int startPosition = getItemCount();
        notifyItemRangeInserted(startPosition, 1);
    }

    /**
     * ok
     */
    public void appendItems(List newItems) {
        checkListValidity(newItems);

        int startPosition = mItems.size();
        mItems.addAll(newItems);
        notifyItemRangeInserted(startPosition, newItems.size());
    }

    /**
     * ok
     */
    public void insertItem(int startPosition, Object item) {
        checkItemValidity(item);

        mItems.add(startPosition, item);
        notifyItemRangeInserted(startPosition, 1);
    }

    /**
     * ok
     */
    public void insertItems(int startPosition, List newItems) {
        checkListValidity(newItems);

        mItems.addAll(startPosition, newItems);
        notifyItemRangeInserted(startPosition, newItems.size());
    }

    /**
     * ok
     */
    public void removeItem(int startPosition) {
        removeItems(startPosition, 1);
    }

    /**
     * ok
     */
    public void removeItems(int startPosition, int itemCount) {
        for (int i = startPosition; i < startPosition + itemCount; i++) {
            mItems.remove(startPosition);
        }
        notifyItemRangeRemoved(startPosition, itemCount);
    }

    public void move(int startToPosition, int toPosition) {

    }

    protected void checkItemValidity(Object item) {
        if (item == null) {
            throw new RuntimeException("item 不能为空");
        }
        if (item instanceof Collection) {
            throw new RuntimeException("item 不能是容器类");
        }
    }

    protected void checkListValidity(List items) {
        if (items == null) {
            throw new RuntimeException("items 不能为空");
        }
    }
}
