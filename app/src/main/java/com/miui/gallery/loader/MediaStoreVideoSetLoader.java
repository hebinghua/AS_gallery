package com.miui.gallery.loader;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import ch.qos.logback.core.FileAppender;
import com.miui.gallery.loader.MediaSetLoader;
import com.miui.gallery.model.BaseDataItem;
import com.miui.gallery.model.CursorDataSet;
import com.miui.gallery.util.BaseFileMimeUtil;
import com.miui.gallery.util.BaseFileUtils;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.stat.a.j;

/* loaded from: classes2.dex */
public class MediaStoreVideoSetLoader extends MediaSetLoader {
    @Override // com.miui.gallery.loader.CursorSetLoader
    public String getTAG() {
        return "MediaStoreVideoSetLoader";
    }

    public MediaStoreVideoSetLoader(Context context, Uri uri, Bundle bundle, boolean z) {
        super(context, uri, bundle, z);
    }

    @Override // com.miui.gallery.loader.MediaSetLoader
    public Uri getContentUri(boolean z) {
        return MediaStore.Video.Media.getContentUri(z ? "internal" : "external");
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public String[] getProjection() {
        return new String[]{j.c, "_data", "_size", "mime_type", "datetaken", "duration", nexExportFormat.TAG_FORMAT_WIDTH, nexExportFormat.TAG_FORMAT_HEIGHT, "orientation"};
    }

    @Override // com.miui.gallery.loader.CursorSetLoader
    public CursorDataSet wrapDataSet(Cursor cursor) {
        MediaVideoDataSet mediaVideoDataSet = new MediaVideoDataSet(cursor);
        printLog(mediaVideoDataSet);
        return mediaVideoDataSet;
    }

    /* loaded from: classes2.dex */
    public class MediaVideoDataSet extends MediaSetLoader.MediaDataSet {
        @Override // com.miui.gallery.model.CursorDataSet
        public int burstKeyIndex() {
            return 1;
        }

        public MediaVideoDataSet(Cursor cursor) {
            super(cursor);
        }

        @Override // com.miui.gallery.loader.MediaSetLoader.MediaDataSet
        public void wrapItemByCursor(BaseDataItem baseDataItem, Cursor cursor) {
            String string = cursor.getString(1);
            baseDataItem.setKey(cursor.getLong(0)).setFilePath(string).setThumbPath(string).setTitle(BaseFileUtils.getFileTitle(BaseFileUtils.getFileName(string))).setSize(cursor.getLong(2)).setMimeType(cursor.getString(3)).setCreateTime(cursor.getLong(4)).setDuration(cursor.getInt(5) / 1000).setWidth(cursor.getInt(6)).setHeight(cursor.getInt(7)).mo1096setOrientation(cursor.getInt(8));
            if (BaseFileMimeUtil.isRawFromMimeType(baseDataItem.getMimeType())) {
                baseDataItem.setSpecialTypeFlags(FileAppender.DEFAULT_BUFFER_SIZE);
            }
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public long getItemKey(int i) {
            if (isValidate(i)) {
                this.mCursor.moveToPosition(i);
                return this.mCursor.getLong(0);
            }
            return -1L;
        }

        @Override // com.miui.gallery.model.BaseDataSet
        public String getItemPath(int i) {
            if (isValidate(i)) {
                this.mCursor.moveToPosition(i);
                return this.mCursor.getString(1);
            }
            return null;
        }
    }
}
