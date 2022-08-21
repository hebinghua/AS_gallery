package com.miui.gallery.vlog.clip;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.R$style;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class ReverseDialogFragment extends GalleryDialogFragment {
    public ReverseCallback mCallback;
    public ProgressDialog mProgressDialog;

    /* loaded from: classes2.dex */
    public interface ReverseCallback {
        void onCancel();
    }

    public static /* synthetic */ boolean $r8$lambda$Lv4D2XppCYnFX8QBl674ZECSDTk(ReverseDialogFragment reverseDialogFragment, DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        return reverseDialogFragment.lambda$onCreateDialog$0(dialogInterface, i, keyEvent);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity(), R$style.AlertDialog_Theme_Dark);
        this.mProgressDialog = progressDialog;
        progressDialog.setMessage(getResources().getString(R$string.vlog_reverse_dialog_msg));
        this.mProgressDialog.setProgressStyle(1);
        this.mProgressDialog.setIndeterminate(false);
        this.mProgressDialog.setMax(100);
        this.mProgressDialog.setCanceledOnTouchOutside(false);
        this.mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.miui.gallery.vlog.clip.ReverseDialogFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnKeyListener
            public final boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return ReverseDialogFragment.$r8$lambda$Lv4D2XppCYnFX8QBl674ZECSDTk(ReverseDialogFragment.this, dialogInterface, i, keyEvent);
            }
        });
        setCancelable(false);
        return this.mProgressDialog;
    }

    public /* synthetic */ boolean lambda$onCreateDialog$0(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (i == 4 && keyEvent.getAction() == 0) {
            return backPress();
        }
        return false;
    }

    public void setReverseCallback(ReverseCallback reverseCallback) {
        this.mCallback = reverseCallback;
    }

    public final boolean backPress() {
        ReverseCallback reverseCallback = this.mCallback;
        if (reverseCallback != null) {
            reverseCallback.onCancel();
            return true;
        }
        return true;
    }

    @Override // androidx.fragment.app.DialogFragment
    public void dismiss() {
        dismissSafely();
    }

    public void setProgress(int i) {
        this.mProgressDialog.setProgress(i);
    }
}
