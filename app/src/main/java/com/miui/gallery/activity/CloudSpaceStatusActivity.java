package com.miui.gallery.activity;

import android.os.Bundle;
import com.miui.gallery.R;

/* loaded from: classes.dex */
public class CloudSpaceStatusActivity extends BaseActivity {
    @Override // com.miui.gallery.activity.BaseActivity
    public boolean allowUseOnOffline() {
        return false;
    }

    @Override // com.miui.gallery.activity.BaseActivity
    public boolean hasCustomContentView() {
        return true;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.cloud_space_status_activity);
    }
}
