package com.miui.gallery.loader;

import android.database.Cursor;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.model.dto.AlbumList;

/* loaded from: classes2.dex */
public class AlbumConvertCallback implements CursorConvertCallback<AlbumList> {
    /* JADX WARN: Can't rename method to resolve collision */
    @Override // com.miui.gallery.loader.CursorConvertCallback
    /* renamed from: convert */
    public AlbumList mo1129convert(Cursor cursor) {
        if (cursor == null || cursor.isClosed() || cursor.getCount() <= 0) {
            return null;
        }
        AlbumList albumList = new AlbumList(cursor.getCount());
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Album fromCursor = Album.fromCursor(cursor);
            if (fromCursor != null) {
                albumList.add(fromCursor);
            }
            cursor.moveToNext();
        }
        return albumList;
    }
}
