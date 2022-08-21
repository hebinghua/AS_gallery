package com.xiaomi.opensdk.file.model;

/* loaded from: classes3.dex */
public class RequestUploadParameter {
    public String mAwsString;
    public String mFileMimeType = null;
    public String mFileName;
    public String mFileSHA1;
    public long mFileSize;
    public String mKssString;

    public RequestUploadParameter(String str, String str2, String str3, long j, String str4) {
        this.mFileSize = -1L;
        this.mKssString = str;
        this.mAwsString = str2;
        this.mFileSHA1 = str3;
        this.mFileSize = j;
        this.mFileName = str4;
    }

    public String getKssString() {
        return this.mKssString;
    }

    public String getFileSHA1() {
        return this.mFileSHA1;
    }
}
