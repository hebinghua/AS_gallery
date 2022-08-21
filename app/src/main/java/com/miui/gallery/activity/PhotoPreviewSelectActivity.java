package com.miui.gallery.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.android.internal.WindowCompat;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.ui.PhotoPreviewSelectFragment;
import com.miui.gallery.util.BaseBuildUtil;
import com.miui.gallery.util.SystemUiUtil;

/* loaded from: classes.dex */
public class PhotoPreviewSelectActivity extends BaseActivity {
    public Fragment mFragment;

    /* renamed from: $r8$lambda$OJk89CKioTneMKDGh6-XEeu8pOA */
    public static /* synthetic */ Fragment m473$r8$lambda$OJk89CKioTneMKDGh6XEeu8pOA(Uri uri, Intent intent, String str) {
        return lambda$onCreate$0(uri, intent, str);
    }

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
        final Uri data;
        super.onCreate(bundle);
        if (!BaseBuildUtil.isLargeScreenIndependentOrientation()) {
            setRequestedOrientation(1);
        }
        WindowCompat.setCutoutModeShortEdges(getWindow());
        final Intent intent = getIntent();
        if (intent == null || (data = intent.getData()) == null) {
            finish();
        } else {
            this.mFragment = startFragment(new BaseActivity.FragmentCreator() { // from class: com.miui.gallery.activity.PhotoPreviewSelectActivity$$ExternalSyntheticLambda0
                @Override // com.miui.gallery.activity.BaseActivity.FragmentCreator
                public final Fragment create(String str) {
                    return PhotoPreviewSelectActivity.m473$r8$lambda$OJk89CKioTneMKDGh6XEeu8pOA(data, intent, str);
                }
            }, "PhotoPreviewSelectFragment", true, true);
        }
    }

    public static /* synthetic */ Fragment lambda$onCreate$0(Uri uri, Intent intent, String str) {
        return PhotoPreviewSelectFragment.newInstance(uri, intent.getExtras());
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onAttachedToWindow() {
        if (WindowCompat.isNotch(this)) {
            SystemUiUtil.extendToStatusBar(getWindow().getDecorView());
        }
        SystemUiUtil.hideSystemBars(getWindow().getDecorView(), isInMultiWindowMode());
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        SystemUiUtil.hideSystemBars(getWindow().getDecorView(), isInMultiWindowMode());
    }

    @Override // com.miui.gallery.activity.BaseActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mFragment.onConfigurationChanged(configuration);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        finish();
    }
}
