package com.miui.gallery.vlog.base.net.resource;

import android.content.Context;
import androidx.fragment.app.FragmentManager;
import com.miui.gallery.net.download.Request;
import com.miui.gallery.net.downloadqueues.DownloadCommand;
import com.miui.gallery.net.downloadqueues.DownloadCommandQueue;
import com.miui.gallery.net.downloadqueues.IZipFileConfig;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.ui.NetworkConsider;
import com.miui.gallery.util.BaseNetworkUtils;
import com.miui.gallery.util.ToastUtils;
import com.miui.gallery.util.concurrent.ThreadManager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.vlog.R$string;
import com.miui.gallery.vlog.base.interfaces.IUnzipFileListener;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public class ResourceDownloadManager<T extends LocalResource> {
    public static String TAG = "ResourceDownloadManager";
    public DownloadCommand mCommand;
    public DownloadCommandQueue mCommandQueue;
    public Context mContext;
    public DownloadManager mDownloadManager;
    public IDownloadTaskListener mDownloadTaskListener;
    public FragmentManager mFragmentManager;
    public IZipFileConfig mIZipFileConfig;
    public UnZipAsyncTask mUnZipAsyncTask;
    public VlogRequestListener mVlogRequestListener;
    public int mCommandState = 19;
    public IUnzipFileListener mUnzipFileListener = new IUnzipFileListener() { // from class: com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.1
        {
            ResourceDownloadManager.this = this;
        }

        @Override // com.miui.gallery.vlog.base.interfaces.IUnzipFileListener
        public void onUnzipFileSuccess() {
            if (ResourceDownloadManager.this.allowResourceInstall()) {
                DefaultLogger.d(ResourceDownloadManager.TAG, "resource unzip success, to install it!");
                ResourceDownloadManager.this.installResource();
                return;
            }
            DefaultLogger.d(ResourceDownloadManager.TAG, "resource unzip success，the task end!");
            ResourceDownloadManager resourceDownloadManager = ResourceDownloadManager.this;
            resourceDownloadManager.onCommandSuccess(resourceDownloadManager.mCommand);
            ResourceDownloadManager resourceDownloadManager2 = ResourceDownloadManager.this;
            resourceDownloadManager2.onNextCommandRunning(resourceDownloadManager2.mCommand);
        }

        @Override // com.miui.gallery.vlog.base.interfaces.IUnzipFileListener
        public void onUnzipFileFailed(boolean z) {
            if (!z) {
                ToastUtils.makeText(ResourceDownloadManager.this.mContext, R$string.vlog_unzip_file_fail);
            }
            ResourceDownloadManager resourceDownloadManager = ResourceDownloadManager.this;
            resourceDownloadManager.onCommandFail(resourceDownloadManager.mCommand);
            ResourceDownloadManager resourceDownloadManager2 = ResourceDownloadManager.this;
            resourceDownloadManager2.onNextCommandRunning(resourceDownloadManager2.mCommand);
        }
    };

    /* loaded from: classes2.dex */
    public interface IDownloadTaskListener<F extends LocalResource> {
        void onCommandFail(F f, int i);

        void onCommandStart(F f, int i);

        void onCommandSuccess(F f, int i);

        void onTaskCancel(F f, int i);
    }

    /* renamed from: $r8$lambda$rqAAQWjQ-6yqqNZcRbT2UNv7u60 */
    public static /* synthetic */ void m1775$r8$lambda$rqAAQWjQ6yqqNZcRbT2UNv7u60(ResourceDownloadManager resourceDownloadManager, LocalResource localResource, int i, boolean z, boolean z2) {
        resourceDownloadManager.lambda$createDownloadCommand$0(localResource, i, z, z2);
    }

    public void installResource() {
    }

    public ResourceDownloadManager(Context context, FragmentManager fragmentManager, IZipFileConfig iZipFileConfig) {
        this.mContext = context;
        this.mFragmentManager = fragmentManager;
        this.mIZipFileConfig = iZipFileConfig;
    }

    public void createDownloadCommand(final T t, final int i) {
        if (!BaseNetworkUtils.isNetworkConnected()) {
            ToastUtils.makeText(this.mContext, R$string.vlog_download_failed_for_notwork);
        } else if (BaseNetworkUtils.isActiveNetworkMetered()) {
            if (hasCurrentCommand(t.getId())) {
                DefaultLogger.d(TAG, "the command has exist.");
            } else {
                NetworkConsider.consider(this.mContext, new NetworkConsider.OnConfirmed() { // from class: com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager$$ExternalSyntheticLambda0
                    @Override // com.miui.gallery.ui.NetworkConsider.OnConfirmed
                    public final void onConfirmed(boolean z, boolean z2) {
                        ResourceDownloadManager.m1775$r8$lambda$rqAAQWjQ6yqqNZcRbT2UNv7u60(ResourceDownloadManager.this, t, i, z, z2);
                    }
                });
            }
        } else if (hasCurrentCommand(t.getId())) {
            DefaultLogger.d(TAG, "the command has exist.");
        } else {
            buildCommand(t, i);
        }
    }

    /* JADX WARN: Multi-variable type inference failed */
    public /* synthetic */ void lambda$createDownloadCommand$0(LocalResource localResource, int i, boolean z, boolean z2) {
        if (z) {
            buildCommand(localResource, i);
        }
    }

    public final void buildCommand(T t, int i) {
        if (this.mCommandQueue == null) {
            this.mCommandQueue = new DownloadCommandQueue();
        }
        DownloadCommand downloadCommand = new DownloadCommand(t, i, this.mIZipFileConfig);
        boolean put = this.mCommandQueue.put(downloadCommand);
        DefaultLogger.d(TAG, "command num: %s ", Integer.valueOf(this.mCommandQueue.getCapacity()));
        if (put) {
            onCommandStart(downloadCommand);
            downloadResource();
        }
    }

    /* JADX WARN: Code restructure failed: missing block: B:36:0x0022, code lost:
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public final boolean hasCurrentCommand(long r5) {
        /*
            r4 = this;
            r0 = 0
            r1 = r0
        L2:
            com.miui.gallery.net.downloadqueues.DownloadCommandQueue r2 = r4.mCommandQueue
            if (r2 == 0) goto L22
            int r2 = r2.getCapacity()
            if (r1 >= r2) goto L22
            com.miui.gallery.net.downloadqueues.DownloadCommandQueue r2 = r4.mCommandQueue
            com.miui.gallery.net.downloadqueues.DownloadCommand r2 = r2.get(r1)
            if (r2 != 0) goto L15
            goto L1f
        L15:
            long r2 = r2.getId()
            int r2 = (r2 > r5 ? 1 : (r2 == r5 ? 0 : -1))
            if (r2 != 0) goto L1f
            r5 = 1
            return r5
        L1f:
            int r1 = r1 + 1
            goto L2
        L22:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.hasCurrentCommand(long):boolean");
    }

    public final void downloadResource() {
        if (hasCommandRunning()) {
            DefaultLogger.e(TAG, "the other command is running.");
        } else {
            confirmDownloadResource();
        }
    }

    public final boolean allowResourceInstall() {
        DownloadCommand downloadCommand = this.mCommand;
        if (downloadCommand == null) {
            return false;
        }
        return downloadCommand.isTemplate();
    }

    public final void confirmDownloadResource() {
        if (this.mCommandQueue.isEmpty()) {
            return;
        }
        if (this.mDownloadManager == null) {
            this.mDownloadManager = new DownloadManager();
        }
        DownloadCommand downloadCommand = this.mCommandQueue.get();
        this.mCommand = downloadCommand;
        this.mCommandState = 17;
        this.mDownloadManager.download(downloadCommand, getVlogRequestListener());
    }

    public final void unzipResource() {
        if (this.mCommand == null) {
            return;
        }
        UnZipAsyncTask unZipAsyncTask = new UnZipAsyncTask(this.mCommand, this.mUnzipFileListener);
        this.mUnZipAsyncTask = unZipAsyncTask;
        unZipAsyncTask.execute(new Void[0]);
    }

    public final VlogRequestListener getVlogRequestListener() {
        if (this.mVlogRequestListener == null) {
            this.mVlogRequestListener = new VlogRequestListener(this);
        }
        return this.mVlogRequestListener;
    }

    /* loaded from: classes2.dex */
    public static class VlogRequestListener implements Request.Listener {
        public WeakReference<ResourceDownloadManager> mResourceDownloadManager;

        @Override // com.miui.gallery.net.download.Request.Listener
        public void onProgressUpdate(int i) {
        }

        public VlogRequestListener(ResourceDownloadManager resourceDownloadManager) {
            this.mResourceDownloadManager = new WeakReference<>(resourceDownloadManager);
        }

        @Override // com.miui.gallery.net.download.Request.Listener
        public void onStart() {
            DefaultLogger.d(ResourceDownloadManager.TAG, "the request download start! ");
        }

        @Override // com.miui.gallery.net.download.Request.Listener
        public void onComplete(int i) {
            final ResourceDownloadManager resourceDownloadManager = this.mResourceDownloadManager.get();
            if (resourceDownloadManager == null) {
                return;
            }
            if (i == 0) {
                DefaultLogger.d(ResourceDownloadManager.TAG, "the request download success!");
                resourceDownloadManager.unzipResource();
                return;
            }
            DefaultLogger.e(ResourceDownloadManager.TAG, "the request download fail!");
            ThreadManager.runOnMainThread(new Runnable() { // from class: com.miui.gallery.vlog.base.net.resource.ResourceDownloadManager.VlogRequestListener.1
                {
                    VlogRequestListener.this = this;
                }

                @Override // java.lang.Runnable
                public void run() {
                    ResourceDownloadManager resourceDownloadManager2 = resourceDownloadManager;
                    resourceDownloadManager2.onCommandFail(resourceDownloadManager2.mCommand);
                    ResourceDownloadManager resourceDownloadManager3 = resourceDownloadManager;
                    resourceDownloadManager3.onNextCommandRunning(resourceDownloadManager3.mCommand);
                }
            });
        }

        public void release() {
            WeakReference<ResourceDownloadManager> weakReference = this.mResourceDownloadManager;
            if (weakReference != null) {
                weakReference.clear();
                this.mResourceDownloadManager = null;
            }
        }
    }

    public void setDownloadTaskListener(IDownloadTaskListener iDownloadTaskListener) {
        this.mDownloadTaskListener = iDownloadTaskListener;
    }

    public void cancel() {
        onTaskCancel(this.mCommand);
        DownloadManager downloadManager = this.mDownloadManager;
        if (downloadManager != null) {
            downloadManager.cancelAll();
            this.mDownloadManager = null;
        }
        UnZipAsyncTask unZipAsyncTask = this.mUnZipAsyncTask;
        if (unZipAsyncTask != null) {
            unZipAsyncTask.clearListener();
            this.mUnZipAsyncTask.cancel(true);
        }
        if (this.mDownloadTaskListener != null) {
            this.mDownloadTaskListener = null;
        }
        VlogRequestListener vlogRequestListener = this.mVlogRequestListener;
        if (vlogRequestListener != null) {
            vlogRequestListener.release();
        }
    }

    public final void onCommandStart(DownloadCommand downloadCommand) {
        if (this.mDownloadTaskListener != null) {
            DefaultLogger.d(TAG, "the command %s start. ", downloadCommand.getData().getLabel());
            this.mCommandState = hasCommandRunning() ? this.mCommandState : 16;
            this.mDownloadTaskListener.onCommandStart(downloadCommand.getData(), downloadCommand.getPosition());
        }
    }

    public final void onCommandSuccess(DownloadCommand downloadCommand) {
        if (this.mDownloadTaskListener == null || downloadCommand == null) {
            return;
        }
        DefaultLogger.d(TAG, "the command %s is completed on success. ", downloadCommand.getData().getLabel());
        this.mDownloadTaskListener.onCommandSuccess(downloadCommand.getData(), downloadCommand.getPosition());
    }

    public final void onCommandFail(DownloadCommand downloadCommand) {
        if (this.mDownloadTaskListener == null || downloadCommand == null) {
            return;
        }
        DefaultLogger.d(TAG, "the command %s is completed on fail. ", downloadCommand.getData().getLabel());
        this.mDownloadTaskListener.onCommandFail(downloadCommand.getData(), downloadCommand.getPosition());
    }

    public final void onTaskCancel(DownloadCommand downloadCommand) {
        IDownloadTaskListener iDownloadTaskListener = this.mDownloadTaskListener;
        if (iDownloadTaskListener != null && downloadCommand != null) {
            this.mCommandState = 19;
            iDownloadTaskListener.onTaskCancel(downloadCommand.getData(), downloadCommand.getPosition());
        }
        while (true) {
            DownloadCommandQueue downloadCommandQueue = this.mCommandQueue;
            if (downloadCommandQueue == null || downloadCommandQueue.isEmpty()) {
                break;
            }
            DownloadCommand downloadCommand2 = this.mCommandQueue.get();
            onCommandFail(downloadCommand2);
            this.mCommandQueue.remove(downloadCommand2);
        }
        DownloadCommandQueue downloadCommandQueue2 = this.mCommandQueue;
        if (downloadCommandQueue2 != null) {
            downloadCommandQueue2.clear();
            this.mCommandQueue = null;
        }
        this.mCommand = null;
        DefaultLogger.d(TAG, "the task is completed. ");
    }

    public final void onNextCommandRunning(DownloadCommand downloadCommand) {
        DownloadCommandQueue downloadCommandQueue = this.mCommandQueue;
        if (downloadCommandQueue == null || downloadCommandQueue.isEmpty()) {
            return;
        }
        this.mCommandState = 18;
        this.mCommandQueue.remove(downloadCommand);
        confirmDownloadResource();
    }

    public final boolean hasCommandRunning() {
        return this.mCommandState == 17;
    }
}
