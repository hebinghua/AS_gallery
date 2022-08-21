package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import com.miui.gallery.cleaner.ScannerManager;
import com.miui.gallery.cleaner.slim.SlimScanner;
import com.miui.gallery.stat.SamplingStatHelper;
import com.miui.gallery.util.FormatUtil;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.AlertDialog;

/* loaded from: classes2.dex */
public class SlimConfirmDialogFragment extends GalleryDialogFragment {
    public DialogInterface.OnClickListener mPositiveListener = new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.SlimConfirmDialogFragment.1
        @Override // android.content.DialogInterface.OnClickListener
        public void onClick(DialogInterface dialogInterface, int i) {
            ((SlimScanner) ScannerManager.getInstance().getScanner(0)).doSlim(SlimConfirmDialogFragment.this.getActivity());
            SamplingStatHelper.recordCountEvent("settings", "settings_do_slim");
            TrackController.trackClick("403.22.1.1.14553", AutoTracking.getRef());
        }
    };
    public long mResultSize;

    public static SlimConfirmDialogFragment newInstance(long j) {
        SlimConfirmDialogFragment slimConfirmDialogFragment = new SlimConfirmDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putLong("result_size", j);
        slimConfirmDialogFragment.setArguments(bundle);
        return slimConfirmDialogFragment;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Bundle arguments = getArguments();
        if (arguments != null) {
            this.mResultSize = arguments.getLong("result_size");
        }
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        if (this.mResultSize <= 0) {
            TrackController.trackClick("403.22.1.1.15962", AutoTracking.getRef());
            TrackController.trackExpose("403.22.1.1.16847", AutoTracking.getRef());
            SamplingStatHelper.recordCountEvent("settings", "settings_slim_confirm_empty");
            return new AlertDialog.Builder(getActivity()).setTitle(R.string.slim_dialog_empty_title).setMessage(R.string.slim_dialog_empty_message).setPositiveButton(R.string.slim_dialog_empty_positive, (DialogInterface.OnClickListener) null).setCancelable(true).create();
        }
        SamplingStatHelper.recordCountEvent("settings", "settings_slim_confirm");
        TrackController.trackExpose("403.22.1.1.16844", AutoTracking.getRef());
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.slim_dialog_title).setMessage(getString(R.string.slim_dialog_message, 30, FormatUtil.formatFileSize(getActivity(), this.mResultSize))).setPositiveButton(R.string.slim_dialog_positive, this.mPositiveListener).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setCancelable(true).create();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        long j = this.mResultSize;
        if (j > 0) {
            bundle.putLong("result_size", j);
        }
    }
}
