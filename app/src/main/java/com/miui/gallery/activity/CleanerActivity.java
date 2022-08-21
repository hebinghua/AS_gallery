package com.miui.gallery.activity;

import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.ui.CleanerFragment;
import com.miui.gallery.util.BaseBuildUtil;

/* loaded from: classes.dex */
public class CleanerActivity extends BaseActivity {
    public CleanerFragment mCleanerFragment;

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (!BaseBuildUtil.isLargeScreenIndependentOrientation()) {
            setRequestedOrientation(1);
        }
        setContentView(R.layout.cleaner_activity);
        this.mCleanerFragment = (CleanerFragment) getSupportFragmentManager().findFragmentById(R.id.cleaner);
        getWindow().setBackgroundDrawableResource(R.color.cleaner_background);
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        this.mCleanerFragment.onBackPressed();
    }
}
