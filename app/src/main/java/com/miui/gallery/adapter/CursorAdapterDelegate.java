package com.miui.gallery.adapter;

import android.database.Cursor;
import androidx.recyclerview.widget.RecyclerView;
import com.xiaomi.stat.a.j;
import kotlin.jvm.internal.Intrinsics;

/* compiled from: CursorAdapterDelegate.kt */
/* loaded from: classes.dex */
public final class CursorAdapterDelegate implements ICursorAdapter {
    public Cursor cursor;
    public final RecyclerView.Adapter<?> host;
    public boolean isDataValid;
    public int rowIDColumn;

    public CursorAdapterDelegate(RecyclerView.Adapter<?> host) {
        Intrinsics.checkNotNullParameter(host, "host");
        this.host = host;
    }

    public long getItemId(int i) {
        Cursor cursor;
        if (!this.isDataValid || (cursor = this.cursor) == null) {
            return -1L;
        }
        Intrinsics.checkNotNull(cursor);
        if (!cursor.moveToPosition(i)) {
            return -1L;
        }
        Cursor cursor2 = this.cursor;
        Intrinsics.checkNotNull(cursor2);
        return cursor2.getLong(this.rowIDColumn);
    }

    public int getItemCount() {
        Cursor cursor;
        if (!this.isDataValid || (cursor = this.cursor) == null) {
            return 0;
        }
        Intrinsics.checkNotNull(cursor);
        return cursor.getCount();
    }

    public Cursor getItem(int i) {
        Cursor cursor;
        if (!this.isDataValid || (cursor = this.cursor) == null) {
            return null;
        }
        Intrinsics.checkNotNull(cursor);
        cursor.moveToPosition(i);
        return this.cursor;
    }

    @Override // com.miui.gallery.adapter.ICursorAdapter
    public Cursor swapCursor(Cursor cursor) {
        Cursor cursor2 = this.cursor;
        if (cursor == cursor2) {
            return null;
        }
        this.cursor = cursor;
        if (cursor != null) {
            this.isDataValid = true;
            this.rowIDColumn = cursor.getColumnIndexOrThrow(j.c);
            this.host.notifyDataSetChanged();
        } else {
            this.isDataValid = false;
            this.rowIDColumn = -1;
            this.host.notifyDataSetChanged();
        }
        return cursor2;
    }

    @Override // com.miui.gallery.adapter.ICursorAdapter
    public Cursor getCursor() {
        return this.cursor;
    }
}
