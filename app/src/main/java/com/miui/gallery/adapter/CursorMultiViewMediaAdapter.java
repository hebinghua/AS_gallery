package com.miui.gallery.adapter;

import android.content.Context;
import android.database.Cursor;
import androidx.lifecycle.Lifecycle;
import com.miui.gallery.util.BurstFilterCursor;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CursorMultiViewMediaAdapter.kt */
/* loaded from: classes.dex */
public abstract class CursorMultiViewMediaAdapter extends MultiViewMediaAdapter<Cursor, Cursor> implements ICursorAdapter {
    public final Lazy adapterDelegate$delegate;

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CursorMultiViewMediaAdapter(Context context, SyncStateDisplay$DisplayScene scene, int i, Lifecycle lifecycle) {
        super(context, scene, i, lifecycle);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(scene, "scene");
        Intrinsics.checkNotNullParameter(lifecycle, "lifecycle");
        this.adapterDelegate$delegate = LazyKt__LazyJVMKt.lazy(LazyThreadSafetyMode.NONE, new CursorMultiViewMediaAdapter$adapterDelegate$2(this));
    }

    private final CursorAdapterDelegate getAdapterDelegate() {
        return (CursorAdapterDelegate) this.adapterDelegate$delegate.mo119getValue();
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public CursorMultiViewMediaAdapter(Context context, Lifecycle lifecycle) {
        this(context, SyncStateDisplay$DisplayScene.SCENE_NONE, lifecycle);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(lifecycle, "lifecycle");
    }

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public CursorMultiViewMediaAdapter(Context context, SyncStateDisplay$DisplayScene scene, Lifecycle lifecycle) {
        this(context, scene, 7, lifecycle);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(scene, "scene");
        Intrinsics.checkNotNullParameter(lifecycle, "lifecycle");
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return getAdapterDelegate().getItemId(i);
    }

    @Override // com.miui.gallery.adapter.BaseRecyclerAdapter, com.miui.gallery.adapter.IBaseRecyclerAdapter
    /* renamed from: getItem */
    public Cursor mo1558getItem(int i) {
        return getAdapterDelegate().getItem(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return getAdapterDelegate().getItemCount();
    }

    @Override // com.miui.gallery.adapter.ICursorAdapter
    public Cursor swapCursor(Cursor cursor) {
        return getAdapterDelegate().swapCursor(preSwapData(cursor));
    }

    @Override // com.miui.gallery.adapter.ICursorAdapter
    public Cursor getCursor() {
        return getAdapterDelegate().getCursor();
    }

    @Override // com.miui.gallery.adapter.GroupedMediaAdapter
    public Cursor processBursts(Cursor cursor) {
        return (!supportFoldBurstItems() || cursor == null || (cursor instanceof BurstFilterCursor)) ? cursor : new BurstFilterCursor(cursor);
    }

    @Override // com.miui.gallery.adapter.GroupedMediaAdapter
    public void processClusters(Cursor cursor, Cursor cursor2) {
        if (cursor != null) {
            this.mClusterAdapter.setViewMode(this.mViewMode);
            this.mClusterAdapter.swapData(cursor, cursor2 instanceof BurstFilterCursor ? ((BurstFilterCursor) cursor2).getResultPos() : null);
        }
        this.mHorizontalSpacing = this.mViewMode.getSpacing();
    }
}
