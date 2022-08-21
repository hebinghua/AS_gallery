package com.miui.gallery.activity;

import android.os.Bundle;
import com.miui.gallery.app.activity.GalleryActivity;
import com.miui.gallery.ui.BackupLoginSettingsDialogFragment;

/* loaded from: classes.dex */
public class BackupSettingsLoginActivity extends GalleryActivity {
    public BackupLoginSettingsDialogFragment mFragment;

    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        getWindow().addFlags(67108864);
        getWindow().setFlags(1024, 1024);
        BackupLoginSettingsDialogFragment backupLoginSettingsDialogFragment = new BackupLoginSettingsDialogFragment();
        this.mFragment = backupLoginSettingsDialogFragment;
        backupLoginSettingsDialogFragment.finishActivityWhenDone(true);
        this.mFragment.showAllowingStateLoss(getSupportFragmentManager(), "BackupLoginSettingsDialogFragment");
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }
}
