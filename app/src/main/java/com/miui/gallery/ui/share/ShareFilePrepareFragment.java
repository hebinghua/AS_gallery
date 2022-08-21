package com.miui.gallery.ui.share;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.ui.share.PrepareItem;
import com.miui.gallery.ui.share.PrepareTask;
import com.miui.gallery.widget.GalleryDialogFragment;
import java.util.ArrayList;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class ShareFilePrepareFragment<T extends PrepareItem> extends GalleryDialogFragment {
    public DialogInterface.OnCancelListener mCancelListener;
    public boolean mPendingPrepareEnd;
    public OnPrepareListener<T> mPrepareListener;
    public ArrayList<T> mPreparedItemsHolder;
    public ProgressDialog mProgressDialog;
    public boolean mStopped;
    public PrepareTask mTask;

    /* renamed from: $r8$lambda$_F_Z-8XRxaMNsbroxS0k_46xD9M */
    public static /* synthetic */ void m1668$r8$lambda$_F_Z8XRxaMNsbroxS0k_46xD9M(ShareFilePrepareFragment shareFilePrepareFragment, DialogInterface dialogInterface, int i) {
        shareFilePrepareFragment.lambda$onCreateDialog$0(dialogInterface, i);
    }

    public static <T extends PrepareItem> ShareFilePrepareFragment<T> newInstance(ArrayList<T> arrayList, OnPrepareListener<T> onPrepareListener) {
        ShareFilePrepareFragment<T> shareFilePrepareFragment = new ShareFilePrepareFragment<>();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("key_items", arrayList);
        shareFilePrepareFragment.setArguments(bundle);
        shareFilePrepareFragment.setOnPrepareListener(onPrepareListener);
        return shareFilePrepareFragment;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        this.mProgressDialog = progressDialog;
        progressDialog.setMessage(getResources().getString(R.string.download_title));
        this.mProgressDialog.setProgressStyle(1);
        this.mProgressDialog.setIndeterminate(false);
        this.mProgressDialog.setMax(100);
        this.mProgressDialog.setCanceledOnTouchOutside(false);
        this.mProgressDialog.setButton(-2, getResources().getString(17039360), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.share.ShareFilePrepareFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                ShareFilePrepareFragment.m1668$r8$lambda$_F_Z8XRxaMNsbroxS0k_46xD9M(ShareFilePrepareFragment.this, dialogInterface, i);
            }
        });
        setCancelable(false);
        return this.mProgressDialog;
    }

    public /* synthetic */ void lambda$onCreateDialog$0(DialogInterface dialogInterface, int i) {
        this.mProgressDialog.cancel();
    }

    public void setOnPrepareListener(OnPrepareListener<T> onPrepareListener) {
        this.mPrepareListener = onPrepareListener;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener onCancelListener) {
        this.mCancelListener = onCancelListener;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        handleFiles();
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStart() {
        this.mStopped = false;
        super.onStart();
        if (this.mPendingPrepareEnd) {
            this.mPendingPrepareEnd = false;
            onPreparedComplete(this.mPreparedItemsHolder);
        }
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onStop() {
        this.mStopped = true;
        super.onStop();
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroy() {
        cancelTask();
        super.onDestroy();
    }

    public final void cancelTask() {
        PrepareTask prepareTask = this.mTask;
        if (prepareTask != null) {
            prepareTask.release();
            this.mTask = null;
        }
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnCancelListener
    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        cancelTask();
        DialogInterface.OnCancelListener onCancelListener = this.mCancelListener;
        if (onCancelListener != null) {
            onCancelListener.onCancel(dialogInterface);
        }
    }

    public final void handleFiles() {
        ArrayList<T> parcelableArrayList = getArguments().getParcelableArrayList("key_items");
        if (parcelableArrayList != null && !parcelableArrayList.isEmpty()) {
            cancelTask();
            this.mTask = new PrepareTask(getActivity(), 100, new PrepareTask.OnPrepareListener<T>() { // from class: com.miui.gallery.ui.share.ShareFilePrepareFragment.1
                @Override // com.miui.gallery.ui.share.PrepareTask.OnPrepareListener
                public void onCancelled(ArrayList<T> arrayList) {
                }

                @Override // com.miui.gallery.ui.share.PrepareTask.OnPrepareListener
                public void onStarted(ArrayList<T> arrayList) {
                }

                {
                    ShareFilePrepareFragment.this = this;
                }

                @Override // com.miui.gallery.ui.share.PrepareTask.OnPrepareListener
                public void onProgressUpdate(int i) {
                    ShareFilePrepareFragment.this.updateProgress(i);
                }

                @Override // com.miui.gallery.ui.share.PrepareTask.OnPrepareListener
                public void onPrepared(ArrayList<T> arrayList) {
                    ShareFilePrepareFragment.this.onPreparedComplete(arrayList);
                }
            }).invoke(parcelableArrayList);
            return;
        }
        dismissAllowingStateLoss();
    }

    public final void updateProgress(int i) {
        this.mProgressDialog.setProgress(i);
    }

    public final void onPreparedComplete(ArrayList<T> arrayList) {
        if (!this.mStopped) {
            dismissAllowingStateLoss();
            OnPrepareListener<T> onPrepareListener = this.mPrepareListener;
            if (onPrepareListener == null) {
                return;
            }
            onPrepareListener.onPrepareComplete(arrayList);
            return;
        }
        this.mPreparedItemsHolder = arrayList;
        this.mPendingPrepareEnd = true;
    }
}
