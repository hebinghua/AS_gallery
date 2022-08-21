package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.uri;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.provider.cloudmanager.CursorTask2;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.util.GalleryUtils;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class CopyAndMoveByUriFactory {
    public static CursorTask2 create(int i, Context context, ArrayList<Long> arrayList, long j, Uri uri, SupportSQLiteDatabase supportSQLiteDatabase) {
        boolean z = i == 0;
        String queryPath = queryPath(uri);
        if (TextUtils.isEmpty(queryPath)) {
            throw new IllegalArgumentException("path is null");
        }
        Cursor queryCloudItemByFilePath = Util.queryCloudItemByFilePath(context, supportSQLiteDatabase, queryPath);
        if (z) {
            return new UriMediaCopy(context, arrayList, queryPath, queryCloudItemByFilePath, j);
        }
        return new UriMediaMove(context, arrayList, queryPath, queryCloudItemByFilePath, j);
    }

    public static String queryPath(Uri uri) {
        if (MiStat.Param.CONTENT.equals(uri.getScheme())) {
            return (String) GalleryUtils.safeQuery(uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null, CopyAndMoveByUriFactory$$ExternalSyntheticLambda0.INSTANCE);
        }
        if (!Action.FILE_ATTRIBUTE.equals(uri.getScheme())) {
            return null;
        }
        return uri.getPath();
    }

    public static /* synthetic */ String lambda$queryPath$0(Cursor cursor) {
        if (cursor == null || !cursor.moveToFirst()) {
            return null;
        }
        return cursor.getString(0);
    }
}
