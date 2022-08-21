package com.miui.gallery.vlog.base.net;

import com.miui.gallery.net.resource.LocalResource;

/* loaded from: classes2.dex */
public class VlogResource extends LocalResource implements Comparable<VlogResource> {
    public volatile int mDownloadState = 19;
    public boolean mIsTemplate;
    public String mUnZipPath;

    @Override // com.miui.gallery.net.resource.LocalResource
    public boolean isTemplate() {
        return this.mIsTemplate;
    }

    public void setTemplate(boolean z) {
        this.mIsTemplate = z;
    }

    @Override // com.miui.gallery.net.resource.LocalResource
    public void setUnZipPath(String str) {
        this.mUnZipPath = str;
    }

    @Override // com.miui.gallery.net.resource.LocalResource
    public String getLabel() {
        return this.label;
    }

    public boolean isDownloaded() {
        return this.mDownloadState == 17;
    }

    public boolean isDownloadSuccess() {
        return this.mDownloadState == 0;
    }

    public int getDownloadState() {
        return this.mDownloadState;
    }

    public void setDownloadState(int i) {
        this.mDownloadState = i;
    }

    @Override // java.lang.Comparable
    public int compareTo(VlogResource vlogResource) {
        return this.index - vlogResource.index;
    }
}
