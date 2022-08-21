package com.miui.gallery.magic.special.effects.image.preview;

import android.graphics.Bitmap;
import android.net.Uri;

/* loaded from: classes2.dex */
public interface IPreview$VP {
    void contrastImage();

    Bitmap getOriginBitmap();

    void loadPreview(Bitmap bitmap);

    void saveImage();

    void selectPhotos(Uri uri);

    void setPreviewBitmap(Bitmap bitmap);

    void setPreviewBitmap(Bitmap bitmap, boolean z);
}
