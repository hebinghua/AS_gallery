package com.miui.gallery.magic.matting.preview;

import android.graphics.Bitmap;
import android.net.Uri;
import com.miui.gallery.magic.matting.bean.BackgroundItem;

/* loaded from: classes2.dex */
public interface IPreview$M {
    Bitmap decodeOrigin(Uri uri);

    Bitmap decodeOrigin(BackgroundItem backgroundItem);
}
