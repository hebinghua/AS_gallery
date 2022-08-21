package com.miui.gallery.picker.uri;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class DownloadConfirmDialog extends GalleryDialogFragment {
    public DialogInterface.OnClickListener mNegativeListener;
    public DialogInterface.OnClickListener mPositiveListener;

    /* renamed from: $r8$lambda$for1xOgEnTannA7oyDz-TGN_txo */
    public static /* synthetic */ void m1182$r8$lambda$for1xOgEnTannA7oyDzTGN_txo(DownloadConfirmDialog downloadConfirmDialog, DialogInterface dialogInterface, int i) {
        downloadConfirmDialog.lambda$onCreateDialog$0(dialogInterface, i);
    }

    public void setPositiveListener(DialogInterface.OnClickListener onClickListener) {
        this.mPositiveListener = onClickListener;
    }

    public void setNegativeListener(DialogInterface.OnClickListener onClickListener) {
        this.mNegativeListener = onClickListener;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        String string;
        String string2;
        String string3;
        int i = getArguments().getInt("download_file_count");
        int i2 = getArguments().getInt("local_file_count");
        int ceil = (int) Math.ceil((getArguments().getInt("download_file_size", 0) / 1024.0d) / 1024.0d);
        if (getArguments().getBoolean("retry_mode")) {
            if (BaseNetworkUtils.isNetworkConnected()) {
                if (ceil != 0) {
                    string = getActivity().getResources().getQuantityString(R.plurals.picker_retry_origin_confirm_message, i, Integer.valueOf(i), Integer.valueOf(ceil));
                } else {
                    string = getActivity().getResources().getQuantityString(R.plurals.picker_retry_image_confirm_message, i, Integer.valueOf(i));
                }
            } else if (ceil != 0) {
                string = getActivity().getResources().getQuantityString(R.plurals.picker_no_network_retry_origin_confirm_message, i, Integer.valueOf(i), Integer.valueOf(ceil));
            } else {
                string = getActivity().getResources().getQuantityString(R.plurals.picker_no_network_retry_image_confirm_message, i, Integer.valueOf(i));
            }
            string2 = getActivity().getString(R.string.download_retry_text);
        } else if (BaseNetworkUtils.isNetworkConnected()) {
            if (i2 == 0) {
                if (ceil != 0) {
                    string3 = getActivity().getResources().getQuantityString(R.plurals.picker_all_origin_need_download, i, Integer.valueOf(ceil));
                } else {
                    string3 = getActivity().getResources().getQuantityString(R.plurals.picker_all_image_need_download, i);
                }
            } else if (ceil != 0) {
                string3 = getActivity().getString(R.string.picker_origin_need_download, new Object[]{Integer.valueOf(i), Integer.valueOf(ceil)});
            } else {
                string3 = getActivity().getString(R.string.picker_image_need_download, new Object[]{Integer.valueOf(i)});
            }
            return new AlertDialog.Builder(getActivity()).setTitle(R.string.toast_download_with_metered_network_title).setMessage(string3).setPositiveButton(R.string.toast_download_with_metered_network_btn_continue, new DialogInterface.OnClickListener() { // from class: com.miui.gallery.picker.uri.DownloadConfirmDialog$$ExternalSyntheticLambda0
                @Override // android.content.DialogInterface.OnClickListener
                public final void onClick(DialogInterface dialogInterface, int i3) {
                    DownloadConfirmDialog.m1182$r8$lambda$for1xOgEnTannA7oyDzTGN_txo(DownloadConfirmDialog.this, dialogInterface, i3);
                }
            }).setNegativeButton(R.string.toast_download_with_metered_network_btn_cancel, this.mNegativeListener).setCancelable(true).create();
        } else {
            string = getActivity().getResources().getString(R.string.picker_no_network_message);
            string2 = getActivity().getString(R.string.picker_download);
        }
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.title_tip).setMessage(string).setPositiveButton(string2, this.mPositiveListener).setNegativeButton(getActivity().getString(R.string.picker_cancel_download), this.mNegativeListener).setCancelable(true).create();
    }

    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        NetworkConsider.sAgreedUsingMeteredNetwork = true;
        this.mPositiveListener.onClick(dialogInterface, i);
    }
}
