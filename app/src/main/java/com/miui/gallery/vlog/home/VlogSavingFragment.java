package com.miui.gallery.vlog.home;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.concurrent.Future;
import com.miui.gallery.concurrent.FutureHandler;
import com.miui.gallery.concurrent.ThreadPool;
import com.miui.gallery.imodule.loader.ModuleRegistry;
import com.miui.gallery.imodule.modules.VlogDependsModule;
import com.miui.gallery.storage.StorageSolutionProvider;
import com.miui.gallery.storage.strategies.IStoragePermissionStrategy;
import com.miui.gallery.util.FileHandleRecordHelper;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.manager.MiVideoSdkManager;
import com.miui.gallery.vlog.sdk.callbacks.CompileCallback;
import com.miui.gallery.vlog.tools.DebugLogUtils;
import com.miui.gallery.vlog.tools.VlogUtils;
import com.miui.gallery.widget.GalleryDialogFragment;
import java.io.File;
import miuix.appcompat.app.ProgressDialog;

/* loaded from: classes2.dex */
public class VlogSavingFragment extends GalleryDialogFragment {
    public Future mExportFuture;
    public long mLastBackPressedTime;
    public OnSavingFinishListener mOnSavingFinishListener;
    public ProgressDialog mProgressDialog;

    /* loaded from: classes2.dex */
    public interface OnSavingFinishListener {
        void onCanceled();

        void onFinish(boolean z, String str);
    }

    public static /* synthetic */ boolean $r8$lambda$lMfc6AuatV7yjapvmfuVARPJAc4(VlogSavingFragment vlogSavingFragment, DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        return vlogSavingFragment.lambda$onCreateDialog$0(dialogInterface, i, keyEvent);
    }

    @Override // androidx.fragment.app.DialogFragment
    /* renamed from: onCreateDialog */
    public Dialog mo1072onCreateDialog(Bundle bundle) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        this.mProgressDialog = progressDialog;
        progressDialog.setMessage(getResources().getString(R$string.vlog_saving));
        this.mProgressDialog.setProgressStyle(1);
        this.mProgressDialog.setIndeterminate(false);
        this.mProgressDialog.setMax(100);
        this.mProgressDialog.setCanceledOnTouchOutside(false);
        this.mProgressDialog.setOnKeyListener(new DialogInterface.OnKeyListener() { // from class: com.miui.gallery.vlog.home.VlogSavingFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnKeyListener
            public final boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
                return VlogSavingFragment.$r8$lambda$lMfc6AuatV7yjapvmfuVARPJAc4(VlogSavingFragment.this, dialogInterface, i, keyEvent);
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

    public void setOnSavingFinishListener(OnSavingFinishListener onSavingFinishListener) {
        this.mOnSavingFinishListener = onSavingFinishListener;
    }

    public final boolean backPress() {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - this.mLastBackPressedTime > 3000) {
            this.mLastBackPressedTime = currentTimeMillis;
            ToastUtils.makeText(getContext(), getString(R$string.vlog_save_stop_tips));
            return true;
        }
        cancelExport();
        return true;
    }

    public void setProgress(int i) {
        this.mProgressDialog.setProgress(i);
    }

    public void cancelExport() {
        OnSavingFinishListener onSavingFinishListener = this.mOnSavingFinishListener;
        if (onSavingFinishListener != null) {
            onSavingFinishListener.onCanceled();
        }
        this.mLastBackPressedTime = 0L;
        Future future = this.mExportFuture;
        if (future != null) {
            future.cancel();
            this.mExportFuture = null;
        }
    }

    public void export(FragmentManager fragmentManager, final MiVideoSdkManager miVideoSdkManager) {
        if (isAdded()) {
            return;
        }
        DebugLogUtils.startDebugLog("VlogSavingFragment_", "vlog Export");
        showAllowingStateLoss(fragmentManager, "VlogSavingFragment_");
        this.mExportFuture = ThreadManager.getMiscPool().submit(VlogSavingFragment$$ExternalSyntheticLambda1.INSTANCE, new FutureHandler<String>() { // from class: com.miui.gallery.vlog.home.VlogSavingFragment.1
            {
                VlogSavingFragment.this = this;
            }

            @Override // com.miui.gallery.concurrent.FutureHandler
            public void onPostExecute(Future<String> future) {
                if (!future.isCancelled()) {
                    VlogSavingFragment.this.exportInternal(miVideoSdkManager, future.get());
                } else {
                    VlogSavingFragment.this.dismissSafely();
                }
            }
        });
    }

    public static /* synthetic */ String lambda$export$1(ThreadPool.JobContext jobContext) {
        String tempFilePath = VlogStorage.getTempFilePath();
        File file = new File(tempFilePath);
        if (file.exists()) {
            file.delete();
        }
        return tempFilePath;
    }

    public final void exportInternal(final MiVideoSdkManager miVideoSdkManager, final String str) {
        final int currentTimeMicro = (int) (miVideoSdkManager.getCurrentTimeMicro() / 1000);
        miVideoSdkManager.export(str, new CompileCallback() { // from class: com.miui.gallery.vlog.home.VlogSavingFragment.2
            {
                VlogSavingFragment.this = this;
            }

            @Override // com.miui.gallery.vlog.sdk.callbacks.CompileCallback
            public void onCompileProgress(int i) {
                DefaultLogger.d("VlogSavingFragment_", "onCompileProgress: %s .", Integer.valueOf(i));
                VlogSavingFragment.this.setProgress(i);
            }

            @Override // com.miui.gallery.vlog.sdk.callbacks.CompileCallback
            public void onCompileFailed() {
                DefaultLogger.d("VlogSavingFragment_", "export failed. ");
                miVideoSdkManager.removeCompileCallback();
                VlogSavingFragment.this.dismissSafely();
                miVideoSdkManager.prepare(currentTimeMicro);
                if (VlogSavingFragment.this.mOnSavingFinishListener != null) {
                    VlogSavingFragment.this.mOnSavingFinishListener.onFinish(false, null);
                }
            }

            @Override // com.miui.gallery.vlog.sdk.callbacks.CompileCallback
            public void onCompileCompleted(boolean z) {
                DebugLogUtils.endDebugLog("VlogSavingFragment_", "vlog Export");
                DefaultLogger.d("VlogSavingFragment_", "onCompileCompleted isCanceled:  %s .", Boolean.valueOf(z));
                miVideoSdkManager.removeCompileCallback();
                if (z) {
                    VlogSavingFragment.this.dismissSafely();
                    miVideoSdkManager.prepare(currentTimeMicro);
                    return;
                }
                ThreadManager.getMiscPool().submit(new ThreadPool.Job<String>() { // from class: com.miui.gallery.vlog.home.VlogSavingFragment.2.1
                    {
                        AnonymousClass2.this = this;
                    }

                    @Override // com.miui.gallery.concurrent.ThreadPool.Job
                    /* renamed from: run */
                    public String mo1807run(ThreadPool.JobContext jobContext) {
                        VlogModel vlogModel = (VlogModel) VlogUtils.getViewModel(VlogSavingFragment.this.getActivity(), VlogModel.class);
                        String outFilePath = vlogModel.getOutFilePath();
                        String appendInvokerTag = FileHandleRecordHelper.appendInvokerTag("VlogSavingFragment_", "exportInternal");
                        DefaultLogger.d("VlogSavingFragment_", "move: %s tempFilePath: %s， outFilePath: %s", Boolean.valueOf(StorageSolutionProvider.get().moveFile(str, outFilePath, appendInvokerTag)), str, outFilePath);
                        VlogDependsModule vlogDependsModule = (VlogDependsModule) ModuleRegistry.getModule(VlogDependsModule.class);
                        if (vlogDependsModule != null) {
                            vlogDependsModule.scanSingleFile(VlogSavingFragment.this.getActivity(), outFilePath);
                        }
                        DocumentFile documentFile = StorageSolutionProvider.get().getDocumentFile(outFilePath, IStoragePermissionStrategy.Permission.QUERY, appendInvokerTag);
                        if (documentFile != null) {
                            StorageSolutionProvider.get().apply(documentFile);
                        }
                        if (vlogModel.isSingleVideoEdit() && vlogModel.isFavorite()) {
                            vlogDependsModule.addToFavorite(VlogSavingFragment.this.getActivity(), outFilePath);
                        }
                        return outFilePath;
                    }
                }, new FutureHandler<String>() { // from class: com.miui.gallery.vlog.home.VlogSavingFragment.2.2
                    {
                        AnonymousClass2.this = this;
                    }

                    @Override // com.miui.gallery.concurrent.FutureHandler
                    public void onPostExecute(Future<String> future) {
                        VlogSavingFragment.this.dismissSafely();
                        if (VlogSavingFragment.this.mOnSavingFinishListener != null) {
                            VlogSavingFragment.this.mOnSavingFinishListener.onFinish(true, future.get());
                        }
                    }
                });
            }
        });
    }
}
