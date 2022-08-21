package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.MediaConflict;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.b.h;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class BaseCopyAndMoveByMediaId extends BaseDataProvider {
    public long mAlbumId;
    public Cursor mCursor;
    public long mMediaId;
    public ContentValues mValues;

    public abstract boolean isToShareAlbum();

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getFileName() {
        return super.getFileName();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.IDataProvider
    public /* bridge */ /* synthetic */ long getGroupId() {
        return super.getGroupId();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getLocalFile() {
        return super.getLocalFile();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.IDataProvider
    public /* bridge */ /* synthetic */ int getLocalFlag() {
        return super.getLocalFlag();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.IDataProvider
    public /* bridge */ /* synthetic */ long getServerId() {
        return super.getServerId();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getServerStatus() {
        return super.getServerStatus();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getSha1() {
        return super.getSha1();
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.BaseDataProvider, com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.IDataProvider
    public /* bridge */ /* synthetic */ String getThumbnailFile() {
        return super.getThumbnailFile();
    }

    public BaseCopyAndMoveByMediaId(Context context, ArrayList<Long> arrayList, long j, long j2, Cursor cursor) {
        super(context, arrayList, cursor);
        this.mMediaId = j;
        this.mAlbumId = j2;
        this.mCursor = cursor;
        this.mValues = new ContentValues();
    }

    public void modifierNameCheck(SupportSQLiteDatabase supportSQLiteDatabase, boolean z) {
        if (this.mValidation == -3) {
            String generatedNewName = Util.generatedNewName(this.mFileName, this.mSha1);
            long verifyNewFileName = MediaConflict.verifyNewFileName(generatedNewName, this.mAlbumId, supportSQLiteDatabase);
            this.mValidation = verifyNewFileName;
            if (verifyNewFileName == -120) {
                generatedNewName = Util.generatedNewNameWithUUID(generatedNewName);
            }
            DefaultLogger.d("galleryAction_Method_AddToAlbum", "new fileName: %s", generatedNewName);
            this.mValues.put("fileName", generatedNewName);
            if (!generatedNewName.contains(".")) {
                return;
            }
            String substring = generatedNewName.substring(0, generatedNewName.lastIndexOf("."));
            DefaultLogger.d("galleryAction_Method_AddToAlbum", "new title: %s", substring);
            this.mValues.put("title", substring);
        }
    }

    public String getFromFilePath() {
        String str = this.mLocalFile;
        return TextUtils.isEmpty(str) ? this.mThumbnailFile : str;
    }

    public String getToFilePath() {
        String queryAlbumPathByAlbumId = AlbumDataHelper.queryAlbumPathByAlbumId(this.mContext, this.mAlbumId, isToShareAlbum());
        if (TextUtils.isEmpty(queryAlbumPathByAlbumId)) {
            return null;
        }
        String asString = this.mValues.getAsString("fileName");
        if (TextUtils.isEmpty(asString)) {
            asString = this.mFileName;
        }
        if (TextUtils.isEmpty(asString)) {
            return null;
        }
        return queryAlbumPathByAlbumId + h.g + asString;
    }

    public String getFinalFileName() {
        ContentValues contentValues = this.mValues;
        if (contentValues == null) {
            return this.mFileName;
        }
        String asString = contentValues.getAsString("fileName");
        return TextUtils.isEmpty(asString) ? this.mFileName : asString;
    }

    @Override // com.miui.gallery.provider.cloudmanager.CursorTask2
    public void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        DefaultLogger.d("galleryAction_Method_AddToAlbum", "%s{%s => %s}", getClass().getSimpleName(), getFromFilePath(), getToFilePath());
    }
}
