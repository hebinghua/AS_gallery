package com.miui.gallery.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import com.miui.gallery.ui.SecretAlbumDetailFragment;
import com.miui.gallery.util.IntentUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.List;

/* loaded from: classes.dex */
public class SecretAlbumDetailActivity extends BaseActivity {
    public static String TAG = "SecretAlbumDetailActivity";
    public SecretAlbumDetailFragment mSecretAlbumDetailFragment;

    @Override // com.miui.gallery.activity.BaseActivity
    public int getFragmentContainerId() {
        return 16908290;
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public boolean hasCustomContentView() {
        return true;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Intent intent = getIntent();
        if (needPassWordSecretAlbum(intent)) {
            intent.putExtra("other_enter_secret_album", true);
        }
        this.mSecretAlbumDetailFragment = (SecretAlbumDetailFragment) startFragment(SecretAlbumDetailActivity$$ExternalSyntheticLambda0.INSTANCE, "albumDetail", false, true);
    }

    public static /* synthetic */ Fragment lambda$onCreate$0(String str) {
        return new SecretAlbumDetailFragment();
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        SecretAlbumDetailFragment secretAlbumDetailFragment = this.mSecretAlbumDetailFragment;
        if (secretAlbumDetailFragment == null || !secretAlbumDetailFragment.isVisible()) {
            return false;
        }
        this.mSecretAlbumDetailFragment.onCreateOptionsMenu(menu, getMenuInflater());
        return true;
    }

    public final boolean needPassWordSecretAlbum(Intent intent) {
        String callingPackage = IntentUtil.getCallingPackage(this);
        if (TextUtils.equals(callingPackage, "com.miui.voiceassist")) {
            DefaultLogger.d(TAG, "opening secret album from voice assistant");
            return true;
        } else if (intent.getLongExtra("album_id", -1L) != -1000 || TextUtils.equals(callingPackage, "com.miui.gallery")) {
            return false;
        } else {
            DefaultLogger.e(TAG, "someone is attacking secret album");
            return true;
        }
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        SecretAlbumDetailFragment secretAlbumDetailFragment = this.mSecretAlbumDetailFragment;
        if (secretAlbumDetailFragment != null && secretAlbumDetailFragment.isVisible()) {
            this.mSecretAlbumDetailFragment.onPrepareOptionsMenu(menu);
            return true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        SecretAlbumDetailFragment secretAlbumDetailFragment = this.mSecretAlbumDetailFragment;
        if (secretAlbumDetailFragment == null || !secretAlbumDetailFragment.isVisible() || !this.mSecretAlbumDetailFragment.onOptionsItemSelected(menuItem)) {
            return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        SecretAlbumDetailFragment secretAlbumDetailFragment = this.mSecretAlbumDetailFragment;
        if (secretAlbumDetailFragment != null) {
            secretAlbumDetailFragment.onActivityResult(i, i2, intent);
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        SecretAlbumDetailFragment secretAlbumDetailFragment = this.mSecretAlbumDetailFragment;
        if (secretAlbumDetailFragment != null) {
            secretAlbumDetailFragment.onProvideKeyboardShortcuts(list, menu, i);
        }
        super.onProvideKeyboardShortcuts(list, menu, i);
    }

    @Override // android.app.Activity
    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        SecretAlbumDetailFragment secretAlbumDetailFragment = this.mSecretAlbumDetailFragment;
        if (secretAlbumDetailFragment == null || !secretAlbumDetailFragment.onKeyShortcut(i, keyEvent)) {
            return super.onKeyShortcut(i, keyEvent);
        }
        return true;
    }
}
