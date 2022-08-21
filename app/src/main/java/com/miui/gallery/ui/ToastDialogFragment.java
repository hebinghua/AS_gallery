package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class ToastDialogFragment extends GalleryDialogFragment {
    public int mButtonMessageId;
    public DialogInterface.OnClickListener mConfirmListener;
    public String mMessage;
    public String mTitle;

    public static final ToastDialogFragment newInstance(String str, String str2, int i) {
        ToastDialogFragment toastDialogFragment = new ToastDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString("TITLE", str);
        bundle.putString("message", str2);
        bundle.putInt("buttonMessageId", i);
        toastDialogFragment.setArguments(bundle);
        return toastDialogFragment;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        this.mTitle = getArguments().getString("TITLE");
        this.mMessage = getArguments().getString("message");
        this.mButtonMessageId = getArguments().getInt("buttonMessageId");
        super.onCreate(bundle);
    }

    public void setConfirmListener(DialogInterface.OnClickListener onClickListener) {
        this.mConfirmListener = onClickListener;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setCancelable(true).setTitle(this.mTitle).setMessage(this.mMessage).setPositiveButton(this.mButtonMessageId, this.mConfirmListener).create();
    }
}
