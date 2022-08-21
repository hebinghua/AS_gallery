package com.miui.gallery.net.fetch;

import java.io.File;

/* loaded from: classes2.dex */
public abstract class Request {
    public long mId;
    public Listener mListener;
    public String mName;
    public File mDestDir = getDestDir();
    public File mZipFile = getZipFile();

    /* loaded from: classes2.dex */
    public interface Listener {
        void onFail();

        void onStart();

        void onSuccess();
    }

    public abstract void deleteHistoricVersion();

    public abstract File getDestDir();

    public abstract File getZipFile();

    public Request(String str, long j) {
        this.mName = str;
        this.mId = j;
    }

    public long getId() {
        return this.mId;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public Listener getListener() {
        return this.mListener;
    }

    public File destDir() {
        return this.mDestDir;
    }

    public File zipFile() {
        return this.mZipFile;
    }
}
