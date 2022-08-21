package com.miui.gallery.activity;

import android.os.Bundle;
import android.view.MenuItem;
import com.miui.gallery.R;
import com.miui.gallery.ui.PhotoSlimFragment;

/* loaded from: classes.dex */
public class PhotoSlimActivity extends BaseActivity {
    public PhotoSlimFragment mPhotoSlimFragment;

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.photo_slim_activity);
        this.mPhotoSlimFragment = (PhotoSlimFragment) getSupportFragmentManager().findFragmentById(R.id.photo_slim_fragment);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (!this.mPhotoSlimFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332 || !this.mPhotoSlimFragment.onBackPressed()) {
            return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }
}
