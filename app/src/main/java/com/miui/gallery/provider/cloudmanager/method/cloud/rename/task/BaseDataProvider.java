package com.miui.gallery.provider.cloudmanager.method.cloud.rename.task;

import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.provider.cloudmanager.CursorTask2;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class BaseDataProvider extends CursorTask2 {
    public final String mFileName;
    public final long mGroupId;
    public final String mLocalFile;
    public final String mSourcePkg;
    public final String mThumbnailFile;

    public BaseDataProvider(Context context, ArrayList<Long> arrayList, SupportSQLiteDatabase supportSQLiteDatabase, Object[] objArr) {
        super(context, arrayList);
        Cursor query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(new String[]{"localFile", "thumbnailFile", "fileName", "groupId", "source_pkg"}).selection("_id=?", objArr).create());
        if (query != null) {
            try {
                if (query.getCount() == 1) {
                    query.moveToFirst();
                    this.mLocalFile = query.getString(query.getColumnIndexOrThrow("localFile"));
                    this.mThumbnailFile = query.getString(query.getColumnIndexOrThrow("thumbnailFile"));
                    this.mFileName = query.getString(query.getColumnIndexOrThrow("fileName"));
                    this.mGroupId = query.getLong(query.getColumnIndexOrThrow("groupId"));
                    this.mSourcePkg = query.getString(query.getColumnIndexOrThrow("source_pkg"));
                    query.close();
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
        throw new IllegalArgumentException();
    }
}
