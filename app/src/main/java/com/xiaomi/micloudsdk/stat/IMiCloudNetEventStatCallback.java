package com.xiaomi.micloudsdk.stat;

/* loaded from: classes3.dex */
public interface IMiCloudNetEventStatCallback {
    void onAddNetFailedEvent(NetFailedStatParam netFailedStatParam);

    void onAddNetSuccessEvent(NetSuccessStatParam netSuccessStatParam);
}
