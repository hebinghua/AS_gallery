package com.miui.gallery.vlog.clip;

import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.editor.R$layout;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.vlog.base.widget.Selectable$Delegator;
import com.miui.gallery.vlog.entity.TransData;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class TransViewAdapter extends Adapter<TransViewHolder> {
    public Selectable$Delegator mDelegator = new Selectable$Delegator(-1);
    public ArrayList<TransData> mEffects;

    public TransViewAdapter(ArrayList<TransData> arrayList) {
        this.mEffects = arrayList;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public TransViewHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new TransViewHolder(getInflater().inflate(R$layout.common_frame_menu_item, viewGroup, false));
    }

    public ArrayList<TransData> getEffects() {
        return this.mEffects;
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(TransViewHolder transViewHolder, int i) {
        super.onBindViewHolder((TransViewAdapter) transViewHolder, i);
        TransData transData = this.mEffects.get(i);
        transViewHolder.setIcon(transData);
        transViewHolder.setName(transData.getNameResId());
        if (transData.isDownloadSuccess()) {
            transData.setDownloadState(17);
        }
        this.mDelegator.onBindViewHolder(transViewHolder, i);
    }

    public TransData getItemData(int i) {
        ArrayList<TransData> arrayList = this.mEffects;
        if (arrayList == null || i < 0 || i >= arrayList.size()) {
            return null;
        }
        return this.mEffects.get(i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        if (BaseMiscUtil.isValid(this.mEffects)) {
            return this.mEffects.size();
        }
        return 0;
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

    public int getSelection() {
        Selectable$Delegator selectable$Delegator = this.mDelegator;
        if (selectable$Delegator != null) {
            return selectable$Delegator.getSelection();
        }
        return -1;
    }
}
