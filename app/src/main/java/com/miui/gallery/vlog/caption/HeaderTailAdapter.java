package com.miui.gallery.vlog.caption;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.vlog.R$layout;
import com.miui.gallery.vlog.base.widget.Selectable$Delegator;
import com.miui.gallery.vlog.entity.HeaderTailData;
import com.miui.gallery.widget.recyclerview.Adapter;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class HeaderTailAdapter extends Adapter<HeaderTailHolder> {
    public Context mContext;
    public ArrayList<HeaderTailData> mDataList;
    public OnItemSelectListener mOnItemSelectListener;
    public int mLastSelectedPos = -1;
    public Selectable$Delegator mDelegator = new Selectable$Delegator(0);

    /* loaded from: classes2.dex */
    public interface OnItemSelectListener {
        void itemSelected(int i);
    }

    public HeaderTailAdapter(Context context, ArrayList<HeaderTailData> arrayList) {
        this.mContext = context;
        this.mDataList = arrayList;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public HeaderTailHolder mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new HeaderTailHolder(getInflater().inflate(R$layout.vlog_header_tail_item, viewGroup, false));
    }

    @Override // com.miui.gallery.widget.recyclerview.Adapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(HeaderTailHolder headerTailHolder, int i) {
        super.onBindViewHolder((HeaderTailAdapter) headerTailHolder, i);
        boolean z = i == this.mDelegator.getSelection();
        HeaderTailData headerTailData = this.mDataList.get(i);
        headerTailHolder.bind(this.mDataList.get(i));
        headerTailHolder.setIcon(headerTailData.getIconUrl(), headerTailData.getIconResId(), headerTailData.getBgColor());
        headerTailHolder.setState(z);
        headerTailHolder.setDownloadViewState(headerTailData);
        if (headerTailData.isDownloadSuccess()) {
            headerTailData.setDownloadState(17);
        }
        this.mDelegator.onBindViewHolder(headerTailHolder, i);
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

    public HeaderTailData getItemData(int i) {
        ArrayList<HeaderTailData> arrayList = this.mDataList;
        if (arrayList == null || i < 0 || i >= arrayList.size()) {
            return null;
        }
        return this.mDataList.get(i);
    }

    public ArrayList<HeaderTailData> getDataList() {
        return this.mDataList;
    }

    public void setSelection(int i) {
        this.mDelegator.setSelection(i);
        notifyItemChanged(i);
        notifyItemChanged(this.mLastSelectedPos);
        this.mLastSelectedPos = i;
        OnItemSelectListener onItemSelectListener = this.mOnItemSelectListener;
        if (onItemSelectListener != null) {
            onItemSelectListener.itemSelected(i);
        }
    }

    public int getSelection() {
        return this.mDelegator.getSelection();
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.mOnItemSelectListener = onItemSelectListener;
    }
}
