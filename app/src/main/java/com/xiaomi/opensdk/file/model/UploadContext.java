package com.xiaomi.opensdk.file.model;

import cn.kuaipan.android.kss.upload.KssUploadFile;

/* loaded from: classes3.dex */
public class UploadContext {
    public boolean isNeedRequestUpload;
    public String mCommitString;
    public String mKssString;
    public final MiCloudFileListener mListener;
    public final KssUploadFile mLocalFile;
    public long mMaxChunkSize;
    public String mSha1;
    public final MiCloudTransferStopper mStopper;
    public String mUploadID;
    public UploadParameter mUploadParam;

    public UploadContext(KssUploadFile kssUploadFile, MiCloudFileListener miCloudFileListener, MiCloudTransferStopper miCloudTransferStopper) {
        this(kssUploadFile, miCloudFileListener, miCloudTransferStopper, 4194304L);
    }

    public UploadContext(KssUploadFile kssUploadFile, MiCloudFileListener miCloudFileListener, MiCloudTransferStopper miCloudTransferStopper, long j) {
        this.mLocalFile = kssUploadFile;
        this.mListener = miCloudFileListener;
        this.mStopper = miCloudTransferStopper;
        this.mMaxChunkSize = j;
    }

    public KssUploadFile getLocalFile() {
        return this.mLocalFile;
    }

    public String getFilePath() {
        return this.mLocalFile.fileAbsPath;
    }

    public long getFileSize() {
        return this.mLocalFile.fileSize;
    }

    public MiCloudFileListener getListener() {
        return this.mListener;
    }

    public MiCloudTransferStopper getStopper() {
        return this.mStopper;
    }

    public UploadParameter getUploadParam() {
        return this.mUploadParam;
    }

    public void setUploadParam(UploadParameter uploadParameter) {
        this.mUploadParam = uploadParameter;
    }

    public boolean isNeedRequestUpload() {
        return this.isNeedRequestUpload;
    }

    public void setNeedRequestUpload(boolean z) {
        this.isNeedRequestUpload = z;
    }

    public String getKssString() {
        return this.mKssString;
    }

    public void setKssString(String str) {
        this.mKssString = str;
    }

    public String getSha1() {
        return this.mSha1;
    }

    public void setSha1(String str) {
        this.mSha1 = str;
    }

    public String getCommitString() {
        return this.mCommitString;
    }

    public void setCommitString(String str) {
        this.mCommitString = str;
    }

    public void setUploadId(String str) {
        this.mUploadID = str;
    }

    public String getUploadId() {
        return this.mUploadID;
    }

    public long getMaxChunkSize() {
        return this.mMaxChunkSize;
    }
}
