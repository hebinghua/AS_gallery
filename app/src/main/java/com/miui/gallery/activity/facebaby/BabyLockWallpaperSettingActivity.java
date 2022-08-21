package com.miui.gallery.activity.facebaby;

import android.os.Bundle;
import android.view.MenuItem;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.R;
import com.miui.gallery.app.activity.MiuiActivity;

/* loaded from: classes.dex */
public class BabyLockWallpaperSettingActivity extends MiuiActivity {
    public BabyLockWallpaperSettingsFragment mFragment;

    @Override // com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.preference_container);
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        BabyLockWallpaperSettingsFragment babyLockWallpaperSettingsFragment = new BabyLockWallpaperSettingsFragment();
        this.mFragment = babyLockWallpaperSettingsFragment;
        beginTransaction.replace(R.id.preference_container, babyLockWallpaperSettingsFragment).commit();
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        this.mFragment.onExit();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == 16908332) {
            this.mFragment.onExit();
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
