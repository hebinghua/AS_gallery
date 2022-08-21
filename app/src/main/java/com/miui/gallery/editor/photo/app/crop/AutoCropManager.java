package com.miui.gallery.editor.photo.app.crop;

import android.graphics.Bitmap;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class AutoCropManager {
    public long handle = 0;

    /* loaded from: classes2.dex */
    public enum AutoCropError {
        AUTO_CROP_ERROR_SUCCESS,
        AUTO_CROP_ERROR_ERROR
    }

    public int create() {
        if (this.handle == 0) {
            this.handle = AutoCropJni.nativeCreate();
        }
        return AutoCropError.AUTO_CROP_ERROR_SUCCESS.ordinal();
    }

    public void destroy() {
        long j = this.handle;
        if (j != 0) {
            AutoCropJni.nativeDestroy(j);
            this.handle = 0L;
        }
    }

    public int init() {
        long j = this.handle;
        if (j != 0) {
            if (AutoCropJni.nativeInit(j) == 0) {
                return AutoCropError.AUTO_CROP_ERROR_SUCCESS.ordinal();
            }
            return AutoCropError.AUTO_CROP_ERROR_ERROR.ordinal();
        }
        return AutoCropError.AUTO_CROP_ERROR_ERROR.ordinal();
    }

    public int release() {
        long j = this.handle;
        if (j != 0) {
            if (AutoCropJni.nativeRelease(j) == 0) {
                return AutoCropError.AUTO_CROP_ERROR_SUCCESS.ordinal();
            }
            return AutoCropError.AUTO_CROP_ERROR_ERROR.ordinal();
        }
        return AutoCropError.AUTO_CROP_ERROR_ERROR.ordinal();
    }

    public String getVersion() {
        long j = this.handle;
        if (j != 0) {
            return AutoCropJni.nativeGetVersion(j);
        }
        return null;
    }

    public int getCropParams(Bitmap bitmap, float[] fArr, Bbox bbox) {
        if (this.handle != 0) {
            long currentTimeMillis = System.currentTimeMillis();
            int nativeGetCropParams = AutoCropJni.nativeGetCropParams(this.handle, bitmap, fArr, bbox);
            DefaultLogger.d("AutoCropUtils", "getCropParams: result = " + nativeGetCropParams + " consume time = " + (System.currentTimeMillis() - currentTimeMillis));
            if (nativeGetCropParams == 1) {
                return AutoCropError.AUTO_CROP_ERROR_SUCCESS.ordinal();
            }
            return AutoCropError.AUTO_CROP_ERROR_ERROR.ordinal();
        }
        return AutoCropError.AUTO_CROP_ERROR_ERROR.ordinal();
    }
}
