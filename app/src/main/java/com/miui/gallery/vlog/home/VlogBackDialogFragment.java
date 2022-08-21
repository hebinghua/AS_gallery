package com.miui.gallery.vlog.home;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.R$style;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class VlogBackDialogFragment extends GalleryDialogFragment {
    public Context mContext;
    public AlertDialog mDialog;
    public DialogClickListener mListener;

    /* loaded from: classes2.dex */
    public interface DialogClickListener {
        void exit();
    }

    public static /* synthetic */ void $r8$lambda$NzNs0O2w2g7tSpCxKkRgOGxXKbw(VlogBackDialogFragment vlogBackDialogFragment, DialogInterface dialogInterface, int i) {
        vlogBackDialogFragment.lambda$onCreateDialog$0(dialogInterface, i);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        FragmentActivity activity = getActivity();
        this.mContext = activity;
        AlertDialog create = new AlertDialog.Builder(activity, R$style.AlertDialog_Theme_Dark).setTitle(R$string.vlog_exit_title).setMessage(R$string.vlog_exit_message).setPositiveButton(R$string.vlog_back_dialog_exit, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.vlog.home.VlogBackDialogFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                VlogBackDialogFragment.$r8$lambda$NzNs0O2w2g7tSpCxKkRgOGxXKbw(VlogBackDialogFragment.this, dialogInterface, i);
            }
        }).setNegativeButton(R$string.vlog_back_dialog_exit_cancel, (DialogInterface.OnClickListener) null).create();
        this.mDialog = create;
        return create;
    }

    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        if (this.mListener != null) {
            dismissSafely();
            this.mListener.exit();
        }
    }

    public void setDialogClickListener(DialogClickListener dialogClickListener) {
        this.mListener = dialogClickListener;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        this.mListener = null;
    }
}
