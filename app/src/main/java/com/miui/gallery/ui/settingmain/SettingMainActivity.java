package com.miui.gallery.ui.settingmain;

import android.content.res.Configuration;
import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.base_optimization.mvp.view.BaseToolBarActivity;
import com.miui.gallery.ui.album.cloudalbum.CloudAlbumListFragment;
import com.miui.gallery.ui.album.hiddenalbum.HiddenAlbumFragment;
import com.miui.gallery.util.SplitUtils;
import miui.os.Build;
import miuix.appcompat.app.floatingactivity.FloatingActivitySwitcher;

/* loaded from: classes2.dex */
public class SettingMainActivity extends BaseToolBarActivity {
    @Override // com.miui.gallery.base_optimization.mvp.view.Activity
    public int getLayoutId() {
        return R.layout.activity_setting_main;
    }

    @Override // com.miui.gallery.base_optimization.mvp.view.BaseToolBarActivity, com.miui.gallery.base_optimization.mvp.view.Activity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        loadFragment();
        setActivitySwitcher();
        updateConfiguration(getResources().getConfiguration());
    }

    public void setActivitySwitcher() {
        if (!needForceSplit()) {
            FloatingActivitySwitcher.install(this);
        }
    }

    public final void loadFragment() {
        int intExtra = getIntent().getIntExtra("extra_to_type", -1);
        if (intExtra == 1001) {
            setTitle(R.string.album_hidden_page_title);
            loadRootFragment(R.id.flSettingMain, HiddenAlbumFragment.newInstance(needForceSplit()));
        } else if (intExtra == 1002) {
            setTitle(R.string.album_cloud_list_page_title);
            loadRootFragment(R.id.flSettingMain, CloudAlbumListFragment.newInstance(needForceSplit()));
        } else {
            finish();
        }
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateConfiguration(configuration);
    }

    public final void updateConfiguration(Configuration configuration) {
        if (useDialog()) {
            setFloatingWindowMode(true);
        } else {
            setFloatingWindowMode(false);
        }
    }

    public boolean useDialog() {
        return (getResources().getInteger(R.integer.preference_dialog) == 1) && getIntent().getBooleanExtra("use_dialog", false) && !needForceSplit();
    }

    public boolean needForceSplit() {
        return (!Build.IS_TABLET || getIntent() == null || (SplitUtils.getMiuiFlags(getIntent()) & 16) == 0) ? false : true;
    }
}
