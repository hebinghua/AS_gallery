package com.miui.gallery.cloud;

import com.miui.gallery.cloud.HostManager;

/* loaded from: classes.dex */
public abstract class CloudGroupUrlProvider {
    public abstract String getEditGroupUrl(String str, String str2);

    public abstract String getThumbnailInfoUrl(String str, String str2);

    /* loaded from: classes.dex */
    public static class OwnerCloudGroupUrlProvider extends CloudGroupUrlProvider {
        public OwnerCloudGroupUrlProvider() {
        }

        @Override // com.miui.gallery.cloud.CloudGroupUrlProvider
        public String getThumbnailInfoUrl(String str, String str2) {
            return HostManager.OwnerAlbum.getThumbnailInfoUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudGroupUrlProvider
        public String getEditGroupUrl(String str, String str2) {
            return HostManager.OwnerAlbum.getEditAlbumUrl(str2);
        }
    }

    /* loaded from: classes.dex */
    public static class SharerCloudGroupUrlProvider extends CloudGroupUrlProvider {
        public SharerCloudGroupUrlProvider() {
        }

        @Override // com.miui.gallery.cloud.CloudGroupUrlProvider
        public String getThumbnailInfoUrl(String str, String str2) {
            return HostManager.ShareAlbum.getThumbnailInfoUrl();
        }

        @Override // com.miui.gallery.cloud.CloudGroupUrlProvider
        public String getEditGroupUrl(String str, String str2) {
            throw new UnsupportedOperationException("sharer album doesn't support to be edited!");
        }
    }

    /* loaded from: classes.dex */
    public static class OwnerInstanceHolder {
        public static final CloudGroupUrlProvider sOwnerUrlProvider = new OwnerCloudGroupUrlProvider();
    }

    /* loaded from: classes.dex */
    public static class SharerInstanceHolder {
        public static final CloudGroupUrlProvider sSharerUrlProvider = new SharerCloudGroupUrlProvider();
    }

    public static CloudGroupUrlProvider getUrlProvider(boolean z) {
        if (!z) {
            return OwnerInstanceHolder.sOwnerUrlProvider;
        }
        return SharerInstanceHolder.sSharerUrlProvider;
    }
}
