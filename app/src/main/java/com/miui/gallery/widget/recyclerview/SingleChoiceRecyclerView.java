package com.miui.gallery.widget.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.baseui.R$id;

/* loaded from: classes3.dex */
public class SingleChoiceRecyclerView extends RecyclerView {
    public SingleChoiceRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* loaded from: classes3.dex */
    public static abstract class SingleChoiceRecyclerViewAdapter<SCVH extends SingleChoiceViewHolder> extends RecyclerView.Adapter<SCVH> implements View.OnClickListener {
        public ItemSelectChangeListener mItemSelectChangeListener;
        public int selectedItemPosition = 0;
        public int lastSelectedItemPosition = -1;

        /* loaded from: classes3.dex */
        public interface ItemSelectChangeListener {
            boolean onItemSelect(SingleChoiceRecyclerViewAdapter singleChoiceRecyclerViewAdapter, int i, boolean z);
        }

        public abstract void onBindView(SCVH scvh, int i);

        /* renamed from: onCreateSingleChoiceViewHolder */
        public abstract SCVH mo1797onCreateSingleChoiceViewHolder(ViewGroup viewGroup, int i);

        /* JADX WARN: Multi-variable type inference failed */
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public /* bridge */ /* synthetic */ void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
            onBindViewHolder((SingleChoiceRecyclerViewAdapter<SCVH>) ((SingleChoiceViewHolder) viewHolder), i);
        }

        public void setItemSelectChangeListener(ItemSelectChangeListener itemSelectChangeListener) {
            this.mItemSelectChangeListener = itemSelectChangeListener;
        }

        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        /* renamed from: onCreateViewHolder  reason: collision with other method in class */
        public SCVH mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
            SCVH mo1797onCreateSingleChoiceViewHolder = mo1797onCreateSingleChoiceViewHolder(viewGroup, i);
            mo1797onCreateSingleChoiceViewHolder.setItemViewOnClickListener(this);
            return mo1797onCreateSingleChoiceViewHolder;
        }

        public void setSelectedItemPosition(int i) {
            this.selectedItemPosition = i;
            notifyItemChanged(i, 1);
        }

        public int getSelectedItemPosition() {
            return this.selectedItemPosition;
        }

        public void clearLastSelectedPostion() {
            notifyItemChanged(this.lastSelectedItemPosition, 1);
        }

        public void onBindViewHolder(SCVH scvh, int i) {
            scvh.setSelect(i == this.selectedItemPosition);
            scvh.setItemPosition(i);
            onBindView(scvh, i);
        }

        @Override // android.view.View.OnClickListener
        public void onClick(View view) {
            int itemPosition = ((SingleChoiceViewHolder) view.getTag(R$id.editor_viewHolder)).getItemPosition();
            int i = this.selectedItemPosition;
            boolean z = i != itemPosition;
            if (z) {
                this.lastSelectedItemPosition = i;
            }
            ItemSelectChangeListener itemSelectChangeListener = this.mItemSelectChangeListener;
            if (itemSelectChangeListener != null) {
                itemSelectChangeListener.onItemSelect(this, itemPosition, z);
            }
        }

        /* loaded from: classes3.dex */
        public static abstract class SingleChoiceViewHolder extends RecyclerView.ViewHolder {
            public int itemPosition;
            public View mItemView;

            public abstract void setSelect(boolean z);

            public SingleChoiceViewHolder(View view) {
                super(view);
                this.mItemView = view;
                view.setTag(R$id.editor_viewHolder, this);
            }

            public void setItemPosition(int i) {
                this.itemPosition = i;
                this.itemView.setTag(R$id.editor_itemIndex, Integer.valueOf(i));
            }

            public int getItemPosition() {
                return this.itemPosition;
            }

            public void setItemViewOnClickListener(View.OnClickListener onClickListener) {
                this.mItemView.setOnClickListener(onClickListener);
            }
        }
    }
}
