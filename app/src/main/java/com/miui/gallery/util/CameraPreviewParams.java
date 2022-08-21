package com.miui.gallery.util;

import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.text.TextUtils;
import com.baidu.platform.comapi.map.MapBundleKey;
import com.nexstreaming.nexeditorsdk.nexExportFormat;
import com.xiaomi.mirror.synergy.CallMethod;

/* loaded from: classes2.dex */
public class CameraPreviewParams {
    public int mByteCount;
    public int mHeight;
    public boolean mIsValid = true;
    public ParcelFileDescriptor mPFD;
    public String mUri;
    public int mWidth;

    public static CameraPreviewParams create(Bundle bundle) {
        ParcelFileDescriptor parcelFileDescriptor;
        int i;
        int i2;
        int i3;
        if (bundle == null) {
            return null;
        }
        String string = bundle.getString(CallMethod.ARG_URI);
        if (TextUtils.isEmpty(string) || (parcelFileDescriptor = (ParcelFileDescriptor) bundle.getParcelable("bitmap")) == null || (i = bundle.getInt(nexExportFormat.TAG_FORMAT_WIDTH, -1)) <= 0 || (i2 = bundle.getInt(nexExportFormat.TAG_FORMAT_HEIGHT, -1)) <= 0 || (i3 = bundle.getInt(MapBundleKey.OfflineMapKey.OFFLINE_TOTAL_SIZE, -1)) <= 0) {
            return null;
        }
        return new CameraPreviewParams(string, parcelFileDescriptor, i, i2, i3);
    }

    public CameraPreviewParams(String str, ParcelFileDescriptor parcelFileDescriptor, int i, int i2, int i3) {
        this.mUri = str;
        this.mPFD = parcelFileDescriptor;
        this.mWidth = i;
        this.mHeight = i2;
        this.mByteCount = i3;
    }

    public String getUri() {
        return this.mUri;
    }

    public ParcelFileDescriptor getPFD() {
        return this.mPFD;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getByteCount() {
        return this.mByteCount;
    }

    public void invalid() {
        this.mIsValid = false;
    }

    public boolean isValid() {
        return this.mIsValid;
    }
}
