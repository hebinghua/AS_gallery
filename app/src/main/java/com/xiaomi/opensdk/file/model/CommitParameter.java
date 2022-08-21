package com.xiaomi.opensdk.file.model;

/* loaded from: classes3.dex */
public class CommitParameter {
    public String mAwsString;
    public String mFileSHA1;
    public long mFileSize;
    public String mKssString;
    public String mUploadId;

    public CommitParameter(String str, String str2, String str3, String str4, long j) {
        this.mFileSize = -1L;
        this.mKssString = str;
        this.mAwsString = str2;
        this.mUploadId = str3;
        this.mFileSHA1 = str4;
        this.mFileSize = j;
    }

    public String getKssString() {
        return this.mKssString;
    }

    public String getUploadId() {
        return this.mUploadId;
    }
}
