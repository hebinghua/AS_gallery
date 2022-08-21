package com.miui.gallery.activity.facebaby;

import android.content.Intent;

/* loaded from: classes.dex */
public class OwnerBabyAlbumSettingActivity extends BabyAlbumSettingActivity {
    @Override // com.miui.gallery.activity.facebaby.BabyAlbumSettingActivity
    public String getPageName() {
        return "album_baby_share_owner_setting";
    }

    @Override // com.miui.gallery.activity.facebaby.BabyAlbumSettingActivity
    public BabyAlbumSettingsFragment createFragment() {
        return new OwnerBabyAlbumSettingsFragment();
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.mFragment.onActivityResult(i, i2, intent);
    }
}
