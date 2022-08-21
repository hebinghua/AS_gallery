package com.miui.gallery.provider.cloudmanager.method.cloud;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import androidx.sqlite.db.SupportSQLiteQueryBuilder;
import ch.qos.logback.core.joran.action.Action;
import com.miui.gallery.cloud.GalleryCloudUtils;
import com.miui.gallery.provider.CloudUtils;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.CheckPostProcessing;
import com.miui.gallery.provider.cloudmanager.Contracts;
import com.miui.gallery.provider.cloudmanager.CursorTask;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.BaseFileUtils;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.GalleryUtils;
import com.miui.gallery.util.logger.DefaultLogger;
import com.xiaomi.stat.MiStat;
import com.xiaomi.stat.a.j;
import java.io.Closeable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Locale;

/* loaded from: classes2.dex */
public class AddRemoveFavoriteMethod implements ICLoudMethod {
    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public String getInvokerTag() {
        return "galleryAction_Method_AddRemoveFavoriteMethod";
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public void doExecute(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, Bundle bundle, Bundle bundle2, ArrayList<Long> arrayList) throws StoragePermissionMissingException {
        long[] jArr;
        int i = bundle.getInt("add_remove_by");
        int i2 = bundle.getInt("operation_type");
        if (i == 1) {
            long[] longArray = bundle.getLongArray("extra_src_media_ids");
            if (longArray != null) {
                long[] jArr2 = new long[longArray.length];
                int i3 = 0;
                while (i3 < longArray.length) {
                    int i4 = i3;
                    long[] jArr3 = jArr2;
                    jArr3[i4] = addRemoveFavoritesById(context, supportSQLiteDatabase, mediaManager, arrayList, longArray[i3], i2);
                    i3 = i4 + 1;
                    jArr2 = jArr3;
                }
                jArr = jArr2;
            }
            jArr = null;
        } else if (i == 2) {
            String[] stringArray = bundle.getStringArray("extra_src_paths");
            if (stringArray != null) {
                long[] jArr4 = new long[stringArray.length];
                int i5 = 0;
                while (i5 < stringArray.length) {
                    int i6 = i5;
                    jArr4[i6] = addRemoveFavoritesByPath(context, supportSQLiteDatabase, mediaManager, arrayList, stringArray[i5], i2);
                    i5 = i6 + 1;
                }
                jArr = jArr4;
            }
            jArr = null;
        } else if (i == 3) {
            long[] longArray2 = bundle.getLongArray("extra_src_media_ids");
            if (longArray2 != null && longArray2.length == 2) {
                jArr = new long[]{replaceFavoritesById(context, supportSQLiteDatabase, mediaManager, arrayList, longArray2[0], longArray2[1], i2)};
            }
            jArr = null;
        } else if (i == 4) {
            ArrayList parcelableArrayList = bundle.getParcelableArrayList("extra_src_uris");
            jArr = new long[parcelableArrayList.size()];
            LinkedList linkedList = new LinkedList();
            for (int i7 = 0; i7 < parcelableArrayList.size(); i7++) {
                try {
                    try {
                        try {
                            jArr[i7] = AddRemoveFavoritesByUriFactory.create(context, arrayList, (Uri) parcelableArrayList.get(i7), i2, supportSQLiteDatabase).run(supportSQLiteDatabase, mediaManager);
                        } catch (StoragePermissionMissingException e) {
                            e = e;
                            linkedList.addAll(e.getPermissionResultList());
                        } catch (Exception unused) {
                            DefaultLogger.e("galleryAction_Method_AddRemoveFavoriteMethod", "[%s] to favorites error %s", i2 == 1 ? "add" : "remove", parcelableArrayList.get(i7));
                            jArr[i7] = -100;
                        }
                    } catch (StoragePermissionMissingException e2) {
                        e = e2;
                    } catch (Exception unused2) {
                    }
                } catch (StoragePermissionMissingException e3) {
                    e = e3;
                } catch (Exception unused3) {
                }
            }
            if (BaseMiscUtil.isValid(linkedList)) {
                throw new StoragePermissionMissingException(linkedList);
            }
        } else {
            throw new IllegalArgumentException();
        }
        bundle2.putLongArray("ids", jArr);
        CloudUtils.parceNotifyUri(bundle2, GalleryContract.Album.URI_CACHE);
    }

    public static long addRemoveFavoritesByPath(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, ArrayList<Long> arrayList, String str, int i) {
        try {
            return new AddRemoveFavoritesByPath(context, arrayList, i, str).run(supportSQLiteDatabase, mediaManager);
        } catch (Exception unused) {
            DefaultLogger.e("galleryAction_Method_AddRemoveFavoriteMethod", "Add or remove favorites by path with error: %s", str);
            return -100L;
        }
    }

    public static long addRemoveFavoritesById(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, ArrayList<Long> arrayList, long j, int i) {
        try {
            return new AddRemoveFavoritesById(context, arrayList, i, j).run(supportSQLiteDatabase, mediaManager);
        } catch (Exception unused) {
            DefaultLogger.e("galleryAction_Method_AddRemoveFavoriteMethod", "Add or remove favorites by id with error: %s", Long.valueOf(j));
            return -100L;
        }
    }

    public static long replaceFavoritesById(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, ArrayList<Long> arrayList, long j, long j2, int i) {
        try {
            return new ReplaceFavoritesById(context, arrayList, i, j, j2).run(supportSQLiteDatabase, mediaManager);
        } catch (StoragePermissionMissingException unused) {
            DefaultLogger.e("galleryAction_Method_AddRemoveFavoriteMethod", "replace favorites by id with error: %s:%s", Long.valueOf(j), Long.valueOf(j2));
            return -100L;
        }
    }

    public static Cursor queryFavoritesTableById(SupportSQLiteDatabase supportSQLiteDatabase, String[] strArr, Long l) {
        return supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("favorites").columns(strArr).selection("cloud_id = ?", new String[]{String.valueOf(l)}).create());
    }

    public static int updateFavoritesById(SupportSQLiteDatabase supportSQLiteDatabase, ContentValues contentValues, Long l) {
        return supportSQLiteDatabase.update("favorites", 0, contentValues, "cloud_id = ?", new String[]{String.valueOf(l)});
    }

    /* loaded from: classes2.dex */
    public static class AddRemoveFavoritesById extends CursorTask {
        public long mId;
        public int mOperationType;

        public AddRemoveFavoritesById(Context context, ArrayList<Long> arrayList, int i, long j) {
            super(context, arrayList);
            this.mOperationType = i;
            this.mId = j;
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public Cursor prepare(SupportSQLiteDatabase supportSQLiteDatabase) {
            return supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(Contracts.PROJECTION).selection("_id = ? AND serverType IN (1,2) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND (localGroupId!=-1000)", new String[]{String.valueOf(this.mId)}).create());
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public long verify(SupportSQLiteDatabase supportSQLiteDatabase) throws StoragePermissionMissingException {
            long verify = super.verify(supportSQLiteDatabase);
            if (verify != -1) {
                return verify;
            }
            if (this.mCursor.isNull(4) && new CheckPostProcessing(this.mContext, this.mCursor.getString(7)).run(supportSQLiteDatabase, null) == -111) {
                return -111L;
            }
            int columnIndex = this.mCursor.getColumnIndex("serverStatus");
            return (columnIndex < 0 || !"recovery".equalsIgnoreCase(this.mCursor.getString(columnIndex))) ? -1L : -115L;
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j) {
            long insert;
            if (j != -1) {
                return j;
            }
            long currentTimeMillis = System.currentTimeMillis();
            ContentValues contentValues = new ContentValues();
            contentValues.put("dateFavorite", Long.valueOf(currentTimeMillis));
            contentValues.put("isFavorite", Integer.valueOf(this.mOperationType == 1 ? 1 : 0));
            Cursor cursor = null;
            try {
                cursor = AddRemoveFavoriteMethod.queryFavoritesTableById(supportSQLiteDatabase, new String[]{j.c}, Long.valueOf(this.mId));
                if (cursor != null && cursor.getCount() > 0) {
                    insert = AddRemoveFavoriteMethod.updateFavoritesById(supportSQLiteDatabase, contentValues, Long.valueOf(this.mId));
                } else {
                    contentValues.put("cloud_id", Long.valueOf(this.mId));
                    contentValues.put("source", (Integer) 1);
                    insert = supportSQLiteDatabase.insert("favorites", 0, contentValues);
                }
                if (insert > 0) {
                    if (this.mOperationType == 1) {
                        mediaManager.insertToFavorites(Long.valueOf(this.mId));
                    } else {
                        mediaManager.removeFromFavorites(Long.valueOf(this.mId));
                    }
                    String transformToEditedColumnsElement = GalleryCloudUtils.transformToEditedColumnsElement(-1);
                    supportSQLiteDatabase.execSQL(String.format("update %s set %s=coalesce(replace(%s, '%s', '') || '%s', '%s') where %s='%s'", "cloud", "editedColumns", "editedColumns", transformToEditedColumnsElement, transformToEditedColumnsElement, transformToEditedColumnsElement, j.c, Long.valueOf(this.mId)));
                }
                return insert;
            } finally {
                BaseMiscUtil.closeSilently(cursor);
            }
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public String toString() {
            Locale locale = Locale.US;
            Object[] objArr = new Object[2];
            objArr[0] = this.mOperationType == 1 ? "Add to" : "Remove from";
            objArr[1] = Long.valueOf(this.mId);
            return String.format(locale, "%s favorites by id: [%d]", objArr);
        }
    }

    /* loaded from: classes2.dex */
    public static class AddRemoveFavoritesByPath extends AddRemoveFavoritesById {
        public String mPath;

        public AddRemoveFavoritesByPath(Context context, ArrayList<Long> arrayList, int i, String str) {
            super(context, arrayList, i, -1L);
            this.mPath = str;
        }

        /* JADX WARN: Not initialized variable reg: 2, insn: 0x0065: MOVE  (r1 I:??[OBJECT, ARRAY]) = (r2 I:??[OBJECT, ARRAY]), block:B:34:0x0065 */
        @Override // com.miui.gallery.provider.cloudmanager.method.cloud.AddRemoveFavoriteMethod.AddRemoveFavoritesById, com.miui.gallery.provider.cloudmanager.CursorTask
        public Cursor prepare(SupportSQLiteDatabase supportSQLiteDatabase) {
            Cursor cursor;
            Closeable closeable;
            Closeable closeable2 = null;
            try {
                try {
                    cursor = Util.queryCloudItemByFilePath(this.mContext, supportSQLiteDatabase, this.mPath);
                    if (cursor != null) {
                        try {
                            if (cursor.moveToFirst()) {
                                if ((cursor.getExtras() != null && cursor.getExtras().getBoolean("is_thumbnail", false)) || cursor.getLong(2) == BaseFileUtils.getFileSize(this.mPath)) {
                                    this.mId = cursor.getLong(0);
                                    BaseMiscUtil.closeSilently(cursor);
                                    return super.prepare(supportSQLiteDatabase);
                                }
                                DefaultLogger.e("galleryAction_Method_AddRemoveFavoriteMethod", "file size not equals, can not favorite: %s", this.mPath);
                                BaseMiscUtil.closeSilently(cursor);
                                return null;
                            }
                        } catch (Exception e) {
                            e = e;
                            DefaultLogger.e("galleryAction_Method_AddRemoveFavoriteMethod", e);
                            BaseMiscUtil.closeSilently(cursor);
                            return null;
                        }
                    }
                    BaseMiscUtil.closeSilently(cursor);
                    return null;
                } catch (Throwable th) {
                    th = th;
                    closeable2 = closeable;
                    BaseMiscUtil.closeSilently(closeable2);
                    throw th;
                }
            } catch (Exception e2) {
                e = e2;
                cursor = null;
            } catch (Throwable th2) {
                th = th2;
                BaseMiscUtil.closeSilently(closeable2);
                throw th;
            }
        }

        @Override // com.miui.gallery.provider.cloudmanager.method.cloud.AddRemoveFavoriteMethod.AddRemoveFavoritesById, com.miui.gallery.provider.cloudmanager.CursorTask
        public String toString() {
            Locale locale = Locale.US;
            Object[] objArr = new Object[2];
            objArr[0] = this.mOperationType == 1 ? "Add to" : "Remove from";
            objArr[1] = this.mPath;
            return String.format(locale, "%s favorites by path: %s", objArr);
        }
    }

    /* loaded from: classes2.dex */
    public static class ReplaceFavoritesById extends CursorTask {
        public long mNewId;
        public long mOldId;
        public int mOperationType;

        public ReplaceFavoritesById(Context context, ArrayList<Long> arrayList, int i, long j, long j2) {
            super(context, arrayList);
            this.mOperationType = i;
            this.mOldId = j;
            this.mNewId = j2;
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public Cursor prepare(SupportSQLiteDatabase supportSQLiteDatabase) {
            return supportSQLiteDatabase.query(SupportSQLiteQueryBuilder.builder("cloud").columns(Contracts.PROJECTION).selection("_id = ? AND serverType IN (1,2) AND (localFlag IS NULL OR localFlag NOT IN (11, 0, -1, 2, 15) OR (localFlag=0 AND (serverStatus='custom' OR serverStatus = 'recovery'))) AND (localGroupId!=-1000)", new String[]{String.valueOf(this.mOldId)}).create());
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public long verify(SupportSQLiteDatabase supportSQLiteDatabase) throws StoragePermissionMissingException {
            long verify = super.verify(supportSQLiteDatabase);
            if (verify != -1) {
                return verify;
            }
            if (this.mCursor.isNull(4) && new CheckPostProcessing(this.mContext, this.mCursor.getString(7)).run(supportSQLiteDatabase, null) == -111) {
                return -111L;
            }
            int columnIndex = this.mCursor.getColumnIndex("serverStatus");
            return (columnIndex < 0 || !"recovery".equalsIgnoreCase(this.mCursor.getString(columnIndex))) ? -1L : -115L;
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j) {
            if (j != -1) {
                return j;
            }
            long currentTimeMillis = System.currentTimeMillis();
            ContentValues contentValues = new ContentValues();
            contentValues.put("dateFavorite", Long.valueOf(currentTimeMillis));
            contentValues.put("cloud_id", Long.valueOf(this.mNewId));
            Cursor cursor = null;
            try {
                cursor = AddRemoveFavoriteMethod.queryFavoritesTableById(supportSQLiteDatabase, new String[]{j.c}, Long.valueOf(this.mOldId));
                long updateFavoritesById = (cursor == null || cursor.getCount() <= 0) ? 0L : AddRemoveFavoriteMethod.updateFavoritesById(supportSQLiteDatabase, contentValues, Long.valueOf(this.mOldId));
                if (updateFavoritesById > 0) {
                    mediaManager.removeFromFavorites(Long.valueOf(this.mOldId));
                    mediaManager.insertToFavorites(Long.valueOf(this.mNewId));
                    String transformToEditedColumnsElement = GalleryCloudUtils.transformToEditedColumnsElement(-1);
                    supportSQLiteDatabase.execSQL(String.format("update %s set %s=coalesce(replace(%s, '%s', '') || '%s', '%s') where %s IN ('%s', '%s')", "cloud", "editedColumns", "editedColumns", transformToEditedColumnsElement, transformToEditedColumnsElement, transformToEditedColumnsElement, j.c, Long.valueOf(this.mOldId), Long.valueOf(this.mNewId)));
                }
                return updateFavoritesById;
            } finally {
                BaseMiscUtil.closeSilently(cursor);
            }
        }

        @Override // com.miui.gallery.provider.cloudmanager.CursorTask
        public String toString() {
            return String.format(Locale.US, "%s favorites by id: [%d]-[%d]", "Replace", Long.valueOf(this.mOldId), Long.valueOf(this.mNewId));
        }
    }

    /* loaded from: classes2.dex */
    public static class AddRemoveFavoritesByUriFactory {
        public static CursorTask create(Context context, ArrayList<Long> arrayList, Uri uri, int i, SupportSQLiteDatabase supportSQLiteDatabase) {
            if (uri == null) {
                throw new IllegalArgumentException();
            }
            String str = null;
            if (MiStat.Param.CONTENT.equals(uri.getScheme())) {
                str = (String) GalleryUtils.safeQuery(uri, new String[]{"_data"}, (String) null, (String[]) null, (String) null, AddRemoveFavoriteMethod$AddRemoveFavoritesByUriFactory$$ExternalSyntheticLambda0.INSTANCE);
            } else if (Action.FILE_ATTRIBUTE.equals(uri.getScheme())) {
                str = uri.getPath();
            }
            if (TextUtils.isEmpty(str)) {
                throw new IllegalArgumentException("path is null");
            }
            Cursor queryCloudItemByFilePath = Util.queryCloudItemByFilePath(context, supportSQLiteDatabase, str);
            if (queryCloudItemByFilePath != null) {
                try {
                    if (queryCloudItemByFilePath.moveToFirst() && queryCloudItemByFilePath.getCount() == 1) {
                        AddRemoveFavoritesById addRemoveFavoritesById = new AddRemoveFavoritesById(context, arrayList, i, queryCloudItemByFilePath.getLong(0));
                        queryCloudItemByFilePath.close();
                        return addRemoveFavoritesById;
                    }
                } catch (Throwable th) {
                    if (queryCloudItemByFilePath != null) {
                        try {
                            queryCloudItemByFilePath.close();
                        } catch (Throwable th2) {
                            th.addSuppressed(th2);
                        }
                    }
                    throw th;
                }
            }
            AddRemoveFavoritesByPath addRemoveFavoritesByPath = new AddRemoveFavoritesByPath(context, arrayList, i, str);
            if (queryCloudItemByFilePath != null) {
                queryCloudItemByFilePath.close();
            }
            return addRemoveFavoritesByPath;
        }

        public static /* synthetic */ String lambda$create$0(Cursor cursor) {
            if (cursor == null || !cursor.moveToFirst()) {
                return null;
            }
            return cursor.getString(0);
        }
    }
}
