package com.miui.gallery.analytics;

import android.content.Context;
import java.util.Map;

/* loaded from: classes.dex */
public interface ITrackHelper {
    void init(Context context);

    void track(String str, Map<String, Object> map);
}
