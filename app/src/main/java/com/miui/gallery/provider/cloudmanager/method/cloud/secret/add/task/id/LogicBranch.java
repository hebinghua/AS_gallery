package com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id;

import android.content.ContentProviderOperation;
import android.content.ContentValues;
import android.content.Context;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.GalleryContract;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.method.cloud.delete.DeleteMethod;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/* loaded from: classes2.dex */
public abstract class LogicBranch extends BaseLogicBranch {
    public final int mCheckConflict;
    public final long mMediaId;
    public final List<CloudItem> mSha1ConflictItems;

    /* loaded from: classes2.dex */
    public static class CloudItem {
        public long id;
        public String path;
        public String serverId;
    }

    public LogicBranch(Context context, ArrayList<Long> arrayList, IDataProvider iDataProvider, long j, int i) {
        super(context, arrayList, iDataProvider);
        this.mSha1ConflictItems = new LinkedList();
        this.mMediaId = j;
        this.mCheckConflict = i;
    }

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void doPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) {
        querySha1ConflictItems(supportSQLiteDatabase, this.mSha1ConflictItems, false);
    }

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public void postPrepare(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        LinkedList linkedList = new LinkedList();
        for (CloudItem cloudItem : this.mSha1ConflictItems) {
            if (!TextUtils.isEmpty(cloudItem.path)) {
                IStoragePermissionStrategy.PermissionResult checkPermission = StorageSolutionProvider.get().checkPermission(cloudItem.path, IStoragePermissionStrategy.Permission.DELETE);
                if (!checkPermission.granted) {
                    linkedList.add(checkPermission);
                }
            }
        }
        if (!BaseMiscUtil.isValid(linkedList)) {
            return;
        }
        throw new StoragePermissionMissingException(linkedList);
    }

    @Override // com.miui.gallery.provider.cloudmanager.LogicBranch, com.miui.gallery.provider.cloudmanager.CursorTask2
    public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager) throws StoragePermissionMissingException {
        long[] array = this.mSha1ConflictItems.stream().mapToLong(LogicBranch$$ExternalSyntheticLambda2.INSTANCE).toArray();
        if (array == null || array.length <= 0) {
            DefaultLogger.d("galleryAction_addSecretBase", "execute => no sameSha1Conflict");
            return 0L;
        }
        try {
            DefaultLogger.d("galleryAction_addSecretBase", "delete file =>");
            DeleteMethod.delete(this.mContext, supportSQLiteDatabase, mediaManager, getDirtyBulk(), array, 36);
            DefaultLogger.d("galleryAction_addSecretBase", "update db =>");
            updateLocalDBNotShowInRecycleBin((List) this.mSha1ConflictItems.stream().map(LogicBranch$$ExternalSyntheticLambda0.INSTANCE).filter(LogicBranch$$ExternalSyntheticLambda1.INSTANCE).collect(Collectors.toList()));
            return 0L;
        } catch (Exception e) {
            DefaultLogger.e("galleryAction_addSecretBase", "execute delete exception %s", e);
            return -121L;
        }
    }

    public static /* synthetic */ boolean lambda$execute$2(String str) {
        return !TextUtils.isEmpty(str);
    }

    /* JADX WARN: Code restructure failed: missing block: B:29:0x0095, code lost:
        r6.close();
     */
    /* JADX WARN: Code restructure failed: missing block: B:30:0x0098, code lost:
        return;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void querySha1ConflictItems(androidx.sqlite.db.SupportSQLiteDatabase r6, java.util.List<com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.LogicBranch.CloudItem> r7, boolean r8) {
        /*
            r5 = this;
            java.lang.String r0 = r5.mSha1
            boolean r0 = android.text.TextUtils.isEmpty(r0)
            if (r0 == 0) goto L9
            return
        L9:
            java.lang.String r0 = "sha1=? AND localGroupId!=-1000"
            if (r8 != 0) goto L23
            java.lang.StringBuilder r8 = new java.lang.StringBuilder
            r8.<init>()
            r8.append(r0)
            java.lang.String r0 = " AND _id!="
            r8.append(r0)
            long r0 = r5.mMediaId
            r8.append(r0)
            java.lang.String r0 = r8.toString()
        L23:
            java.lang.String r8 = "cloud"
            androidx.sqlite.db.SupportSQLiteQueryBuilder r8 = androidx.sqlite.db.SupportSQLiteQueryBuilder.builder(r8)     // Catch: java.lang.Exception -> L99
            java.lang.String r1 = "_id"
            java.lang.String r2 = "serverId"
            java.lang.String r3 = "localFile"
            java.lang.String r4 = "thumbnailFile"
            java.lang.String[] r1 = new java.lang.String[]{r1, r2, r3, r4}     // Catch: java.lang.Exception -> L99
            androidx.sqlite.db.SupportSQLiteQueryBuilder r8 = r8.columns(r1)     // Catch: java.lang.Exception -> L99
            r1 = 1
            java.lang.String[] r2 = new java.lang.String[r1]     // Catch: java.lang.Exception -> L99
            java.lang.String r3 = r5.mSha1     // Catch: java.lang.Exception -> L99
            r4 = 0
            r2[r4] = r3     // Catch: java.lang.Exception -> L99
            androidx.sqlite.db.SupportSQLiteQueryBuilder r8 = r8.selection(r0, r2)     // Catch: java.lang.Exception -> L99
            androidx.sqlite.db.SupportSQLiteQuery r8 = r8.create()     // Catch: java.lang.Exception -> L99
            android.database.Cursor r6 = r6.query(r8)     // Catch: java.lang.Exception -> L99
            if (r6 == 0) goto L93
            int r8 = r6.getCount()     // Catch: java.lang.Throwable -> L89
            if (r8 > 0) goto L56
            goto L93
        L56:
            boolean r8 = r6.moveToNext()     // Catch: java.lang.Throwable -> L89
            if (r8 == 0) goto L85
            com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.LogicBranch$CloudItem r8 = new com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.LogicBranch$CloudItem     // Catch: java.lang.Throwable -> L89
            r8.<init>()     // Catch: java.lang.Throwable -> L89
            long r2 = r6.getLong(r4)     // Catch: java.lang.Throwable -> L89
            r8.id = r2     // Catch: java.lang.Throwable -> L89
            java.lang.String r0 = r6.getString(r1)     // Catch: java.lang.Throwable -> L89
            r8.serverId = r0     // Catch: java.lang.Throwable -> L89
            r0 = 2
            java.lang.String r0 = r6.getString(r0)     // Catch: java.lang.Throwable -> L89
            r8.path = r0     // Catch: java.lang.Throwable -> L89
            boolean r0 = android.text.TextUtils.isEmpty(r0)     // Catch: java.lang.Throwable -> L89
            if (r0 == 0) goto L81
            r0 = 3
            java.lang.String r0 = r6.getString(r0)     // Catch: java.lang.Throwable -> L89
            r8.path = r0     // Catch: java.lang.Throwable -> L89
        L81:
            r7.add(r8)     // Catch: java.lang.Throwable -> L89
            goto L56
        L85:
            r6.close()     // Catch: java.lang.Exception -> L99
            goto L9f
        L89:
            r7 = move-exception
            r6.close()     // Catch: java.lang.Throwable -> L8e
            goto L92
        L8e:
            r6 = move-exception
            r7.addSuppressed(r6)     // Catch: java.lang.Exception -> L99
        L92:
            throw r7     // Catch: java.lang.Exception -> L99
        L93:
            if (r6 == 0) goto L98
            r6.close()     // Catch: java.lang.Exception -> L99
        L98:
            return
        L99:
            r6 = move-exception
            java.lang.String r7 = "galleryAction_addSecretBase"
            com.miui.gallery.util.logger.DefaultLogger.e(r7, r6)
        L9f:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.provider.cloudmanager.method.cloud.secret.add.task.id.LogicBranch.querySha1ConflictItems(androidx.sqlite.db.SupportSQLiteDatabase, java.util.List, boolean):void");
    }

    public void updateLocalDBNotShowInRecycleBin(List<String> list) {
        ArrayList<ContentProviderOperation> arrayList = new ArrayList<>();
        ContentValues contentValues = new ContentValues();
        if (BaseMiscUtil.isValid(list)) {
            contentValues.put("serverStatus", "toBePurged");
            arrayList.add(ContentProviderOperation.newUpdate(GalleryContract.Cloud.CLOUD_URI).withValues(contentValues).withSelection("_id IN (" + TextUtils.join(",", list) + ")", null).build());
            if (arrayList.isEmpty()) {
                return;
            }
            try {
                this.mContext.getContentResolver().applyBatch("com.miui.gallery.cloud.provider", arrayList);
            } catch (Exception e) {
                DefaultLogger.e("galleryAction_addSecretBase", "update serverId item cloud failed", e);
            }
        }
    }
}
