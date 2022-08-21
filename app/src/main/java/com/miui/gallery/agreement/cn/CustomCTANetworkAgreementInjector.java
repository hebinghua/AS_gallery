package com.miui.gallery.agreement.cn;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.permission.R$string;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes.dex */
public class CustomCTANetworkAgreementInjector extends GalleryDialogFragment {
    public OnAgreementInvokedListener mListener;

    /* renamed from: $r8$lambda$iZWKObEHqQnEaMK0iq0T-4Ha9W8 */
    public static /* synthetic */ void m542$r8$lambda$iZWKObEHqQnEaMK0iq0T4Ha9W8(CustomCTANetworkAgreementInjector customCTANetworkAgreementInjector, DialogInterface dialogInterface, int i) {
        customCTANetworkAgreementInjector.lambda$getNegativeListener$0(dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$kBwanYeV153rlGANygbsN70y_OA(CustomCTANetworkAgreementInjector customCTANetworkAgreementInjector, DialogInterface dialogInterface, int i) {
        customCTANetworkAgreementInjector.lambda$getPositiveListener$1(dialogInterface, i);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(requireActivity()).setTitle(getTitle()).setMessage(getMessage()).setCancelable(false).setPositiveButton(getPositiveText(), getPositiveListener()).setNegativeButton(getNegativeText(), getNegativeListener()).create();
    }

    public DialogInterface.OnClickListener getNegativeListener() {
        return new DialogInterface.OnClickListener() { // from class: com.miui.gallery.agreement.cn.CustomCTANetworkAgreementInjector$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                CustomCTANetworkAgreementInjector.m542$r8$lambda$iZWKObEHqQnEaMK0iq0T4Ha9W8(CustomCTANetworkAgreementInjector.this, dialogInterface, i);
            }
        };
    }

    public /* synthetic */ void lambda$getNegativeListener$0(DialogInterface dialogInterface, int i) {
        OnAgreementInvokedListener onAgreementInvokedListener = this.mListener;
        if (onAgreementInvokedListener != null) {
            onAgreementInvokedListener.onAgreementInvoked(false);
            onActivityResult(111, 0, null);
        }
    }

    public DialogInterface.OnClickListener getPositiveListener() {
        return new DialogInterface.OnClickListener() { // from class: com.miui.gallery.agreement.cn.CustomCTANetworkAgreementInjector$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                CustomCTANetworkAgreementInjector.$r8$lambda$kBwanYeV153rlGANygbsN70y_OA(CustomCTANetworkAgreementInjector.this, dialogInterface, i);
            }
        };
    }

    public /* synthetic */ void lambda$getPositiveListener$1(DialogInterface dialogInterface, int i) {
        OnAgreementInvokedListener onAgreementInvokedListener = this.mListener;
        if (onAgreementInvokedListener != null) {
            onAgreementInvokedListener.onAgreementInvoked(true);
            onActivityResult(111, 1, null);
        }
    }

    public int getNegativeText() {
        return R$string.permission_custom_cta_network_button_negative;
    }

    public int getPositiveText() {
        return R$string.permission_custom_cta_network_button_positive;
    }

    public CharSequence getMessage() {
        return getResources().getString(R$string.cta_network_declare);
    }

    public int getTitle() {
        return R$string.permission_custom_cta_network_title;
    }

    public void invoke(FragmentActivity fragmentActivity, OnAgreementInvokedListener onAgreementInvokedListener) {
        Fragment findFragmentByTag = fragmentActivity.getSupportFragmentManager().findFragmentByTag("CustomNetworkAgreementInjector");
        if (findFragmentByTag != null && findFragmentByTag != this && (findFragmentByTag instanceof CustomCTANetworkAgreementInjector)) {
            ((DialogFragment) findFragmentByTag).dismiss();
        }
        this.mListener = onAgreementInvokedListener;
        show(fragmentActivity.getSupportFragmentManager(), "CustomNetworkAgreementInjector");
    }
}
