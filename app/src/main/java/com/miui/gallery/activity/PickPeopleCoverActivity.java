package com.miui.gallery.activity;

import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.ui.PickPeopleCoverFragment;

/* loaded from: classes.dex */
public class PickPeopleCoverActivity extends BaseActivity {
    public PickPeopleCoverFragment mFragment;

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.pick_people_cover_activity);
        this.mFragment = (PickPeopleCoverFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
    }
}
