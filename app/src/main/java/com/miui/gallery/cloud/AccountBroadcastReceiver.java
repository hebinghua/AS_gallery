package com.miui.gallery.cloud;

import android.accounts.Account;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import com.miui.gallery.util.BackgroundServiceHelper;
import com.miui.gallery.util.SyncLogger;
import com.miui.gallery.util.deviceprovider.ApplicationHelper;

/* loaded from: classes.dex */
public class AccountBroadcastReceiver extends BroadcastReceiver {
    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Account account;
        SyncLogger.d("AccountBroadcastReceiver", "onReceive broadcast");
        String action = intent.getAction();
        if (!isFromAccount(intent)) {
            SyncLogger.d("AccountBroadcastReceiver", "error package send account broadcast");
        }
        if ("android.accounts.LOGIN_ACCOUNTS_PRE_CHANGED".equals(action)) {
            Account account2 = (Account) intent.getParcelableExtra("extra_account");
            if (account2 != null && !ApplicationHelper.getIntentProvider().getXiaomiAccountType().equals(account2.type)) {
                return;
            }
            GalleryCloudUtils.resetAccountCache();
            if (account2 == null) {
                return;
            }
            com.miui.gallery.request.HostManager.clearCookies();
            boolean z = true;
            if (intent.getIntExtra("extra_update_type", -1) != 1) {
                return;
            }
            Bundle bundleExtra = intent.getBundleExtra("extra_bundle");
            if (bundleExtra != null && bundleExtra.containsKey("extra_wipe_data")) {
                z = bundleExtra.getBoolean("extra_wipe_data", true);
            }
            Intent intent2 = new Intent(context, AccountChangeService.class);
            intent2.setAction("delete_account");
            intent2.putExtra("extra_account", account2);
            intent2.putExtra("extra_wipe_data", z);
            BackgroundServiceHelper.startForegroundServiceIfNeed(context, intent2);
        } else if (!"android.accounts.LOGIN_ACCOUNTS_POST_CHANGED".equals(action) || (account = (Account) intent.getParcelableExtra("extra_account")) == null || !ApplicationHelper.getIntentProvider().getXiaomiAccountType().equals(account.type) || 2 != intent.getIntExtra("extra_update_type", -1)) {
        } else {
            Intent intent3 = new Intent(context, AccountChangeService.class);
            intent3.setAction("add_account");
            intent3.putExtra("extra_account", account);
            BackgroundServiceHelper.startForegroundServiceIfNeed(context, intent3);
        }
    }

    public final boolean isFromAccount(Intent intent) {
        try {
            Object invoke = Class.forName("android.content.Intent").getMethod("getSender", new Class[0]).invoke(intent, new Object[0]);
            if (invoke instanceof String) {
                return "com.xiaomi.account".equals((String) invoke);
            }
        } catch (Exception unused) {
        }
        return true;
    }
}
