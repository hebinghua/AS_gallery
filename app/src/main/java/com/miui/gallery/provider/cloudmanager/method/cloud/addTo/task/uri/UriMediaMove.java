package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.uri;

import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.CopyAndMoveByIdFactory;
import com.miui.gallery.scanner.core.ScanContracts$ScanResultReason;
import com.miui.gallery.scanner.core.model.SaveParams;
import com.miui.gallery.scanner.core.task.eventual.ScanResult;
import com.miui.gallery.scanner.utils.SaveToCloudUtil;
import java.io.File;
import java.util.ArrayList;
import java.util.Locale;

/* loaded from: classes2.dex */
public class UriMediaMove extends BaseCopyAndMoveByMediaUri {
    public UriMediaMove(Context context, ArrayList<Long> arrayList, String str, Cursor cursor, long j) {
        super(context, arrayList, str, cursor, j);
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws Exception {
        if (this.mCloudId > 0) {
            return CopyAndMoveByIdFactory.create(1, this.mContext, getDirtyBulk(), this.mCloudId, this.mAlbumId, supportSQLiteDatabase).run(supportSQLiteDatabase, mediaManager);
        }
        ScanResult saveToCloudDB = SaveToCloudUtil.saveToCloudDB(this.mContext.getApplicationContext(), new SaveParams.Builder().setSaveFile(new File(this.mTarPath)).setAlbumId(this.mAlbumId).setLocalFlag(8).setCredible(true).build());
        long mediaId = saveToCloudDB.getMediaId();
        if (mediaId <= 0) {
            return saveToCloudDB.getReasonCode() == ScanContracts$ScanResultReason.ALREADY_EXISTS ? -103L : -101L;
        }
        markAsDirty(mediaId);
        return mediaId;
    }

    public String toString() {
        return String.format(Locale.US, "Move URI => uri [%s] albumId [%d]", this.mTarPath, Long.valueOf(this.mAlbumId));
    }
}
