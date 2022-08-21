package com.miui.gallery.cloud;

import com.miui.gallery.cloud.HostManager;

/* loaded from: classes.dex */
public abstract class CloudUrlProvider {
    public static final CloudUrlProvider sOwnerImage = new OwnerImageUrlProvider();
    public static final CloudUrlProvider sSharerImage = new SharerImageUrlProvider();
    public static final CloudUrlProvider sOwnerVideo = new OwnerVideoUrlProvider();
    public static final CloudUrlProvider sSharerVideo = new SharerVideoUrlProvider();

    public abstract String getCommitSubUbiUrl(String str, String str2, int i);

    public abstract String getCommitUrl(String str, String str2);

    public abstract String getCopyUrl(String str, String str2);

    public abstract String getCreateSubUbiUrl(String str, String str2, int i);

    public abstract String getCreateUrl(String str, String str2);

    public abstract String getDeleteUrl(String str, String str2);

    public abstract String getEditUrl(String str, String str2);

    public abstract String getHideCopyUrl(String str, String str2);

    public abstract String getHideMoveUrl(String str, String str2);

    public abstract String getMoveUrl(String str, String str2);

    public abstract String getRequestDownloadUrl(String str, String str2);

    public abstract String getUnHideMoveUrl(String str, String str2);

    public abstract String getUnhideCopyUrl(String str, String str2);

    public abstract String getUpdateUrl(String str, String str2);

    /* loaded from: classes.dex */
    public static class OwnerImageUrlProvider extends CloudUrlProvider {
        public OwnerImageUrlProvider() {
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCommitUrl(String str, String str2) {
            return HostManager.OwnerMedia.getCommitUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCreateUrl(String str, String str2) {
            return HostManager.OwnerImage.getCreateUrl();
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getRequestDownloadUrl(String str, String str2) {
            return HostManager.OwnerImage.getDownloadUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCopyUrl(String str, String str2) {
            return HostManager.OwnerImage.getCopyUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getDeleteUrl(String str, String str2) {
            return HostManager.OwnerImage.getDeleteUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getMoveUrl(String str, String str2) {
            return HostManager.OwnerImage.getMoveUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCreateSubUbiUrl(String str, String str2, int i) {
            return HostManager.OwnerImage.getCreateSubUbiUrl(str2, i);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCommitSubUbiUrl(String str, String str2, int i) {
            return HostManager.OwnerImage.getCommitSubUbiUrl(str2, i);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getEditUrl(String str, String str2) {
            return HostManager.OwnerImage.getEditUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getHideMoveUrl(String str, String str2) {
            return HostManager.OwnerImage.getHideMoveUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getUnHideMoveUrl(String str, String str2) {
            return HostManager.OwnerImage.getUnHideMoveUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getHideCopyUrl(String str, String str2) {
            return HostManager.OwnerImage.getHideCopyUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getUnhideCopyUrl(String str, String str2) {
            return HostManager.OwnerImage.getUnHideCopyUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getUpdateUrl(String str, String str2) {
            return HostManager.OwnerMedia.getUpdateUrl(str2);
        }
    }

    /* loaded from: classes.dex */
    public static class OwnerVideoUrlProvider extends CloudUrlProvider {
        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCommitSubUbiUrl(String str, String str2, int i) {
            return null;
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCreateSubUbiUrl(String str, String str2, int i) {
            return null;
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getEditUrl(String str, String str2) {
            return null;
        }

        public OwnerVideoUrlProvider() {
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCommitUrl(String str, String str2) {
            return HostManager.OwnerMedia.getCommitUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCreateUrl(String str, String str2) {
            return HostManager.OwnerVideo.getCreateUrl();
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getRequestDownloadUrl(String str, String str2) {
            return HostManager.OwnerVideo.getDownloadUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCopyUrl(String str, String str2) {
            return HostManager.OwnerVideo.getCopyUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getDeleteUrl(String str, String str2) {
            return HostManager.OwnerVideo.getDeleteUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getMoveUrl(String str, String str2) {
            return HostManager.OwnerVideo.getMoveUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getHideMoveUrl(String str, String str2) {
            return HostManager.OwnerVideo.getHideMoveUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getUnHideMoveUrl(String str, String str2) {
            return HostManager.OwnerVideo.getUnHideMoveUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getHideCopyUrl(String str, String str2) {
            return HostManager.OwnerVideo.getHideCopyUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getUnhideCopyUrl(String str, String str2) {
            return HostManager.OwnerVideo.getUnHideCopyUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getUpdateUrl(String str, String str2) {
            return HostManager.OwnerMedia.getUpdateUrl(str2);
        }
    }

    /* loaded from: classes.dex */
    public static class SharerImageUrlProvider extends CloudUrlProvider {
        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getEditUrl(String str, String str2) {
            return null;
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getHideCopyUrl(String str, String str2) {
            return null;
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getHideMoveUrl(String str, String str2) {
            return null;
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getUnHideMoveUrl(String str, String str2) {
            return null;
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getUnhideCopyUrl(String str, String str2) {
            return null;
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getUpdateUrl(String str, String str2) {
            return null;
        }

        public SharerImageUrlProvider() {
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCommitUrl(String str, String str2) {
            return HostManager.ShareMedia.getCommitUrl();
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCreateUrl(String str, String str2) {
            return HostManager.ShareImage.getCreateUrl();
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getRequestDownloadUrl(String str, String str2) {
            return HostManager.ShareImage.getDownloadUrl();
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCopyUrl(String str, String str2) {
            return HostManager.ShareImage.getCopyUrl();
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getDeleteUrl(String str, String str2) {
            return HostManager.ShareImage.getDeleteUrl();
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getMoveUrl(String str, String str2) {
            return HostManager.ShareImage.getMoveUrl(str2);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCreateSubUbiUrl(String str, String str2, int i) {
            return HostManager.ShareImage.getCreateSubUbiUrl(i);
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCommitSubUbiUrl(String str, String str2, int i) {
            return HostManager.ShareImage.getCommitSubUbiUrl(i);
        }
    }

    /* loaded from: classes.dex */
    public static class SharerVideoUrlProvider extends CloudUrlProvider {
        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCommitSubUbiUrl(String str, String str2, int i) {
            return null;
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCreateSubUbiUrl(String str, String str2, int i) {
            return null;
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getEditUrl(String str, String str2) {
            return null;
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getHideCopyUrl(String str, String str2) {
            return null;
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getHideMoveUrl(String str, String str2) {
            return null;
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getUnHideMoveUrl(String str, String str2) {
            return null;
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getUnhideCopyUrl(String str, String str2) {
            return null;
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getUpdateUrl(String str, String str2) {
            return null;
        }

        public SharerVideoUrlProvider() {
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCommitUrl(String str, String str2) {
            return HostManager.ShareMedia.getCommitUrl();
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCreateUrl(String str, String str2) {
            return HostManager.ShareVideo.getCreateUrl();
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getRequestDownloadUrl(String str, String str2) {
            return HostManager.ShareVideo.getDownloadUrl();
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getCopyUrl(String str, String str2) {
            return HostManager.ShareVideo.getCopyUrl();
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getDeleteUrl(String str, String str2) {
            return HostManager.ShareVideo.getDeleteUrl();
        }

        @Override // com.miui.gallery.cloud.CloudUrlProvider
        public String getMoveUrl(String str, String str2) {
            return HostManager.ShareVideo.getMoveUrl(str2);
        }
    }

    public static CloudUrlProvider getUrlProvider(boolean z, boolean z2) {
        return z ? z2 ? sSharerVideo : sSharerImage : z2 ? sOwnerVideo : sOwnerImage;
    }
}
