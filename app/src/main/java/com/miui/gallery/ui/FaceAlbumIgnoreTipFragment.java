package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import com.miui.gallery.R;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class FaceAlbumIgnoreTipFragment extends GalleryDialogFragment {
    public DialogInterface.OnClickListener mCancelListener;
    public DialogInterface.OnClickListener mConfirmListener;

    public void setConfirmAndCancelListener(DialogInterface.OnClickListener onClickListener, DialogInterface.OnClickListener onClickListener2) {
        this.mConfirmListener = onClickListener;
        this.mCancelListener = onClickListener2;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        return new AlertDialog.Builder(getActivity()).setCancelable(true).setIconAttribute(16843605).setMessage(Html.fromHtml(getActivity().getString(R.string.ignore_alert_title))).setPositiveButton(R.string.ok, this.mConfirmListener).setNegativeButton(17039360, this.mCancelListener).create();
    }
}
