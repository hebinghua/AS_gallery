package com.miui.gallery.util.recorder;

import android.accounts.Account;
import android.text.TextUtils;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.HashSet;
import java.util.Set;

/* loaded from: classes2.dex */
public class RecorderManager {
    public static volatile RecorderManager mInstance;
    public final Set<IRecorder> mRecorders = new HashSet();

    public static RecorderManager getInstance() {
        if (mInstance == null) {
            synchronized (RecorderManager.class) {
                if (mInstance == null) {
                    mInstance = new RecorderManager();
                }
            }
        }
        return mInstance;
    }

    public void registerRecorder(IRecorder iRecorder) {
        this.mRecorders.add(iRecorder);
    }

    public void clearExpiredRecords() {
        DefaultLogger.d("galleryAction_RecorderManager", "clearExpiredRecords =>");
        for (IRecorder iRecorder : this.mRecorders) {
            if (iRecorder != null) {
                iRecorder.clearExpiredRecords();
            }
        }
    }

    public void onAddAccount() {
        onAddAccount(getHashAccount());
    }

    public void onDeleteAccount() {
        onDeleteAccount(getHashAccount());
    }

    public static String getHashAccount() {
        Account account = AccountCache.getAccount();
        if (account == null || TextUtils.isEmpty(account.name)) {
            return null;
        }
        return String.valueOf(account.name.hashCode());
    }

    public void onAddAccount(String str) {
        DefaultLogger.d("galleryAction_RecorderManager", "onAddAccount =>");
        if (TextUtils.isEmpty(str) || !BaseMiscUtil.isValid(this.mRecorders)) {
            return;
        }
        for (IRecorder iRecorder : this.mRecorders) {
            if (iRecorder != null && !iRecorder.isObserveAccountChanged()) {
                iRecorder.onAddAccount(str);
            }
        }
    }

    public void onDeleteAccount(String str) {
        DefaultLogger.d("galleryAction_RecorderManager", "onDeleteAccount =>");
        if (TextUtils.isEmpty(str) || !BaseMiscUtil.isValid(this.mRecorders)) {
            return;
        }
        for (IRecorder iRecorder : this.mRecorders) {
            if (iRecorder != null && !iRecorder.isObserveAccountChanged()) {
                iRecorder.onDeleteAccount(str);
            }
        }
    }
}
