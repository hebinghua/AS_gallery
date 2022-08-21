package com.miui.gallery.activity;

import android.accounts.Account;
import android.content.ContentResolver;
import android.content.Intent;
import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.cloud.AccountCache;
import com.miui.gallery.util.SplitUtils;
import miui.cloud.util.SyncAutoSettingUtil;

/* loaded from: classes.dex */
public class CloudSettingsActivity extends FloatingWindowActivity {
    @Override // com.miui.gallery.activity.FloatingWindowActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Account account = AccountCache.getAccount();
        if (account != null && SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically() && ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider")) {
            Intent intent = new Intent(this, BackupSettingsActivity.class);
            if (needForceSplit()) {
                SplitUtils.addMiuiFlags(intent, 16);
            }
            startActivity(intent);
            finish();
            return;
        }
        setContentView(R.layout.gallery_settings);
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        Account account = AccountCache.getAccount();
        if (account != null && SyncAutoSettingUtil.getXiaomiGlobalSyncAutomatically() && ContentResolver.getSyncAutomatically(account, "com.miui.gallery.cloud.provider")) {
            finish();
        }
    }
}
