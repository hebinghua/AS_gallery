package com.miui.gallery.util.recorder;

import android.database.Cursor;
import com.miui.gallery.dao.GalleryEntityManager;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.Locale;

/* loaded from: classes2.dex */
public abstract class BaseRecorder implements IRecorder {

    /* loaded from: classes2.dex */
    public interface RecordCallback {
        void onRecord(int i);
    }

    public abstract Class<? extends RecordEntity> getEntityClass();

    public abstract long getExpireTime();

    public abstract int getMaxCount();

    public abstract String getTag();

    @Override // com.miui.gallery.util.recorder.IRecorder
    public void onAddAccount(String str) {
    }

    @Override // com.miui.gallery.util.recorder.IRecorder
    public void onDeleteAccount(String str) {
    }

    public BaseRecorder() {
        RecorderManager.getInstance().registerRecorder(this);
    }

    @Override // com.miui.gallery.util.recorder.IRecorder
    public boolean clearExpiredRecords() {
        return clearExpiredRecords(getMaxCount(), getExpireTime());
    }

    public boolean clearExpiredRecords(int i, long j) {
        long currentTimeMillis = System.currentTimeMillis() - j;
        int queryRecordCount = queryRecordCount();
        if (queryRecordCount <= i) {
            DefaultLogger.d(getTag(), "Record count doesn't exceed max");
            return false;
        }
        int deleteCount = GalleryEntityManager.getInstance().deleteCount(getEntityClass(), "timestamp<=?", new String[]{String.valueOf(currentTimeMillis)});
        boolean z = deleteCount > 0;
        int i2 = queryRecordCount - deleteCount;
        if (i2 > i) {
            int i3 = (i2 - i) + 2000;
            z = GalleryEntityManager.getInstance().execSQL(String.format(Locale.US, "DELETE FROM %s WHERE rowId IN (SELECT rowId FROM %s ORDER BY %s ASC LIMIT %s)", getEntityClass().getSimpleName(), getEntityClass().getSimpleName(), "timestamp", Integer.valueOf(i3)));
            if (z) {
                deleteCount += i3;
            }
        }
        DefaultLogger.d(getTag(), "Done delete [%s] records before %s", Integer.valueOf(deleteCount), Long.valueOf(currentTimeMillis));
        return z;
    }

    public final int queryRecordCount() {
        int i = 0;
        Cursor cursor = null;
        try {
            cursor = GalleryEntityManager.getInstance().rawQuery(getEntityClass(), new String[]{"count(*)"}, null, null, null, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                i = cursor.getInt(0);
            }
            return i;
        } catch (Exception e) {
            DefaultLogger.e(getTag(), e);
            return 0;
        } finally {
            BaseMiscUtil.closeSilently(cursor);
        }
    }

    public boolean clearAllRecords() {
        try {
            GalleryEntityManager.getInstance().deleteAll(getEntityClass());
            return true;
        } catch (Exception e) {
            DefaultLogger.e(getTag(), "clearAllRecords occur error %s.", e);
            return false;
        }
    }
}
