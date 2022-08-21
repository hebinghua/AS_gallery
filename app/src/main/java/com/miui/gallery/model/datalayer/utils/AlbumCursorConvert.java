package com.miui.gallery.model.datalayer.utils;

import android.database.Cursor;
import com.miui.epoxy.common.CollectionUtils;
import com.miui.gallery.loader.CursorConvertCallback;
import com.miui.gallery.model.dto.Album;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class AlbumCursorConvert implements CursorConvertCallback<List<Album>> {
    public static AlbumCursorConvert getInstance() {
        return SingletonHolder.INSTANCE;
    }

    /* loaded from: classes2.dex */
    public static class SingletonHolder {
        public static final AlbumCursorConvert INSTANCE = new AlbumCursorConvert();
    }

    public AlbumCursorConvert() {
    }

    @Override // com.miui.gallery.loader.CursorConvertCallback
    /* renamed from: convert  reason: collision with other method in class */
    public List<Album> mo1129convert(Cursor cursor) {
        if (cursor != null && !cursor.isClosed() && cursor.getCount() > 0) {
            LinkedList linkedList = new LinkedList();
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Album fromCursor = Album.fromCursor(cursor);
                if (fromCursor != null) {
                    linkedList.add(fromCursor);
                }
                cursor.moveToNext();
            }
            return linkedList;
        }
        return CollectionUtils.emptyList();
    }
}
