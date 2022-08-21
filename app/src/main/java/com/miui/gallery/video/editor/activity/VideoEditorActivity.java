package com.miui.gallery.video.editor.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.android.internal.WindowCompat;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.compat.app.ActivityCompat;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.SystemUiUtil;
import com.miui.gallery.video.editor.config.VideoEditorConfig;
import com.miui.gallery.video.editor.ui.MenuFragment;
import com.miui.gallery.video.editor.ui.VideoEditorFragment;
import com.miui.gallery.view.BrightnessManager;
import com.miui.gallery.vlog.VideoEditorEntranceUtils;

/* loaded from: classes2.dex */
public class VideoEditorActivity extends BaseActivity {
    public BrightnessManager mBrightnessManager;
    public boolean mIsInMultiWindowMode;
    public boolean mNightMode;
    public OnBackPressedListener mOnBackPressedListener;
    public VideoEditorFragment mVideoEditorFragment;

    /* loaded from: classes2.dex */
    public interface OnBackPressedListener {
        boolean onBackPressed();
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public boolean hasCustomContentView() {
        return true;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(null);
        if (VideoEditorEntranceUtils.isAvailable()) {
            finish();
            return;
        }
        WindowCompat.setCutoutModeShortEdges(getWindow());
        SystemUiUtil.setDrawSystemBarBackground(getWindow());
        VideoEditorConfig.init(this);
        initView();
        initScreenBrightness();
        this.mIsInMultiWindowMode = ActivityCompat.isInMultiWindowMode(this);
        this.mNightMode = BaseMiscUtil.isNightMode(this);
    }

    public final void initScreenBrightness() {
        Intent intent = getIntent();
        if (intent != null) {
            float floatExtra = intent.getFloatExtra("photo-brightness-manual", -1.0f);
            float floatExtra2 = intent.getFloatExtra("photo-brightness-auto", -1.0f);
            if (floatExtra < 0.0f && floatExtra2 < 0.0f) {
                return;
            }
            this.mBrightnessManager = new BrightnessManager(this, floatExtra, floatExtra2);
        }
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onAttachedToWindow() {
        if (!WindowCompat.isNotch(this) || this.mIsInMultiWindowMode) {
            return;
        }
        SystemUiUtil.extendToStatusBar(getWindow().getDecorView());
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, android.app.Activity
    public void onMultiWindowModeChanged(boolean z, Configuration configuration) {
        super.onMultiWindowModeChanged(z, configuration);
        this.mIsInMultiWindowMode = z;
        if (z) {
            SystemUiUtil.showSystemBars(getWindow().getDecorView(), this.mIsInMultiWindowMode);
        } else {
            SystemUiUtil.hideSystemBars(getWindow().getDecorView(), !this.mNightMode, false, false);
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        BrightnessManager brightnessManager = this.mBrightnessManager;
        if (brightnessManager != null) {
            brightnessManager.onResume();
        }
        if (this.mIsInMultiWindowMode) {
            SystemUiUtil.showSystemBars(getWindow().getDecorView(), isInMultiWindowMode());
        } else {
            SystemUiUtil.setWindowFullScreenFlag(this);
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        BrightnessManager brightnessManager = this.mBrightnessManager;
        if (brightnessManager != null) {
            brightnessManager.onPause();
        }
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, android.app.Activity, android.view.Window.Callback
    public void onWindowFocusChanged(boolean z) {
        super.onWindowFocusChanged(z);
        BrightnessManager brightnessManager = this.mBrightnessManager;
        if (brightnessManager != null) {
            brightnessManager.onWindowFocusChanged(z);
        }
    }

    public void setOnBackPressedListener(OnBackPressedListener onBackPressedListener) {
        this.mOnBackPressedListener = onBackPressedListener;
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        OnBackPressedListener onBackPressedListener = this.mOnBackPressedListener;
        if (onBackPressedListener == null || !onBackPressedListener.onBackPressed()) {
            super.onBackPressed();
        }
    }

    public final void initView() {
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_video_editor);
        loadVideoEditorFragment();
    }

    public void loadVideoEditorFragment() {
        if (!isFinishing()) {
            this.mVideoEditorFragment = new VideoEditorFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.video_editor_activity_root, this.mVideoEditorFragment).commitAllowingStateLoss();
        }
    }

    @Override // androidx.fragment.app.FragmentActivity
    public void onAttachFragment(Fragment fragment) {
        super.onAttachFragment(fragment);
        if (fragment instanceof MenuFragment) {
            this.mVideoEditorFragment.onAttachMenuFragment(fragment);
        }
    }
}
