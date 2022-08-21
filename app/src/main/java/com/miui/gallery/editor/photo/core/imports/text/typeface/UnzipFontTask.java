package com.miui.gallery.editor.photo.core.imports.text.typeface;

import android.os.AsyncTask;
import com.miui.gallery.util.UnzipUtils;

/* loaded from: classes2.dex */
public class UnzipFontTask extends AsyncTask<Void, Void, Boolean> {
    public DownloadCallback mCallBack;
    public TextStyle mResource;

    public UnzipFontTask(TextStyle textStyle, DownloadCallback downloadCallback) {
        this.mResource = textStyle;
        this.mCallBack = downloadCallback;
    }

    @Override // android.os.AsyncTask
    public Boolean doInBackground(Void... voidArr) {
        if (isCancelled()) {
            return Boolean.FALSE;
        }
        return Boolean.valueOf(UnzipUtils.unZipFile(this.mResource.getDownloadFilePath()));
    }

    @Override // android.os.AsyncTask
    public void onPostExecute(Boolean bool) {
        DownloadCallback downloadCallback;
        if (!isCancelled() && (downloadCallback = this.mCallBack) != null) {
            downloadCallback.onCompleted(bool.booleanValue());
        }
    }

    public void release() {
        cancel(true);
        this.mCallBack = null;
    }
}
