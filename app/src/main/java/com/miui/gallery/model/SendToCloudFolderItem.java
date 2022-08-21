package com.miui.gallery.model;

import com.miui.gallery.cloud.baby.BabyInfo;

/* loaded from: classes2.dex */
public class SendToCloudFolderItem {
    public BabyInfo babyInfo;
    public final int count;
    public String folderName;
    public final boolean isShareAlbum;
    public final String localGroupId;

    public SendToCloudFolderItem(int i, String str, boolean z) {
        this.count = i;
        this.localGroupId = str;
        this.isShareAlbum = z;
    }

    public SendToCloudFolderItem(int i, String str, boolean z, String str2, BabyInfo babyInfo) {
        this(i, str, z);
        this.folderName = str2;
        this.babyInfo = babyInfo;
    }

    public String getLocalGroupId() {
        return this.localGroupId;
    }

    public boolean isShareAlbum() {
        return this.isShareAlbum;
    }

    public String getFolderName() {
        return this.folderName;
    }
}
