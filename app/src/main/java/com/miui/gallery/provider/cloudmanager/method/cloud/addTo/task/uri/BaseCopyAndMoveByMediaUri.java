package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.uri;

import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.CheckPostProcessing;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class BaseCopyAndMoveByMediaUri extends BaseDataProvider {
    public long mAlbumId;
    public String mTarPath;

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
    }

    public BaseCopyAndMoveByMediaUri(Context context, ArrayList<Long> arrayList, String str, Cursor cursor, long j) {
        super(context, arrayList, cursor);
        this.mTarPath = str;
        this.mAlbumId = j;
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long verify(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        long run = new CheckPostProcessing(this.mContext, this.mTarPath).run(supportSQLiteDatabase, null);
        return run == -111 ? run : "recovery".equalsIgnoreCase(this.mServerStatus) ? -115L : -1L;
    }
}
