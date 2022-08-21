package com.miui.gallery.activity.facebaby;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.MenuItem;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.ui.BabyAlbumDetailFragment;
import java.util.List;

/* loaded from: classes.dex */
public class BabyAlbumDetailActivity extends BaseActivity {
    public BabyAlbumDetailFragment mAlbumDetailFragment;

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.baby_album_detail_activity);
        this.mAlbumDetailFragment = (BabyAlbumDetailFragment) getSupportFragmentManager().findFragmentById(R.id.baby_album_detail);
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.mAlbumDetailFragment.onActivityResult(i, i2, intent);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        this.mAlbumDetailFragment.onCreateOptionsMenu(menu, getMenuInflater());
        requestDisableStrategy(StrategyContext.DisableStrategyType.NAVIGATION_BAR);
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (this.mAlbumDetailFragment.onOptionsItemSelected(menuItem)) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        this.mAlbumDetailFragment.onProvideKeyboardShortcuts(list, menu, i);
        super.onProvideKeyboardShortcuts(list, menu, i);
    }

    @Override // android.app.Activity
    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        if (this.mAlbumDetailFragment.onKeyShortcut(i, keyEvent)) {
            return true;
        }
        return super.onKeyShortcut(i, keyEvent);
    }
}
