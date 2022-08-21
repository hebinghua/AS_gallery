package com.miui.gallery.vlog.base.net.resource;

import android.os.AsyncTask;
import android.text.TextUtils;
import com.miui.gallery.net.downloadqueues.DownloadCommand;
import com.miui.gallery.net.resource.LocalResource;
import com.miui.gallery.vlog.base.interfaces.IUnzipFileListener;
import com.miui.gallery.vlog.tools.FileHelper;

/* loaded from: classes2.dex */
public class UnZipAsyncTask extends AsyncTask<Void, Void, Boolean> {
    public LocalResource mData;
    public String mUnZipPath;
    public IUnzipFileListener mUnzipFileListener;
    public String mZipPath;

    public UnZipAsyncTask(DownloadCommand downloadCommand, IUnzipFileListener iUnzipFileListener) {
        this.mData = downloadCommand.getData();
        this.mZipPath = downloadCommand.getZipPath();
        this.mUnZipPath = downloadCommand.getUnzipPath();
        this.mUnzipFileListener = iUnzipFileListener;
    }

    @Override // android.os.AsyncTask
    public Boolean doInBackground(Void... voidArr) {
        boolean z;
        if (!isCancelled()) {
            String unZipFile = FileHelper.unZipFile(this.mZipPath, this.mUnZipPath);
            this.mUnZipPath = unZipFile;
            if (!TextUtils.isEmpty(unZipFile)) {
                this.mData.setUnZipPath(this.mUnZipPath);
                z = true;
                return Boolean.valueOf(z);
            }
        }
        z = false;
        return Boolean.valueOf(z);
    }

    @Override // android.os.AsyncTask
    public void onPostExecute(Boolean bool) {
        if (this.mUnzipFileListener == null) {
            return;
        }
        if (bool.booleanValue()) {
            this.mUnzipFileListener.onUnzipFileSuccess();
        } else {
            this.mUnzipFileListener.onUnzipFileFailed(false);
        }
    }

    public void clearListener() {
        IUnzipFileListener iUnzipFileListener = this.mUnzipFileListener;
        if (iUnzipFileListener != null) {
            iUnzipFileListener.onUnzipFileFailed(true);
            this.mUnzipFileListener = null;
        }
    }
}
