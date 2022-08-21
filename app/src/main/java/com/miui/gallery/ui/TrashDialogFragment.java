package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.util.ResourceUtils;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class TrashDialogFragment extends GalleryDialogFragment {
    public DialogInterface.OnClickListener mCancelListener;
    public String mConfirmBtn;
    public DialogInterface.OnClickListener mConfirmListener;
    public String mMessage;
    public String mTitle;
    public String mCancelBtn = ResourceUtils.getString(17039360);
    public boolean mCancelable = true;

    public static /* synthetic */ void $r8$lambda$bBQxtz6T4pjv_ssCNBHSATYPFQQ(TrashDialogFragment trashDialogFragment, DialogInterface dialogInterface) {
        trashDialogFragment.lambda$onCreateDialog$0(dialogInterface);
    }

    public static TrashDialogFragment newInstance() {
        return new TrashDialogFragment();
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog create = new AlertDialog.Builder(requireActivity()).setTitle(this.mTitle).setCancelable(this.mCancelable).setMessage(this.mMessage).setNeutralButton(this.mConfirmBtn, this.mConfirmListener).setNegativeButton(this.mCancelBtn, this.mCancelListener).create();
        create.setOnShowListener(new DialogInterface.OnShowListener() { // from class: com.miui.gallery.ui.TrashDialogFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnShowListener
            public final void onShow(DialogInterface dialogInterface) {
                TrashDialogFragment.$r8$lambda$bBQxtz6T4pjv_ssCNBHSATYPFQQ(TrashDialogFragment.this, dialogInterface);
            }
        });
        return create;
    }

    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface) {
        ((AlertDialog) dialogInterface).getButton(-3).setTextColor(getResources().getColor(R.color.trash_delete_all_button_text_color, null));
    }

    public void setMessage(String str) {
        this.mMessage = str;
    }

    public void setTitle(String str) {
        this.mTitle = str;
    }

    public void setConfirmButton(String str, DialogInterface.OnClickListener onClickListener) {
        this.mConfirmBtn = str;
        this.mConfirmListener = onClickListener;
    }
}
