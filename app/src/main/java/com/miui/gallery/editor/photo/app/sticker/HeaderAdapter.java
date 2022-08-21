package com.miui.gallery.editor.photo.app.sticker;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.StickerCategory;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class HeaderAdapter extends Adapter<HeaderHolder> implements Selectable {
    public List<StickerCategory> mCategories;
    public Selectable.Delegator mDelegator;

    public HeaderAdapter(List<StickerCategory> list, Selectable.Selector selector) {
        this.mCategories = list;
        this.mDelegator = new Selectable.Delegator(0, selector);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public HeaderHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new HeaderHolder(getInflater().inflate(R.layout.sticker_category_navigation_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(HeaderHolder headerHolder, int i) {
        super.onBindViewHolder((HeaderAdapter) headerHolder, i);
        headerHolder.bind(this.mCategories.get(i));
        this.mDelegator.onBindViewHolder(headerHolder, i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mCategories.size();
    }

    public void setSelection(int i) {
        this.mDelegator.setSelection(i);
    }

    @Override // com.miui.gallery.editor.photo.widgets.recyclerview.Selectable
    public int getSelection() {
        return this.mDelegator.getSelection();
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
