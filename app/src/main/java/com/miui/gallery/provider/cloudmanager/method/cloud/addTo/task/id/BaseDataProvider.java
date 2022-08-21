package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id;

import android.content.Context;
import android.database.Cursor;
import com.miui.gallery.provider.cloudmanager.CursorTask2;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class BaseDataProvider extends CursorTask2 implements IDataProvider {
    public final String mFileName;
    public final long mGroupId;
    public final String mLocalFile;
    public final int mLocalFlag;
    public final long mServerId;
    public final String mServerStatus;
    public final String mSha1;
    public final String mThumbnailFile;

    public BaseDataProvider(Context context, ArrayList<Long> arrayList, Cursor cursor) {
        super(context, arrayList);
        if (cursor == null || cursor.getCount() != 1) {
            throw new IllegalArgumentException();
        }
        cursor.moveToFirst();
        this.mServerId = cursor.getLong(cursor.getColumnIndexOrThrow("serverId"));
        this.mLocalFile = cursor.getString(cursor.getColumnIndexOrThrow("localFile"));
        this.mThumbnailFile = cursor.getString(cursor.getColumnIndexOrThrow("thumbnailFile"));
        this.mServerStatus = cursor.getString(cursor.getColumnIndexOrThrow("serverStatus"));
        this.mFileName = cursor.getString(cursor.getColumnIndexOrThrow("fileName"));
        this.mSha1 = cursor.getString(cursor.getColumnIndexOrThrow("sha1"));
        this.mLocalFlag = cursor.getInt(cursor.getColumnIndexOrThrow("localFlag"));
        this.mGroupId = cursor.getLong(cursor.getColumnIndexOrThrow("groupId"));
    }

    public long getServerId() {
        return this.mServerId;
    }

    public String getLocalFile() {
        return this.mLocalFile;
    }

    public String getThumbnailFile() {
        return this.mThumbnailFile;
    }

    public String getServerStatus() {
        return this.mServerStatus;
    }

    public String getFileName() {
        return this.mFileName;
    }

    public String getSha1() {
        return this.mSha1;
    }

    public int getLocalFlag() {
        return this.mLocalFlag;
    }

    public long getGroupId() {
        return this.mGroupId;
    }
}
