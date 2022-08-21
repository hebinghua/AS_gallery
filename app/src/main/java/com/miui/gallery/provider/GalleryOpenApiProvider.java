package com.miui.gallery.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import com.miui.gallery.card.scenario.DateUtils;
import com.miui.gallery.preference.BaseGalleryPreferences;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.ui.AIAlbumStatusHelper;
import com.miui.gallery.util.SafeDBUtil;
import java.util.Locale;

/* loaded from: classes2.dex */
public class GalleryOpenApiProvider extends ContentProvider {
    public static final UriMatcher sUriMatcher;

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }

    static {
        UriMatcher uriMatcher = new UriMatcher(-1);
        sUriMatcher = uriMatcher;
        uriMatcher.addURI("com.miui.gallery.open_api", "check_thumbnail", 1);
        uriMatcher.addURI("com.miui.gallery.open_api", "search_status", 2);
        uriMatcher.addURI("com.miui.gallery.open_api", "secret_album", 3);
        uriMatcher.addURI("com.miui.gallery.open_api", "check_cta", 4);
        uriMatcher.addURI("com.miui.gallery.open_api", "backup_strategy", 6);
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        switch (sUriMatcher.match(uri)) {
            case 1:
                return CheckThumbnailHelper.checkThumbnail(getContext(), strArr2, uri.getBooleanQueryParameter("strict_mode", false));
            case 2:
                int openApiSearchStatus = AIAlbumStatusHelper.getOpenApiSearchStatus();
                MatrixCursor matrixCursor = new MatrixCursor(new String[]{"status"}, 1);
                matrixCursor.addRow(new Object[]{Integer.valueOf(openApiSearchStatus)});
                return matrixCursor;
            case 3:
                int secretAlbumUnsyncedCount = getSecretAlbumUnsyncedCount();
                MatrixCursor matrixCursor2 = new MatrixCursor(new String[]{"unsynced_count"}, 1);
                matrixCursor2.addRow(new Object[]{Integer.valueOf(secretAlbumUnsyncedCount)});
                return matrixCursor2;
            case 4:
                MatrixCursor matrixCursor3 = new MatrixCursor(new String[]{"is_accept"}, 1);
                matrixCursor3.addRow(new Object[]{Boolean.valueOf(BaseGalleryPreferences.CTA.canConnectNetwork())});
                return matrixCursor3;
            case 5:
                int increaseMediaInCurDay = getIncreaseMediaInCurDay(getContext());
                MatrixCursor matrixCursor4 = new MatrixCursor(new String[]{"cur_day_count"}, 1);
                matrixCursor4.addRow(new Object[]{Integer.valueOf(increaseMediaInCurDay)});
                return matrixCursor4;
            case 6:
                MatrixCursor matrixCursor5 = new MatrixCursor(new String[]{"is_upgrade"}, 1);
                matrixCursor5.addRow(new Object[]{Boolean.TRUE});
                return matrixCursor5;
            default:
                return null;
        }
    }

    public final int getSecretAlbumUnsyncedCount() {
        Integer num = (Integer) SafeDBUtil.safeQuery(getContext(), GalleryContract.Cloud.CLOUD_URI, new String[]{"count(_id)"}, String.format(Locale.US, "(%s) AND (%s) AND (%s = %d OR %s = %d)", com.miui.gallery.cloud.CloudUtils.SELECTION_OWNER_NEED_SYNC, "(localGroupId=-1000)", "localFlag", 7, "localFlag", 8), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Integer>() { // from class: com.miui.gallery.provider.GalleryOpenApiProvider.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public Integer mo1808handle(Cursor cursor) {
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                return Integer.valueOf(cursor.getInt(0));
            }
        });
        if (num != null) {
            return num.intValue();
        }
        return 0;
    }

    public static int getIncreaseMediaInCurDay(Context context) {
        if (context == null) {
            return 0;
        }
        long currentTimeMillis = System.currentTimeMillis();
        Integer num = (Integer) SafeDBUtil.safeQuery(context, GalleryContract.Cloud.CLOUD_URI, new String[]{"count(_id)"}, String.format(Locale.US, "(%s) AND (%s) AND %s >= %d AND %s <= %d", "serverType IN (1,2)", "(localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery')))", "mixedDateTime", Long.valueOf(DateUtils.getDateTime(currentTimeMillis)), "mixedDateTime", Long.valueOf(currentTimeMillis)), (String[]) null, (String) null, new SafeDBUtil.QueryHandler<Integer>() { // from class: com.miui.gallery.provider.GalleryOpenApiProvider.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // com.miui.gallery.util.SafeDBUtil.QueryHandler
            /* renamed from: handle */
            public Integer mo1808handle(Cursor cursor) {
                if (cursor == null || !cursor.moveToFirst()) {
                    return null;
                }
                return Integer.valueOf(cursor.getInt(0));
            }
        });
        if (num == null) {
            return 0;
        }
        return num.intValue();
    }
}
