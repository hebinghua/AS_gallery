package com.miui.gallery.map.location;

import android.content.Context;

/* loaded from: classes2.dex */
public interface ILocationClient {
    void init(Context context);

    void registerLocationListener(ILocationListener iLocationListener);

    void start();

    void stop();

    void unregisterLocationListener();
}
