package com.miui.gallery.share;

import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.miui.gallery.activity.BaseActivity;

/* loaded from: classes2.dex */
public class LoginAndSyncForInvitationActivity extends BaseActivity {
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
        LoginAndSyncForInvitationFragment loginAndSyncForInvitationFragment = new LoginAndSyncForInvitationFragment();
        loginAndSyncForInvitationFragment.setArguments(getIntent().getExtras());
        FragmentTransaction beginTransaction = getSupportFragmentManager().beginTransaction();
        beginTransaction.add(loginAndSyncForInvitationFragment, "LoginAndSyncForInvitationFragment");
        beginTransaction.commitAllowingStateLoss();
    }
}
