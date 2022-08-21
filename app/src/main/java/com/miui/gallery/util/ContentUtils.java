package com.miui.gallery.util;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import androidx.documentfile.provider.DocumentFile;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.SafeDBUtil;
import com.xiaomi.stat.MiStat;

/* loaded from: classes2.dex */
public class ContentUtils {
    public static String getValidFilePathForContentUri(Context context, Uri uri) {
        if (context == null || uri == null || !MiStat.Param.CONTENT.equals(uri.getScheme())) {
            return null;
        }
        String lastPathSegment = uri.getLastPathSegment();
        if (isValidFile(context, lastPathSegment)) {
            return lastPathSegment;
        }
        String str = (String) SafeDBUtil.safeQuery(context, uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null, new SafeDBUtil.QueryHandler<String>() { // from class: com.miui.gallery.util.ContentUtils.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public String mo1808handle(Cursor cursor) {
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                return cursor.getString(0);
            }
        });
        if (!isValidFile(context, str)) {
            return null;
        }
        return str;
    }

    public static boolean isValidFile(Context context, String str) {
        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(str, IStoragePermissionStrategy.Permission.QUERY, FileHandleRecordHelper.appendInvokerTag("ContentUtils", "isValidFile"));
        return documentFile != null && documentFile.exists() && StorageUtils.isInExternalStorage(context, str);
    }
}
