package com.xiaomi.micloudsdk.sync;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SyncAdapterType;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.micloudsdk.sync.utils.SyncContentUtils;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes3.dex */
public class MiCloudResolver {
    public static void requestSync(Context context, Account account, String str) {
        assertContext(context);
        assertAccount(account);
        assertAuthority(str);
        Log.d("MiCloudResolver", "requestSync: authority: " + str);
        if (isSyncPausing(context, account, str)) {
            Log.e("MiCloudResolver", "requestSync: authority: " + str + "is paused. request sync fail");
        }
        ContentResolver.requestSync(account, str, new Bundle());
    }

    public static void cancelSync(Context context, Account account, String str) {
        assertContext(context);
        assertAccount(account);
        assertAuthority(str);
        Log.d("MiCloudResolver", "cancelSync: authority: " + str);
        ContentResolver.cancelSync(account, str);
        sendCancelCommand(context, str);
    }

    public static void pauseSync(Context context, Account account, String str, long j) {
        assertContext(context);
        assertAccount(account);
        assertAuthority(str);
        assertTime(j);
        Log.d("MiCloudResolver", "pauseSync: authority: " + str + ", time: " + j);
        cancelSync(context, account, str);
        SyncContentUtils.savePauseTime(context, str, j);
        tryToScheduleResume(context);
    }

    public static void resumeSync(Context context, Account account, String str) {
        assertContext(context);
        assertAccount(account);
        assertAuthority(str);
        Log.d("MiCloudResolver", "resumeSync: authority: " + str);
        SyncContentUtils.insertPauseExceptAuthority(context, str);
        SyncContentUtils.savePauseTime(context, str, 0L);
        requestSync(context, account, str);
        tryToScheduleResume(context);
    }

    public static long getResumeTime(Context context, String str) {
        assertContext(context);
        assertAuthority(str);
        Log.d("MiCloudResolver", "getResumeTime: authority: " + str);
        boolean isPauseExceptAuthority = SyncContentUtils.isPauseExceptAuthority(context, str);
        long loadResumeTime = SyncContentUtils.loadResumeTime(context, str);
        if (isPauseExceptAuthority) {
            return loadResumeTime;
        }
        long loadResumeTime2 = SyncContentUtils.loadResumeTime(context, "_all");
        return loadResumeTime2 < loadResumeTime ? loadResumeTime : loadResumeTime2;
    }

    public static boolean isSyncPausing(Context context, Account account, String str) {
        assertContext(context);
        assertAccount(account);
        assertAuthority(str);
        Log.d("MiCloudResolver", "isSyncPausing: authority: " + str);
        return System.currentTimeMillis() <= getResumeTime(context, str);
    }

    public static void assertContext(Context context) {
        if (context != null) {
            return;
        }
        throw new IllegalArgumentException("context is null");
    }

    public static void assertAccount(Account account) {
        if (account == null || !"com.xiaomi".equals(account.type)) {
            throw new IllegalArgumentException("illegal account");
        }
    }

    public static void assertAuthority(String str) {
        if (TextUtils.isEmpty(str)) {
            throw new IllegalArgumentException("illegal authority: empty");
        }
        if (getAuthorityList().contains(str)) {
            return;
        }
        throw new IllegalArgumentException("illegal authority: not registered authority: " + str);
    }

    public static void assertTime(long j) {
        if (j >= 0) {
            return;
        }
        throw new IllegalArgumentException("illegal time");
    }

    public static List<String> getAuthorityList() {
        SyncAdapterType[] syncAdapterTypes;
        ArrayList arrayList = new ArrayList();
        for (SyncAdapterType syncAdapterType : ContentResolver.getSyncAdapterTypes()) {
            if ("com.xiaomi".equals(syncAdapterType.accountType)) {
                arrayList.add(syncAdapterType.authority);
            }
        }
        return arrayList;
    }

    public static void tryToScheduleResume(Context context) {
        Intent intent = new Intent("com.xiaomi.action.SYNC_RESUME");
        intent.setPackage("com.miui.cloudservice");
        intent.putExtra("extra_operation", "alarm");
        if (context.getPackageManager().resolveService(intent, 32) != null) {
            context.startService(intent);
        } else {
            context.sendBroadcast(intent);
        }
    }

    public static void sendCancelCommand(Context context, String str) {
        String str2 = SyncCommandConstant.SUPPORT_COMMAND_PKGS.get(str);
        if (TextUtils.isEmpty(str2)) {
            Log.e("MiCloudResolver", "sendCancelCommand: " + str + " doesn't match pkgName");
            return;
        }
        Intent intent = new Intent("com.xiaomi.micloud.action.SYNC_COMMAND");
        intent.putExtra("key_command", "value_command_cancel_sync");
        intent.setPackage(str2);
        List<ResolveInfo> queryBroadcastReceivers = context.getPackageManager().queryBroadcastReceivers(intent, 0);
        if (queryBroadcastReceivers != null && queryBroadcastReceivers.size() > 0) {
            context.sendBroadcast(intent);
            return;
        }
        try {
            context.startService(intent);
        } catch (IllegalStateException e) {
            Log.e("MiCloudResolver", "sendCancelCommandFailed: ", e);
        }
    }
}
