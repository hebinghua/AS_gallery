package com.xiaomi.mirror.synergy;

import android.graphics.Bitmap;

/* loaded from: classes3.dex */
public interface RelayIconCallback {
    void onIconHide();

    void onIconShow();

    void onIconUpdate(String str, Bitmap bitmap);
}
