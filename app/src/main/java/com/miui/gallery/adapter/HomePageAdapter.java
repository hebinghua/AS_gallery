package com.miui.gallery.adapter;

import android.database.Cursor;
import com.miui.gallery.util.BurstFilterCursor;
import com.miui.gallery.widget.recyclerview.AbsSingleImageViewHolder;
import java.util.List;

/* loaded from: classes.dex */
public class HomePageAdapter extends CursorMultiViewMediaAdapter implements CheckableAdapter {
    public static String getDefaultThumbFilePath(Cursor cursor) {
        return BaseMediaAdapter.getMicroPath(cursor, 1, 5);
    }

    public static BurstFilterCursor wrapCursor(Cursor cursor) {
        if (cursor == null) {
            return null;
        }
        if (cursor instanceof BurstFilterCursor) {
            return (BurstFilterCursor) cursor;
        }
        return new BurstFilterCursor(cursor);
    }

    /* loaded from: classes.dex */
    public class SingleImageViewHolder extends AbsSingleImageViewHolder {
        @Override // com.miui.gallery.widget.recyclerview.AbsViewHolder
        public void bindData(int i, int i2, List<Object> list) {
            throw null;
        }
    }
}
