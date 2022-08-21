package com.miui.gallery.editor.photo.screen.mosaic;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.editor.photo.screen.mosaic.shader.MosaicData;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class MosaicAdapter extends Adapter<MosaicHolder> implements Selectable {
    public Context mContext;
    public Selectable.Delegator mDelegator;
    public List<MosaicData> mMosaicDataList;

    public MosaicAdapter(Context context, List<MosaicData> list, int i) {
        this.mContext = context;
        this.mMosaicDataList = new ArrayList(list);
        this.mDelegator = new Selectable.Delegator(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public MosaicHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MosaicHolder(this.mContext, viewGroup);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(MosaicHolder mosaicHolder, int i) {
        super.onBindViewHolder((MosaicAdapter) mosaicHolder, i);
        mosaicHolder.setIconPath(this.mMosaicDataList.get(i).iconPath, i);
        this.mDelegator.onBindViewHolder(mosaicHolder, i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<MosaicData> list = this.mMosaicDataList;
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
