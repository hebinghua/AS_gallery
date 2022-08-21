package com.miui.gallery.video.editor.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class SingleChoiceRecyclerView extends RecyclerView {
    public SingleChoiceRecyclerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* loaded from: classes2.dex */
    public static abstract class SingleChoiceRecyclerViewAdapter<SCVH extends SingleChoiceViewHolder> extends RecyclerView.Adapter<SCVH> implements View.OnClickListener {
        public ItemSelectChangeListener mItemSelectChangeListener;
        public int selectedItemPosition = 0;
        public int lastSelectedItemPosition = 0;

        /* loaded from: classes2.dex */
        public interface ItemSelectChangeListener {
            boolean onItemSelect(SingleChoiceRecyclerViewAdapter singleChoiceRecyclerViewAdapter, int i, boolean z);
        }

        public abstract void onBindView(SCVH scvh, int i);

        /* renamed from: onCreateSingleChoiceViewHolder */
        public abstract SCVH mo1754onCreateSingleChoiceViewHolder(ViewGroup viewGroup, int i);

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
            SCVH mo1754onCreateSingleChoiceViewHolder = mo1754onCreateSingleChoiceViewHolder(viewGroup, i);
            mo1754onCreateSingleChoiceViewHolder.setItemViewOnClickListener(this);
            return mo1754onCreateSingleChoiceViewHolder;
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
            int itemPosition = ((SingleChoiceViewHolder) view.getTag(R.id.video_editor_viewHolder)).getItemPosition();
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

        /* loaded from: classes2.dex */
        public static abstract class SingleChoiceViewHolder extends RecyclerView.ViewHolder {
            public int itemPosition;
            public View mItemView;

            public abstract void setSelect(boolean z);

            public SingleChoiceViewHolder(View view) {
                super(view);
                this.mItemView = view;
                view.setTag(R.id.video_editor_viewHolder, this);
            }

            public void setMarginLeft(int i) {
                ViewGroup.LayoutParams layoutParams = this.mItemView.getLayoutParams();
                if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                    ((ViewGroup.MarginLayoutParams) layoutParams).setMarginStart(i);
                }
            }

            public void setItemPosition(int i) {
                this.itemPosition = i;
                this.itemView.setTag(R.id.video_editor_itemIndex, Integer.valueOf(i));
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
