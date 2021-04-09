package com.vivek.wo.common.adapter;

import android.annotation.SuppressLint;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class BaseMultiTypeAdapter extends BaseAdapter<Object, ViewTypeHolder> {
    private static final int VIEWTYPE_OFFSET = 10;
    private Map<Class<?>, MultiTypeItem<?, ?>> mViewTypeMaps = new HashMap<>();

    public BaseMultiTypeAdapter() {
        super(0);
    }

    /**
     * 添加不同的Item处理操作，使用MultiTypeItem中数据的实体类型作为ViewType依据
     *
     * @param multiTypeItem
     */
    public void addViewType(MultiTypeItem<?, ?> multiTypeItem) {
        multiTypeItem.setViewType(mViewTypeMaps.size() + VIEWTYPE_OFFSET);
        multiTypeItem.attachAdapter(this);
        mViewTypeMaps.put(multiTypeItem.getItemClass(), multiTypeItem);
    }

    /**
     * 此方法不起作用，使用{@link #setDifferSupport()}
     *
     * @param itemCallback
     */
    @Override
    public void setDifferSupport(DiffUtil.ItemCallback<Object> itemCallback) {
    }

    /**
     * 设置支持Differ,设置后以Differ的方式处理数据
     * <p>
     * 具体回调参考{@link MultiTypeItem#MultiTypeItem(int, DiffUtil.ItemCallback)}设置
     */
    public void setDifferSupport() {
        mDiffer = new AsyncListDiffer<Object>(this, new ItemCallback());
    }

    @NonNull
    @Override
    public ViewTypeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        MultiTypeItem<?, ?> multiTypeItem = findDefaultViewType(viewType);
        if (multiTypeItem != null) {
            return multiTypeItem.onCreateViewHolder(parent, viewType);
        }
        throw new IllegalArgumentException("Can not find viewType holder.");
    }

    @Override
    public void onBindViewHolder(@NonNull ViewTypeHolder holder, int position) {
        MultiTypeItem<?, ?> multiTypeItem = findDefaultViewType(holder.getItemViewType());
        if (multiTypeItem != null) {
            multiTypeItem.onBindViewHolder(holder, position);
        }
    }

    private MultiTypeItem<?, ?> findDefaultViewType(int viewType) {
        Iterator<MultiTypeItem<?, ?>> iterator = mViewTypeMaps.values().iterator();
        while (iterator.hasNext()) {
            MultiTypeItem<?, ?> multiTypeItem = iterator.next();
            if (multiTypeItem.getViewType() == viewType) {
                return multiTypeItem;
            }
        }
        return null;
    }

    @Override
    public int getItemViewType(int position) {
        Object object = getItem(position);
        MultiTypeItem<?, ?> viewType = mViewTypeMaps.get(object.getClass());
        if (viewType != null) {
            return viewType.getViewType();
        }
        return super.getItemViewType(position);
    }

    class SpanSizeLookup extends GridLayoutManager.SpanSizeLookup {

        @Override
        public int getSpanSize(int position) {
            Object object = getItem(position);
            MultiTypeItem<?, ?> viewType = mViewTypeMaps.get(object.getClass());
            if (viewType != null) {
                return viewType.getSpanSize();
            }
            return 0;
        }
    }

    class ItemCallback extends DiffUtil.ItemCallback<Object> {

        @Override
        public boolean areItemsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
            if (oldItem.getClass() == newItem.getClass()) {
                MultiTypeItem<?, ?> multiTypeItem = mViewTypeMaps.get(oldItem.getClass());
                if (multiTypeItem != null) {
                    return multiTypeItem.areItemsTheSame(oldItem, newItem);
                }
            }
            return oldItem == newItem;
        }

        @SuppressLint("DiffUtilEquals")
        @Override
        public boolean areContentsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
            if (oldItem.getClass() == newItem.getClass()) {
                MultiTypeItem<?, ?> multiTypeItem = mViewTypeMaps.get(oldItem.getClass());
                if (multiTypeItem != null) {
                    return multiTypeItem.areContentsTheSame(oldItem, newItem);
                }
            }
            return false;
        }

        @Nullable
        @Override
        public Object getChangePayload(@NonNull Object oldItem, @NonNull Object newItem) {
            if (oldItem.getClass() == newItem.getClass()) {
                MultiTypeItem<?, ?> multiTypeItem = mViewTypeMaps.get(oldItem.getClass());
                if (multiTypeItem != null) {
                    return multiTypeItem.getChangePayload(oldItem, newItem);
                }
            }
            return super.getChangePayload(oldItem, newItem);
        }
    }
}
