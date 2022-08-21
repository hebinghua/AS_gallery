package com.miui.gallery.permission.cn.legacy;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import androidx.fragment.app.DialogFragment;
import com.miui.gallery.agreement.core.CtaAgreement;
import com.miui.gallery.permission.R$string;
import com.miui.gallery.permission.core.OnPermissionIntroduced;
import com.miui.gallery.util.logger.DefaultLogger;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class CtaPermissionIntroduceDialog extends DialogFragment {
    public OnPermissionIntroduced mIntroduceListener;
    public String mPermissionToRequest;

    public static CtaPermissionIntroduceDialog newInstance(String str, OnPermissionIntroduced onPermissionIntroduced) {
        CtaPermissionIntroduceDialog ctaPermissionIntroduceDialog = new CtaPermissionIntroduceDialog();
        Bundle bundle = new Bundle();
        bundle.putString("permission", str);
        ctaPermissionIntroduceDialog.setArguments(bundle);
        ctaPermissionIntroduceDialog.setOnIntroducedListener(onPermissionIntroduced);
        return ctaPermissionIntroduceDialog;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setCancelable(false);
        this.mPermissionToRequest = getArguments().getString("permission");
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setTitle(getTitle()).setMessage(getMessage()).setPositiveButton(getPositiveText(), getPositiveListener()).setNegativeButton(getNegativeText(), getNegativeListener()).create();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        ((AlertDialog) getDialog()).getMessageView().setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void setOnIntroducedListener(OnPermissionIntroduced onPermissionIntroduced) {
        this.mIntroduceListener = onPermissionIntroduced;
    }

    public String getTitle() {
        return getString(R$string.privacy_permission_request_title);
    }

    public CharSequence getMessage() {
        String format;
        PackageManager packageManager = getActivity().getPackageManager();
        String str = this.mPermissionToRequest;
        try {
            try {
                CharSequence loadLabel = packageManager.getPermissionInfo(str, 128).loadLabel(packageManager);
                if (loadLabel != null) {
                    str = loadLabel.toString();
                }
                format = String.format(getString(R$string.grant_permission_item), str);
            } catch (PackageManager.NameNotFoundException unused) {
                DefaultLogger.w("CtaPrivacyPermissionRequestDialog", "Get permission info failed, %s", str);
                format = String.format(getString(R$string.grant_permission_item), str);
            }
            return Html.fromHtml(getActivity().getResources().getString(R$string.privacy_permission_request_message, CtaAgreement.Licence.getUserAgreementUrl(), CtaAgreement.Licence.getPrivacyUrl(), format));
        } catch (Throwable th) {
            String.format(getString(R$string.grant_permission_item), str);
            throw th;
        }
    }

    public String getPositiveText() {
        return getString(R$string.privacy_permission_request_positive);
    }

    public String getNegativeText() {
        return getString(R$string.privacy_permission_request_negative);
    }

    public DialogInterface.OnClickListener getPositiveListener() {
        return new DialogInterface.OnClickListener() { // from class: com.miui.gallery.permission.cn.legacy.CtaPermissionIntroduceDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (CtaPermissionIntroduceDialog.this.mIntroduceListener != null) {
                    CtaPermissionIntroduceDialog.this.mIntroduceListener.onPermissionIntroduced(true);
                }
            }
        };
    }

    public DialogInterface.OnClickListener getNegativeListener() {
        return new DialogInterface.OnClickListener() { // from class: com.miui.gallery.permission.cn.legacy.CtaPermissionIntroduceDialog.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (CtaPermissionIntroduceDialog.this.mIntroduceListener != null) {
                    CtaPermissionIntroduceDialog.this.mIntroduceListener.onPermissionIntroduced(false);
                }
            }
        };
    }
}
