package com.miui.gallery.provider.updater;

import android.database.Cursor;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SafeDBUtil;
import com.xiaomi.stat.a.j;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes2.dex */
public class GalleryDBUpdater107 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        String[] strArr = {j.c};
        List list = (List) SafeDBUtil.safeQuery(supportSQLiteDatabase, "album", strArr, "localPath like '" + StorageConstants.RELATIVE_DIRECTORY_GALLERY_ALBUM + "%' AND localFlag=7", (String[]) null, (String) null, new SafeDBUtil.QueryHandler<List<Long>>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater107.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public List<Long> mo1808handle(Cursor cursor) {
                if (cursor == null || cursor.getCount() <= 0) {
                    return Collections.emptyList();
                }
                LinkedList linkedList = new LinkedList();
                while (cursor.moveToNext()) {
                    linkedList.add(Long.valueOf(cursor.getLong(0)));
                }
                return linkedList;
            }
        });
        if (!BaseMiscUtil.isValid(list)) {
            return UpdateResult.defaultResult();
        }
        GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format("DELETE FROM %s WHERE %s IN (%s)", "album", j.c, TextUtils.join(", ", list)));
        GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format("DELETE FROM %s WHERE %s IN (%s)", "cloud", "localGroupId", TextUtils.join(", ", list)));
        return UpdateResult.defaultResult();
    }
}
