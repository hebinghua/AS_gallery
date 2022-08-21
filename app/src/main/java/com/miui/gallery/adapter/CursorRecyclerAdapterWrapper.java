package com.miui.gallery.adapter;

import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter;
import com.miui.gallery.adapter.IBaseRecyclerAdapter;
import com.miui.gallery.adapter.ICursorAdapter;

/* JADX WARN: Incorrect field signature: TT; */
/* loaded from: classes.dex */
public abstract class CursorRecyclerAdapterWrapper<VH extends RecyclerView.ViewHolder, T extends RecyclerView.Adapter<VH> & ICursorAdapter & IBaseRecyclerAdapter<Cursor>> extends SimpleWrapperAdapter<VH> implements ICursorAdapter, IBaseRecyclerAdapter<Cursor> {
    public final RecyclerView.Adapter mWrapped;

    /* JADX WARN: Incorrect types in method signature: (TT;)V */
    public CursorRecyclerAdapterWrapper(RecyclerView.Adapter adapter) {
        super(adapter);
        this.mWrapped = adapter;
    }

    @Override // com.miui.gallery.adapter.ICursorAdapter
    public Cursor swapCursor(Cursor cursor) {
        return ((ICursorAdapter) this.mWrapped).swapCursor(cursor);
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return this.mWrapped.getItemId(i);
    }

    @Override // com.h6ah4i.android.widget.advrecyclerview.adapter.SimpleWrapperAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mWrapped.getItemCount();
    }

    @Override // com.miui.gallery.adapter.IBaseRecyclerAdapter
    /* renamed from: getItem */
    public Cursor mo1558getItem(int i) {
        return (Cursor) ((IBaseRecyclerAdapter) this.mWrapped).mo1558getItem(i);
    }

    @Override // com.miui.gallery.adapter.ICursorAdapter
    public Cursor getCursor() {
        return ((ICursorAdapter) this.mWrapped).getCursor();
    }
}
