package com.miui.gallery.vlog.filter;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.editor.R$layout;
import com.miui.gallery.vlog.base.widget.Selectable$Delegator;
import com.miui.gallery.vlog.entity.FilterData;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class FilterAdapter extends Adapter<FilterHolder> {
    public ArrayList<FilterData> mEffects;
    public int mHighlightColor;
    public int mLastSelectedPos = -1;
    public boolean mEditMode = false;
    public Selectable$Delegator mDelegator = new Selectable$Delegator(0, null);

    public FilterAdapter(ArrayList<FilterData> arrayList, int i) {
        this.mEffects = arrayList;
        this.mHighlightColor = i;
    }

    public FilterData getItemData(int i) {
        ArrayList<FilterData> arrayList = this.mEffects;
        if (arrayList == null || i < 0 || i >= arrayList.size()) {
            return null;
        }
        return this.mEffects.get(i);
    }

    public ArrayList<FilterData> getEffects() {
        return this.mEffects;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public FilterHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new FilterHolder(getInflater().inflate(R$layout.common_filter_menu_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(FilterHolder filterHolder, int i) {
        super.onBindViewHolder((FilterAdapter) filterHolder, i);
        boolean z = i == this.mDelegator.getSelection();
        FilterData filterData = this.mEffects.get(i);
        filterHolder.setName(filterData);
        filterHolder.setIcon(filterData);
        filterHolder.setDownloadViewState(filterData);
        filterHolder.setState(filterData, z, this.mEditMode);
        filterHolder.itemView.setContentDescription(VlogUtils.getGalleryApp().getResources().getString(filterData.mNameResId));
        this.mDelegator.onBindViewHolder(filterHolder, i);
        if (z && this.mEditMode && !filterData.isNone()) {
            filterHolder.updateIndicator(getValue());
        }
        if (filterData.isDownloadSuccess()) {
            filterData.setDownloadState(17);
        }
        if (z) {
            filterHolder.setConfigIndicator();
        }
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mEffects.size();
    }

    public void setSelection(int i) {
        this.mDelegator.setSelection(i);
        notifyItemChanged(i);
        notifyItemChanged(this.mLastSelectedPos);
        this.mLastSelectedPos = i;
    }

    public int getSelection() {
        return this.mDelegator.getSelection();
    }

    public boolean isInEditMode() {
        return this.mEditMode;
    }

    public void enterEditMode() {
        this.mEditMode = true;
        if (this.mDelegator.getSelection() >= 0) {
            notifyItemChanged(this.mDelegator.getSelection());
        }
    }

    public void exitEditMode() {
        this.mEditMode = false;
        if (this.mDelegator.getSelection() > 0) {
            notifyItemChanged(this.mDelegator.getSelection());
        }
    }

    public FilterData getSelectedItem() {
        if (this.mDelegator.getSelection() < 0 || this.mDelegator.getSelection() >= this.mEffects.size()) {
            return null;
        }
        return this.mEffects.get(this.mDelegator.getSelection());
    }

    public int getValue() {
        if (this.mDelegator.getSelection() < 0 || this.mDelegator.getSelection() >= this.mEffects.size()) {
            return 0;
        }
        return this.mEffects.get(this.mDelegator.getSelection()).getProgress();
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
