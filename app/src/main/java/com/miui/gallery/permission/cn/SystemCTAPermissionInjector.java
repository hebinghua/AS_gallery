package com.miui.gallery.permission.cn;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.app.fragment.AndroidFragment;
import com.miui.gallery.permission.R$string;

/* loaded from: classes2.dex */
public class SystemCTAPermissionInjector extends AndroidFragment {
    public OnAgreementInvokedListener mListener;

    public static SystemCTAPermissionInjector getInstance(boolean z) {
        SystemCTAPermissionInjector systemCTAPermissionInjector = new SystemCTAPermissionInjector();
        Bundle bundle = new Bundle();
        bundle.putBoolean("SHOW_WHEN_LOCK", z);
        systemCTAPermissionInjector.setArguments(bundle);
        return systemCTAPermissionInjector;
    }

    public void invoke(FragmentActivity fragmentActivity, OnAgreementInvokedListener onAgreementInvokedListener) {
        FragmentManager supportFragmentManager = fragmentActivity.getSupportFragmentManager();
        SystemCTAPermissionInjector systemCTAPermissionInjector = (SystemCTAPermissionInjector) supportFragmentManager.findFragmentByTag("SystemCTAPermissionInjector");
        if (systemCTAPermissionInjector == null) {
            setListener(onAgreementInvokedListener);
            supportFragmentManager.beginTransaction().add(this, "SystemCTAPermissionInjector").commit();
            return;
        }
        systemCTAPermissionInjector.setListener(onAgreementInvokedListener);
    }

    @Override // com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            return;
        }
        requestCtaDialog();
    }

    public void requestCtaDialog() {
        Intent intent = new Intent("miui.intent.action.SYSTEM_PERMISSION_DECLARE");
        intent.setPackage("com.miui.securitycenter");
        intent.putExtra("show_locked", getArguments() != null ? getArguments().getBoolean("SHOW_WHEN_LOCK", false) : false);
        intent.putExtra("main_purpose", getString(R$string.app_summary_2));
        intent.putExtra("use_network", false);
        intent.putExtra("mandatory_permission", true);
        intent.putExtra("runtime_perm", new String[]{"android.permission-group.STORAGE"});
        intent.putExtra("runtime_perm_desc", new String[]{getString(R$string.permission_storage_desc_2)});
        String[] strArr = {getString(R$string.permission_contacts_name_2), getString(R$string.permission_location)};
        String[] strArr2 = {getString(R$string.permission_contacts_desc_2), getString(R$string.permission_location_desc)};
        intent.putExtra("optional_perm", strArr);
        intent.putExtra("optional_perm_desc", strArr2);
        startActivityForResult(intent, 111);
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        if (i == 111) {
            if (i2 == -3) {
                requestCtaDialog();
                return;
            }
            OnAgreementInvokedListener onAgreementInvokedListener = this.mListener;
            if (onAgreementInvokedListener != null) {
                boolean z = true;
                if (i2 != 1) {
                    z = false;
                }
                onAgreementInvokedListener.onAgreementInvoked(z);
            }
            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    public void setListener(OnAgreementInvokedListener onAgreementInvokedListener) {
        this.mListener = onAgreementInvokedListener;
    }
}
