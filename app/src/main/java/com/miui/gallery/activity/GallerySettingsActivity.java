package com.miui.gallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import com.miui.gallery.R;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.ui.GallerySettingsFragment;
import miuix.appcompat.app.ActionBar;

/* loaded from: classes.dex */
public class GallerySettingsActivity extends FloatingWindowActivity {
    public GallerySettingsFragment mFragment;

    @Override // com.miui.gallery.activity.FloatingWindowActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        if (useDialog()) {
            setTheme(2131952051);
        }
        super.onCreate(bundle);
        setContentView(R.layout.gallery_settings);
        this.mFragment = (GallerySettingsFragment) getSupportFragmentManager().findFragmentById(R.id.gallery_settings);
        AutoTracking.track(GallerySettingsFragment.class.getCanonicalName());
        ActionBar appCompatActionBar = getAppCompatActionBar();
        if (appCompatActionBar == null || !needHideBackAndFixedSmallTitle()) {
            return;
        }
        appCompatActionBar.setExpandState(0);
        appCompatActionBar.setResizable(false);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        if (i == 25 || i == 24) {
            this.mFragment.onKeyDown(i, keyEvent);
        }
        return super.onKeyDown(i, keyEvent);
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        this.mFragment.onActivityResult(i, i2, intent);
    }
}
