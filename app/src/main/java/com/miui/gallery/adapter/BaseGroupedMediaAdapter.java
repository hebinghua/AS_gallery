package com.miui.gallery.adapter;

import android.content.Context;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.widget.MirrorFunctionHelper;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import com.miui.gallery.widget.recyclerview.GroupedItemAdapter;
import java.util.List;

/* loaded from: classes.dex */
public abstract class BaseGroupedMediaAdapter<M, S, GVH extends BaseViewHolder, CVH extends BaseViewHolder> extends BaseMediaSyncStateAdapter<M, S> implements GroupedItemAdapter<GVH, CVH> {
    public boolean mShowTimeLine;

    public abstract void doBindChildViewHolder(CVH cvh, int i, int i2, List<Object> list);

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public int getChildItemViewType(int i, int i2) {
        return 0;
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public long getGroupId(int i) {
        return i;
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public int getGroupItemViewType(int i) {
        return 0;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public /* bridge */ /* synthetic */ void onBindChildViewHolder(RecyclerView.ViewHolder viewHolder, int i, int i2, int i3, List list) {
        onBindChildViewHolder((BaseGroupedMediaAdapter<M, S, GVH, CVH>) ((BaseViewHolder) viewHolder), i, i2, i3, (List<Object>) list);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public /* bridge */ /* synthetic */ void onBindGroupViewHolder(RecyclerView.ViewHolder viewHolder, int i, int i2, List list) {
        onBindGroupViewHolder((BaseGroupedMediaAdapter<M, S, GVH, CVH>) ((BaseViewHolder) viewHolder), i, i2, (List<Object>) list);
    }

    public BaseGroupedMediaAdapter(Context context, SyncStateDisplay$DisplayScene syncStateDisplay$DisplayScene, int i) {
        super(context, syncStateDisplay$DisplayScene, i);
        this.mShowTimeLine = true;
        setHasStableIds(true);
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public boolean isFlatten() {
        return !this.mShowTimeLine;
    }

    public void setShowTimeLine(boolean z) {
        this.mShowTimeLine = z;
    }

    public boolean isShowTimeLine() {
        return this.mShowTimeLine;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    /* renamed from: onCreateViewHolder  reason: collision with other method in class */
    public final CVH mo1843onCreateViewHolder(ViewGroup viewGroup, int i) {
        throw new IllegalStateException("onCreateViewHolder should not be called directly.");
    }

    @Override // com.miui.gallery.adapter.BaseMediaAdapter
    public final void doBindViewHolder(BaseViewHolder baseViewHolder, int i) {
        throw new IllegalStateException("onBindViewHolder should not be called directly.");
    }

    @Override // com.miui.gallery.widget.recyclerview.grouped.GroupedItemAdapter
    public long getChildId(int i, int i2) {
        return getItemId(packDataPosition(i, i2));
    }

    public final void onBindGroupViewHolder(GVH gvh, int i, int i2, List<Object> list) {
        onBindGroupViewHolder(gvh, i, i2);
    }

    public final void onBindChildViewHolder(CVH cvh, int i, int i2, int i3, List<Object> list) {
        int packDataPosition = packDataPosition(i, i2);
        cvh.itemView.setTag(R.id.tag_item_unique_id, Long.valueOf(getItemKey(packDataPosition)));
        MirrorFunctionHelper.registerPCRightClick(cvh.itemView, getBestQualityPath(packDataPosition));
        doBindChildViewHolder(cvh, i, i2, i3, list);
    }

    public void doBindChildViewHolder(CVH cvh, int i, int i2, int i3, List<Object> list) {
        doBindChildViewHolder(cvh, packDataPosition(i, i2), i3, list);
    }
}
