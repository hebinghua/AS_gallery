package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.Context;
import com.miui.gallery.util.DeleteDataUtil;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.deprecated.Preference;

/* loaded from: classes.dex */
public class ClearDataManager {
    public boolean mHasClearOnce;

    /* loaded from: classes.dex */
    public static class SingletonHolder {
        public static final ClearDataManager Instance = new ClearDataManager();
    }

    public ClearDataManager() {
        this.mHasClearOnce = false;
    }

    public static ClearDataManager getInstance() {
        return SingletonHolder.Instance;
    }

    public boolean clearDataBaseIfNeeded(Context context, Account account) {
        this.mHasClearOnce = false;
        if (Preference.getSyncShouldClearDataBase()) {
            SyncLogger.d("ClearDataManager", "clear data start");
            SyncLogger.d("ClearDataManager", "clear data result %s", Boolean.valueOf(DeleteDataUtil.delete(context, 0)));
            GalleryCloudSyncTagUtils.insertAccountToDB(context, account);
            this.mHasClearOnce = true;
            Preference.setSyncShouldClearDataBase(false);
            AddAccount.scanSecretFiles(context, account);
            SyncLogger.d("ClearDataManager", "clear data end");
            return true;
        }
        return false;
    }
}
