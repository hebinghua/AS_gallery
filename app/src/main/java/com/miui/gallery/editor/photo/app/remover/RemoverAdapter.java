package com.miui.gallery.editor.photo.app.remover;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.RemoverData;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class RemoverAdapter extends Adapter<RemoverItemHolder> implements Selectable {
    public List<RemoverData> mDataList;
    public Selectable.Delegator mDelegator = new Selectable.Delegator(-1, null);

    public RemoverAdapter(Context context, List<RemoverData> list, Selectable.Selector selector) {
        this.mDataList = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public RemoverItemHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RemoverItemHolder(getInflater().inflate(R.layout.remover_menu_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RemoverItemHolder removerItemHolder, int i) {
        super.onBindViewHolder((RemoverAdapter) removerItemHolder, i);
        removerItemHolder.bind(this.mDataList.get(i), this.mDelegator.getSelection() == i);
        this.mDelegator.onBindViewHolder(removerItemHolder, i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<RemoverData> list = this.mDataList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void setSelection(int i) {
        this.mDelegator.setSelection(i);
        notifyDataSetChanged();
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
