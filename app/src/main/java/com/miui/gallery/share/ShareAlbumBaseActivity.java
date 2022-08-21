package com.miui.gallery.share;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.R;
import com.miui.gallery.app.activity.MiuiActivity;
import com.miui.gallery.stat.SamplingStatHelper;

/* loaded from: classes2.dex */
public abstract class ShareAlbumBaseActivity extends MiuiActivity {
    public ShareAlbumBaseFragment mFragment;

    public abstract ShareAlbumBaseFragment createFragment();

    public abstract String getPageName();

    @Override // com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.preference_container);
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        ShareAlbumBaseFragment createFragment = createFragment();
        this.mFragment = createFragment;
        beginTransaction.replace(R.id.preference_container, createFragment).commit();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        SamplingStatHelper.recordPageStart(this, getPageName());
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        SamplingStatHelper.recordPageEnd(this, getPageName());
    }
}
