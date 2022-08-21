package com.miui.gallery.provider.cloudmanager.method.cloud.delete.task.batchtask;

import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import java.util.List;

/* loaded from: classes2.dex */
public abstract class BatchTask2<K, D> {
    public abstract String describeBundle(Bundle bundle);

    public abstract void executeBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, D d, List<IStoragePermissionStrategy.PermissionResult> list);

    public abstract Bundle getBatchBundle(int i, int i2, Bundle bundle);

    /* renamed from: getBatchExecuteKeys */
    public abstract K[] mo1234getBatchExecuteKeys(Bundle bundle);

    public abstract int getTotalCount();
}
