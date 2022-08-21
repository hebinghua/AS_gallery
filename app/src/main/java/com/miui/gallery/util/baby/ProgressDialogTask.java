package com.miui.gallery.util.baby;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import miuix.appcompat.app.ProgressDialog;

/* compiled from: CopyFaceAlbumItemsToBabyAlbumTask.java */
/* loaded from: classes2.dex */
public abstract class ProgressDialogTask extends AsyncTask<Void, Integer, Void> {
    public boolean mCancelable;
    public Context mContext;
    public ProgressDialog mDialog;
    public int mMessageId;
    public int mProgressMax;
    public boolean mShowProgress;
    public int mTitleId;

    public static ProgressDialog showProgressDialog(Context context, int i, int i2, int i3, boolean z) {
        ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle(i == 0 ? null : context.getResources().getString(i));
        progressDialog.setMessage(i2 == 0 ? null : context.getResources().getString(i2));
        progressDialog.setCancelable(z);
        progressDialog.setCanceledOnTouchOutside(false);
        if (z) {
            progressDialog.setButton(-2, context.getString(17039360), (Message) null);
        }
        if (i3 > 1) {
            progressDialog.setMax(i3);
            progressDialog.setProgressStyle(1);
        }
        progressDialog.show();
        return progressDialog;
    }

    public ProgressDialogTask(Context context, int i, int i2, int i3, boolean z, boolean z2) {
        this.mContext = context;
        this.mProgressMax = i;
        this.mTitleId = i2;
        this.mMessageId = i3;
        this.mCancelable = z;
        this.mShowProgress = z2;
    }

    @Override // android.os.AsyncTask
    public void onPreExecute() {
        if (!this.mShowProgress) {
            return;
        }
        ProgressDialog showProgressDialog = showProgressDialog(this.mContext, this.mTitleId, this.mMessageId, this.mProgressMax, this.mCancelable);
        this.mDialog = showProgressDialog;
        if (!this.mCancelable) {
            return;
        }
        Button button = showProgressDialog.getButton(-2);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.util.baby.ProgressDialogTask.1
                @Override // android.view.View.OnClickListener
                public void onClick(View view) {
                    ProgressDialogTask.this.cancel(true);
                }
            });
        }
        this.mDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.util.baby.ProgressDialogTask.2
            @Override // android.content.DialogInterface.OnCancelListener
            public void onCancel(DialogInterface dialogInterface) {
                ProgressDialogTask.this.cancel(true);
            }
        });
    }

    @Override // android.os.AsyncTask
    public void onProgressUpdate(Integer... numArr) {
        try {
            ProgressDialog progressDialog = this.mDialog;
            if (progressDialog == null || !progressDialog.isShowing()) {
                return;
            }
            this.mDialog.setProgress(numArr[0].intValue());
        } catch (Exception unused) {
        }
    }

    public void onPostExecute(Void r1) {
        dismissDialog();
    }

    @Override // android.os.AsyncTask
    public void onCancelled() {
        dismissDialog();
    }

    public void dismissDialog() {
        try {
            ProgressDialog progressDialog = this.mDialog;
            if (progressDialog == null || !progressDialog.isShowing()) {
                return;
            }
            this.mDialog.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isDialogShowing() {
        try {
            ProgressDialog progressDialog = this.mDialog;
            if (progressDialog == null) {
                return false;
            }
            return progressDialog.isShowing();
        } catch (Exception unused) {
            return false;
        }
    }
}
