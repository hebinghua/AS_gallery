package com.miui.gallery.map.utils;

import android.graphics.Bitmap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.miui.gallery.map.view.BitmapDescriptorWrapper;

/* loaded from: classes2.dex */
public class BitmapDescriptorWrapperFactory {
    public static BitmapDescriptorWrapper fromBitmap(Bitmap bitmap) {
        return new BitmapDescriptorWrapper(BitmapDescriptorFactory.fromBitmap(bitmap));
    }
}
