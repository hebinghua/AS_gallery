package com.miui.gallery.googlelens;

import android.content.Context;
import android.graphics.Bitmap;

/* loaded from: classes2.dex */
public interface IGoogleLens {
    void checkPostCaptureAvailability(LensAvailabilityCallback lensAvailabilityCallback);

    void init(Context context);

    boolean launchLensActivityWithBitmap(Bitmap bitmap);

    void onPause();

    void onResume();

    void release();
}
