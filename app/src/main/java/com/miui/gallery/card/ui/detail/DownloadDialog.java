package com.miui.gallery.card.ui.detail;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.R;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes.dex */
public class DownloadDialog extends GalleryDialogFragment {
    public CallBack mCallBack;
    public long mLastBackPressedTime;
    public ProgressDialog mProgressDialog;

    /* loaded from: classes.dex */
    public interface CallBack {
        void cancel();
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        this.mProgressDialog = progressDialog;
        progressDialog.setMessage(getResources().getString(R.string.story_photo_downloading));
        this.mProgressDialog.setProgressStyle(1);
        this.mProgressDialog.setIndeterminate(false);
        this.mProgressDialog.setMax(100);
        this.mProgressDialog.setCanceledOnTouchOutside(false);
        this.mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.miui.gallery.card.ui.detail.DownloadDialog.1
            @Override // android.content.DialogInterface.OnKeyListener
            public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                if (i == 4 && keyEvent.getAction() == 0) {
                    return DownloadDialog.this.backPress();
                }
                return false;
            }
        });
        setCancelable(false);
        return this.mProgressDialog;
    }

    public final boolean backPress() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.mLastBackPressedTime > 3000) {
            this.mLastBackPressedTime = currentTimeMillis;
            ToastUtils.makeText(getActivity(), getString(R.string.story_back_stop_download));
            return true;
        }
        CallBack callBack = this.mCallBack;
        if (callBack != null) {
            callBack.cancel();
        }
        dismissSafely();
        return true;
    }

    public void show(FragmentActivity fragmentActivity) {
        showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "DownloadDialog");
    }

    public void setCallBack(CallBack callBack) {
        this.mCallBack = callBack;
    }

    public void updateProgress(float f) {
        ProgressDialog progressDialog = this.mProgressDialog;
        if (progressDialog != null) {
            progressDialog.setProgress((int) (f * 100.0f));
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        this.mCallBack = null;
    }
}
