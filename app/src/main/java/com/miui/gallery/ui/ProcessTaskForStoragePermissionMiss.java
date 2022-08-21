package com.miui.gallery.ui;

import androidx.fragment.app.FragmentActivity;
import com.miui.gallery.storage.exceptions.StoragePermissionMissingException;
import com.miui.gallery.ui.ProcessTask;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class ProcessTaskForStoragePermissionMiss<Params, PrepareResult, Result> extends ProcessTask<Params, PrepareResult, Result> {
    public WeakReference<FragmentActivity> fragmentActivityWeakReference;

    public ProcessTaskForStoragePermissionMiss(ProcessTask.ProcessCallback processCallback) {
        super(processCallback);
    }

    public ProcessTaskForStoragePermissionMiss(ProcessTask.ProcessCallback processCallback, ProcessTask.OnCompleteListener onCompleteListener) {
        super(processCallback, onCompleteListener);
    }

    public void setFragmentActivityForStoragePermissionMiss(FragmentActivity fragmentActivity) {
        this.fragmentActivityWeakReference = new WeakReference<>(fragmentActivity);
        if (this.mOnDoProcessExceptionHandler == null) {
            this.mOnDoProcessExceptionHandler = new ProcessTask.OnDoProcessExceptionHandler<Result>() { // from class: com.miui.gallery.ui.ProcessTaskForStoragePermissionMiss.1
                public boolean shouldContinueComplete = true;

                @Override // com.miui.gallery.ui.ProcessTask.OnDoProcessExceptionHandler
                public boolean shouldHandle(Exception exc) {
                    if (exc.getCause() instanceof StoragePermissionMissingException) {
                        this.shouldContinueComplete = false;
                        return true;
                    }
                    return false;
                }

                @Override // com.miui.gallery.ui.ProcessTask.OnDoProcessExceptionHandler
                public Result handle(Exception exc) {
                    if (!ProcessTaskForStoragePermissionMiss.this.isCancelled() && ProcessTaskForStoragePermissionMiss.this.fragmentActivityWeakReference != null && ProcessTaskForStoragePermissionMiss.this.fragmentActivityWeakReference.get() != null) {
                        ((StoragePermissionMissingException) exc.getCause()).offer((FragmentActivity) ProcessTaskForStoragePermissionMiss.this.fragmentActivityWeakReference.get());
                        return ProcessTaskForStoragePermissionMiss.this.mo1446getDefaultExceptionResult((StoragePermissionMissingException) exc.getCause());
                    }
                    return ProcessTaskForStoragePermissionMiss.this.mo1446getDefaultExceptionResult((StoragePermissionMissingException) exc.getCause());
                }

                @Override // com.miui.gallery.ui.ProcessTask.OnDoProcessExceptionHandler
                public boolean shouldContinueComplete() {
                    return this.shouldContinueComplete;
                }
            };
        }
    }
}
