package com.miui.gallery.picker.uri;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class PrepareProgressDialog extends GalleryDialogFragment {
    public DialogInterface.OnCancelListener mCancelListener;
    public int mMax;
    public int mProgress;
    public int mStage;

    public void setCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.mCancelListener = onCancelListener;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(true);
        int dialogMessageId = getDialogMessageId();
        if (dialogMessageId != 0) {
            progressDialog.setMessage(getActivity().getString(dialogMessageId, new Object[]{Integer.valueOf(this.mProgress), Integer.valueOf(this.mMax)}));
        }
        return progressDialog;
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        DialogInterface.OnCancelListener onCancelListener = this.mCancelListener;
        if (onCancelListener != null) {
            onCancelListener.onCancel(dialogInterface);
        }
    }

    public void updateProgress(int i) {
        this.mProgress = i;
        setDialogMessage();
    }

    public void setMax(int i) {
        this.mMax = i;
        setDialogMessage();
    }

    public void setStage(int i) {
        this.mStage = i;
        setDialogMessage();
    }

    public void setDialogMessage() {
        ProgressDialog progressDialog;
        int dialogMessageId = getDialogMessageId();
        if (dialogMessageId == 0 || (progressDialog = (ProgressDialog) getDialog()) == null || !progressDialog.isShowing()) {
            return;
        }
        progressDialog.setMessage(getActivity().getString(dialogMessageId, new Object[]{Integer.valueOf(this.mProgress), Integer.valueOf(this.mMax)}));
    }

    public int getDialogMessageId() {
        int i = this.mStage;
        if (i != 0) {
            if (i == 1) {
                return R.string.picker_origin_request_progress_message;
            }
            return 0;
        }
        return R.string.picker_download_progress_file_message;
    }
}
