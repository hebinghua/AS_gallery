package com.miui.gallery.magic.ui;

import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.R$style;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class ConfirmDialog extends GalleryDialogFragment {
    public ConfirmDialogInterface mConfirmDialogInterface;

    /* loaded from: classes2.dex */
    public interface ConfirmDialogInterface {
        void onCancel(DialogFragment dialogFragment);

        void onConfirm(DialogFragment dialogFragment);
    }

    public static void showConfirmDialog(FragmentManager fragmentManager, String str, String str2, String str3, String str4, ConfirmDialogInterface confirmDialogInterface) {
        Bundle bundle = new Bundle();
        if (!TextUtils.isEmpty(str)) {
            bundle.putString("title", str);
        }
        bundle.putString("msg", str2);
        bundle.putString("negativeButton", str3);
        bundle.putString("positiveButton", str4);
        ConfirmDialog confirmDialog = new ConfirmDialog();
        confirmDialog.setArguments(bundle);
        confirmDialog.setConfirmDialogInterface(confirmDialogInterface);
        confirmDialog.showAllowingStateLoss(fragmentManager, "ConfirmDialog");
    }

    public void setConfirmDialogInterface(ConfirmDialogInterface confirmDialogInterface) {
        this.mConfirmDialogInterface = confirmDialogInterface;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog  reason: collision with other method in class */
    public AlertDialog mo1072onCreateDialog(Bundle bundle) {
        Bundle arguments = getArguments();
        AlertDialog.Builder positiveButton = new AlertDialog.Builder(getActivity(), R$style.AlertDialog_Theme_Dark).setCancelable(true).setIconAttribute(16843605).setMessage(arguments.getString("msg")).setNegativeButton(arguments.getString("negativeButton"), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.magic.ui.ConfirmDialog.2
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ConfirmDialog.this.mConfirmDialogInterface != null) {
                    ConfirmDialog.this.mConfirmDialogInterface.onCancel(ConfirmDialog.this);
                }
                ConfirmDialog.this.dismissAllowingStateLoss();
            }
        }).setPositiveButton(arguments.getString("positiveButton"), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.magic.ui.ConfirmDialog.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (ConfirmDialog.this.mConfirmDialogInterface != null) {
                    ConfirmDialog.this.mConfirmDialogInterface.onConfirm(ConfirmDialog.this);
                }
                ConfirmDialog.this.dismissAllowingStateLoss();
            }
        });
        String string = arguments.getString("title");
        if (!TextUtils.isEmpty(string)) {
            positiveButton.setTitle(string);
        }
        AlertDialog create = positiveButton.create();
        create.show();
        return create;
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        ConfirmDialogInterface confirmDialogInterface = this.mConfirmDialogInterface;
        if (confirmDialogInterface != null) {
            confirmDialogInterface.onCancel(this);
        }
    }
}
