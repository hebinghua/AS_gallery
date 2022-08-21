package com.miui.gallery.activity.facebaby;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.R;
import com.miui.gallery.app.activity.MiuiActivity;
import com.miui.gallery.stat.SamplingStatHelper;

/* loaded from: classes.dex */
public abstract class BabyAlbumSettingActivity extends MiuiActivity {
    public BabyAlbumSettingsFragment mFragment;

    public abstract BabyAlbumSettingsFragment createFragment();

    public abstract String getPageName();

    @Override // com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.preference_container);
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        BabyAlbumSettingsFragment createFragment = createFragment();
        this.mFragment = createFragment;
        beginTransaction.replace(R.id.preference_container, createFragment).commit();
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        BabyAlbumSettingsFragment babyAlbumSettingsFragment = this.mFragment;
        if (babyAlbumSettingsFragment != null) {
            babyAlbumSettingsFragment.saveBabyInfo();
        }
        super.onBackPressed();
    }

    @Override // android.app.Activity
    public boolean onNavigateUp() {
        BabyAlbumSettingsFragment babyAlbumSettingsFragment = this.mFragment;
        if (babyAlbumSettingsFragment != null) {
            babyAlbumSettingsFragment.saveBabyInfo();
        }
        return super.onNavigateUp();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        SamplingStatHelper.recordPageEnd(this, getPageName());
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        SamplingStatHelper.recordPageStart(this, getPageName());
    }
}
