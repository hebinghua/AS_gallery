package com.miui.gallery.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.miui.gallery.R;
import com.miui.gallery.ui.RecommendFragment;

/* loaded from: classes.dex */
public class RecommendActivity extends BaseActivity {
    public Fragment mFragment;

    @Override // com.miui.gallery.activity.BaseActivity
    public int getFragmentContainerId() {
        return R.id.content;
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.recommend_activity);
        setTitle(R.string.assistant_page_discovery);
        this.mFragment = startFragment(RecommendActivity$$ExternalSyntheticLambda0.INSTANCE, "RootFragment", false, false);
    }

    public static /* synthetic */ Fragment lambda$onCreate$0(String str) {
        return new RecommendFragment();
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.mFragment.onActivityResult(i, i2, intent);
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setTitle(R.string.assistant_page_discovery);
        this.mFragment.onConfigurationChanged(configuration);
    }
}
