package com.miui.gallery.video.editor.manager;

import android.os.AsyncTask;
import android.text.TextUtils;
import com.miui.gallery.video.editor.DownloadCommand;
import com.miui.gallery.video.editor.interfaces.IVideoEditorListener$IUnzipFileListener;
import com.miui.gallery.video.editor.model.VideoEditorBaseModel;
import com.miui.gallery.video.editor.util.FileHelper;

/* loaded from: classes2.dex */
public class UnZipAsyncTask extends AsyncTask<Void, Void, Boolean> {
    public VideoEditorBaseModel mData;
    public String mUnZipPath;
    public IVideoEditorListener$IUnzipFileListener mUnzipFileListener;
    public String mZipPath;

    public UnZipAsyncTask(DownloadCommand downloadCommand, IVideoEditorListener$IUnzipFileListener iVideoEditorListener$IUnzipFileListener) {
        this.mData = downloadCommand.getData();
        this.mZipPath = downloadCommand.getZipPath();
        this.mUnZipPath = downloadCommand.getUnzipPath();
        this.mUnzipFileListener = iVideoEditorListener$IUnzipFileListener;
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
        IVideoEditorListener$IUnzipFileListener iVideoEditorListener$IUnzipFileListener = this.mUnzipFileListener;
        if (iVideoEditorListener$IUnzipFileListener != null) {
            iVideoEditorListener$IUnzipFileListener.onUnzipFileFailed(true);
            this.mUnzipFileListener = null;
        }
    }
}
