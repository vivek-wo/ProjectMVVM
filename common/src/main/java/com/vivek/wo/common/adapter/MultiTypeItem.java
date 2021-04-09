package com.vivek.wo.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

public abstract class MultiTypeItem<T, VH extends ViewTypeHolder> {
    private BaseMultiTypeAdapter adapter;
    private DiffUtil.ItemCallback<T> itemCallback;
    private int layoutResId;
    private Class<?> itemClass;
    private int viewType;
    private int spanSize;

    public MultiTypeItem(int layoutResId) {
        this(layoutResId, null);
    }

    public MultiTypeItem(int layoutResId, DiffUtil.ItemCallback<T> itemCallback) {
        this(layoutResId, 0, itemCallback);
    }

    public MultiTypeItem(int layoutResId, int spanSize) {
        this(layoutResId, spanSize, null);
    }

    public MultiTypeItem(int layoutResId, int spanSize, DiffUtil.ItemCallback<T> itemCallback) {
        this.layoutResId = layoutResId;
        this.itemCallback = itemCallback;
        this.spanSize = spanSize;
        initItemClass();
    }

    public void attachAdapter(BaseMultiTypeAdapter adapter) {
        this.adapter = adapter;
    }

    public void initItemClass() {
        Type type = this.getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            itemClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[0];
        }
        if (itemClass == null) {
            throw new IllegalArgumentException("Can not parse class type.");
        }
    }

    public Class<?> getItemClass() {
        return itemClass;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public BaseMultiTypeAdapter getAdapter() {
        return adapter;
    }

    VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return createViewHolder(parent, viewType);
    }

    void onBindViewHolder(@NonNull ViewTypeHolder holder, int position) {
        T t = (T) this.adapter.getItem(position);
        executeBindViewHolder((VH) holder, t, position);
    }

    protected VH createViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflaterItemView(parent);
        VH holder = createViewHolder(itemView);
        bindItemEventListener(holder);
        return holder;
    }

    /**
     * 覆写此方法实现View的绑定操作
     *
     * @param holder
     * @param t
     * @param position
     */
    protected abstract void executeBindViewHolder(@NonNull VH holder, T t, int position);

    /**
     * 覆写此方法实现Item的点击事件
     *
     * @param holder
     * @param position
     */
    protected void onItemClick(VH holder, int position) {
    }

    /**
     * 覆写此方法实现Item的长按事件
     *
     * @param holder
     * @param position
     */
    protected void onItemLongClick(VH holder, int position) {
    }

    /**
     * 绑定Item View的处理事件，覆写此方法实现更多事件监听
     * <p>
     * 默认实现Item的点击和长按操作
     *
     * @param holder
     */
    protected void bindItemEventListener(VH holder) {
        bindItemClickListener(holder);
        bindItemLongClickListener(holder);
    }

    protected void bindItemClickListener(VH holder) {
        holder.itemView.setOnClickListener(v -> {
            int position = holder.getAdapterPosition();
            if (position == NO_POSITION) {
                return;
            }
            onItemClick(holder, position);
        });
    }

    protected void bindItemLongClickListener(VH holder) {
        holder.itemView.setOnLongClickListener(v -> {
            int position = holder.getAdapterPosition();
            if (position == NO_POSITION) {
                return false;
            }
            onItemLongClick(holder, position);
            return false;
        });
    }

    protected View inflaterItemView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext())
                .inflate(layoutResId, parent, false);
    }

    /**
     * 覆写此方法可实现实例化ViewHolder的操作，默认反射实现，建议覆写实现
     *
     * @param itemView
     * @return
     */
    protected VH createViewHolder(View itemView) {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            Class holderClass = (Class) ((ParameterizedType) type).getActualTypeArguments()[1];
            if (ViewTypeHolder.class.isAssignableFrom(holderClass)) {
                try {
                    return (VH) holderClass.getConstructor(View.class).newInstance(itemView);
                } catch (IllegalAccessException | InstantiationException
                        | InvocationTargetException | NoSuchMethodException e) {
                    e.printStackTrace();
                }
            }
        }
        return (VH) new ViewTypeHolder(itemView);
    }

    boolean areItemsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
        if (this.itemCallback != null) {
            return this.itemCallback.areItemsTheSame((T) oldItem, (T) newItem);
        }
        return oldItem == newItem;
    }

    boolean areContentsTheSame(@NonNull Object oldItem, @NonNull Object newItem) {
        if (this.itemCallback != null) {
            return this.itemCallback.areContentsTheSame((T) oldItem, (T) newItem);
        }
        return false;
    }

    Object getChangePayload(@NonNull Object oldItem, @NonNull Object newItem) {
        if (this.itemCallback != null) {
            return this.itemCallback.getChangePayload((T) oldItem, (T) newItem);
        }
        return null;
    }

}
