package com.miui.gallery.util.deviceprovider;

import android.graphics.BitmapFactory;
import java.io.IOException;

/* loaded from: classes2.dex */
public class BigBitmapProvider implements BitmapProviderInterface {
    @Override // com.miui.gallery.util.deviceprovider.BitmapProviderInterface
    public BitmapFactory.Options getBitmapSize(String str) throws IOException {
        return miuix.graphics.BitmapFactory.getBitmapSize(str);
    }
}
