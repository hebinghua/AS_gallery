package com.miui.gallery.provider.updater;

import android.database.Cursor;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.provider.GalleryDBHelper;
import com.miui.gallery.storage.constants.StorageConstants;
import com.miui.gallery.util.ExtraTextUtils;
import com.miui.gallery.util.SafeDBUtil;
import com.miui.gallery.util.deprecated.Preference;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.Locale;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class GalleryDBUpdater74 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(final SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        if (!updateResult.isRecreateTableCloud()) {
            return UpdateResult.defaultResult();
        }
        SafeDBUtil.safeQuery(supportSQLiteDatabase, "cloud", new String[]{j.c, "description", "fileName", "appKey", "serverId"}, String.format(Locale.US, "%s=%s AND (%s=%d OR %s=%d OR (%s=%d AND %s='%s')) AND %s IS NULL AND %s != %d", "serverType", 0, "localFlag", 8, "localFlag", 10, "localFlag", 0, "serverStatus", "custom", "localFile", "serverId", 1L), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Void>() { // from class: com.miui.gallery.provider.updater.GalleryDBUpdater74.1
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle  reason: collision with other method in class */
            public Void mo1808handle(Cursor cursor) {
                boolean z;
                String str;
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        int i = cursor.getInt(0);
                        String string = cursor.getString(1);
                        String string2 = cursor.getString(2);
                        String string3 = cursor.getString(3);
                        String string4 = cursor.getString(4);
                        String localFileFromDescription = GalleryDBUpdater74.getLocalFileFromDescription(string);
                        if (TextUtils.isEmpty(localFileFromDescription)) {
                            localFileFromDescription = AlbumDataHelper.getOwnerAlbumLocalFile(string2, string3);
                        }
                        Boolean autoUploadAttributeFromDescription = AlbumDataHelper.getAutoUploadAttributeFromDescription(string);
                        if (String.valueOf(2L).equals(string4)) {
                            String screenshotsLocalPath = AlbumDataHelper.getScreenshotsLocalPath();
                            if (autoUploadAttributeFromDescription == null) {
                                autoUploadAttributeFromDescription = Boolean.valueOf(Preference.sGetIsScreenShotAutoUploadOpen());
                            }
                            str = screenshotsLocalPath;
                            z = true;
                        } else {
                            if (TextUtils.isEmpty(string3)) {
                                boolean startsWithIgnoreCase = ExtraTextUtils.startsWithIgnoreCase(localFileFromDescription, StorageConstants.RELATIVE_DIRECTORY_GALLERY_ALBUM);
                                if (autoUploadAttributeFromDescription == null) {
                                    autoUploadAttributeFromDescription = Boolean.valueOf(startsWithIgnoreCase);
                                }
                            } else if (autoUploadAttributeFromDescription == null) {
                                autoUploadAttributeFromDescription = Boolean.valueOf(Preference.sGetIsAlbumAutoUploadOpen(string3));
                            }
                            z = false;
                            str = localFileFromDescription;
                        }
                        GalleryDBUpdater74.this.refillLocalFileAndAttributes(supportSQLiteDatabase, i, str, autoUploadAttributeFromDescription.booleanValue(), z);
                    }
                    return null;
                }
                return null;
            }
        });
        return UpdateResult.defaultResult();
    }

    public final void refillLocalFileAndAttributes(SupportSQLiteDatabase supportSQLiteDatabase, int i, String str, boolean z, boolean z2) {
        String transformToEditedColumnsElement = GalleryCloudUtils.transformToEditedColumnsElement(6);
        Locale locale = Locale.US;
        Object[] objArr = new Object[15];
        objArr[0] = "cloud";
        objArr[1] = "localFile";
        objArr[2] = str;
        objArr[3] = "attributes";
        objArr[4] = "attributes";
        objArr[5] = 5L;
        long j = 0;
        objArr[6] = Long.valueOf(z ? 1L : 0L);
        if (z2) {
            j = 4;
        }
        objArr[7] = Long.valueOf(j);
        objArr[8] = "editedColumns";
        objArr[9] = "editedColumns";
        objArr[10] = transformToEditedColumnsElement;
        objArr[11] = transformToEditedColumnsElement;
        objArr[12] = transformToEditedColumnsElement;
        objArr[13] = j.c;
        objArr[14] = Integer.valueOf(i);
        GalleryDBHelper.safeExecSQL(supportSQLiteDatabase, String.format(locale, "update %s set %s='%s', %s=((%s & ~%d) | %d | %d), %s=coalesce(replace(%s, '%s', '') || '%s', '%s') where %s=%d", objArr));
    }

    public static String getLocalFileFromDescription(String str) {
        if (!TextUtils.isEmpty(str)) {
            try {
                return new JSONObject(str).optString("localFile");
            } catch (Exception e) {
                DefaultLogger.w("GalleryDBUpdater74", e);
                return null;
            }
        }
        return null;
    }
}
