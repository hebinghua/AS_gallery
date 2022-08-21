package com.miui.gallery.editor.photo.app.doodle;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.editor.photo.core.common.model.DoodleData;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class DoodleAdapter extends Adapter<DoodleHolder> implements Selectable {
    public Context mContext;
    public List<DoodleData> mDataList;
    public Selectable.Delegator mDelegator = new Selectable.Delegator(0);

    public DoodleAdapter(Context context, List<DoodleData> list) {
        this.mContext = context;
        this.mDataList = new ArrayList(list);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public DoodleHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new DoodleHolder(this.mContext, viewGroup);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(DoodleHolder doodleHolder, int i) {
        super.onBindViewHolder((DoodleAdapter) doodleHolder, i);
        doodleHolder.setIconPath(i != getSelection() ? this.mDataList.get(i).normal : this.mDataList.get(i).selected, i, i == getSelection());
        doodleHolder.itemView.setContentDescription(this.mContext.getResources().getString(this.mDataList.get(i).talkback));
        this.mDelegator.onBindViewHolder(doodleHolder, i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<DoodleData> list = this.mDataList;
        if (list == null) {
            return 0;
        }
        return list.size();
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
