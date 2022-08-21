package com.miui.gallery.provider.cloudmanager.method.cloud.editdatatime.task;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import ch.qos.logback.core.CoreConstants;
import com.miui.gallery.provider.cloudmanager.CursorTask2;
import com.miui.gallery.util.BaseFileUtils;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Locale;

/* loaded from: classes2.dex */
public class EditDateTimeFactory {
    public static final String[] COLUMNS = {"dateTaken", "localFile", "serverId", "localGroupId", "location", "source_pkg"};

    public static CursorTask2 create(Context context, SupportSQLiteDatabase supportSQLiteDatabase, ArrayList<Long> arrayList, Bundle bundle, Bundle bundle2) {
        int i = bundle.getInt("update_photo_by");
        long j = bundle.getLong("newtime");
        boolean z = bundle.getBoolean("is_favorites");
        if (i == 1) {
            long j2 = bundle.getLong("photo_id");
            return new EditPhotoDateInfoById(context, arrayList, j2, j, z, queryById(supportSQLiteDatabase, j2), bundle2);
        } else if (i == 2) {
            String string = bundle.getString("photo_path");
            return new EditPhotoDateInfoByPath(context, arrayList, j, string, z, queryByPath(supportSQLiteDatabase, string), bundle2);
        } else {
            throw new IllegalArgumentException("type error!");
        }
    }

    public static Cursor queryById(SupportSQLiteDatabase supportSQLiteDatabase, long j) {
        return supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(COLUMNS).selection(getIdSelection(j), null).create());
    }

    public static Cursor queryByPath(SupportSQLiteDatabase supportSQLiteDatabase, String str) {
        return supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(COLUMNS).selection(String.format(Locale.US, "%s like '%s%c' AND %s <> %s", "localFile", BaseFileUtils.getFileName(str), Character.valueOf(CoreConstants.PERCENT_CHAR), "localFile", 15), null).create());
    }

    public static String getIdSelection(long j) {
        return String.format(Locale.US, "%s = %d AND %s <> %s", j.c, Long.valueOf(j), "localFlag", 15);
    }
}
