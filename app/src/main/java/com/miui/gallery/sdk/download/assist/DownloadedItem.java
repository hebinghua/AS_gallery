package com.miui.gallery.sdk.download.assist;

/* loaded from: classes2.dex */
public class DownloadedItem {
    public byte[] mFileCipher;
    public String mFilePath;

    public DownloadedItem(String str, byte[] bArr) {
        this.mFilePath = str;
        this.mFileCipher = bArr;
    }

    public String getFilePath() {
        return this.mFilePath;
    }

    public byte[] getFileCipher() {
        return this.mFileCipher;
    }
}
