package com.miui.gallery.provider.cloudmanager.method.cloud.delete;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.provider.cloudmanager.Util;
import com.miui.gallery.provider.cloudmanager.handleFile.FileHandleManager;
import com.miui.gallery.provider.cloudmanager.method.cloud.ICLoudMethod;
import com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.cloud.Delete;
import com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.cloud.DeleteOwner;
import com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.local.DeleteFile;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.MiscUtil;
import com.miui.gallery.util.Numbers;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.logger.TimingTracing;
import com.xiaomi.stat.MiStat;
import java.util.ArrayList;
import java.util.LinkedList;

/* loaded from: classes2.dex */
public class DeleteMethod implements ICLoudMethod {
    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public String getInvokerTag() {
        return "galleryAction_Method_DeleteMethod";
    }

    @Override // com.miui.gallery.provider.cloudmanager.method.IMethod
    public void doExecute(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, String str, Bundle bundle, Bundle bundle2, ArrayList<Long> arrayList) throws StoragePermissionMissingException {
        long[] deleteById;
        ArrayList arrayList2 = new ArrayList();
        int i = bundle.getInt("delete_by");
        int i2 = bundle.getInt("extra_delete_options", 0);
        int i3 = bundle.getInt("extra_delete_reason", 24);
        if (i == 0) {
            deleteById = deleteById(context, supportSQLiteDatabase, mediaManager, arrayList, bundle.getLongArray("extra_ids"), i2, arrayList2, i3);
            bundle2.putLongArray("ids", MiscUtil.ListToArray(arrayList2));
        } else if (i == 1) {
            deleteById = deleteByPath(context, supportSQLiteDatabase, mediaManager, arrayList, bundle.getStringArray("extra_paths"), i2, arrayList2, i3);
            bundle2.putLongArray("ids", MiscUtil.ListToArray(arrayList2));
        } else if (i != 3) {
            return;
        } else {
            deleteById = deleteCloudByPath(context, supportSQLiteDatabase, mediaManager, arrayList, bundle.getStringArray("extra_paths"), arrayList2, i3);
            bundle2.putLongArray("ids", MiscUtil.ListToArray(arrayList2));
        }
        bundle2.putLong(MiStat.Param.COUNT, Util.getValidCount(deleteById));
    }

    public static long[] deleteById(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, ArrayList<Long> arrayList, long[] jArr, int i, ArrayList<Long> arrayList2, int i2) throws StoragePermissionMissingException {
        try {
            Numbers.ensurePositive(jArr);
            return delete(context, supportSQLiteDatabase, mediaManager, arrayList, jArr, i, arrayList2, i2);
        } catch (StoragePermissionMissingException e) {
            throw e;
        } catch (Exception e2) {
            DefaultLogger.w("galleryAction_Method_DeleteMethod", e2);
            return new long[]{-100};
        }
    }

    public static long[] deleteByPath(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, ArrayList<Long> arrayList, String[] strArr, int i, ArrayList<Long> arrayList2, int i2) throws StoragePermissionMissingException {
        try {
            long[] jArr = new long[strArr.length];
            for (int i3 = 0; i3 < strArr.length; i3++) {
                jArr[i3] = new DeleteByPath(context, arrayList, strArr[i3], i, arrayList2, i2).run(supportSQLiteDatabase, mediaManager);
            }
            return jArr;
        } catch (StoragePermissionMissingException e) {
            throw e;
        } catch (Exception e2) {
            DefaultLogger.w("galleryAction_Method_DeleteMethod", e2);
            return new long[]{-100};
        }
    }

    public static long[] deleteCloudByPath(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, ArrayList<Long> arrayList, String[] strArr, ArrayList<Long> arrayList2, int i) throws StoragePermissionMissingException {
        try {
            long[] jArr = new long[strArr.length];
            for (int i2 = 0; i2 < strArr.length; i2++) {
                jArr[i2] = new DeleteCloudByPath(context, arrayList, strArr[i2], arrayList2, i).run(supportSQLiteDatabase, mediaManager);
            }
            return jArr;
        } catch (StoragePermissionMissingException e) {
            throw e;
        } catch (Exception e2) {
            DefaultLogger.w("galleryAction_Method_DeleteMethod", e2);
            return new long[]{-100};
        }
    }

    public static long[] cloudDelete(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, ArrayList<Long> arrayList, long[] jArr, ArrayList<Long> arrayList2, int i) throws StoragePermissionMissingException {
        long[] run = new DeleteOwner(context, arrayList, jArr, true, i).run(supportSQLiteDatabase, mediaManager);
        if (arrayList2 != null) {
            arrayList2.addAll(arrayList);
        }
        return run;
    }

    public static long[] delete(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, ArrayList<Long> arrayList, long[] jArr, int i) throws StoragePermissionMissingException {
        return delete(context, supportSQLiteDatabase, mediaManager, arrayList, jArr, 0, null, i);
    }

    public static long[] delete(Context context, SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, ArrayList<Long> arrayList, long[] jArr, int i, ArrayList<Long> arrayList2, int i2) throws StoragePermissionMissingException {
        if (i == 1) {
            TimingTracing.beginTracing(String.format("deleteLocal{%s}", Long.valueOf(Thread.currentThread().getId())), String.format("count{%s}", Integer.valueOf(jArr.length)));
            long[] jArr2 = new long[jArr.length];
            LinkedList linkedList = new LinkedList();
            for (int i3 = 0; i3 < jArr.length; i3++) {
                try {
                    jArr2[i3] = new DeleteFile(context, arrayList, jArr[i3], i2, supportSQLiteDatabase).run(supportSQLiteDatabase, mediaManager);
                    if (jArr2[i3] > 0 && arrayList2 != null) {
                        arrayList2.add(Long.valueOf(jArr[i3]));
                    }
                } catch (StoragePermissionMissingException e) {
                    linkedList.addAll(e.getPermissionResultList());
                } catch (Exception e2) {
                    DefaultLogger.e("galleryAction_Method_DeleteMethod", "delete local error %s", e2);
                }
            }
            TimingTracing.stopTracing(null);
            if (BaseMiscUtil.isValid(linkedList)) {
                throw new StoragePermissionMissingException(linkedList);
            }
            return jArr2;
        }
        long[] run = new Delete(context, arrayList, jArr, i2).run(supportSQLiteDatabase, mediaManager);
        if (arrayList2 != null) {
            arrayList2.addAll(arrayList);
        }
        return run;
    }

    /* loaded from: classes2.dex */
    public static class DeleteByPath extends CursorTaskWithException {
        public ArrayList<Long> mDeleteIds;
        public int mDeleteOptions;
        public int mDeleteReason;
        public String mPath;

        public DeleteByPath(Context context, ArrayList<Long> arrayList, String str, int i, ArrayList<Long> arrayList2, int i2) {
            super(context, arrayList);
            this.mPath = str;
            this.mDeleteOptions = i;
            this.mDeleteIds = arrayList2;
            this.mDeleteReason = i2;
        }

        @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.CursorTaskWithException
        public long verify(SupportSQLiteDatabase supportSQLiteDatabase) {
            return TextUtils.isEmpty(this.mPath) ? -100L : -1L;
        }

        @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.CursorTaskWithException
        public Cursor prepare(SupportSQLiteDatabase supportSQLiteDatabase) {
            return Util.queryCloudItemByFilePath(this.mContext, supportSQLiteDatabase, this.mPath);
        }

        @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.CursorTaskWithException
        public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j) throws StoragePermissionMissingException {
            Cursor cursor = this.mCursor;
            if (cursor != null && cursor.moveToFirst()) {
                long j2 = this.mCursor.getLong(0);
                if (j2 > 0) {
                    long[] delete = DeleteMethod.delete(this.mContext, supportSQLiteDatabase, mediaManager, getDirtyBulk(), new long[]{j2}, this.mDeleteOptions, this.mDeleteIds, this.mDeleteReason);
                    if (delete.length <= 0) {
                        return -101L;
                    }
                    return delete[0];
                }
            }
            return FileHandleManager.deleteFile(this.mPath, this.mDeleteReason, "DeleteByPath") ? 1L : 0L;
        }

        @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.CursorTaskWithException
        public String toString() {
            return String.format("%s{%s}", "DeleteByPath", this.mPath);
        }
    }

    /* loaded from: classes2.dex */
    public static class DeleteCloudByPath extends CursorTaskWithException {
        public ArrayList<Long> mDeleteIds;
        public int mDeleteReason;
        public String mPath;

        public DeleteCloudByPath(Context context, ArrayList<Long> arrayList, String str, ArrayList<Long> arrayList2, int i) {
            super(context, arrayList);
            this.mPath = str;
            this.mDeleteIds = arrayList2;
            this.mDeleteReason = i;
        }

        @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.CursorTaskWithException
        public long verify(SupportSQLiteDatabase supportSQLiteDatabase) {
            return TextUtils.isEmpty(this.mPath) ? -100L : -1L;
        }

        @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.CursorTaskWithException
        public Cursor prepare(SupportSQLiteDatabase supportSQLiteDatabase) {
            return Util.queryCloudItemByFilePath(this.mContext, supportSQLiteDatabase, this.mPath);
        }

        @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.CursorTaskWithException
        public long execute(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, long j) throws StoragePermissionMissingException {
            Cursor cursor = this.mCursor;
            if (cursor != null && cursor.moveToFirst()) {
                long j2 = this.mCursor.getLong(0);
                if (j2 > 0) {
                    long[] cloudDelete = DeleteMethod.cloudDelete(this.mContext, supportSQLiteDatabase, mediaManager, getDirtyBulk(), new long[]{j2}, this.mDeleteIds, this.mDeleteReason);
                    if (cloudDelete.length <= 0) {
                        return -101L;
                    }
                    return cloudDelete[0];
                }
            }
            return 0L;
        }

        @Override // com.miui.gallery.provider.cloudmanager.method.cloud.delete.CursorTaskWithException
        public String toString() {
            return String.format("DeleteCloudByPath{%s}", this.mPath);
        }
    }
}
