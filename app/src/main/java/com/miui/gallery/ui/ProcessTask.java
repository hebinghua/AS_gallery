package com.miui.gallery.ui;

import android.content.DialogInterface;
import android.os.AsyncTask;
import androidx.fragment.app.FragmentActivity;

/* loaded from: classes2.dex */
public class ProcessTask<Params, PrepareResult, Result> extends AsyncTask<Params, Void, Result> {
    public OnCompleteListener<Result> mCompleteListener;
    public boolean mIsCancelable;
    public volatile boolean mIsDone;
    public OnCancelListener<Result> mOnCancelListener;
    public OnDoProcessExceptionHandler<Result> mOnDoProcessExceptionHandler;
    public OnPrepareCompleteListener<PrepareResult> mPrepareCompleteListener;
    public final ProcessCallback<Params, PrepareResult, Result> mProcessCallback;
    public ProgressDialogFragment mProgressDialog;

    /* loaded from: classes2.dex */
    public interface OnCancelListener<R> {
        void onCancelled(R r);
    }

    /* loaded from: classes2.dex */
    public interface OnCompleteListener<R> {
        void onCompleteProcess(R r);
    }

    /* loaded from: classes2.dex */
    public interface OnDoProcessExceptionHandler<Result> {
        Result handle(Exception exc);

        default boolean shouldContinueComplete() {
            return true;
        }

        default boolean shouldHandle(Exception exc) {
            return false;
        }
    }

    /* loaded from: classes2.dex */
    public interface OnPrepareCompleteListener<R> {
        boolean onPrepareComplete(R r);
    }

    /* loaded from: classes2.dex */
    public interface ProcessCallback<P, R1, R2> {
        default R1 doPrepare(P[] pArr) {
            return null;
        }

        R2 doProcess(P[] pArr);
    }

    /* renamed from: getDefaultExceptionResult */
    public Result mo1446getDefaultExceptionResult(Exception exc) {
        return null;
    }

    public ProcessTask(ProcessCallback<Params, PrepareResult, Result> processCallback) {
        this(processCallback, null, null);
    }

    public ProcessTask(ProcessCallback<Params, PrepareResult, Result> processCallback, OnPrepareCompleteListener<PrepareResult> onPrepareCompleteListener) {
        this(processCallback, onPrepareCompleteListener, null);
    }

    public ProcessTask(ProcessCallback<Params, PrepareResult, Result> processCallback, OnCompleteListener<Result> onCompleteListener) {
        this(processCallback, null, onCompleteListener);
    }

    public ProcessTask(ProcessCallback<Params, PrepareResult, Result> processCallback, OnPrepareCompleteListener<PrepareResult> onPrepareCompleteListener, OnCompleteListener<Result> onCompleteListener) {
        if (processCallback == null) {
            throw new IllegalArgumentException("Null processCallback is not accepted!");
        }
        this.mIsDone = false;
        this.mProcessCallback = processCallback;
        this.mPrepareCompleteListener = onPrepareCompleteListener;
        this.mCompleteListener = onCompleteListener;
    }

    public void showProgress(FragmentActivity fragmentActivity, String str) {
        if (fragmentActivity == null || this.mIsDone) {
            return;
        }
        ProgressDialogFragment progressDialogFragment = new ProgressDialogFragment();
        this.mProgressDialog = progressDialogFragment;
        progressDialogFragment.setIndeterminate(true);
        this.mProgressDialog.setMessage(str);
        this.mProgressDialog.setCancelable(this.mIsCancelable);
        if (this.mIsCancelable) {
            this.mProgressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() { // from class: com.miui.gallery.ui.ProcessTask.1
                @Override // android.content.DialogInterface.OnCancelListener
                public void onCancel(DialogInterface dialogInterface) {
                    ProcessTask.this.cancel(true);
                }
            });
        }
        this.mProgressDialog.showAllowingStateLoss(fragmentActivity.getSupportFragmentManager(), "ProcessTask");
    }

    @Override // android.os.AsyncTask
    public Result doInBackground(Params... paramsArr) {
        Result handle;
        OnPrepareCompleteListener<PrepareResult> onPrepareCompleteListener;
        PrepareResult doPrepare = this.mProcessCallback.doPrepare(paramsArr);
        if (doPrepare == null || (onPrepareCompleteListener = this.mPrepareCompleteListener) == null || onPrepareCompleteListener.onPrepareComplete(doPrepare)) {
            try {
                handle = this.mProcessCallback.doProcess(paramsArr);
            } catch (Exception e) {
                OnDoProcessExceptionHandler<Result> onDoProcessExceptionHandler = this.mOnDoProcessExceptionHandler;
                if (onDoProcessExceptionHandler != null && onDoProcessExceptionHandler.shouldHandle(e)) {
                    handle = this.mOnDoProcessExceptionHandler.handle(e);
                } else {
                    throw e;
                }
            }
            if (!isCancelled()) {
                return handle;
            }
            return null;
        }
        return null;
    }

    public void setOnDoProcessExceptionHandler(OnDoProcessExceptionHandler<Result> onDoProcessExceptionHandler) {
        this.mOnDoProcessExceptionHandler = onDoProcessExceptionHandler;
    }

    @Override // android.os.AsyncTask
    public void onCancelled(Result result) {
        super.onCancelled(result);
        OnCancelListener<Result> onCancelListener = this.mOnCancelListener;
        if (onCancelListener != null) {
            onCancelListener.onCancelled(result);
        }
    }

    @Override // android.os.AsyncTask
    public void onPostExecute(Result result) {
        OnCompleteListener<Result> onCompleteListener;
        super.onPostExecute(result);
        this.mIsDone = true;
        ProgressDialogFragment progressDialogFragment = this.mProgressDialog;
        if (progressDialogFragment != null) {
            progressDialogFragment.dismissSafely();
        }
        OnDoProcessExceptionHandler<Result> onDoProcessExceptionHandler = this.mOnDoProcessExceptionHandler;
        if ((onDoProcessExceptionHandler == null || onDoProcessExceptionHandler.shouldContinueComplete()) && !isCancelled() && (onCompleteListener = this.mCompleteListener) != null) {
            onCompleteListener.onCompleteProcess(result);
        }
    }

    public void setCancelable(boolean z) {
        this.mIsCancelable = z;
    }

    public void setOnPrepareCompleteListener(OnPrepareCompleteListener<PrepareResult> onPrepareCompleteListener) {
        this.mPrepareCompleteListener = onPrepareCompleteListener;
    }

    public void setCompleteListener(OnCompleteListener<Result> onCompleteListener) {
        this.mCompleteListener = onCompleteListener;
    }
}
