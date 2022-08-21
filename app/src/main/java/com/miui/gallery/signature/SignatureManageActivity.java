package com.miui.gallery.signature;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.R;
import com.miui.gallery.app.activity.AndroidActivity;
import com.miui.gallery.signature.dialog.manage.SignatureManagerDialog;

/* loaded from: classes2.dex */
public class SignatureManageActivity extends AndroidActivity {
    public SignatureManagerDialog signatureManagerDialog;

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.signature_manage_main);
        SignatureManagerDialog newInstance = SignatureManagerDialog.newInstance(getIntent().getStringExtra("param_current_signature_path"));
        this.signatureManagerDialog = newInstance;
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        newInstance.showAllowingStateLoss(supportFragmentManager, "tag-" + SignatureManagerDialog.class.getCanonicalName());
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        SignatureManagerDialog signatureManagerDialog = this.signatureManagerDialog;
        signatureManagerDialog.finish = false;
        if (signatureManagerDialog.isAdded()) {
            this.signatureManagerDialog.dismissAllowingStateLoss();
        }
        SignatureManagerDialog newInstance = SignatureManagerDialog.newInstance(getIntent().getStringExtra("param_current_signature_path"));
        this.signatureManagerDialog = newInstance;
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        newInstance.showAllowingStateLoss(supportFragmentManager, "tag-" + SignatureManagerDialog.class.getCanonicalName());
    }

    public void prepareDataAndFinish(int i, String str, boolean z) {
        Intent intent = new Intent();
        if (i == 2) {
            intent.putExtra("result_param_signature_path", str);
        }
        intent.putExtra("result_param_signature_result_code", i);
        intent.putExtra("RESULT_PARAM_CURRENT_SIGNATURE_IS_DELETE", z);
        setResult(-1, intent);
        finish();
    }
}
