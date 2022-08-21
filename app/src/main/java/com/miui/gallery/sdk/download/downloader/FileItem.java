package com.miui.gallery.sdk.download.downloader;

import android.text.TextUtils;

/* loaded from: classes2.dex */
public class FileItem {
    public static final FileItem NONE = new FileItem(-1, null);
    public final int mFileType;
    public final String mPath;

    public FileItem(int i, String str) {
        this.mFileType = i;
        this.mPath = str;
    }

    public int getFileType() {
        return this.mFileType;
    }

    public String getPath() {
        return this.mPath;
    }

    public boolean isFileValid() {
        return this.mFileType != -1 && !TextUtils.isEmpty(this.mPath);
    }
}
