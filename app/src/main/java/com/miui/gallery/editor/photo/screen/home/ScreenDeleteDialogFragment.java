package com.miui.gallery.editor.photo.screen.home;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class ScreenDeleteDialogFragment extends GalleryDialogFragment {
    public Context mContext;
    public AlertDialog mDialog;
    public DialogClickListener mListener;

    /* loaded from: classes2.dex */
    public interface DialogClickListener {
        void onDelete();
    }

    public static /* synthetic */ void $r8$lambda$_IUZU7Vxb_tbtoEOr4vjdDqh9EA(ScreenDeleteDialogFragment screenDeleteDialogFragment, DialogInterface dialogInterface, int i) {
        screenDeleteDialogFragment.lambda$onCreateDialog$0(dialogInterface, i);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        FragmentActivity activity = getActivity();
        this.mContext = activity;
        AlertDialog create = new AlertDialog.Builder(activity).setTitle(R.string.screen_editor_btn_delete).setMessage(R.string.screen_editor_delete_dialog_msg).setPositiveButton(R.string.screen_editor_btn_delete, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.editor.photo.screen.home.ScreenDeleteDialogFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ScreenDeleteDialogFragment.$r8$lambda$_IUZU7Vxb_tbtoEOr4vjdDqh9EA(ScreenDeleteDialogFragment.this, dialogInterface, i);
            }
        }).setNegativeButton(R.string.screen_save_dialog_btn_cancel, (DialogInterface.OnClickListener) null).create();
        this.mDialog = create;
        return create;
    }

    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        DialogClickListener dialogClickListener = this.mListener;
        if (dialogClickListener != null) {
            dialogClickListener.onDelete();
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
