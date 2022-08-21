package com.miui.gallery.editor.photo.app.remover2;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.core.common.model.Remover2Data;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class Remover2Adapter extends Adapter<Remover2ItemHolder> implements Selectable {
    public List<Remover2Data> mDataList;
    public int mLastPosition = 0;
    public boolean mPeopleDisable = false;
    public Selectable.Delegator mDelegator = new Selectable.Delegator(-1, null);

    public Remover2Adapter(Context context, List<Remover2Data> list, Selectable.Selector selector) {
        this.mDataList = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public Remover2ItemHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new Remover2ItemHolder(getInflater().inflate(R.layout.remover2_menu_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(Remover2ItemHolder remover2ItemHolder, int i) {
        super.onBindViewHolder((Remover2Adapter) remover2ItemHolder, i);
        remover2ItemHolder.bind(this.mDataList.get(i), this.mDelegator.getSelection() == i);
        this.mDelegator.onBindViewHolder(remover2ItemHolder, i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<Remover2Data> list = this.mDataList;
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    public void setSelection(int i) {
        this.mDelegator.setSelection(i);
        notifyItemChanged(i);
        notifyItemChanged(this.mLastPosition);
        this.mLastPosition = i;
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
