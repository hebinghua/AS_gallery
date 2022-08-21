package com.miui.gallery.activity.facebaby;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.MenuItem;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.ui.FacePageFragment;
import java.util.List;

/* loaded from: classes.dex */
public class FacePageActivity extends BaseActivity {
    public FacePageFragment mFaceFragment;

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.face_page_activity);
        this.mFaceFragment = (FacePageFragment) getSupportFragmentManager().findFragmentById(R.id.faceFragment);
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        setImmersionMenuEnabled(true);
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.face_album_menu, menu);
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean isFaceDisplayMode = this.mFaceFragment.isFaceDisplayMode();
        MenuItem findItem = menu.findItem(R.id.action_change_mode_to_photo);
        if (findItem != null) {
            findItem.setVisible(isFaceDisplayMode);
        }
        MenuItem findItem2 = menu.findItem(R.id.action_change_mode_to_face);
        if (findItem2 != null) {
            findItem2.setVisible(!isFaceDisplayMode);
        }
        return true;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (!this.mFaceFragment.onOptionsItemSelected(menuItem)) {
            return super.onOptionsItemSelected(menuItem);
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.mFaceFragment.onActivityResult(i, i2, intent);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        FacePageFragment facePageFragment = this.mFaceFragment;
        if (facePageFragment != null) {
            facePageFragment.onProvideKeyboardShortcuts(list, menu, i);
        }
        super.onProvideKeyboardShortcuts(list, menu, i);
    }

    @Override // android.app.Activity
    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        FacePageFragment facePageFragment = this.mFaceFragment;
        if (facePageFragment == null || !facePageFragment.onKeyShortcut(i, keyEvent)) {
            return super.onKeyShortcut(i, keyEvent);
        }
        return true;
    }
}
