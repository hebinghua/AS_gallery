package com.miui.gallery.editor.photo.app.crop;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.CropData;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class CropAdapter extends Adapter<ItemHolder> implements Selectable {
    public List<CropData> mDataList;
    public Selectable.Delegator mDelegator;

    public CropAdapter(Context context, List<CropData> list, Selectable.Selector selector) {
        this.mDataList = list;
        this.mDelegator = new Selectable.Delegator(0, selector);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public ItemHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ItemHolder(getInflater().inflate(R.layout.crop_menu_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ItemHolder itemHolder, int i) {
        super.onBindViewHolder((CropAdapter) itemHolder, i);
        itemHolder.bind(this.mDataList.get(i));
        this.mDelegator.onBindViewHolder(itemHolder, i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mDataList.size();
    }

    public void setSelection(int i) {
        getSelectedItem().isSelected = false;
        notifyItemChanged(getSelection());
        this.mDataList.get(i).isSelected = true;
        this.mDelegator.setSelection(i);
        notifyItemChanged(i);
    }

    @Override // com.miui.gallery.editor.photo.widgets.recyclerview.Selectable
    public int getSelection() {
        return this.mDelegator.getSelection();
    }

    public CropData getSelectedItem() {
        return this.mDataList.get(this.mDelegator.getSelection());
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
