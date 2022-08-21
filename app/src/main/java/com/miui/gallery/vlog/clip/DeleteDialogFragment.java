package com.miui.gallery.vlog.clip;

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
public class DeleteDialogFragment extends GalleryDialogFragment {
    public Context mContext;
    public AlertDialog mDialog;
    public DialogClickListener mListener;

    /* loaded from: classes2.dex */
    public interface DialogClickListener {
        void onDelete();
    }

    public static /* synthetic */ void $r8$lambda$gyHWNWdcXSZx6ED9m1tnctS0rF8(DeleteDialogFragment deleteDialogFragment, DialogInterface dialogInterface, int i) {
        deleteDialogFragment.lambda$onCreateDialog$0(dialogInterface, i);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        FragmentActivity activity = getActivity();
        this.mContext = activity;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity, R$style.AlertDialog_Theme_Dark);
        int i = R$string.vlog_clip_delete_dialog_btn_delete;
        AlertDialog create = builder.setTitle(i).setMessage(R$string.vlog_clip_delete_dialog_btn_msg).setPositiveButton(i, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.vlog.clip.DeleteDialogFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i2) {
                DeleteDialogFragment.$r8$lambda$gyHWNWdcXSZx6ED9m1tnctS0rF8(DeleteDialogFragment.this, dialogInterface, i2);
            }
        }).setNegativeButton(R$string.vlog_clip_delete_dialog_btn_cancel, (DialogInterface.OnClickListener) null).create();
        this.mDialog = create;
        return create;
    }

    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        DialogClickListener dialogClickListener = this.mListener;
        if (dialogClickListener != null) {
            dialogClickListener.onDelete();
        }
        dismissSafely();
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
