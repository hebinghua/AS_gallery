package com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.Contracts;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class LogicBranch1 extends LogicBranch {
    public LogicBranch1(Context context, ArrayList<Long> arrayList, long j, long j2, long j3, IDataProvider iDataProvider) {
        super(context, arrayList, j, j2, j3, iDataProvider);
    }

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        this.mContentValues.put("localFlag", (Integer) 5);
        SupportSQLiteQueryBuilder builder = SupportSQLiteQueryBuilder.builder("cloud");
        String[] strArr = Contracts.PUBLIC_COPYABLE_PROJECTION;
        SupportSQLiteQueryBuilder columns = builder.columns(strArr);
        Cursor query = supportSQLiteDatabase.query(columns.selection("_id=" + this.mMediaId, null).create());
        if (query != null) {
            try {
                boolean z = true;
                if (query.getCount() == 1) {
                    query.moveToFirst();
                    this.mContentValues.putAll(Util.copyOf(strArr, query));
                    query.close();
                    ContentValues contentValues = this.mContentValues;
                    if (this.mMediaConflictValidation != -105) {
                        z = false;
                    }
                    addFilePath(contentValues, z);
                    this.mContentValues.put("fromLocalGroupId", Long.valueOf(this.mLocalGroupId));
                    this.mContentValues.put("localGroupId", Long.valueOf(this.mAlbumId));
                    this.mContentValues.put("localImageId", Long.valueOf(this.mMediaId));
                    return;
                }
            } catch (Throwable th) {
                if (query != null) {
                    try {
                        query.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        throw new IllegalArgumentException("copy error");
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.LogicBranch, com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        super.execute(supportSQLiteDatabase, mediaManager);
        ContentValues contentValues = new ContentValues();
        contentValues.put("localFlag", (Integer) 11);
        long insert = supportSQLiteDatabase.insert("cloud", 0, this.mContentValues);
        DefaultLogger.d("galleryAction_removeSecret1", "executeDB => insert %s ; id = [%d]", Util.desensitization(this.mContentValues), Long.valueOf(insert));
        if (insert > 0) {
            mediaManager.insert(insert, this.mContentValues);
            int update = supportSQLiteDatabase.update("cloud", 0, contentValues, "_id=?", new String[]{String.valueOf(this.mMediaId)});
            DefaultLogger.d("galleryAction_removeSecret1", "executeDB update => %s ; count = [%d]", Util.desensitization(contentValues), Integer.valueOf(update));
            if (update > 0) {
                mediaManager.delete("_id=?", new String[]{String.valueOf(this.mMediaId)});
            }
            markAsDirty(insert);
        }
        return insert;
    }
}
