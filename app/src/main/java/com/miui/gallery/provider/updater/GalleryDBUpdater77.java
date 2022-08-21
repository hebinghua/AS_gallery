package com.miui.gallery.provider.updater;

import android.database.Cursor;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.model.PeopleContactInfo;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.util.SafeDBUtil;
import com.xiaomi.stat.a.j;
import java.util.Locale;

/* loaded from: classes2.dex */
public class GalleryDBUpdater77 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            refillBabyAlbumDataTaken(supportSQLiteDatabase);
        }
        if (!updateResult.isRecreateTablePeopleFace()) {
            refillRelationTypeOfPeople(supportSQLiteDatabase);
        }
        return UpdateResult.defaultResult();
    }

    public final void refillBabyAlbumDataTaken(final SupportSQLiteDatabase supportSQLiteDatabase) {
        SafeDBUtil.safeQuery(supportSQLiteDatabase, "cloud", new String[]{j.c}, String.format(Locale.US, " NOT ( %s is null )", "peopleId"), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Void>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater77.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public Void mo1808handle(Cursor cursor) {
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format(Locale.US, "update %s set %s = %d where %s = %d", "cloud", "dateTaken", -996, j.c, Integer.valueOf(cursor.getInt(0))));
                    }
                    return null;
                }
                return null;
            }
        });
    }

    public final void refillRelationTypeOfPeople(final SupportSQLiteDatabase supportSQLiteDatabase) {
        SafeDBUtil.safeQuery(supportSQLiteDatabase, "peopleFace", new String[]{j.c, "peopleContactJsonInfo"}, String.format(Locale.US, " NOT ( %s is null )", "peopleContactJsonInfo"), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Void>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater77.2
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public Void mo1808handle(Cursor cursor) {
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        int i = cursor.getInt(0);
                        PeopleContactInfo fromJson = PeopleContactInfo.fromJson(cursor.getString(1));
                        if (fromJson != null && !TextUtils.isEmpty(fromJson.relationWithMe)) {
                            GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format(Locale.US, "update %s set %s = %d where %s = %d", "peopleFace", "relationType", Integer.valueOf(fromJson.getRelationType()), j.c, Integer.valueOf(i)));
                        }
                    }
                    return null;
                }
                return null;
            }
        });
    }
}
