package com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id;

import android.content.Context;
import android.database.Cursor;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.provider.cloudmanager.CursorTask2;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class BaseDataProvider extends CursorTask2 implements IDataProvider {
    public final String mFileName;
    public final String mLocalFile;
    public final int mLocalFlag;
    public final long mLocalGroupId;
    public final String mMicroThumbnailFile;
    public final String mServerId;
    public final String mServerStatus;
    public final int mServerType;
    public final String mSha1;
    public final String mThumbnailFile;

    public BaseDataProvider(Context context, ArrayList<Long> arrayList, SupportSQLiteDatabase supportSQLiteDatabase, Object[] objArr) {
        super(context, arrayList);
        Cursor query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(new String[]{"localFlag", "localGroupId", "sha1", "fileName", "serverType", "microthumbfile", "thumbnailFile", "localFile", "serverId", "serverStatus"}).selection("_id=?", objArr).create());
        if (query != null) {
            try {
                if (query.getCount() == 1) {
                    query.moveToFirst();
                    this.mLocalFlag = query.getInt(query.getColumnIndexOrThrow("localFlag"));
                    this.mLocalGroupId = query.getLong(query.getColumnIndexOrThrow("localGroupId"));
                    this.mSha1 = query.getString(query.getColumnIndexOrThrow("sha1"));
                    this.mFileName = query.getString(query.getColumnIndexOrThrow("fileName"));
                    this.mServerType = query.getInt(query.getColumnIndexOrThrow("serverType"));
                    this.mMicroThumbnailFile = query.getString(query.getColumnIndexOrThrow("microthumbfile"));
                    this.mThumbnailFile = query.getString(query.getColumnIndexOrThrow("thumbnailFile"));
                    this.mLocalFile = query.getString(query.getColumnIndexOrThrow("localFile"));
                    this.mServerId = query.getString(query.getColumnIndexOrThrow("serverId"));
                    this.mServerStatus = query.getString(query.getColumnIndexOrThrow("serverStatus"));
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

    public int getLocalFlag() {
        return this.mLocalFlag;
    }

    public long getLocalGroupId() {
        return this.mLocalGroupId;
    }

    public String getSha1() {
        return this.mSha1;
    }

    public String getFileName() {
        return this.mFileName;
    }

    public int getServerType() {
        return this.mServerType;
    }

    public String getMicroThumbnailFile() {
        return this.mMicroThumbnailFile;
    }

    public String getThumbnailFile() {
        return this.mThumbnailFile;
    }

    public String getLocalFile() {
        return this.mLocalFile;
    }

    public String getServerId() {
        return this.mServerId;
    }

    public String getServerStatus() {
        return this.mServerStatus;
    }
}
