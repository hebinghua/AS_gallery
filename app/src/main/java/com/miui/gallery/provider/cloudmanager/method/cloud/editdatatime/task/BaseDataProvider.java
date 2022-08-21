package com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task;

import android.content.Context;
import android.database.Cursor;
import com.miui.gallery.provider.cloudmanager.CursorTask2;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class BaseDataProvider extends CursorTask2 {
    public final long mDateTaken;
    public final String mLocalFile;
    public final long mLocalGroupId;
    public final String mLocation;
    public final long mServerId;
    public final String mSourcePkg;

    public BaseDataProvider(Context context, ArrayList<Long> arrayList, Cursor cursor) {
        super(context, arrayList);
        if (cursor == null || cursor.getCount() != 1) {
            throw new IllegalArgumentException();
        }
        cursor.moveToFirst();
        this.mDateTaken = cursor.getLong(cursor.getColumnIndexOrThrow("dateTaken"));
        this.mLocalFile = cursor.getString(cursor.getColumnIndexOrThrow("localFile"));
        this.mServerId = cursor.getLong(cursor.getColumnIndexOrThrow("serverId"));
        this.mLocalGroupId = cursor.getLong(cursor.getColumnIndexOrThrow("localGroupId"));
        this.mLocation = cursor.getString(cursor.getColumnIndexOrThrow("location"));
        this.mSourcePkg = cursor.getString(cursor.getColumnIndexOrThrow("source_pkg"));
    }
}
