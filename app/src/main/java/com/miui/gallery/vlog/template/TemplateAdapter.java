package com.miui.gallery.vlog.template;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.widget.Selectable$Delegator;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class TemplateAdapter extends Adapter<TemplateHolder> {
    public Context mContext;
    public List<TemplateResource> mDataList;
    public Selectable$Delegator mDelegator;
    public int mLastPosition;

    public TemplateAdapter(Context context, List<TemplateResource> list) {
        ArrayList arrayList = new ArrayList();
        this.mDataList = arrayList;
        this.mLastPosition = 0;
        this.mContext = context;
        arrayList.addAll(list);
        this.mDelegator = new Selectable$Delegator(0);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public TemplateHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new TemplateHolder(getInflater().inflate(R$layout.vlog_template_menu_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(TemplateHolder templateHolder, int i) {
        super.onBindViewHolder((TemplateAdapter) templateHolder, i);
        TemplateResource templateResource = this.mDataList.get(i);
        if (templateResource.isDownloadSuccess()) {
            templateResource.setDownloadState(17);
        }
        boolean z = i == this.mDelegator.getSelection();
        templateHolder.setTitleViewState(templateResource);
        templateHolder.setDownloadViewState(templateResource);
        templateHolder.setName(templateResource);
        templateHolder.setIcon(templateResource, z);
        this.mDelegator.onBindViewHolder(templateHolder, i);
        if (i == 0) {
            templateHolder.itemView.setContentDescription(this.mContext.getResources().getString(R$string.vlog_talkback_template_none));
        }
    }

    public TemplateResource getItem(int i) {
        return this.mDataList.get(i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        List<TemplateResource> list = this.mDataList;
        if (list == null) {
            return 0;
        }
        return list.size();
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
        this.mDelegator.setSelection(i);
        notifyItemChanged(i);
        notifyItemChanged(this.mLastPosition);
        this.mLastPosition = i;
    }

    public int getSelection() {
        return this.mDelegator.getSelection();
    }
}
