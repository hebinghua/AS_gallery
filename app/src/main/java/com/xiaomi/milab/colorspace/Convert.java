package com.xiaomi.milab.colorspace;

import android.graphics.Bitmap;
import com.xiaomi.milab.gpu.GPUReaderThread;

/* loaded from: classes3.dex */
public class Convert {
    public static Bitmap processLutByGPU(Bitmap bitmap) {
        return new GPUReaderThread(bitmap, null, null).runGL();
    }
}
