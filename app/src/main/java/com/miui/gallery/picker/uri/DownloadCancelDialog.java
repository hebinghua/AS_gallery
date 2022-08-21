package com.miui.gallery.picker.uri;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class DownloadCancelDialog extends GalleryDialogFragment {
    public DialogInterface.OnClickListener mCancelListener;
    public DialogInterface.OnClickListener mContinueListener;

    public void setCancelListener(DialogInterface.OnClickListener onClickListener) {
        this.mCancelListener = onClickListener;
    }

    public void setContinueListener(DialogInterface.OnClickListener onClickListener) {
        this.mContinueListener = onClickListener;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        int i = getArguments().getInt("remaining_count");
        return new AlertDialog.Builder(getActivity()).setMessage(getResources().getQuantityString(R.plurals.picker_cancel_confirm_message, i, Integer.valueOf(i))).setPositiveButton(R.string.picker_continue_download, this.mContinueListener).setNegativeButton(R.string.picker_cancel_download, this.mCancelListener).setCancelable(false).create();
    }
}
