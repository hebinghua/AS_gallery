package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.copy;

import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.provider.cloudmanager.Contracts;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.provider.cloudmanager.remark.RemarkManager;
import com.miui.gallery.provider.cloudmanager.remark.info.RemarkInfoFactory;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class CopyOwnerMediaToShareAlbum extends BaseCopy {
    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.BaseCopyAndMoveByMediaId
    public boolean isToShareAlbum() {
        return true;
    }

    public CopyOwnerMediaToShareAlbum(Context context, ArrayList<Long> arrayList, long j, long j2, Cursor cursor) {
        super(context, arrayList, j, j2, cursor);
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.copy.BaseCopy
    public void buildValues(boolean z) {
        this.mValues.putAll(Util.copyOf(Contracts.PUBLIC_COPYABLE_PROJECTION, this.mCursor));
        this.mValues.put("localFlag", Integer.valueOf(z ? -2 : 6));
        this.mValues.putAll(Util.copyOf(Contracts.PRIVATE_COPYABLE_PROJECTION, this.mCursor));
        this.mValues.put("localGroupId", Long.valueOf(ShareAlbumHelper.getOriginalAlbumId(this.mAlbumId)));
        this.mValues.put("localImageId", Long.valueOf(this.mMediaId));
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        supportSQLiteDatabase.beginTransactionNonExclusive();
        try {
            DefaultLogger.d("galleryAction_Method_AddToAlbum", "COPY (CopyOwnerMediaToShareAlbum) => inserts : %s", Util.desensitization(this.mValues));
            long insert = supportSQLiteDatabase.insert("shareImage", 0, this.mValues);
            DefaultLogger.d("galleryAction_Method_AddToAlbum", "COPY (CopyOwnerMediaToShareAlbum) => inserts finish id [%d]", Long.valueOf(insert));
            ShareMediaManager.getInstance().insert(insert, this.mValues);
            long convertToMediaId = ShareMediaManager.convertToMediaId(insert);
            if (convertToMediaId > 0) {
                markAsDirty(convertToMediaId);
                RemarkManager.remarkMediaId(RemarkInfoFactory.createCopyRemarkInfo(convertToMediaId, getFromFilePath(), getToFilePath()));
            }
            supportSQLiteDatabase.setTransactionSuccessful();
            return convertToMediaId;
        } finally {
            supportSQLiteDatabase.endTransaction();
        }
    }
}
