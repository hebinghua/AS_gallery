package com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id;

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
    public final byte[] mSecretKey;
    public final String mServerId;
    public final int mServerType;
    public final String mSha1;
    public final String mThumbnailFile;
    public final String mTitle;

    public BaseDataProvider(Context context, ArrayList<Long> arrayList, SupportSQLiteDatabase supportSQLiteDatabase, Object[] objArr) {
        super(context, arrayList);
        Cursor query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(new String[]{"fileName", "sha1", "localFlag", "serverId", "localGroupId", "localFile", "thumbnailFile", "microthumbfile", "secretKey", "serverType", "title"}).selection("_id=?", objArr).create());
        if (query != null) {
            try {
                if (query.getCount() == 1) {
                    query.moveToFirst();
                    this.mFileName = query.getString(query.getColumnIndexOrThrow("fileName"));
                    this.mSha1 = query.getString(query.getColumnIndexOrThrow("sha1"));
                    this.mLocalFlag = query.getInt(query.getColumnIndexOrThrow("localFlag"));
                    this.mServerId = query.getString(query.getColumnIndexOrThrow("serverId"));
                    this.mLocalGroupId = query.getLong(query.getColumnIndexOrThrow("localGroupId"));
                    this.mLocalFile = query.getString(query.getColumnIndexOrThrow("localFile"));
                    this.mThumbnailFile = query.getString(query.getColumnIndexOrThrow("thumbnailFile"));
                    this.mMicroThumbnailFile = query.getString(query.getColumnIndexOrThrow("microthumbfile"));
                    this.mSecretKey = query.getBlob(query.getColumnIndexOrThrow("secretKey"));
                    this.mServerType = query.getInt(query.getColumnIndexOrThrow("serverType"));
                    this.mTitle = query.getString(query.getColumnIndexOrThrow("title"));
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

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public String getFileName() {
        return this.mFileName;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public String getSha1() {
        return this.mSha1;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public int getLocalFlag() {
        return this.mLocalFlag;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public String getServerId() {
        return this.mServerId;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public long getLocalGroupId() {
        return this.mLocalGroupId;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public String getLocalFile() {
        return this.mLocalFile;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public String getThumbnailFile() {
        return this.mThumbnailFile;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public String getMicroThumbnailFile() {
        return this.mMicroThumbnailFile;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public byte[] getSecretKey() {
        return this.mSecretKey;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public int getServerType() {
        return this.mServerType;
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.cloud.secret.remove.task.id.IDataProvider
    public String getTitle() {
        return this.mTitle;
    }
}
