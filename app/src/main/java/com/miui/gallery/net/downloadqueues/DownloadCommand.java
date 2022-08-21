package com.miui.gallery.net.downloadqueues;

import com.miui.gallery.net.resource.LocalResource;

/* loaded from: classes2.dex */
public class DownloadCommand<T extends LocalResource> {
    public T mBaseModel;
    public long mId;
    public boolean mIsTemplate;
    public int mPosition;
    public String mUnzipPath;
    public String mZipPath;

    public DownloadCommand(T t, int i, IZipFileConfig iZipFileConfig) {
        this.mBaseModel = t;
        this.mId = t.getId();
        this.mPosition = i;
        this.mIsTemplate = t.isTemplate();
        this.mZipPath = iZipFileConfig.getZipPath(t.id);
        this.mUnzipPath = iZipFileConfig.getUnzipPath();
    }

    public boolean equals(Object obj) {
        return obj != null && (obj instanceof DownloadCommand) && ((DownloadCommand) obj).getId() == this.mId;
    }

    public int hashCode() {
        return String.valueOf(this.mId).hashCode();
    }

    public String getZipPath() {
        return this.mZipPath;
    }

    public String getUnzipPath() {
        return this.mUnzipPath;
    }

    public long getId() {
        return this.mId;
    }

    public int getPosition() {
        return this.mPosition;
    }

    public T getData() {
        return this.mBaseModel;
    }

    public boolean isTemplate() {
        return this.mIsTemplate;
    }
}
