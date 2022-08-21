package com.nexstreaming.nexeditorsdk.module;

import android.content.Context;
import android.graphics.RectF;

/* loaded from: classes3.dex */
public interface nexFaceDetectionMethod {
    boolean deinit();

    boolean detect(RectF rectF);

    boolean init(String str, Context context);
}
