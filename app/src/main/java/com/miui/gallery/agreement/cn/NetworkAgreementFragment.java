package com.miui.gallery.agreement.cn;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.agreement.core.CtaAgreement;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.permission.R$string;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes.dex */
public class NetworkAgreementFragment extends DialogFragment {
    public OnAgreementInvokedListener mListener;

    public void invoke(FragmentActivity fragmentActivity, OnAgreementInvokedListener onAgreementInvokedListener) {
        Fragment findFragmentByTag = fragmentActivity.getSupportFragmentManager().findFragmentByTag("NetworkAgreementFragment");
        if (findFragmentByTag != null && findFragmentByTag != this && (findFragmentByTag instanceof NetworkAgreementFragment)) {
            ((DialogFragment) findFragmentByTag).dismiss();
        }
        this.mListener = onAgreementInvokedListener;
        show(fragmentActivity.getSupportFragmentManager(), "NetworkAgreementFragment");
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setCancelable(false);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setTitle(getTitle()).setMessage(getMessage()).setPositiveButton(getPositiveText(), getPositiveListener()).setNegativeButton(getNegativeText(), getNegativeListener()).create();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            ((AlertDialog) getDialog()).getMessageView().setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public String getTitle() {
        return getString(R$string.user_notice_title);
    }

    public CharSequence getMessage() {
        return CtaAgreement.buildUserNotice(getActivity(), R$string.user_notice_identify_summary_format);
    }

    public String getPositiveText() {
        return getString(R$string.user_agree);
    }

    public String getNegativeText() {
        return getString(17039360);
    }

    public DialogInterface.OnClickListener getPositiveListener() {
        return new DialogInterface.OnClickListener() { // from class: com.miui.gallery.agreement.cn.NetworkAgreementFragment.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (NetworkAgreementFragment.this.mListener != null) {
                    NetworkAgreementFragment.this.mListener.onAgreementInvoked(true);
                }
            }
        };
    }

    public DialogInterface.OnClickListener getNegativeListener() {
        return new DialogInterface.OnClickListener() { // from class: com.miui.gallery.agreement.cn.NetworkAgreementFragment.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (NetworkAgreementFragment.this.mListener != null) {
                    NetworkAgreementFragment.this.mListener.onAgreementInvoked(false);
                }
            }
        };
    }
}
