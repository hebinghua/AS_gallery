package com.miui.gallery.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import android.view.MenuItem;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.miui.gallery.ui.JumpDialogFragment;
import com.miui.gallery.ui.ModernAlbumDetailFragment;
import com.miui.gallery.viewmodel.AlbumDetailViewModel;
import java.util.List;

/* loaded from: classes.dex */
public class AlbumDetailActivity extends BaseActivity {
    public ModernAlbumDetailFragment mAlbumDetailFragment;
    public AlbumDetailViewModel mViewModel;

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
        if (isSecretAlbum(intent).booleanValue()) {
            return;
        }
        AlbumDetailViewModel albumDetailViewModel = (AlbumDetailViewModel) new ViewModelProvider(this).get(AlbumDetailViewModel.class);
        this.mViewModel = albumDetailViewModel;
        albumDetailViewModel.setIsAlbumGroup(false);
        if ("android.intent.action.VIEW".equals(intent.getAction())) {
            JumpDialogFragment.obtainAlbumIntent(this, intent.getData(), new AnonymousClass1());
            return;
        }
        this.mAlbumDetailFragment = (ModernAlbumDetailFragment) startFragment(AlbumDetailActivity$$ExternalSyntheticLambda0.INSTANCE, "albumDetail", false, true);
    }

    /* renamed from: com.miui.gallery.activity.AlbumDetailActivity$1  reason: invalid class name */
    /* loaded from: classes.dex */
    public class AnonymousClass1 implements JumpDialogFragment.HandleIntentCallback {
        public AnonymousClass1() {
        }

        @Override // com.miui.gallery.ui.JumpDialogFragment.HandleIntentCallback
        public void onHandleIntent(Intent intent) {
            if (AlbumDetailActivity.this.isSecretAlbum(intent).booleanValue()) {
                return;
            }
            AlbumDetailActivity.this.setIntent(intent);
            AlbumDetailActivity albumDetailActivity = AlbumDetailActivity.this;
            albumDetailActivity.mAlbumDetailFragment = (ModernAlbumDetailFragment) albumDetailActivity.startFragment(AlbumDetailActivity$1$$ExternalSyntheticLambda0.INSTANCE, "albumDetail", false, true);
        }

        public static /* synthetic */ Fragment lambda$onHandleIntent$0(String str) {
            return new ModernAlbumDetailFragment();
        }

        @Override // com.miui.gallery.ui.JumpDialogFragment.HandleIntentCallback
        public void onHandleFailed(Context context, String str) {
            AlbumDetailActivity.this.finish();
        }
    }

    public static /* synthetic */ Fragment lambda$onCreate$0(String str) {
        return new ModernAlbumDetailFragment();
    }

    public Boolean isSecretAlbum(Intent intent) {
        if (intent.getLongExtra("album_id", -1L) == -1000) {
            finish();
            return Boolean.TRUE;
        }
        return Boolean.FALSE;
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onCreateOptionsMenu(Menu menu) {
        ModernAlbumDetailFragment modernAlbumDetailFragment = this.mAlbumDetailFragment;
        if (modernAlbumDetailFragment == null || !modernAlbumDetailFragment.isVisible()) {
            return false;
        }
        this.mAlbumDetailFragment.onCreateOptionsMenu(menu, getMenuInflater());
        return true;
    }

    @Override // android.app.Activity
    public boolean onPrepareOptionsMenu(Menu menu) {
        ModernAlbumDetailFragment modernAlbumDetailFragment = this.mAlbumDetailFragment;
        if (modernAlbumDetailFragment != null && modernAlbumDetailFragment.isVisible()) {
            this.mAlbumDetailFragment.onPrepareOptionsMenu(menu);
            return true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        ModernAlbumDetailFragment modernAlbumDetailFragment = this.mAlbumDetailFragment;
        if (modernAlbumDetailFragment == null || !modernAlbumDetailFragment.isVisible() || !this.mAlbumDetailFragment.onOptionsItemSelected(menuItem)) {
            return super.onOptionsItemSelected(menuItem);
        }
        return true;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        ModernAlbumDetailFragment modernAlbumDetailFragment = this.mAlbumDetailFragment;
        if (modernAlbumDetailFragment != null) {
            modernAlbumDetailFragment.onActivityResult(i, i2, intent);
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
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
