package com.miui.gallery.googlelens;

import android.content.Context;
import android.graphics.Bitmap;

/* loaded from: classes2.dex */
public class GoogleLensImpl implements IGoogleLens {
    @Override // com.miui.gallery.googlelens.IGoogleLens
    public void init(Context context) {
    }

    @Override // com.miui.gallery.googlelens.IGoogleLens
    public boolean launchLensActivityWithBitmap(Bitmap bitmap) {
        return false;
    }

    @Override // com.miui.gallery.googlelens.IGoogleLens
    public void onPause() {
    }

    @Override // com.miui.gallery.googlelens.IGoogleLens
    public void onResume() {
    }

    @Override // com.miui.gallery.googlelens.IGoogleLens
    public void release() {
    }

    @Override // com.miui.gallery.googlelens.IGoogleLens
    public void checkPostCaptureAvailability(LensAvailabilityCallback lensAvailabilityCallback) {
        lensAvailabilityCallback.onPostCaptureAvailabilityStatus(1);
    }
}
