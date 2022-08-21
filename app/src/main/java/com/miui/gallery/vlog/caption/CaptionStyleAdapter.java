package com.miui.gallery.vlog.caption;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.base.widget.Selectable$Delegator;
import com.miui.gallery.vlog.entity.CaptionStyleData;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.List;

/* loaded from: classes2.dex */
public class CaptionStyleAdapter extends Adapter<CaptionStyleHolder> {
    public Context mContext;
    public List<CaptionStyleData> mDataList;
    public Selectable$Delegator mDelegator = new Selectable$Delegator(0);
    public boolean mIsNightMode;

    public CaptionStyleAdapter(Context context, List<CaptionStyleData> list) {
        this.mContext = context;
        this.mDataList = list;
        boolean z = false;
        this.mIsNightMode = (context.getResources().getConfiguration().uiMode & 48) == 32 ? true : z;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public CaptionStyleHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new CaptionStyleHolder(getInflater().inflate(R$layout.vlog_caption_style_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(CaptionStyleHolder captionStyleHolder, int i) {
        super.onBindViewHolder((CaptionStyleAdapter) captionStyleHolder, i);
        CaptionStyleData captionStyleData = this.mDataList.get(i);
        captionStyleHolder.bind(this.mContext, captionStyleData, this.mIsNightMode);
        captionStyleHolder.setDownloadViewState(captionStyleData);
        if (captionStyleData.isDownloadSuccess()) {
            captionStyleData.setDownloadState(17);
        }
        this.mDelegator.onBindViewHolder(captionStyleHolder, i);
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mDataList.size();
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
    }

    public CaptionStyleData getItemData(int i) {
        List<CaptionStyleData> list = this.mDataList;
        if (list == null || i < 0 || i >= list.size()) {
            return null;
        }
        return this.mDataList.get(i);
    }
}
