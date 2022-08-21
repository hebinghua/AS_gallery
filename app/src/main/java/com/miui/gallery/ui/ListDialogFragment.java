package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class ListDialogFragment extends GalleryDialogFragment {
    public boolean hasNegativeBtn = false;
    public DialogInterface.OnClickListener mItemClickListener;
    public String[] mItems;
    public String mTitle;

    public void setDisplayItems(String[] strArr) {
        this.mItems = strArr;
    }

    public void setItemClickListener(DialogInterface.OnClickListener onClickListener) {
        this.mItemClickListener = onClickListener;
    }

    public void setTitle(String str) {
        this.mTitle = str;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        String str = this.mTitle;
        if (str == null) {
            str = "";
        }
        builder.setTitle(str);
        if (this.hasNegativeBtn) {
            builder.setNegativeButton(R.string.cancel, this.mItemClickListener);
        }
        builder.setSingleChoiceItems(this.mItems, -1, this.mItemClickListener);
        builder.setCancelable(true);
        return builder.create();
    }
}
