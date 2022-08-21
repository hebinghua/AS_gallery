package com.miui.gallery.activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import com.miui.gallery.BaseConfig$ScreenConfig;
import com.miui.gallery.R;
import com.miui.gallery.permission.core.Permission;
import com.miui.gallery.preference.MemoryPreferenceHelper;
import com.miui.gallery.ui.PhotoPageFragment;
import com.miui.gallery.util.ScreenUtils;

/* loaded from: classes.dex */
public class ExternalPhotoPageActivity extends BaseExternalPhotoPageActivity {
    public boolean mSupportWindowAnim;

    @Override // com.miui.gallery.activity.BaseExternalPhotoPageActivity
    public boolean isTranslucent() {
        return false;
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity
    public boolean useDefaultScreenSceneMode() {
        return false;
    }

    @Override // com.miui.gallery.activity.BaseExternalPhotoPageActivity
    public void doIfFromCamera(Intent intent, Bundle bundle) {
        int screenOrientation = BaseExternalPhotoPageActivity.getScreenOrientation(bundle.getInt("device_orientation", Integer.MAX_VALUE));
        int integer = getResources().getInteger(R.integer.photo_page_anim_support_width);
        int integer2 = getResources().getInteger(R.integer.photo_page_anim_support_height);
        if (BaseConfig$ScreenConfig.getScreenWidth() == integer && Math.abs(ScreenUtils.getScreenHeight() - integer2) <= 100 && (screenOrientation == 1 || (screenOrientation == -1 && getResources().getConfiguration().orientation == 1))) {
            getWindow().setWindowAnimations(2131951631);
            this.mSupportWindowAnim = true;
            return;
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(-16777216));
    }

    @Override // com.miui.gallery.activity.BaseExternalPhotoPageActivity, miuix.appcompat.app.AppCompatActivity, android.app.Activity
    public void finish() {
        finishAndRemoveTask();
        if (this.mSupportWindowAnim) {
            PhotoPageFragment photoPageFragment = (PhotoPageFragment) getSupportFragmentManager().findFragmentByTag("PhotoPageFragment");
            if (!this.mSupportWindowAnim) {
                return;
            }
            if (photoPageFragment != null && photoPageFragment.isStartingHomePage()) {
                return;
            }
            overridePendingTransition(R.anim.photo_page_close_enter, R.anim.photo_page_close_exit);
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public void checkPermission() {
        Permission[] runtimePermissions = getRuntimePermissions();
        if (runtimePermissions != null && runtimePermissions.length == 1 && "android.permission.WRITE_EXTERNAL_STORAGE".equals(runtimePermissions[0].mName) && !MemoryPreferenceHelper.getBoolean("is_need_check_write_external_storage_permission", true)) {
            onPermissionsChecked(runtimePermissions, new int[]{0}, new boolean[]{false});
        } else {
            super.checkPermission();
        }
    }
}
