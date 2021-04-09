package com.vivek.wo.common.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.AsyncListDiffer;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static androidx.recyclerview.widget.RecyclerView.NO_POSITION;

public class BaseAdapter<T, VH extends ViewTypeHolder> extends RecyclerView.Adapter<VH> {
    private int layoutResId;
    private int variableId;
    private List<T> data = new ArrayList<>();
    private OnItemClickListener<VH> onItemClickListener;
    private OnItemLongClickListener<VH> onItemLongClickListener;

    AsyncListDiffer<T> mDiffer;

    public BaseAdapter(int layoutResId) {
        this(layoutResId, 0);
    }

    public BaseAdapter(int layoutResId, int variableId) {
        this.layoutResId = layoutResId;
        this.variableId = variableId;
    }

    /**
     * 设置支持Differ,设置后以Differ的方式处理数据
     *
     * @param itemCallback
     */
    public void setDifferSupport(DiffUtil.ItemCallback<T> itemCallback) {
        mDiffer = new AsyncListDiffer<T>(this, itemCallback);
    }

    /**
     * 设置Item的点击事件
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener<VH> listener) {
        this.onItemClickListener = listener;
    }

    /**
     * 设置Item的长按事件
     *
     * @param listener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener<VH> listener) {
        this.onItemLongClickListener = listener;
    }

    public void setList(List<T> dataList) {
        if (mDiffer != null) {
            mDiffer.submitList(dataList);
        } else {
            this.data.clear();
            this.data.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    public void addList(List<T> dataList) {
        if (mDiffer != null) {
            List<T> currentList = mDiffer.getCurrentList();
            currentList.addAll(dataList);
            mDiffer.submitList(currentList);
        } else {
            this.data.addAll(dataList);
            notifyDataSetChanged();
        }
    }

    public void submitList(List<T> dataList, Runnable commitCallback) {
        if (mDiffer == null) {
            return;
        }
        mDiffer.submitList(dataList, commitCallback);
    }

    @Override
    public int getItemCount() {
        return mDiffer != null ? mDiffer.getCurrentList().size() : data.size();
    }

    public T getItem(int position) {
        return mDiffer != null ? mDiffer.getCurrentList().get(position) : data.get(position);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull VH holder) {
        super.onViewAttachedToWindow(holder);
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull VH holder) {
        super.onViewDetachedFromWindow(holder);
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = inflaterItemView(parent);
        VH holder = createViewHolder(itemView);
        bindItemEventListener(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        executeBindViewHolder(holder, getItem(position), position);
    }

    /**
     * 覆写此方法实现更多的绑定操作，默认实现databinding的绑定操作
     *
     * @param holder
     * @param t
     * @param postion
     */
    protected void executeBindViewHolder(VH holder, T t, int postion) {
        if (variableId != 0 && holder instanceof ViewTypeHolder.DataBinding) {
            ViewDataBinding binding = ((ViewTypeHolder.DataBinding) holder).getBinding();
            if (binding != null) {
                binding.setVariable(variableId, t);
                binding.executePendingBindings();
            }
        }
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

    protected void onItemClick(VH holder, int position) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(this, holder, position);
        }
    }

    protected void onItemLongClick(VH holder, int position) {
        if (onItemLongClickListener != null) {
            onItemLongClickListener.onItemLongClick(this, holder, position);
        }
    }

    /**
     * 绑定Item View的处理事件，覆写此方法实现更多事件监听
     * <p>
     * 默认实现Item的点击和长按操作
     *
     * @param holder
     */
    protected void bindItemEventListener(VH holder) {
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(v -> {
                int position = holder.getAdapterPosition();
                if (position == NO_POSITION) {
                    return;
                }
                onItemClick(holder, position);
            });
        }
        if (onItemLongClickListener != null) {
            holder.itemView.setOnLongClickListener(v -> {
                int position = holder.getAdapterPosition();
                if (position == NO_POSITION) {
                    return false;
                }
                onItemLongClick(holder, position);
                return false;
            });
        }
    }

    public interface OnItemClickListener<VH> {
        void onItemClick(BaseAdapter adapter, VH holder, int position);
    }

    public interface OnItemLongClickListener<VH> {
        void onItemLongClick(BaseAdapter adapter, VH holder, int position);
    }

//    public static abstract class OnClickListener implements View.OnClickListener {
//
//        protected abstract void onClick(BaseAdapter adapter, int position, View view);
//
//        BaseAdapter adapter;
//        Holder holder;
//
//        public OnClickListener(BaseAdapter adapter, Holder holder) {
//            this.adapter = adapter;
//            this.holder = holder;
//        }
//
//        @Override
//        public void onClick(View v) {
//            int position = holder.getAdapterPosition();
//            if (position == NO_POSITION) {
//                return;
//            }
//            onClick(adapter, position, v);
//        }
//    }
}
