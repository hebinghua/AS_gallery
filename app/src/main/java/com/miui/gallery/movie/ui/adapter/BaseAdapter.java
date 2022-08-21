package com.miui.gallery.movie.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.movie.R$id;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class BaseAdapter<T> extends RecyclerView.Adapter<BaseHolder<T>> implements View.OnClickListener {
    public Context mContext;
    public ItemSelectChangeListener mItemSelectChangeListener;
    public List<T> mList;
    public int mSelectedItemPosition = 0;
    public int mLastSelectedItemPosition = -1;

    /* loaded from: classes2.dex */
    public interface ItemSelectChangeListener {
        boolean onItemSelect(miuix.recyclerview.widget.RecyclerView recyclerView, BaseHolder baseHolder, int i, boolean z);
    }

    public abstract BaseHolder<T> getHolder(View view);

    public abstract int getLayoutId(int i);

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        onBindViewHolder((BaseHolder) ((BaseHolder) viewHolder), i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i, List list) {
        onBindViewHolder((BaseHolder) ((BaseHolder) viewHolder), i, (List<Object>) list);
    }

    public void setSelectedItemPosition(int i) {
        int i2 = this.mSelectedItemPosition;
        if (i == i2) {
            return;
        }
        this.mLastSelectedItemPosition = i2;
        this.mSelectedItemPosition = i;
        Boolean bool = Boolean.TRUE;
        notifyItemChanged(i2, bool);
        notifyItemChanged(this.mSelectedItemPosition, bool);
    }

    public void setSelectedItemPositionWithoutNotify(int i) {
        this.mLastSelectedItemPosition = this.mSelectedItemPosition;
        this.mSelectedItemPosition = i;
    }

    public void setItemSelectChangeListener(ItemSelectChangeListener itemSelectChangeListener) {
        this.mItemSelectChangeListener = itemSelectChangeListener;
    }

    public ItemSelectChangeListener getItemSelectChangeListener() {
        return this.mItemSelectChangeListener;
    }

    public void setList(List<T> list) {
        this.mList = list;
    }

    public List<T> getList() {
        return this.mList;
    }

    public BaseAdapter(Context context) {
        this.mContext = context;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public BaseHolder<T> mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return getHolder(LayoutInflater.from(this.mContext).inflate(getLayoutId(i), viewGroup, false));
    }

    public void onBindViewHolder(BaseHolder<T> baseHolder, int i) {
        baseHolder.itemView.setOnClickListener(this);
        setSelected(baseHolder, i == this.mSelectedItemPosition);
        baseHolder.bindView(mo1153getItemData(i), i);
    }

    public final void setSelected(BaseHolder baseHolder, boolean z) {
        baseHolder.itemView.setSelected(z);
        baseHolder.mSelected.setVisibility(z ? 0 : 4);
    }

    public void onBindViewHolder(BaseHolder<T> baseHolder, int i, List<Object> list) {
        if (list != null && list.size() > 0) {
            setSelected(baseHolder, i == this.mSelectedItemPosition);
            baseHolder.bindView(mo1153getItemData(i), i, list);
            return;
        }
        super.onBindViewHolder((BaseAdapter<T>) baseHolder, i, list);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        BaseHolder baseHolder = (BaseHolder) view.getTag(R$id.item_root);
        int adapterPosition = baseHolder.getAdapterPosition();
        if (this.mSelectedItemPosition != adapterPosition) {
            ItemSelectChangeListener itemSelectChangeListener = this.mItemSelectChangeListener;
            if (itemSelectChangeListener == null || !itemSelectChangeListener.onItemSelect((miuix.recyclerview.widget.RecyclerView) view.getParent(), baseHolder, adapterPosition, true)) {
                return;
            }
            setSelectedItemPosition(adapterPosition);
            return;
        }
        ItemSelectChangeListener itemSelectChangeListener2 = this.mItemSelectChangeListener;
        if (itemSelectChangeListener2 == null) {
            return;
        }
        itemSelectChangeListener2.onItemSelect((miuix.recyclerview.widget.RecyclerView) view.getParent(), baseHolder, adapterPosition, false);
    }

    /* loaded from: classes2.dex */
    public static abstract class BaseHolder<T> extends RecyclerView.ViewHolder {
        public View mSelected;

        public abstract void bindView(T t, int i);

        public void bindView(T t, int i, Object obj) {
        }

        public BaseHolder(View view) {
            super(view);
            view.setTag(R$id.item_root, this);
            this.mSelected = view.findViewById(R$id.iv_selected);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<T> list = this.mList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    /* renamed from: getItemData */
    public T mo1153getItemData(int i) {
        List<T> list = this.mList;
        if (list == null || i < 0 || i >= list.size()) {
            return null;
        }
        return this.mList.get(i);
    }

    public int getSelectedItemPosition() {
        return this.mSelectedItemPosition;
    }
}
