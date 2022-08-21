package com.miui.gallery.video.compress;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.R;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.widget.GalleryDialogFragment;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class VideoCompressSavingFragment extends GalleryDialogFragment {
    public long mLastBackPressedTime;
    public SaveCancelListener mListener;
    public ProgressDialog mProgressDialog;

    /* loaded from: classes2.dex */
    public interface SaveCancelListener {
        void onCancelCompress();
    }

    public static /* synthetic */ boolean $r8$lambda$cY6TsSMqe1rYF692jVkeC4XIa3c(VideoCompressSavingFragment videoCompressSavingFragment, DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        return videoCompressSavingFragment.lambda$onCreateDialog$0(dialogInterface, i, keyEvent);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        this.mProgressDialog = progressDialog;
        progressDialog.setMessage(getResources().getString(R.string.video_compress_encode_video));
        this.mProgressDialog.setProgressStyle(1);
        this.mProgressDialog.setIndeterminate(false);
        this.mProgressDialog.setMax(100);
        this.mProgressDialog.setCanceledOnTouchOutside(false);
        this.mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.miui.gallery.video.compress.VideoCompressSavingFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnKeyListener
            public final boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return VideoCompressSavingFragment.$r8$lambda$cY6TsSMqe1rYF692jVkeC4XIa3c(VideoCompressSavingFragment.this, dialogInterface, i, keyEvent);
            }
        });
        setCancelable(false);
        return this.mProgressDialog;
    }

    public /* synthetic */ boolean lambda$onCreateDialog$0(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (i == 4 && keyEvent.getAction() == 0) {
            return backPress();
        }
        return false;
    }

    public void cancelCompress() {
        SaveCancelListener saveCancelListener = this.mListener;
        if (saveCancelListener != null) {
            saveCancelListener.onCancelCompress();
        }
    }

    public final boolean backPress() {
        if (getContext() == null) {
            return false;
        }
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.mLastBackPressedTime > 3000) {
            this.mLastBackPressedTime = currentTimeMillis;
            ToastUtils.makeText(getActivity(), getString(R.string.video_compress_save_stop_tips), 0);
            return true;
        }
        SaveCancelListener saveCancelListener = this.mListener;
        if (saveCancelListener != null) {
            saveCancelListener.onCancelCompress();
        }
        this.mLastBackPressedTime = 0L;
        return true;
    }

    public void setProgress(int i) {
        this.mProgressDialog.setProgress(i);
    }

    public void show(FragmentManager fragmentManager, SaveCancelListener saveCancelListener) {
        if (!isAdded()) {
            this.mListener = saveCancelListener;
            showAllowingStateLoss(fragmentManager, "VideoCompressSavingFragment");
        }
    }
}
