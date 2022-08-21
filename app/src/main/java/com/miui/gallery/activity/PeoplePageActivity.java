package com.miui.gallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.MenuItem;
import com.miui.gallery.R;
import com.miui.gallery.cloud.base.SyncRequest;
import com.miui.gallery.cloud.base.SyncType;
import com.miui.gallery.ui.PeoplePageFragment;
import com.miui.gallery.util.SyncUtil;
import java.util.List;

/* loaded from: classes.dex */
public class PeoplePageActivity extends BaseActivity {
    public PeoplePageFragment mPeopleFragment;
    public final Handler mMainHandler = new Handler();
    public Runnable mRequestSyncRunnable = new Runnable() { // from class: com.miui.gallery.activity.PeoplePageActivity.1
        @Override // java.lang.Runnable
        public void run() {
            SyncUtil.requestSync(PeoplePageActivity.this, new SyncRequest.Builder().setSyncType(SyncType.NORMAL).setSyncReason(8L).build());
        }
    };

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.people_page_activity);
        this.mPeopleFragment = (PeoplePageFragment) getSupportFragmentManager().findFragmentById(R.id.people_page);
        this.mMainHandler.postDelayed(this.mRequestSyncRunnable, 3000L);
        setImmersionMenuEnabled(true);
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.people_album_menu, menu);
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (this.mPeopleFragment.onOptionsItemSelected(menuItem)) {
            return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        this.mMainHandler.removeCallbacks(this.mRequestSyncRunnable);
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.mPeopleFragment.onActivityResult(i, i2, intent);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        this.mPeopleFragment.onProvideKeyboardShortcuts(list, menu, i);
        super.onProvideKeyboardShortcuts(list, menu, i);
    }

    @Override // android.app.Activity
    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        if (this.mPeopleFragment.onKeyShortcut(i, keyEvent)) {
            return true;
        }
        return super.onKeyShortcut(i, keyEvent);
    }
}
