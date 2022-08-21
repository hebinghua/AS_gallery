package com.miui.gallery.provider.updater;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.util.Pair;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.model.dto.utils.AlbumDataHelper;
import com.miui.gallery.preference.GalleryPreferences;
import com.miui.gallery.provider.updater.UpdateResult;
import com.miui.gallery.ui.album.main.utils.AlbumConfigSharedPreferences;
import com.xiaomi.mirror.synergy.CallMethod;
import java.util.List;
import org.json.JSONException;
import org.json.JSONObject;

/* loaded from: classes2.dex */
public class GalleryDBUpdater103 extends GalleryDBUpdater {
    @Override // com.miui.gallery.provider.updater.GalleryDBUpdater
    public UpdateResult doUpdate(SupportSQLiteDatabase supportSQLiteDatabase, UpdateResult updateResult) {
        upgrade(supportSQLiteDatabase, updateResult.isRecreateTableShareAlbum());
        return new UpdateResult.Builder().recreateTableAlbum().build();
    }

    /* JADX WARN: Removed duplicated region for block: B:102:0x02fc  */
    /* JADX WARN: Removed duplicated region for block: B:107:0x0309  */
    /* JADX WARN: Removed duplicated region for block: B:74:0x02bd  */
    /* JADX WARN: Removed duplicated region for block: B:81:0x02c7  */
    /* JADX WARN: Removed duplicated region for block: B:84:0x02cf  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final void upgrade(androidx.sqlite.db.SupportSQLiteDatabase r30, boolean r31) {
        /*
            Method dump skipped, instructions count: 784
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.updater.GalleryDBUpdater103.upgrade(androidx.sqlite.db.SupportSQLiteDatabase, boolean):void");
    }

    public final void setFixedAlbumSortInfo(List<Pair<Long, Long>> list) {
        if (list == null) {
            return;
        }
        SharedPreferences.Editor edit = AlbumConfigSharedPreferences.getInstance().edit();
        for (Pair<Long, Long> pair : list) {
            long longValue = ((Long) pair.first).longValue();
            if (longValue == 2147483639) {
                edit.putString(GalleryPreferences.PrefKeys.SORT_POSITION_AI_ALBUM_INDEX, String.valueOf(pair.second));
                edit.putBoolean(GalleryPreferences.PrefKeys.IS_USER_MANUAL_SETTER_SORT_POSITION_AI_ALBUM_INDEX, true);
            } else if (longValue == 2147483641) {
                edit.putString(GalleryPreferences.PrefKeys.SORT_POSITION_OTHER_ALBUM_INDEX, String.valueOf(pair.second));
                edit.putBoolean(GalleryPreferences.PrefKeys.IS_USER_MANUAL_SETTER_SORT_POSITION_OTHER_ALBUM_INDEX, true);
            } else if (longValue == 2147483638) {
                edit.putString(GalleryPreferences.PrefKeys.SORT_POSITION_TRASH_ALBUM_INDEX, String.valueOf(pair.second));
                edit.putBoolean(GalleryPreferences.PrefKeys.IS_USER_MANUAL_SETTER_SORT_POSITION_TRASH_ALBUM_INDEX, true);
            }
        }
        edit.apply();
    }

    public final long convertAlbumToTableFieldContents(Context context, Cursor cursor, ContentValues contentValues) {
        if (cursor == null || contentValues == null) {
            return -1L;
        }
        String string = cursor.getString(0);
        long j = cursor.getLong(1);
        long j2 = cursor.getLong(2);
        long j3 = cursor.getLong(3);
        double d = cursor.getDouble(4);
        int i = cursor.getInt(5);
        String string2 = cursor.getString(6);
        long j4 = cursor.getLong(16);
        String string3 = cursor.getString(12);
        String string4 = cursor.getString(13);
        String string5 = cursor.getString(14);
        ContentValues insertAlbumContentValue = AlbumDataHelper.getInsertAlbumContentValue(context, string, j2, j3, string2, i, String.valueOf(d));
        if (AlbumDataHelper.getCameraLocalPath().equalsIgnoreCase(string2) || AlbumDataHelper.getScreenshotsLocalPath().equalsIgnoreCase(string2)) {
            insertAlbumContentValue.remove("sortInfo");
            insertAlbumContentValue.put("sortInfo", String.valueOf(d));
            insertAlbumContentValue.put("sort_position", Double.valueOf(d));
        } else {
            insertAlbumContentValue.put("sort_position", Double.valueOf(d));
        }
        contentValues.putAll(insertAlbumContentValue);
        contentValues.put("attributes", Long.valueOf(j));
        contentValues.put("serverTag", string4);
        contentValues.put("serverStatus", string5);
        contentValues.put("serverId", string3);
        contentValues.put("realDateModified", Long.valueOf(j4));
        String string6 = cursor.getString(7);
        String string7 = cursor.getString(8);
        String string8 = cursor.getString(9);
        String string9 = cursor.getString(10);
        String string10 = cursor.getString(11);
        JSONObject jSONObject = new JSONObject();
        if (string6 != null) {
            try {
                jSONObject.put("thumbnailInfo", string6);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        if (string7 != null) {
            jSONObject.put("description", string7);
        }
        if (string8 != null) {
            jSONObject.put("peopleId", string8);
        }
        if (string9 != null) {
            jSONObject.put("appKey", string9);
        }
        if (string10 != null) {
            jSONObject.put("babyInfoJson", string10);
        }
        contentValues.put(CallMethod.ARG_EXTRA_STRING, jSONObject.toString());
        return cursor.getLong(15);
    }
}
