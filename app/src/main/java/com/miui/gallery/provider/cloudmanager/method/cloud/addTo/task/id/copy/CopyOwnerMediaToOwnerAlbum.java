package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.copy;

import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.Contracts;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.provider.cloudmanager.remark.RemarkManager;
import com.miui.gallery.provider.cloudmanager.remark.info.RemarkInfoFactory;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class CopyOwnerMediaToOwnerAlbum extends BaseCopy {
    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.BaseCopyAndMoveByMediaId
    public boolean isToShareAlbum() {
        return false;
    }

    public CopyOwnerMediaToOwnerAlbum(Context context, ArrayList<Long> arrayList, long j, long j2, Cursor cursor) {
        super(context, arrayList, j, j2, cursor);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.copy.BaseCopy
    public void buildValues(boolean z) {
        this.mValues.putAll(Util.copyOf(Contracts.PUBLIC_COPYABLE_PROJECTION, this.mCursor));
        this.mValues.put("localFlag", Integer.valueOf(z ? -2 : 6));
        this.mValues.putAll(Util.copyOf(Contracts.PRIVATE_COPYABLE_PROJECTION, this.mCursor));
        this.mValues.put("localGroupId", Long.valueOf(this.mAlbumId));
        this.mValues.put("localImageId", Long.valueOf(this.mMediaId));
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        supportSQLiteDatabase.beginTransactionNonExclusive();
        try {
            DefaultLogger.d("galleryAction_Method_AddToAlbum", "COPY (CopyOwnerMediaToOwnerAlbum) => inserts : %s", Util.desensitization(this.mValues));
            long insert = supportSQLiteDatabase.insert("cloud", 0, this.mValues);
            DefaultLogger.d("galleryAction_Method_AddToAlbum", "COPY (CopyOwnerMediaToOwnerAlbum) => inserts finish id [%d]", Long.valueOf(insert));
            mediaManager.insert(insert, this.mValues);
            if (insert > 0) {
                markAsDirty(insert);
                RemarkManager.remarkMediaId(RemarkInfoFactory.createCopyRemarkInfo(insert, getFromFilePath(), getToFilePath()));
            }
            supportSQLiteDatabase.setTransactionSuccessful();
            return insert;
        } finally {
            supportSQLiteDatabase.endTransaction();
        }
    }
}
