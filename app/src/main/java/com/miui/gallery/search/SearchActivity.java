package com.miui.gallery.search;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.miui.gallery.activity.BaseActivity;

/* loaded from: classes2.dex */
public class SearchActivity extends BaseActivity {
    public SearchFragment mSearchFragment;

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
        super.onCreate(bundle);
        this.mSearchFragment = (SearchFragment) startFragment(SearchActivity$$ExternalSyntheticLambda0.INSTANCE, "RootFragment", false, true);
    }

    public static /* synthetic */ Fragment lambda$onCreate$0(String str) {
        return new SearchFragment();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        SearchFragment searchFragment = this.mSearchFragment;
        if (searchFragment != null) {
            searchFragment.onNewIntent(intent);
        }
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (!this.mSearchFragment.onBackPressed()) {
            super.onBackPressed();
        }
    }

    @Override // com.miui.gallery.activity.BaseActivity, com.miui.gallery.app.activity.MiuiActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onActivityResult(int i, int i2, Intent intent) {
        this.mSearchFragment.onActivityResult(i, i2, intent);
        super.onActivityResult(i, i2, intent);
    }

    @Override // com.miui.gallery.app.activity.GalleryActivity, com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mSearchFragment.onConfigurationChanged(configuration);
    }
}
