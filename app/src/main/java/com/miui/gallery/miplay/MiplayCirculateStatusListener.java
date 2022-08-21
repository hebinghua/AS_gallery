package com.miui.gallery.miplay;

import com.xiaomi.miplay.phoneclientsdk.info.MediaMetaData;

/* loaded from: classes2.dex */
public interface MiplayCirculateStatusListener {
    void onCirculateFailed();

    void onCirculatePreparing(MediaMetaData mediaMetaData);

    void onCirculateStart();

    void onMirrorResumed();
}
