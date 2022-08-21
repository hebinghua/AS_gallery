package com.miui.gallery.activity;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import com.miui.gallery.activity.BaseActivity;
import com.miui.gallery.app.StrategyContext;
import com.miui.gallery.ui.PicToPdfPreviewFragment;

/* loaded from: classes.dex */
public class PicToPdfPreviewActivity extends BaseActivity {
    public PicToPdfPreviewFragment mFragment;

    /* renamed from: $r8$lambda$zQ24-xMckXk82142AtSQ7M9jfeM */
    public static /* synthetic */ Fragment m474$r8$lambda$zQ24xMckXk82142AtSQ7M9jfeM(PicToPdfPreviewActivity picToPdfPreviewActivity, String str) {
        return picToPdfPreviewActivity.lambda$onCreate$0(str);
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
        super.onCreate(bundle);
        requestDisableStrategy(StrategyContext.DisableStrategyType.NAVIGATION_BAR);
        this.mFragment = (PicToPdfPreviewFragment) startFragment(new BaseActivity.FragmentCreator() { // from class: com.miui.gallery.activity.PicToPdfPreviewActivity$$ExternalSyntheticLambda0
            @Override // com.miui.gallery.activity.BaseActivity.FragmentCreator
            public final Fragment create(String str) {
                return PicToPdfPreviewActivity.m474$r8$lambda$zQ24xMckXk82142AtSQ7M9jfeM(PicToPdfPreviewActivity.this, str);
            }
        }, "PicToPdfPreviewFragment", false, true);
    }

    public /* synthetic */ Fragment lambda$onCreate$0(String str) {
        return PicToPdfPreviewFragment.newInstance(getIntent().getExtras());
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        this.mFragment.onBackPressed();
    }
}
