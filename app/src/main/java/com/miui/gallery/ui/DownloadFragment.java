package com.miui.gallery.ui;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import com.miui.gallery.R;
import com.miui.gallery.util.BaseMiscUtil;
import com.miui.gallery.util.BulkDownloadHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.widget.GalleryDialogFragment;
import java.util.ArrayList;
import java.util.List;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class DownloadFragment extends GalleryDialogFragment implements BulkDownloadHelper.BulkDownloadListener {
    public OnDownloadListener mDownloadListener;
    public BulkDownloadHelper mHelper;
    public ProgressDialog mProgressDialog;
    public int mStartProgress;
    public String mTitle;

    /* loaded from: classes2.dex */
    public interface OnDownloadListener {
        void onCanceled();

        void onDownloadComplete(List<BulkDownloadHelper.BulkDownloadItem> list, List<BulkDownloadHelper.BulkDownloadItem> list2);
    }

    public static DownloadFragment newInstance(ArrayList<BulkDownloadHelper.BulkDownloadItem> arrayList) {
        DownloadFragment downloadFragment = new DownloadFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("key_download_items", arrayList);
        downloadFragment.setArguments(bundle);
        return downloadFragment;
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        this.mProgressDialog = new ProgressDialog(getActivity());
        String string = getResources().getString(R.string.download_title);
        this.mTitle = string;
        this.mProgressDialog.setTitle(string);
        this.mProgressDialog.setProgressStyle(1);
        this.mProgressDialog.setIndeterminate(false);
        this.mProgressDialog.setMax(100);
        this.mProgressDialog.setCanceledOnTouchOutside(false);
        this.mProgressDialog.setButton(-2, getResources().getString(17039360), new DialogInterface.OnClickListener() { // from class: com.miui.gallery.ui.DownloadFragment.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                if (DownloadFragment.this.mDownloadListener != null) {
                    DownloadFragment.this.mDownloadListener.onCanceled();
                    DownloadFragment.this.mDownloadListener = null;
                }
                DownloadFragment.this.dismissAllowingStateLoss();
            }
        });
        setCancelable(false);
        this.mProgressDialog.setProgress(this.mStartProgress);
        return this.mProgressDialog;
    }

    public void setOnDownloadListener(OnDownloadListener onDownloadListener) {
        this.mDownloadListener = onDownloadListener;
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        ArrayList parcelableArrayList = getArguments().getParcelableArrayList("key_download_items");
        if (BaseMiscUtil.isValid(parcelableArrayList)) {
            BulkDownloadHelper bulkDownloadHelper = new BulkDownloadHelper();
            this.mHelper = bulkDownloadHelper;
            bulkDownloadHelper.download(parcelableArrayList, true, this);
            return;
        }
        dismissAllowingStateLoss();
    }

    @Override // com.miui.gallery.widget.GalleryDialogFragment, androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        BulkDownloadHelper bulkDownloadHelper = this.mHelper;
        if (bulkDownloadHelper != null) {
            bulkDownloadHelper.cancel();
            this.mHelper = null;
        }
        this.mDownloadListener = null;
    }

    @Override // com.miui.gallery.util.BulkDownloadHelper.BulkDownloadListener
    public void onDownloadProgress(float f) {
        this.mProgressDialog.setProgress((int) (f * 100.0f));
    }

    @Override // com.miui.gallery.util.BulkDownloadHelper.BulkDownloadListener
    public void onDownloadEnd(List<BulkDownloadHelper.BulkDownloadItem> list, List<BulkDownloadHelper.BulkDownloadItem> list2) {
        int size = list2.size();
        if (size > 0) {
            ToastUtils.makeText(getActivity(), getResources().getQuantityString(R.plurals.download_error, size, Integer.valueOf(size)));
        }
        OnDownloadListener onDownloadListener = this.mDownloadListener;
        if (onDownloadListener != null) {
            onDownloadListener.onDownloadComplete(list, list2);
            this.mDownloadListener = null;
        }
        dismissAllowingStateLoss();
    }
}
