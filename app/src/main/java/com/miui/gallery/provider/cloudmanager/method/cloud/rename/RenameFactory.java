package com.miui.gallery.provider.cloudmanager.method.cloud.rename;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.CursorTask2;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.provider.cloudmanager.method.cloud.rename.task.RenameById2;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;

/* loaded from: classes2.dex */
public class RenameFactory {
    public static CursorTask2 create(Context context, ArrayList<Long> arrayList, Bundle bundle, String str, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        if (bundle.getInt("operation_type") == 2) {
            return new RenameById2(context, arrayList, bundle.getLong("src_cloud_id"), str, supportSQLiteDatabase, mediaManager);
        }
        return new RenameById2(context, arrayList, queryId(context, supportSQLiteDatabase, bundle.getString("extra_src_path")), str, supportSQLiteDatabase, mediaManager);
    }

    public static long queryId(Context context, SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        long j = 0;
        Cursor cursor = null;
        try {
            try {
                cursor = Util.queryCloudItemByFilePath(context, supportSQLiteDatabase, str);
                if (cursor != null && cursor.moveToFirst()) {
                    if (!(cursor.getExtras() != null && cursor.getExtras().getBoolean("is_thumbnail", false)) && cursor.getLong(2) != BaseFileUtils.getFileSize(str)) {
                        DefaultLogger.e("galleryAction_Method_RenameMethod", "file size not equals, can not rename: %s", str);
                        return 0L;
                    }
                    j = cursor.getLong(0);
                }
            } catch (Exception e) {
                DefaultLogger.e("galleryAction_Method_RenameMethod", e);
            }
            return j;
        } finally {
            BaseMiscUtil.closeSilently(null);
        }
    }
}
