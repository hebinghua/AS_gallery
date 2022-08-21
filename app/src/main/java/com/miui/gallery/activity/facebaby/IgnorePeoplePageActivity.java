package com.miui.gallery.activity.facebaby;

import android.content.Intent;
import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.ui.IgnorePeoplePageFragment;

/* loaded from: classes.dex */
public class IgnorePeoplePageActivity extends BaseActivity {
    public IgnorePeoplePageFragment mFragment;

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.ignore_people_page_activity);
        this.mFragment = (IgnorePeoplePageFragment) getSupportFragmentManager().findFragmentById(R.id.ignore_people_page_fragment);
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.mFragment.onActivityResult(i, i2, intent);
    }
}
