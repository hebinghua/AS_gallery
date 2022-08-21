package com.miui.gallery.activity;

import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.app.activity.GalleryActivity;

/* loaded from: classes.dex */
public class EmptyActivity extends GalleryActivity {
    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.empty_activity_layout);
    }
}
