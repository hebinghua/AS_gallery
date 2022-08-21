package com.miui.gallery.provider.cloudmanager.method.album;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.model.dto.Album;
import com.miui.gallery.provider.InternalContract$Album;
import com.miui.gallery.provider.album.AlbumManager;
import com.miui.gallery.provider.cache.AlbumCacheManager;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cache.ShareMediaManager;
import com.miui.gallery.provider.cloudmanager.CursorTask;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.ui.album.common.ReplaceAlbumCoverUtils;
import com.miui.gallery.util.ParcelableUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.a.j;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public class DoReplaceAlbumCoverMethod implements IAlbumMethod {
    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public String getInvokerTag() {
        return "galleryAction_Method_DoReplaceAlbumCoverMethod";
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public void doExecute(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, Bundle bundle, Bundle bundle2, ArrayList<Long> arrayList) {
        if (bundle.containsKey("album_id") && replacelbumCover(context, supportSQLiteDatabase, mediaManager, arrayList, bundle.getLong("cover_id"), bundle.getLongArray("album_id"), bundle2) > 0) {
            bundle2.putBoolean("should_request_sync", true);
        }
    }

    public static long replacelbumCover(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, ArrayList<Long> arrayList, long j, long[] jArr, Bundle bundle) {
        try {
            return new DoReplaceAlbumCoverTask(context, arrayList, j, jArr, bundle).run(supportSQLiteDatabase, mediaManager);
        } catch (Exception e) {
            DefaultLogger.e("galleryAction_Method_DoReplaceAlbumCoverMethod", e);
            return -100;
        }
    }

    /* loaded from: classes2.dex */
    public static class DoReplaceAlbumCoverTask extends CursorTask {
        public final boolean isShareMediaId;
        public final List<Long> mAlbumIds;
        public long mCoverId;
        public final Bundle mResult;

        public DoReplaceAlbumCoverTask(Context context, ArrayList<Long> arrayList, long j, long[] jArr, Bundle bundle) {
            super(context, arrayList);
            this.mCoverId = j;
            this.mAlbumIds = (List) Arrays.stream(jArr).boxed().collect(Collectors.toList());
            this.mResult = bundle;
            this.isShareMediaId = ShareMediaManager.isOtherShareMediaId(this.mCoverId);
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public Cursor prepare(SupportSQLiteDatabase supportSQLiteDatabase) {
            if (this.mCoverId == -1) {
                return null;
            }
            SupportSQLiteQueryBuilder columns = SupportSQLiteQueryBuilder.builder(this.isShareMediaId ? "shareImage" : "cloud").columns(new String[]{j.c, InternalContract$Album.ALIAS_COVER_PATH, " CASE WHEN localFlag = 0  THEN 0 WHEN localFlag IN (5, 6, 9) THEN 1 ELSE 3 END  AS coverSyncState"});
            String str = j.c + "=?";
            String[] strArr = new String[1];
            strArr[0] = String.valueOf(this.isShareMediaId ? ShareMediaManager.getOriginalMediaId(this.mCoverId) : this.mCoverId);
            return supportSQLiteDatabase.query(columns.selection(str, strArr).create());
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public long verify(SupportSQLiteDatabase supportSQLiteDatabase) throws StoragePermissionMissingException {
            if (this.mCursor == null && this.mCoverId == -1) {
                return -1L;
            }
            return super.verify(supportSQLiteDatabase);
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j) {
            if (this.isShareMediaId) {
                Cursor query = supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("shareImage").columns(new String[]{"serverId"}).selection("_id=?", new String[]{String.valueOf(ShareMediaManager.getOriginalMediaId(this.mCoverId))}).create());
                if (query != null) {
                    try {
                        if (query.moveToFirst()) {
                            this.mCoverId = query.getLong(0);
                            query.close();
                        }
                    } catch (Throwable th) {
                        if (query != null) {
                            try {
                                query.close();
                            } catch (Throwable th2) {
                                th.addSuppressed(th2);
                            }
                        }
                        throw th;
                    }
                }
                DefaultLogger.e("galleryAction_Method_DoReplaceAlbumCoverMethod", "fatal,设置它人共享图片作为封面，但是这条记录没有serverId");
                if (query != null) {
                    query.close();
                }
                return -1L;
            }
            LinkedList linkedList = new LinkedList();
            long j2 = 0;
            for (Long l : this.mAlbumIds) {
                if (Album.isVirtualAlbum(l.longValue())) {
                    ReplaceAlbumCoverUtils.setVirtualAlbumCoverId(this.mCoverId, l.longValue());
                    j2++;
                } else {
                    linkedList.add(l);
                }
            }
            if (!linkedList.isEmpty()) {
                String transformToEditedColumnsElement = GalleryCloudUtils.transformToEditedColumnsElement(3);
                Locale locale = Locale.US;
                Object[] objArr = new Object[9];
                objArr[0] = "album";
                objArr[1] = "coverId";
                long j3 = this.mCoverId;
                objArr[2] = j3 == -1 ? "null" : Long.valueOf(j3);
                objArr[3] = "editedColumns";
                objArr[4] = "editedColumns";
                objArr[5] = transformToEditedColumnsElement;
                objArr[6] = transformToEditedColumnsElement;
                objArr[7] = j.c;
                objArr[8] = TextUtils.join(",", linkedList);
                supportSQLiteDatabase.execSQL(String.format(locale, "UPDATE %s SET %s = %s, %s=coalesce(%s || '%s','%s') WHERE %s IN (%s)", objArr));
                j2 += linkedList.size();
            }
            if (j2 > 0) {
                ContentValues contentValues = new ContentValues();
                long j4 = this.mCoverId;
                if (j4 == -1) {
                    contentValues.putNull("coverId");
                } else {
                    contentValues.put("coverId", Long.valueOf(j4));
                }
                AlbumCacheManager.getInstance().update(this.mAlbumIds, contentValues);
                fillResult(supportSQLiteDatabase);
            }
            return j2;
        }

        public final void fillResult(SupportSQLiteDatabase supportSQLiteDatabase) {
            boolean z = this.mCoverId == -1;
            int size = this.mAlbumIds.size();
            ArrayList<? extends Parcelable> arrayList = new ArrayList<>(size);
            DoReplaceAlbumCoverResult doReplaceAlbumCoverResult = null;
            Cursor cursor = this.mCursor;
            if (cursor != null) {
                long parseLong = Long.parseLong(cursor.getString(0));
                String string = this.mCursor.getString(1);
                boolean z2 = !z;
                if (this.isShareMediaId) {
                    parseLong = ShareMediaManager.convertToMediaId(parseLong);
                }
                doReplaceAlbumCoverResult = new DoReplaceAlbumCoverResult(string, z2, parseLong, -1L, this.mCursor.getInt(2));
            }
            for (Long l : this.mAlbumIds) {
                if (doReplaceAlbumCoverResult != null) {
                    DoReplaceAlbumCoverResult copy = doReplaceAlbumCoverResult.copy();
                    copy.setAlbumId(l.longValue());
                    arrayList.add(copy);
                } else if (size == 1 && !Album.isVirtualAlbum(l.longValue())) {
                    Album.CoverBean queryDefaultAlbumCover = AlbumManager.queryDefaultAlbumCover(supportSQLiteDatabase, l.longValue());
                    if (queryDefaultAlbumCover != null) {
                        arrayList.add(new DoReplaceAlbumCoverResult(queryDefaultAlbumCover.getCoverPath(), !z, queryDefaultAlbumCover.getId(), l.longValue(), queryDefaultAlbumCover.getCoverSyncState()));
                    }
                }
            }
            this.mResult.putParcelableArrayList("replace_album_cover_result", arrayList);
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public String toString() {
            return String.format(Locale.US, "replace album cover: {coverId: %d},{albumIds: [%s]}", Long.valueOf(this.mCoverId), TextUtils.join(",", this.mAlbumIds));
        }
    }

    /* loaded from: classes2.dex */
    public static final class DoReplaceAlbumCoverResult implements Parcelable {
        public static final Parcelable.Creator<DoReplaceAlbumCoverResult> CREATOR = new Parcelable.Creator<DoReplaceAlbumCoverResult>() { // from class: com.miui.gallery.provider.cloudmanager.method.album.DoReplaceAlbumCoverMethod.DoReplaceAlbumCoverResult.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: createFromParcel */
            public DoReplaceAlbumCoverResult mo1231createFromParcel(Parcel parcel) {
                return new DoReplaceAlbumCoverResult(parcel);
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // android.os.Parcelable.Creator
            /* renamed from: newArray */
            public DoReplaceAlbumCoverResult[] mo1232newArray(int i) {
                return new DoReplaceAlbumCoverResult[i];
            }
        };
        public final boolean isManualSetCover;
        public long mAlbumId;
        public final long mCoverId;
        public final String mCoverPath;
        public final int mCoverSyncState;

        @Override // android.os.Parcelable
        public int describeContents() {
            return 0;
        }

        public DoReplaceAlbumCoverResult(String str, boolean z, long j, long j2, int i) {
            this.mCoverPath = str;
            this.isManualSetCover = z;
            this.mCoverId = j;
            this.mAlbumId = j2;
            this.mCoverSyncState = i;
        }

        public DoReplaceAlbumCoverResult copy() {
            return new DoReplaceAlbumCoverResult(this.mCoverPath, this.isManualSetCover, this.mCoverId, this.mAlbumId, this.mCoverSyncState);
        }

        public DoReplaceAlbumCoverResult(Parcel parcel) {
            this.mCoverPath = parcel.readString();
            this.isManualSetCover = ParcelableUtil.readBool(parcel).booleanValue();
            this.mCoverId = parcel.readLong();
            this.mAlbumId = parcel.readLong();
            this.mCoverSyncState = parcel.readInt();
        }

        @Override // android.os.Parcelable
        public void writeToParcel(Parcel parcel, int i) {
            parcel.writeString(this.mCoverPath);
            ParcelableUtil.writeBool(parcel, Boolean.valueOf(this.isManualSetCover));
            parcel.writeLong(this.mCoverId);
            parcel.writeLong(this.mAlbumId);
            parcel.writeInt(this.mCoverSyncState);
        }

        public void setAlbumId(long j) {
            this.mAlbumId = j;
        }

        public String getNowCoverPath() {
            return this.mCoverPath;
        }

        public Boolean isManualSetCover() {
            return Boolean.valueOf(this.isManualSetCover);
        }

        public long getNowCoverId() {
            return this.mCoverId;
        }

        public long getAlbumId() {
            return this.mAlbumId;
        }

        public int getCoverSyncState() {
            return this.mCoverSyncState;
        }
    }
}
