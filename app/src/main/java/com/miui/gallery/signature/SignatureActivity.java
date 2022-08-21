package com.miui.gallery.signature;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.R;
import com.miui.gallery.app.activity.MiuiActivity;
import com.miui.gallery.signature.dialog.SignatureDialog;

/* loaded from: classes2.dex */
public class SignatureActivity extends MiuiActivity {
    public boolean mIsEdit;
    public int mOrientation;
    public String mSignaturePath;
    public SignatureDialog signatureDialog;

    @Override // com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.signature_main);
        Intent intent = getIntent();
        if (intent != null) {
            this.mSignaturePath = intent.getStringExtra("param_signature_path");
            this.mIsEdit = intent.getBooleanExtra("param_signature_is_edit", false);
        }
        this.signatureDialog = new SignatureDialog();
        if (this.mIsEdit && !TextUtils.isEmpty(this.mSignaturePath)) {
            Bundle bundle2 = new Bundle();
            bundle2.putString("param_signature_dialog_origin_path", this.mSignaturePath);
            this.signatureDialog.setArguments(bundle2);
        }
        SignatureDialog signatureDialog = this.signatureDialog;
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        signatureDialog.showAllowingStateLoss(supportFragmentManager, "tag-" + SignatureDialog.class.getCanonicalName());
        this.mOrientation = getResources().getConfiguration().orientation;
    }

    @Override // com.miui.gallery.app.activity.MiuiActivity, miuix.appcompat.app.AppCompatActivity, androidx.fragment.app.FragmentActivity, android.app.Activity, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        SignatureDialog signatureDialog = this.signatureDialog;
        signatureDialog.finish = false;
        if (signatureDialog.isAdded()) {
            this.signatureDialog.dismissAllowingStateLoss();
        }
        this.signatureDialog = new SignatureDialog();
        if (this.mIsEdit && !TextUtils.isEmpty(this.mSignaturePath)) {
            Bundle bundle = new Bundle();
            bundle.putString("param_signature_dialog_origin_path", this.mSignaturePath);
            this.signatureDialog.setArguments(bundle);
        }
        SignatureDialog signatureDialog2 = this.signatureDialog;
        FragmentManager supportFragmentManager = getSupportFragmentManager();
        signatureDialog2.showAllowingStateLoss(supportFragmentManager, "tag-" + SignatureDialog.class.getCanonicalName());
    }

    public void prepareDataAndFinish(int i, String str) {
        Intent intent = new Intent();
        intent.putExtra("result_param_add_signature_result_code", i);
        intent.putExtra("result_param_signature_path", str);
        setResult(-1, intent);
        finish();
    }

    @Override // miuix.appcompat.app.AppCompatActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onDestroy() {
        super.onDestroy();
    }
}
