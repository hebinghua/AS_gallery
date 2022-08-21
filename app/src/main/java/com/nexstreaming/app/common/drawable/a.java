package com.nexstreaming.app.common.drawable;

import android.graphics.Canvas;
import android.graphics.Picture;
import android.graphics.Rect;
import android.graphics.drawable.PictureDrawable;
import android.util.SparseArray;
import java.lang.ref.WeakReference;

/* compiled from: ScalablePictureDrawable.java */
/* loaded from: classes3.dex */
public class a extends PictureDrawable {
    public static SparseArray<WeakReference<a>> a = new SparseArray<>();

    public a(Picture picture) {
        super(picture);
    }

    @Override // android.graphics.drawable.PictureDrawable, android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Picture picture = getPicture();
        if (picture == null) {
            return;
        }
        Rect bounds = getBounds();
        canvas.save();
        canvas.clipRect(bounds);
        canvas.translate(bounds.left, bounds.top);
        canvas.scale(bounds.width() / getIntrinsicWidth(), bounds.height() / getIntrinsicHeight());
        canvas.drawPicture(picture);
        canvas.restore();
    }
}
