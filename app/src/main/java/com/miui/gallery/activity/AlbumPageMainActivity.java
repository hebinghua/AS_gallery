package com.miui.gallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import com.miui.gallery.R;
import com.miui.gallery.app.base.BaseFragment;
import com.miui.gallery.app.base.BaseToolBarActivity;
import com.miui.gallery.ui.album.aialbum.AIAlbumPageFragment;
import com.miui.gallery.ui.album.otheralbum.OtherAlbumFragment;
import com.miui.gallery.ui.album.rubbishalbum.RubbishAlbumFragment;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes.dex */
public class AlbumPageMainActivity extends BaseToolBarActivity {
    public BaseFragment mFragment;

    @Override // com.miui.gallery.base_optimization.mvp.view.Activity
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override // com.miui.gallery.app.base.BaseToolBarActivity, com.miui.gallery.base_optimization.mvp.view.BaseToolBarActivity, com.miui.gallery.base_optimization.mvp.view.Activity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        loadFragment();
    }

    public final void loadFragment() {
        switch (parseType(getIntent())) {
            case 1003:
                setTitle(R.string.album_other_page_title);
                this.mFragment = OtherAlbumFragment.newInstance();
                break;
            case 1004:
                setTitle(R.string.album_rubbish_page_title);
                RubbishAlbumFragment newInstance = RubbishAlbumFragment.newInstance();
                this.mFragment = newInstance;
                newInstance.setHasOptionsMenu(true);
                break;
            case 1005:
                setTitle(R.string.album_ai_page_title);
                this.mFragment = AIAlbumPageFragment.newInstance();
                break;
            default:
                DefaultLogger.e("AlbumPageMainActivity", "parse enter type failed,intent info:%s", getIntent().toString());
                finish();
                return;
        }
        loadRootFragment(R.id.fl_album_main_page, this.mFragment);
    }

    public final int parseType(Intent intent) {
        int intExtra = intent.getIntExtra("extra_to_type", -1);
        if (-1 != intExtra || intent.getData() == null) {
            return intExtra;
        }
        String path = intent.getData().getPath();
        if (path.contains("rubbish")) {
            return 1004;
        }
        if (path.contains("ai_album")) {
            return 1005;
        }
        if (!path.contains("other")) {
            return intExtra;
        }
        return 1003;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        Fragment topFragment = getTopFragment();
        if (topFragment != null) {
            topFragment.onCreateOptionsMenu(menu, getMenuInflater());
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        Fragment topFragment = getTopFragment();
        if (topFragment != null) {
            topFragment.onPrepareOptionsMenu(menu);
            return true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        Fragment topFragment = getTopFragment();
        if (topFragment == null || !topFragment.onOptionsItemSelected(menuItem)) {
            return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        BaseFragment baseFragment = this.mFragment;
        if (baseFragment == null || !baseFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override // com.miui.gallery.app.base.BaseToolBarActivity, com.miui.gallery.base_optimization.mvp.view.Activity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        BaseFragment baseFragment = this.mFragment;
        if (baseFragment != null) {
            baseFragment.onActivityDestroy();
        }
        super.onDestroy();
    }
}
