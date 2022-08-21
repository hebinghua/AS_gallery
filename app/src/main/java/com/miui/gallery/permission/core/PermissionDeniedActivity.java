package com.miui.gallery.permission.core;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import com.android.internal.WindowCompat;
import com.miui.gallery.app.activity.AndroidActivity;
import com.miui.gallery.permission.R$layout;
import com.miui.gallery.permission.R$string;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.logger.DefaultLogger;
import com.nexstreaming.nexeditorsdk.nexEngine;
import java.util.ArrayList;
import java.util.List;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class PermissionDeniedActivity extends AndroidActivity {
    public AlertDialog mInfoDialog;
    public ScreenBroadcastReceiver mScreenReceiver;
    public ArrayList<String> mUnGrantedPermissions;

    @Override // com.miui.gallery.app.activity.AndroidActivity
    public boolean useDefaultScreenSceneMode() {
        return false;
    }

    public static void startActivity(Activity activity, List<String> list, boolean z) {
        if (activity == null || list == null || list.size() <= 0) {
            return;
        }
        Intent intent = new Intent(activity, PermissionDeniedActivity.class);
        intent.putStringArrayListExtra("permissions", new ArrayList<>(list));
        intent.putExtra("StartActivityWhenLocked", z);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R$layout.permission_denied_activity);
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStart() {
        super.onStart();
        if (isShowWhenLocked()) {
            WindowCompat.setShowWhenLocked(this, true);
        }
        ArrayList<String> stringArrayListExtra = getIntent().getStringArrayListExtra("permissions");
        this.mUnGrantedPermissions = stringArrayListExtra;
        if (stringArrayListExtra == null || stringArrayListExtra.size() <= 0) {
            finish();
            return;
        }
        if (this.mInfoDialog == null) {
            this.mInfoDialog = showDialog();
        }
        if (this.mScreenReceiver != null) {
            return;
        }
        ScreenBroadcastReceiver screenBroadcastReceiver = new ScreenBroadcastReceiver();
        this.mScreenReceiver = screenBroadcastReceiver;
        registerReceiver(screenBroadcastReceiver, new IntentFilter("android.intent.action.SCREEN_OFF"));
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onStop() {
        super.onStop();
        finish();
    }

    @Override // android.app.Activity
    public void finish() {
        AlertDialog alertDialog = this.mInfoDialog;
        if (alertDialog != null) {
            alertDialog.dismiss();
            this.mInfoDialog = null;
        }
        ScreenBroadcastReceiver screenBroadcastReceiver = this.mScreenReceiver;
        if (screenBroadcastReceiver != null) {
            unregisterReceiver(screenBroadcastReceiver);
            this.mScreenReceiver = null;
        }
        super.finish();
        overridePendingTransition(0, 0);
    }

    @Override // com.miui.gallery.app.activity.AndroidActivity, androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onResume() {
        super.onResume();
        SamplingStatHelper.recordPageStart(this, "permission_denied");
    }

    @Override // androidx.fragment.app.FragmentActivity, android.app.Activity
    public void onPause() {
        super.onPause();
        SamplingStatHelper.recordPageEnd(this, "permission_denied");
    }

    public final boolean isShowWhenLocked() {
        return getIntent().getBooleanExtra("StartActivityWhenLocked", false);
    }

    public final AlertDialog showDialog() {
        PackageManager packageManager = getPackageManager();
        String[] strArr = new String[this.mUnGrantedPermissions.size()];
        for (int i = 0; i < this.mUnGrantedPermissions.size(); i++) {
            String str = this.mUnGrantedPermissions.get(i);
            try {
                try {
                    CharSequence loadLabel = packageManager.getPermissionInfo(this.mUnGrantedPermissions.get(i), 128).loadLabel(packageManager);
                    if (loadLabel != null) {
                        str = loadLabel.toString();
                    }
                    strArr[i] = String.format(getString(R$string.grant_permission_item), str);
                } catch (PackageManager.NameNotFoundException unused) {
                    DefaultLogger.w("PermissionDeniedActivity", "Get permission info failed, %s", str);
                    strArr[i] = String.format(getString(R$string.grant_permission_item), str);
                }
            } catch (Throwable th) {
                strArr[i] = String.format(getString(R$string.grant_permission_item), str);
                throw th;
            }
        }
        return new AlertDialog.Builder(this).setCancelable(false).setMessage(String.format(getString(R$string.grant_permission_text), TextUtils.join("\n", strArr))).setTitle(getString(R$string.grant_permission_title)).setPositiveButton(isShowWhenLocked() ? R$string.grant_permission_unlock_and_set : R$string.grant_permission_go_and_set, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.permission.core.PermissionDeniedActivity.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                if (PermissionDeniedActivity.this.isShowWhenLocked()) {
                    PermissionDeniedActivity.this.getWindow().addFlags(nexEngine.ExportHEVCMainTierLevel61);
                }
                PermissionDeniedActivity.enterGalleryAppSetting(PermissionDeniedActivity.this);
                PermissionDeniedActivity.this.finish();
            }
        }).setNegativeButton(17039360, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.permission.core.PermissionDeniedActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                PermissionDeniedActivity.this.finish();
            }
        }).show();
    }

    /* loaded from: classes2.dex */
    public class ScreenBroadcastReceiver extends BroadcastReceiver {
        public ScreenBroadcastReceiver() {
        }

        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            if (!"android.intent.action.SCREEN_OFF".equals(intent.getAction()) || PermissionDeniedActivity.this.isFinishing()) {
                return;
            }
            PermissionDeniedActivity.this.finish();
        }
    }

    public static void enterGalleryAppSetting(Context context) {
        Intent intent = new Intent();
        intent.addFlags(268435456);
        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        context.startActivity(intent);
    }
}
