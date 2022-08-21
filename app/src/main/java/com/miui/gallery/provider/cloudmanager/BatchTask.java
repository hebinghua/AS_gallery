package com.miui.gallery.provider.cloudmanager;

import android.os.Bundle;
import androidx.sqlite.db.SupportSQLiteDatabase;
import com.miui.gallery.provider.cache.MediaManager;

/* loaded from: classes2.dex */
public abstract class BatchTask<K, D> {
    public abstract String describeBundle(Bundle bundle);

    public abstract void executeBatch(SupportSQLiteDatabase supportSQLiteDatabase, MediaManager mediaManager, Bundle bundle, D d);

    public abstract Bundle getBatchBundle(int i, int i2, Bundle bundle);

    /* renamed from: getBatchExecuteKeys */
    public abstract K[] mo1228getBatchExecuteKeys(Bundle bundle);

    public abstract int getTotalCount();
}
