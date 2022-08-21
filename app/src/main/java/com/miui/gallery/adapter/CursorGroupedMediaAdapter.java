package com.miui.gallery.adapter;

import android.content.Context;
import android.database.Cursor;
import com.miui.gallery.widget.recyclerview.BaseViewHolder;
import kotlin.Lazy;
import kotlin.LazyKt__LazyJVMKt;
import kotlin.LazyThreadSafetyMode;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CursorGroupedMediaAdapter.kt */
/* loaded from: classes.dex */
public abstract class CursorGroupedMediaAdapter extends BaseGroupedMediaAdapter<Cursor, Cursor, BaseViewHolder, BaseViewHolder> implements ICursorAdapter {
    public final Lazy adapterDelegate$delegate;

    /* JADX WARN: 'this' call moved to the top of the method (can break code semantics) */
    public CursorGroupedMediaAdapter(Context context) {
        this(context, null, 0, 6, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ CursorGroupedMediaAdapter(Context context, SyncStateDisplay$DisplayScene syncStateDisplay$DisplayScene, int i, int i2, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i2 & 2) != 0 ? SyncStateDisplay$DisplayScene.SCENE_NONE : syncStateDisplay$DisplayScene, (i2 & 4) != 0 ? 7 : i);
    }

    /* JADX WARN: 'super' call moved to the top of the method (can break code semantics) */
    public CursorGroupedMediaAdapter(Context context, SyncStateDisplay$DisplayScene scene, int i) {
        super(context, scene, i);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(scene, "scene");
        this.adapterDelegate$delegate = LazyKt__LazyJVMKt.lazy(LazyThreadSafetyMode.NONE, new CursorGroupedMediaAdapter$adapterDelegate$2(this));
    }

    public final CursorAdapterDelegate getAdapterDelegate() {
        return (CursorAdapterDelegate) this.adapterDelegate$delegate.mo119getValue();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return getAdapterDelegate().getItemId(i);
    }

    @Override // com.miui.gallery.adapter.BaseRecyclerAdapter, com.miui.gallery.adapter.IBaseRecyclerAdapter
    /* renamed from: getItem */
    public Cursor mo1558getItem(int i) {
        Cursor item = getAdapterDelegate().getItem(i);
        Intrinsics.checkNotNull(item);
        return item;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return getAdapterDelegate().getItemCount();
    }

    @Override // com.miui.gallery.adapter.ICursorAdapter
    public Cursor swapCursor(Cursor cursor) {
        return getAdapterDelegate().swapCursor(cursor);
    }

    @Override // com.miui.gallery.adapter.ICursorAdapter
    public Cursor getCursor() {
        return getAdapterDelegate().getCursor();
    }
}
