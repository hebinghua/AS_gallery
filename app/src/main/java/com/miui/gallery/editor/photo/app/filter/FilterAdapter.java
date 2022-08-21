package com.miui.gallery.editor.photo.app.filter;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.FilterData;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class FilterAdapter extends Adapter<RecyclerView.ViewHolder> implements Selectable {
    public List<FilterData> mEffects;
    public boolean mHasAnimationPlayed;
    public int mSubHighlightColor;
    public int mSubItemSize;
    public int mLastSelectedIndex = 0;
    public boolean mEditMode = false;
    public Selectable.Delegator mDelegator = new Selectable.Delegator(0, null);

    public boolean isEditable(int i) {
        return true;
    }

    public FilterAdapter(List<FilterData> list, int i, int i2) {
        this.mEffects = list;
        this.mSubHighlightColor = i;
        this.mSubItemSize = i2;
    }

    public FilterData getItemData(int i) {
        List<FilterData> list = this.mEffects;
        if (list == null || i < 0 || i >= list.size()) {
            return null;
        }
        return this.mEffects.get(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder */
    public RecyclerView.ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new FilterMenuItem(getInflater().inflate(R.layout.filter_menu_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        super.onBindViewHolder(viewHolder, i);
        FilterMenuItem filterMenuItem = (FilterMenuItem) viewHolder;
        boolean z = i == this.mDelegator.getSelection();
        FilterData filterData = this.mEffects.get(i);
        filterMenuItem.bindData(filterData);
        filterMenuItem.setState(z, isEditable(i), this.mEditMode);
        this.mDelegator.onBindViewHolder(filterMenuItem, i);
        if (z && this.mEditMode && !filterData.isNone()) {
            filterMenuItem.updateIndicator(getValue());
        }
        if (!z || this.mHasAnimationPlayed) {
            return;
        }
        filterMenuItem.setConfigIndicator();
        this.mHasAnimationPlayed = true;
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mEffects.size();
    }

    public void setSelection(int i) {
        this.mHasAnimationPlayed = false;
        this.mDelegator.setSelection(i);
        notifyItemChanged(i);
        notifyItemChanged(this.mLastSelectedIndex);
        this.mLastSelectedIndex = i;
    }

    @Override // com.miui.gallery.editor.photo.widgets.recyclerview.Selectable
    public int getSelection() {
        return this.mDelegator.getSelection();
    }

    public boolean isInEditMode() {
        return this.mEditMode;
    }

    public void enterEditMode() {
        this.mEditMode = true;
        notifyItemChanged(this.mDelegator.getSelection());
    }

    public void exitEditMode() {
        this.mEditMode = false;
        notifyItemChanged(this.mDelegator.getSelection());
    }

    public void update(int i) {
        if (!this.mEditMode || this.mDelegator.getSelection() == -1) {
            return;
        }
        this.mEffects.get(this.mDelegator.getSelection()).progress = i;
        notifyItemChanged(this.mDelegator.getSelection());
    }

    public int getValue() {
        return this.mEffects.get(this.mDelegator.getSelection()).progress;
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        this.mDelegator.onAttachedToRecyclerView(recyclerView);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        this.mDelegator.onDetachedFromRecyclerView(recyclerView);
    }
}
