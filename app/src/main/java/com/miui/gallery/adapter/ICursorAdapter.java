package com.miui.gallery.adapter;

import android.database.Cursor;

/* compiled from: ICursorAdapter.kt */
/* loaded from: classes.dex */
public interface ICursorAdapter {
    Cursor getCursor();

    Cursor swapCursor(Cursor cursor);

    default void changeCursor(Cursor cursor) {
        Cursor swapCursor = swapCursor(cursor);
        if (swapCursor == null) {
            return;
        }
        swapCursor.close();
    }
}
