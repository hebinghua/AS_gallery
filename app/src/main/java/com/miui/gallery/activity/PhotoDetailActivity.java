package com.miui.gallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import com.miui.gallery.R;
import com.miui.gallery.ui.photodetail.PhotoDetailFragment;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class PhotoDetailActivity extends com.miui.gallery.app.base.BaseActivity {
    public PhotoDetailFragment mPhotoDetailFragment;

    @Override // com.miui.gallery.base_optimization.mvp.view.Activity
    public int getLayoutId() {
        return R.layout.photo_detail_activity;
    }

    @Override // com.miui.gallery.app.base.BaseActivity
    public boolean supportShowOnScreenLocked() {
        return true;
    }

    @Override // com.miui.gallery.app.base.BaseActivity, com.miui.gallery.app.base.BasePermissionCheckActivitity, com.miui.gallery.base_optimization.mvp.view.Activity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        PhotoDetailFragment photoDetailFragment = (PhotoDetailFragment) getSupportFragmentManager().findFragmentById(R.id.frame_photodetail_main);
        this.mPhotoDetailFragment = photoDetailFragment;
        if (photoDetailFragment == null) {
            PhotoDetailFragment newInstance = PhotoDetailFragment.newInstance();
            this.mPhotoDetailFragment = newInstance;
            DefaultLogger.d("PhotoDetailActivity", "use new fragment(%d)", Integer.valueOf(newInstance.hashCode()));
        } else {
            DefaultLogger.d("PhotoDetailActivity", "use cache fragment(%d)", Integer.valueOf(photoDetailFragment.hashCode()));
        }
        loadRootFragment(R.id.frame_photodetail_main, this.mPhotoDetailFragment);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mPhotoDetailFragment.onCreateOptionsMenu(menu, getMenuInflater());
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        this.mPhotoDetailFragment.onPrepareOptionsMenu(menu);
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (this.mPhotoDetailFragment.onOptionsItemSelected(menuItem)) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.mPhotoDetailFragment.onActivityResult(i, i2, intent);
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        PhotoDetailFragment photoDetailFragment;
        if (i == 4 && (photoDetailFragment = this.mPhotoDetailFragment) != null) {
            photoDetailFragment.onBackPressed();
            DefaultLogger.d("PhotoDetailActivity", "back by fragment(%d)", Integer.valueOf(this.mPhotoDetailFragment.hashCode()));
            return true;
        }
        return super.onKeyDown(i, keyEvent);
    }
}
