package com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.ShareAlbumHelper;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.provider.cloudmanager.Contracts;
import com.miui.gallery.provider.cloudmanager.CursorTask2;
import com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.copy.CopyOwnerMediaToOwnerAlbum;
import com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.copy.CopyOwnerMediaToShareAlbum;
import com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.copy.CopyShareMediaToOwnerAlbum;
import com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.copy.CopyShareMediaToShareAlbum;
import com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.move.MoveFromCloudAlbum;
import com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.move.MoveFromLocalAlbum;
import com.miui.gallery.provider.cloudmanager.method.cloud.addTo.task.id.move.MoveFromSystemAlbum;
import com.miui.gallery.util.BaseMiscUtil;
import java.util.ArrayList;
import java.util.Locale;

/* loaded from: classes2.dex */
public class CopyAndMoveByIdFactory {
    public static CursorTask2 create(int i, Context context, ArrayList<Long> arrayList, long j, long j2, SupportSQLiteDatabase supportSQLiteDatabase) {
        boolean isOtherShareMediaId = ShareMediaManager.isOtherShareMediaId(j);
        boolean isOtherShareAlbumId = ShareAlbumHelper.isOtherShareAlbumId(j2);
        boolean z = true;
        if (i == 0) {
            PreCheckResult preCheckResult = new PreCheckResult();
            if (isOtherShareMediaId && isOtherShareAlbumId) {
                preCheck(j, supportSQLiteDatabase, true, preCheckResult);
                return new CopyShareMediaToShareAlbum(context, arrayList, preCheckResult.getMediaId(), j2, preCheckResult.getCursor());
            } else if (isOtherShareAlbumId) {
                preCheck(j, supportSQLiteDatabase, false, preCheckResult);
                return new CopyOwnerMediaToShareAlbum(context, arrayList, preCheckResult.getMediaId(), j2, preCheckResult.getCursor());
            } else if (isOtherShareMediaId) {
                preCheck(j, supportSQLiteDatabase, true, preCheckResult);
                return new CopyShareMediaToOwnerAlbum(context, arrayList, preCheckResult.getMediaId(), j2, preCheckResult.getCursor());
            } else {
                preCheck(j, supportSQLiteDatabase, false, preCheckResult);
                return new CopyOwnerMediaToOwnerAlbum(context, arrayList, preCheckResult.getMediaId(), j2, preCheckResult.getCursor());
            }
        }
        Cursor queryShareItem = isOtherShareMediaId ? queryShareItem(supportSQLiteDatabase, j) : queryCloudItem(supportSQLiteDatabase, j);
        if (queryShareItem == null || !queryShareItem.moveToFirst()) {
            throw new IllegalArgumentException(String.format(Locale.US, "query media [%d] error!", Long.valueOf(j)));
        }
        if (queryShareItem.getInt(queryShareItem.getColumnIndex("localFlag")) == 0) {
            z = false;
        }
        if (Album.isSystemAlbum(String.valueOf(queryShareItem.getInt(queryShareItem.getColumnIndex("groupId"))))) {
            return new MoveFromSystemAlbum(context, arrayList, j, j2, queryShareItem);
        }
        if (z) {
            return new MoveFromLocalAlbum(context, arrayList, j, j2, queryShareItem);
        }
        return new MoveFromCloudAlbum(context, arrayList, j, j2, queryShareItem);
    }

    /* loaded from: classes2.dex */
    public static class PreCheckResult {
        public Cursor cursor;
        public long mediaId;

        public PreCheckResult() {
        }

        public Cursor getCursor() {
            return this.cursor;
        }

        public void setCursor(Cursor cursor) {
            this.cursor = cursor;
        }

        public long getMediaId() {
            return this.mediaId;
        }

        public void setMediaId(long j) {
            this.mediaId = j;
        }
    }

    public static void preCheck(long j, SupportSQLiteDatabase supportSQLiteDatabase, boolean z, PreCheckResult preCheckResult) {
        Cursor queryShareItem = z ? queryShareItem(supportSQLiteDatabase, j) : queryCloudItem(supportSQLiteDatabase, j);
        if (queryShareItem == null || !queryShareItem.moveToFirst()) {
            throw new IllegalArgumentException(String.format(Locale.US, "query media [%d] error!", Long.valueOf(j)));
        }
        int i = queryShareItem.getInt(queryShareItem.getColumnIndex("localFlag"));
        if (!((i == 0 || i == 5 || i == 6 || i == 9) ? false : true) && i != 0) {
            String string = queryShareItem.getString(queryShareItem.getColumnIndex("localImageId"));
            if (TextUtils.isEmpty(string)) {
                throw new IllegalArgumentException(String.format(Locale.US, "not sync media [%d] and localImageId is empty!", Long.valueOf(j)));
            }
            long parseLong = Long.parseLong(string);
            if (i == 9) {
                parseLong = ShareMediaManager.convertToMediaId(parseLong);
            }
            BaseMiscUtil.closeSilently(queryShareItem);
            queryShareItem = ShareMediaManager.isOtherShareMediaId(parseLong) ? queryShareItem(supportSQLiteDatabase, parseLong) : queryCloudItem(supportSQLiteDatabase, parseLong);
            if (queryShareItem == null || !queryShareItem.moveToFirst()) {
                throw new IllegalArgumentException(String.format(Locale.US, "query media [%d] by localImageId [%d] error!", Long.valueOf(j), Long.valueOf(parseLong)));
            }
            if (queryShareItem.getInt(queryShareItem.getColumnIndex("localFlag")) != 0) {
                throw new IllegalArgumentException(String.format(Locale.US, "localImage media [%d] is not sync too!", Long.valueOf(parseLong)));
            }
            j = parseLong;
        }
        preCheckResult.setCursor(queryShareItem);
        preCheckResult.setMediaId(j);
    }

    public static Cursor queryCloudItem(SupportSQLiteDatabase supportSQLiteDatabase, long j) {
        return supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(Contracts.PROJECTION).selection("_id=?", new String[]{String.valueOf(j)}).create());
    }

    public static Cursor queryShareItem(SupportSQLiteDatabase supportSQLiteDatabase, long j) {
        return supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("shareImage").columns(Contracts.PROJECTION).selection("_id=?", new String[]{String.valueOf(ShareMediaManager.getOriginalMediaId(j))}).create());
    }
}
