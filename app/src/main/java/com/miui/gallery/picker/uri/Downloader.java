package com.miui.gallery.picker.uri;

import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.View;
import com.miui.gallery.error.core.ErrorCode;
import com.miui.gallery.sdk.download.DownloadType;
import com.miui.gallery.util.cloudimageloader.CloudImageLoadingListenerAdapter;
import com.miui.gallery.util.glide.CloudImageLoader;
import com.miui.gallery.util.logger.DefaultLogger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: classes2.dex */
public class Downloader {
    public boolean mCancelled;
    public DownloadListener mDownloadListener;
    public List<DownloadTask> mDownloadTasks;
    public boolean mPaused;
    public Handler.Callback mCallback = new Handler.Callback() { // from class: com.miui.gallery.picker.uri.Downloader.1
        @Override // android.os.Handler.Callback
        public boolean handleMessage(Message message) {
            DownloadResult downloadResult = (DownloadResult) message.obj;
            DefaultLogger.d("Downloader", "%s finished", downloadResult);
            if (downloadResult.mState == DownloadResult.State.SUCCESS) {
                Downloader.this.mSuccess.add(downloadResult);
            } else {
                Downloader.this.mFails.add(downloadResult);
            }
            if (Downloader.this.mDownloadListener != null) {
                Downloader.this.mDownloadListener.onUpdate(Downloader.this.mSuccess, Downloader.this.mFails);
            }
            Downloader.this.checkFinished();
            return false;
        }
    };
    public Handler mHandler = new Handler(Looper.getMainLooper(), this.mCallback);
    public List<DownloadResult> mSuccess = new ArrayList();
    public List<DownloadResult> mFails = new ArrayList();

    /* loaded from: classes2.dex */
    public interface DownloadListener {
        void onCancelled(List<DownloadResult> list, List<DownloadResult> list2);

        void onEnd(List<DownloadResult> list, List<DownloadResult> list2);

        void onStart(List<DownloadTask> list);

        void onUpdate(List<DownloadResult> list, List<DownloadResult> list2);
    }

    public Downloader(ArrayList<DownloadTask> arrayList, DownloadListener downloadListener) {
        this.mDownloadTasks = (List) arrayList.clone();
        this.mDownloadListener = downloadListener;
    }

    public void start() {
        ArrayList arrayList = new ArrayList(this.mDownloadTasks.size());
        ArrayList arrayList2 = new ArrayList(this.mDownloadTasks.size());
        ArrayList arrayList3 = new ArrayList(this.mDownloadTasks.size());
        for (DownloadTask downloadTask : this.mDownloadTasks) {
            arrayList.add(downloadTask.mUri);
            arrayList2.add(downloadTask.mType);
            arrayList3.add(new TaskStateListener(downloadTask));
        }
        DownloadListener downloadListener = this.mDownloadListener;
        if (downloadListener != null) {
            downloadListener.onStart(this.mDownloadTasks);
        }
        CloudImageLoader.getInstance().loadImages(arrayList, arrayList2, arrayList3, null);
    }

    public int getRemainSize() {
        return this.mDownloadTasks.size() - this.mSuccess.size();
    }

    public void cancel() {
        this.mCancelled = true;
        for (DownloadTask downloadTask : this.mDownloadTasks) {
            boolean z = false;
            Iterator<DownloadResult> it = this.mSuccess.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                } else if (it.next().mTask == downloadTask) {
                    z = true;
                    break;
                }
            }
            Iterator<DownloadResult> it2 = this.mFails.iterator();
            while (true) {
                if (!it2.hasNext()) {
                    break;
                } else if (it2.next().mTask == downloadTask) {
                    z = true;
                    break;
                }
            }
            if (!z) {
                DefaultLogger.d("Downloader", "cancel %s", downloadTask);
                CloudImageLoader.getInstance().cancel(downloadTask.mUri, downloadTask.mType);
            }
        }
    }

    public void pause() {
        DefaultLogger.d("Downloader", "downloader pause");
        this.mPaused = true;
    }

    public void resume() {
        DefaultLogger.d("Downloader", "downloader resume");
        this.mPaused = false;
        checkFinished();
    }

    public void destroy() {
        DefaultLogger.d("Downloader", "downloader destroy");
        this.mDownloadListener = null;
        this.mDownloadTasks.clear();
        this.mSuccess.clear();
        this.mFails.clear();
    }

    public final void checkFinished() {
        DownloadListener downloadListener;
        DefaultLogger.d("Downloader", "success: %d, failed: %d, total: %d", Integer.valueOf(this.mSuccess.size()), Integer.valueOf(this.mFails.size()), Integer.valueOf(this.mDownloadTasks.size()));
        if (this.mSuccess.size() + this.mFails.size() >= this.mDownloadTasks.size() && !this.mPaused && (downloadListener = this.mDownloadListener) != null) {
            if (this.mCancelled) {
                downloadListener.onCancelled(this.mSuccess, this.mFails);
            } else {
                downloadListener.onEnd(this.mSuccess, this.mFails);
            }
        }
    }

    /* loaded from: classes2.dex */
    public class TaskStateListener extends CloudImageLoadingListenerAdapter {
        public DownloadTask mTask;

        public TaskStateListener(DownloadTask downloadTask) {
            this.mTask = downloadTask;
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onDownloadComplete(Uri uri, DownloadType downloadType, View view, String str) {
            Downloader.this.mHandler.sendMessage(Downloader.this.mHandler.obtainMessage(1, new DownloadResult(this.mTask, DownloadResult.State.SUCCESS, str)));
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onLoadingFailed(Uri uri, DownloadType downloadType, View view, ErrorCode errorCode, String str) {
            Downloader.this.mHandler.sendMessage(Downloader.this.mHandler.obtainMessage(1, new DownloadResult(this.mTask, DownloadResult.State.FAILED, null)));
        }

        @Override // com.miui.gallery.util.cloudimageloader.CloudImageLoadingListenerAdapter, com.miui.gallery.util.cloudimageloader.CloudImageLoadingListener
        public void onLoadingCancelled(Uri uri, DownloadType downloadType, View view) {
            Downloader.this.mHandler.sendMessage(Downloader.this.mHandler.obtainMessage(1, new DownloadResult(this.mTask, DownloadResult.State.CANCELLED, null)));
        }
    }

    /* loaded from: classes2.dex */
    public static class DownloadResult {
        public String mPath;
        public State mState;
        public DownloadTask mTask;

        /* loaded from: classes2.dex */
        public enum State {
            PENDING,
            SUCCESS,
            FAILED,
            CANCELLED
        }

        public DownloadResult(DownloadTask downloadTask, State state, String str) {
            this.mTask = downloadTask;
            this.mState = state;
            this.mPath = str;
        }

        public String toString() {
            return "DownloadResult{mTask=" + this.mTask + ", mState=" + this.mState + ", mPath='" + this.mPath + "'}";
        }
    }

    /* loaded from: classes2.dex */
    public static class DownloadTask {
        public int mPosition;
        public int mSize;
        public DownloadType mType;
        public Uri mUri;

        public DownloadTask(Uri uri, DownloadType downloadType, int i, int i2) {
            this.mUri = uri;
            this.mType = downloadType;
            this.mSize = i;
            this.mPosition = i2;
        }

        public String toString() {
            return "DownloadTask{mUri=" + this.mUri + ", mType=" + this.mType + ", mSize=" + this.mSize + ", mPosition =" + this.mPosition + '}';
        }
    }
}
