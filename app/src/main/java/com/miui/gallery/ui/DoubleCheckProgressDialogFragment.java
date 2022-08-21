package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import com.miui.gallery.R;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class DoubleCheckProgressDialogFragment extends GalleryDialogFragment {
    public long mLastBackPressedTime;
    public CharSequence mMessage;
    public DialogInterface.OnCancelListener mOnCancelListener;
    public int mStyle = 0;
    public CharSequence mTitle;

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle != null) {
            removeSelf();
        }
    }

    public final void removeSelf() {
        if (isAdded()) {
            getFragmentManager().beginTransaction().remove(this).commitAllowingStateLoss();
        }
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(this.mStyle);
        progressDialog.setTitle(this.mTitle);
        progressDialog.setMessage(this.mMessage);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.miui.gallery.ui.DoubleCheckProgressDialogFragment.1
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == 4 && keyEvent.getAction() == 0) {
                    return DoubleCheckProgressDialogFragment.this.onBackPressed();
                }
                return false;
            }
        });
        return progressDialog;
    }

    public final boolean onBackPressed() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.mLastBackPressedTime > 3000) {
            this.mLastBackPressedTime = currentTimeMillis;
            ToastUtils.makeText(getActivity(), getString(R.string.vlog_save_stop_tips), 0);
            return true;
        }
        this.mLastBackPressedTime = 0L;
        DialogInterface.OnCancelListener onCancelListener = this.mOnCancelListener;
        if (onCancelListener == null) {
            return true;
        }
        onCancelListener.onCancel(getDialog());
        return true;
    }

    public void setProgress(int i) {
        if (this.mStyle != 1 || !(getDialog() instanceof ProgressDialog)) {
            return;
        }
        ((ProgressDialog) getDialog()).setProgress(i);
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        DialogInterface.OnCancelListener onCancelListener = this.mOnCancelListener;
        if (onCancelListener != null) {
            onCancelListener.onCancel(dialogInterface);
        }
    }

    public void setStyle(int i) {
        this.mStyle = i;
    }

    public void setMessage(CharSequence charSequence) {
        this.mMessage = charSequence;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.mOnCancelListener = onCancelListener;
    }
}
