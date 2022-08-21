package com.miui.gallery.cloud;

import android.accounts.Account;
import android.app.Notification;
import android.content.Intent;
import com.miui.gallery.service.IntentServiceBase;
import com.miui.gallery.util.NotificationHelper;
import com.miui.gallery.util.deprecated.Preference;

/* loaded from: classes.dex */
public class AccountChangeService extends IntentServiceBase {
    @Override // com.miui.gallery.service.IntentServiceBase
    public int getNotificationId() {
        return 10;
    }

    @Override // com.miui.gallery.service.IntentServiceBase
    public Notification getNotification() {
        return NotificationHelper.getEmptyNotification(getApplicationContext());
    }

    @Override // com.miui.gallery.service.IntentServiceBase, android.app.IntentService
    public void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);
        String action = intent.getAction();
        if ("delete_account".equals(action)) {
            Account account = (Account) intent.getParcelableExtra("extra_account");
            int i = 1;
            if (intent.getBooleanExtra("extra_wipe_data", true)) {
                i = 2;
            }
            Preference.setDeleteAccountStrategy(i);
            DeleteAccount.deleteAccountInTask(null, account, i, null);
        } else if (!"add_account".equals(action)) {
        } else {
            Preference.setDeleteAccountStrategy(0);
            AddAccount.onAddAccount(this, (Account) intent.getParcelableExtra("extra_account"));
        }
    }
}
