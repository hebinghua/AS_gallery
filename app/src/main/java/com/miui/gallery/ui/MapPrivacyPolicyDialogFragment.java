package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.agreement.core.CtaAgreement;
import com.miui.gallery.agreement.core.OnAgreementInvokedListener;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class MapPrivacyPolicyDialogFragment extends GalleryDialogFragment {
    public OnAgreementInvokedListener mListener;

    public static /* synthetic */ void $r8$lambda$9Wy3uvSezkpdmEefnkJ2tzVCSwY(MapPrivacyPolicyDialogFragment mapPrivacyPolicyDialogFragment, DialogInterface dialogInterface, int i) {
        mapPrivacyPolicyDialogFragment.lambda$getNegativeListener$1(dialogInterface, i);
    }

    public static /* synthetic */ void $r8$lambda$v88Tp4Y4hhikZSKtzVO3ldvfKo8(MapPrivacyPolicyDialogFragment mapPrivacyPolicyDialogFragment, DialogInterface dialogInterface, int i) {
        mapPrivacyPolicyDialogFragment.lambda$getPositiveListener$2(dialogInterface, i);
    }

    public int getNegativeText() {
        return R.string.privacy_cloud_confirm_detail_negative;
    }

    public int getPositiveText() {
        return R.string.privacy_cloud_confirm_detail_positive;
    }

    public int getTitle() {
        return R.string.map_privacy_dialog_title;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(requireActivity()).setTitle(getTitle()).setMessage(getMessage()).setCancelable(false).setPositiveButton(getPositiveText(), getPositiveListener()).setNegativeButton(getNegativeText(), getNegativeListener()).create();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        if (getDialog() != null) {
            TextView messageView = ((AlertDialog) getDialog()).getMessageView();
            messageView.setMovementMethod(LinkMovementMethod.getInstance());
            messageView.setOnClickListener(MapPrivacyPolicyDialogFragment$$ExternalSyntheticLambda2.INSTANCE);
        }
    }

    public DialogInterface.OnClickListener getNegativeListener() {
        return new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.MapPrivacyPolicyDialogFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MapPrivacyPolicyDialogFragment.$r8$lambda$9Wy3uvSezkpdmEefnkJ2tzVCSwY(MapPrivacyPolicyDialogFragment.this, dialogInterface, i);
            }
        };
    }

    public /* synthetic */ void lambda$getNegativeListener$1(DialogInterface dialogInterface, int i) {
        if (this.mListener != null) {
            TrackController.trackClick("403.61.0.1.15331", AutoTracking.getRef(), "cancel");
            this.mListener.onAgreementInvoked(false);
        }
    }

    public DialogInterface.OnClickListener getPositiveListener() {
        return new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.MapPrivacyPolicyDialogFragment$$ExternalSyntheticLambda1
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                MapPrivacyPolicyDialogFragment.$r8$lambda$v88Tp4Y4hhikZSKtzVO3ldvfKo8(MapPrivacyPolicyDialogFragment.this, dialogInterface, i);
            }
        };
    }

    public /* synthetic */ void lambda$getPositiveListener$2(DialogInterface dialogInterface, int i) {
        if (this.mListener != null) {
            TrackController.trackClick("403.61.0.1.15331", AutoTracking.getRef(), "sure");
            this.mListener.onAgreementInvoked(true);
        }
    }

    public CharSequence getMessage() {
        return CtaAgreement.buildMapPrivacyPolicy(requireActivity(), R.string.map_privacy_dialog_content);
    }

    public void invoke(FragmentActivity fragmentActivity, OnAgreementInvokedListener onAgreementInvokedListener) {
        Fragment findFragmentByTag = fragmentActivity.getSupportFragmentManager().findFragmentByTag("MapPrivacyPolicyDialogFragment");
        if (findFragmentByTag != null && findFragmentByTag != this && (findFragmentByTag instanceof MapPrivacyPolicyDialogFragment)) {
            ((DialogFragment) findFragmentByTag).dismiss();
        }
        this.mListener = onAgreementInvokedListener;
        show(fragmentActivity.getSupportFragmentManager(), "MapPrivacyPolicyDialogFragment");
    }
}
