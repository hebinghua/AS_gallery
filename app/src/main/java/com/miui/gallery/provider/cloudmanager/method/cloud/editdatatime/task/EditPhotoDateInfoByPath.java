package com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import java.util.ArrayList;
import java.util.Locale;

/* loaded from: classes2.dex */
public class EditPhotoDateInfoByPath extends EditPhotoDateInfoById {
    public final Cursor mCursor;
    public String mSourceFilePath;

    public EditPhotoDateInfoByPath(Context context, ArrayList<Long> arrayList, long j, String str, boolean z, Cursor cursor, Bundle bundle) {
        super(context, arrayList, -1L, j, z, cursor, bundle);
        this.mCursor = cursor;
        this.mSourceFilePath = str;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.EditPhotoDateInfoById, com.miui.gallery.provider.cloudmanager.CursorTask2
    public long verify(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        long verify = super.verify(supportSQLiteDatabase, mediaManager);
        return verify == -1 ? this.mCursor.getCount() <= 1 ? -1L : -103L : verify;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task.EditPhotoDateInfoById
    public String toString() {
        return String.format(Locale.US, "updatePhotoDateTime by path: [%s]", this.mSourceFilePath);
    }
}
