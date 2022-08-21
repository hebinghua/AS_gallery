package com.miui.gallery.vlog.ratio;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.editor.R$layout;
import com.miui.gallery.vlog.base.widget.Selectable$Delegator;
import com.miui.gallery.vlog.entity.RatioData;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class RatioAdapter extends Adapter<RatioHolder> {
    public List<RatioData> mDataList;
    public Selectable$Delegator mDelegator;

    public RatioAdapter(List<RatioData> list) {
        ArrayList arrayList = new ArrayList();
        this.mDataList = arrayList;
        arrayList.addAll(list);
        this.mDelegator = new Selectable$Delegator(-1);
    }

    public void updateData(List<RatioData> list) {
        this.mDataList.clear();
        this.mDataList.addAll(list);
        notifyDataSetChanged();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public RatioHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new RatioHolder(getInflater().inflate(R$layout.common_frame_menu_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RatioHolder ratioHolder, int i) {
        super.onBindViewHolder((RatioAdapter) ratioHolder, i);
        ratioHolder.bind(this.mDataList.get(i));
        this.mDelegator.onBindViewHolder(ratioHolder, i);
        ratioHolder.itemView.setContentDescription(this.mDataList.get(i).getTalkbackName());
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mDataList.size();
    }

    public RatioData getItemData(int i) {
        List<RatioData> list = this.mDataList;
        if (list == null || i < 0 || i >= list.size()) {
            return null;
        }
        return this.mDataList.get(i);
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

    public void setSelection(int i) {
        Selectable$Delegator selectable$Delegator = this.mDelegator;
        if (selectable$Delegator == null || i < 0) {
            return;
        }
        selectable$Delegator.setSelection(i);
    }
}
