package com.miui.gallery.ui.album.common.base.requestbean;

/* loaded from: classes2.dex */
public class BaseOperationAlbumRequestBean {
    public long[] albumIds;
    public boolean enable;

    public BaseOperationAlbumRequestBean(long[] jArr, boolean z) {
        this.albumIds = jArr;
        this.enable = z;
    }

    public BaseOperationAlbumRequestBean(long j, boolean z) {
        this.albumIds = new long[]{j};
        this.enable = z;
    }

    public long getFirstAlbumId() {
        return this.albumIds[0];
    }

    public long[] getAlbumIds() {
        return this.albumIds;
    }

    public boolean isEnable() {
        return this.enable;
    }
}
