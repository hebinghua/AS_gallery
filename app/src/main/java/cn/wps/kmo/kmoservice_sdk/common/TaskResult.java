package cn.wps.kmo.kmoservice_sdk.common;

import android.os.Bundle;

/* loaded from: classes.dex */
public class TaskResult {
    public Bundle mBundle;
    public int mResultCode;

    public TaskResult(int i, Bundle bundle) {
        this.mResultCode = i;
        this.mBundle = bundle;
    }
}
