package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class ProgressDialogFragment extends GalleryDialogFragment {
    public boolean mIndeterminate;
    public CharSequence mMessage;
    public DialogInterface.OnCancelListener mOnCancelListener;
    public CharSequence mTitle;

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(this.mIndeterminate);
        progressDialog.setTitle(this.mTitle);
        progressDialog.setProgressStyle(0);
        progressDialog.setMessage(this.mMessage);
        return progressDialog;
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        DialogInterface.OnCancelListener onCancelListener = this.mOnCancelListener;
        if (onCancelListener != null) {
            onCancelListener.onCancel(dialogInterface);
        }
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment
    public void dismissSafely() {
        if (getFragmentManager() == null || isDetached()) {
            return;
        }
        dismissAllowingStateLoss();
    }

    public void setIndeterminate(boolean z) {
        this.mIndeterminate = z;
    }

    public void setTitle(CharSequence charSequence) {
        this.mTitle = charSequence;
    }

    public void setMessage(CharSequence charSequence) {
        this.mMessage = charSequence;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.mOnCancelListener = onCancelListener;
    }
}
