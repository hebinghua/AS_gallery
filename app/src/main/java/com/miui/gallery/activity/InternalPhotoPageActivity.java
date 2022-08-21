package com.miui.gallery.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.KeyboardShortcutGroup;
import android.view.Menu;
import androidx.fragment.app.Fragment;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.ui.PhotoPageFragment;
import com.miui.gallery.ui.photoPage.EnterTypeUtils;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.util.photoview.PhotoPageDataCache;
import java.util.List;

/* loaded from: classes.dex */
public class InternalPhotoPageActivity extends BaseActivity {
    public String mParentActivity;

    /* renamed from: $r8$lambda$Lu-Fa_0tj9OzB-bDIn_svbhzYRw */
    public static /* synthetic */ Fragment m468$r8$lambda$LuFa_0tj9OzBbDIn_svbhzYRw(Intent intent, String str) {
        return lambda$onCreate$0(intent, str);
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public int getFragmentContainerId() {
        return 16908290;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.permission.core.PermissionCheckCallback
    public Permission[] getRuntimePermissions() {
        return null;
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public boolean hasCustomContentView() {
        return true;
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity
    public boolean useDefaultScreenSceneMode() {
        return false;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (BaseBuildUtil.isLargeScreen(this)) {
            requestDisableStrategy(StrategyContext.DisableStrategyType.NAVIGATION_BAR);
        }
        if (bundle != null) {
            this.mParentActivity = bundle.getString("extra_photo_page_from");
            PhotoPageDataCache.getInstance().restoreInstance(bundle, this.mParentActivity);
        }
        final Intent intent = getIntent();
        if (TextUtils.isEmpty(this.mParentActivity)) {
            this.mParentActivity = intent.getExtras().getString("extra_photo_page_from", null);
        }
        repackageExtras();
        if (intent.getData() == null) {
            DefaultLogger.e("InternalPhotoPageActivity", "uri shouldn't be null");
            finish();
            return;
        }
        startFragment(new BaseActivity.FragmentCreator() { // from class: com.miui.gallery.activity.InternalPhotoPageActivity$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.activity.BaseActivity.FragmentCreator
            public final Fragment create(String str) {
                return InternalPhotoPageActivity.m468$r8$lambda$LuFa_0tj9OzBbDIn_svbhzYRw(intent, str);
            }
        }, "PhotoPageFragment", false, true);
    }

    public static /* synthetic */ Fragment lambda$onCreate$0(Intent intent, String str) {
        return PhotoPageFragment.newInstance(intent.getData(), intent.getType(), intent.getExtras(), 1);
    }

    public final void repackageExtras() {
        Bundle arguments = PhotoPageDataCache.getInstance().getArguments(this.mParentActivity);
        if (arguments != null) {
            Intent intent = getIntent();
            intent.setData((Uri) arguments.getParcelable("photo_data"));
            checkSetEnterType(arguments);
            intent.putExtras(arguments);
        }
    }

    public final void checkSetEnterType(Bundle bundle) {
        EnterTypeUtils.EnterType enterType = (EnterTypeUtils.EnterType) bundle.getParcelable("photo_page_enter_type");
        if (enterType == null || enterType == EnterTypeUtils.EnterType.FROM_NO_CARE) {
            if (BaseBuildUtil.isLowRamDevice()) {
                bundle.putParcelable("photo_page_enter_type", EnterTypeUtils.EnterType.FROM_COMMON_INTERNAL_WITH_CAMERA_ANIM);
            } else {
                bundle.putParcelable("photo_page_enter_type", EnterTypeUtils.EnterType.FROM_COMMON_INTERNAL);
            }
        }
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("extra_photo_page_from", this.mParentActivity);
        PhotoPageDataCache.getInstance().saveInstance(bundle, this.mParentActivity);
    }

    @Override // android.app.Activity
    public void onActivityReenter(int i, Intent intent) {
        PhotoPageFragment photoPageFragment = (PhotoPageFragment) getSupportFragmentManager().findFragmentByTag("PhotoPageFragment");
        if (photoPageFragment != null) {
            photoPageFragment.onActivityReenter(i, intent);
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        PhotoPageFragment photoPageFragment;
        super.onActivityResult(i, i2, intent);
        if (getSupportFragmentManager().getBackStackEntryCount() != 0 || (photoPageFragment = (PhotoPageFragment) getSupportFragmentManager().findFragmentByTag("PhotoPageFragment")) == null) {
            return;
        }
        photoPageFragment.onActivityResult(i, i2, intent);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, android.app.Activity
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
        PhotoPageDataCache.getInstance().clear(this.mParentActivity);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        PhotoPageFragment photoPageFragment;
        if (getSupportFragmentManager().getBackStackEntryCount() != 0 || (photoPageFragment = (PhotoPageFragment) getSupportFragmentManager().findFragmentByTag("PhotoPageFragment")) == null || !photoPageFragment.isVisible() || !photoPageFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onProvideKeyboardShortcuts(List<KeyboardShortcutGroup> list, Menu menu, int i) {
        PhotoPageFragment photoPageFragment = (PhotoPageFragment) getSupportFragmentManager().findFragmentByTag("PhotoPageFragment");
        if (photoPageFragment != null) {
            photoPageFragment.onProvideKeyboardShortcuts(list, menu, i);
        }
        super.onProvideKeyboardShortcuts(list, menu, i);
    }

    @Override // android.app.Activity
    public boolean onKeyShortcut(int i, KeyEvent keyEvent) {
        PhotoPageFragment photoPageFragment = (PhotoPageFragment) getSupportFragmentManager().findFragmentByTag("PhotoPageFragment");
        if (photoPageFragment == null || !photoPageFragment.onKeyShortcut(i, keyEvent)) {
            return super.onKeyShortcut(i, keyEvent);
        }
        return true;
    }
}
