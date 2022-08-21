package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.uri;

import android.content.Context;
import android.database.Cursor;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.miui.gallery.provider.cloudmanager.CursorTask2;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public abstract class BaseDataProvider extends CursorTask2 {
    public final long mCloudId;
    public final String mServerStatus;
    public final String mSha1;
    public final int mSize;

    public BaseDataProvider(Context context, ArrayList<Long> arrayList, Cursor cursor) {
        super(context, arrayList);
        if (cursor == null || cursor.getCount() != 1) {
            throw new IllegalArgumentException();
        }
        cursor.moveToFirst();
        this.mCloudId = cursor.getLong(cursor.getColumnIndexOrThrow(j.c));
        this.mSha1 = cursor.getString(cursor.getColumnIndexOrThrow("sha1"));
        this.mSize = cursor.getInt(cursor.getColumnIndexOrThrow(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE));
        this.mServerStatus = cursor.getString(cursor.getColumnIndexOrThrow("serverStatus"));
    }
}
