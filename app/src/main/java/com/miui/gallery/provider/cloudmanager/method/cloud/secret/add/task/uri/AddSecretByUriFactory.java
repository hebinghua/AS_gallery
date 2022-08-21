package com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.uri;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import androidx.sqlite.db.SupportSQLiteDatabase;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.provider.cloudmanager.CursorTask2;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.AddSecretById2;
import com.miui.gallery.util.MediaStoreUtils;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class AddSecretByUriFactory {
    public static CursorTask2 create(Context context, ArrayList<Long> arrayList, Uri uri, SupportSQLiteDatabase supportSQLiteDatabase) {
        String path;
        if (uri == null) {
            throw new IllegalArgumentException();
        }
        String scheme = uri.getScheme();
        scheme.hashCode();
        if (scheme.equals(Action.FILE_ATTRIBUTE)) {
            path = uri.getPath();
        } else if (scheme.equals(MiStat.Param.CONTENT)) {
            path = queryFilePathWithContentUri(uri);
        } else {
            throw new IllegalArgumentException();
        }
        if (path == null) {
            throw new IllegalArgumentException();
        }
        Cursor queryCloudItemByFilePath = Util.queryCloudItemByFilePath(context, supportSQLiteDatabase, path);
        if (queryCloudItemByFilePath != null) {
            try {
                if (queryCloudItemByFilePath.moveToFirst() && queryCloudItemByFilePath.getCount() == 1) {
                    AddSecretById2 addSecretById2 = new AddSecretById2(context, arrayList, supportSQLiteDatabase, queryCloudItemByFilePath.getLong(0));
                    queryCloudItemByFilePath.close();
                    return addSecretById2;
                }
            } catch (Throwable th) {
                if (queryCloudItemByFilePath != null) {
                    try {
                        queryCloudItemByFilePath.close();
                    } catch (Throwable th2) {
                        th.addSuppressed(th2);
                    }
                }
                throw th;
            }
        }
        AddSecretByPath2 addSecretByPath2 = new AddSecretByPath2(context, arrayList, path);
        if (queryCloudItemByFilePath != null) {
            queryCloudItemByFilePath.close();
        }
        return addSecretByPath2;
    }

    public static String queryFilePathWithContentUri(Uri uri) {
        if (uri == null) {
            throw new IllegalArgumentException();
        }
        String authority = uri.getAuthority();
        authority.hashCode();
        if (authority.equals("media")) {
            return MediaStoreUtils.getMediaFilePath(uri.toString());
        }
        throw new IllegalArgumentException();
    }
}
