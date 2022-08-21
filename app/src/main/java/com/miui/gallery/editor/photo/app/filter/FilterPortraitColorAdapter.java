package com.miui.gallery.editor.photo.app.filter;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.editor.photo.app.filter.portrait.PortraitColorCheckHelper;
import com.miui.gallery.editor.photo.core.common.model.FilterData;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.List;

/* loaded from: classes2.dex */
public class FilterPortraitColorAdapter extends FilterAdapter {
    @Override // com.miui.gallery.editor.photo.app.filter.FilterAdapter, com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ int getItemCount() {
        return super.getItemCount();
    }

    @Override // com.miui.gallery.editor.photo.app.filter.FilterAdapter
    public /* bridge */ /* synthetic */ FilterData getItemData(int i) {
        return super.getItemData(i);
    }

    @Override // com.miui.gallery.editor.photo.app.filter.FilterAdapter, com.miui.gallery.editor.photo.widgets.recyclerview.Selectable
    public /* bridge */ /* synthetic */ int getSelection() {
        return super.getSelection();
    }

    @Override // com.miui.gallery.editor.photo.app.filter.FilterAdapter, com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override // com.miui.gallery.editor.photo.app.filter.FilterAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder */
    public /* bridge */ /* synthetic */ RecyclerView.ViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return super.mo1843onCreateViewHolder(viewGroup, i);
    }

    @Override // com.miui.gallery.editor.photo.app.filter.FilterAdapter, com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public /* bridge */ /* synthetic */ void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
    }

    @Override // com.miui.gallery.editor.photo.app.filter.FilterAdapter
    public /* bridge */ /* synthetic */ void setSelection(int i) {
        super.setSelection(i);
    }

    public FilterPortraitColorAdapter(List<FilterData> list, int i, int i2) {
        super(list, i, i2);
        setPortraitColorFilterDataState();
    }

    public final void setPortraitColorFilterDataState() {
        if (!PortraitColorCheckHelper.isPortraitColorAvailable()) {
            return;
        }
        for (FilterData filterData : this.mEffects) {
            if (filterData != null && filterData.isPortraitColor()) {
                filterData.state = 17;
                return;
            }
        }
    }

    public FilterData getPortraitColorData() {
        if (!BaseMiscUtil.isValid(this.mEffects)) {
            return null;
        }
        for (FilterData filterData : this.mEffects) {
            if (filterData != null && filterData.isPortraitColor()) {
                return filterData;
            }
        }
        return null;
    }

    @Override // com.miui.gallery.editor.photo.app.filter.FilterAdapter, com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        FilterData filterData = this.mEffects.get(i);
        if (PortraitColorCheckHelper.isPortraitEnable()) {
            ((FilterMenuItem) viewHolder).setDownloadViewState(filterData.state);
        }
        if (filterData.state == 0) {
            filterData.state = 17;
        }
        super.onBindViewHolder(viewHolder, i);
    }
}
