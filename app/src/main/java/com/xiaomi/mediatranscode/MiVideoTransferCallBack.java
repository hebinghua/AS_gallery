package com.xiaomi.mediatranscode;

import com.xiaomi.mediatranscode.MiVideoTranscode;

/* loaded from: classes3.dex */
public class MiVideoTransferCallBack implements MiVideoTranscode.TransferCallBack {
    private static final String TAG = "MiVideoTransferCallBack";
    private long TransferCallBack;

    private native void OnGetPercentJni(long j, int i);

    private native void OnTranscodeFailedJni(long j, int i);

    private native void OnTranscodeSuccessedJni(long j);

    public MiVideoTransferCallBack(long j) {
        this.TransferCallBack = 0L;
        this.TransferCallBack = j;
    }

    @Override // com.xiaomi.mediatranscode.MiVideoTranscode.TransferCallBack
    public void OnTranscodeSuccessed() {
        OnTranscodeSuccessedJni(this.TransferCallBack);
    }

    @Override // com.xiaomi.mediatranscode.MiVideoTranscode.TransferCallBack
    public void OnTranscodeFailed(int i) {
        OnTranscodeFailedJni(this.TransferCallBack, i);
    }

    @Override // com.xiaomi.mediatranscode.MiVideoTranscode.TransferCallBack
    public void OnGetPercent(int i) {
        OnGetPercentJni(this.TransferCallBack, i);
    }
}
