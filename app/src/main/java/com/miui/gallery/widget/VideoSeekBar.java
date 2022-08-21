package com.miui.gallery.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.widget.SeekBar;

/* loaded from: classes2.dex */
public class VideoSeekBar extends SeekBar {
    public VideoSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r10v0, types: [android.graphics.drawable.Drawable] */
    /* JADX WARN: Type inference failed for: r10v1, types: [android.graphics.drawable.Drawable] */
    /* JADX WARN: Type inference failed for: r10v2, types: [android.graphics.drawable.LayerDrawable] */
    /* JADX WARN: Type inference failed for: r9v0, types: [android.widget.SeekBar, com.miui.gallery.widget.VideoSeekBar] */
    @Override // android.widget.ProgressBar
    public void setProgressDrawable(Drawable drawable) {
        if (drawable instanceof LayerDrawable) {
            LayerDrawable layerDrawable = (LayerDrawable) drawable;
            int numberOfLayers = layerDrawable.getNumberOfLayers();
            Drawable[] drawableArr = new Drawable[numberOfLayers];
            boolean z = false;
            for (int i = 0; i < numberOfLayers; i++) {
                int id = layerDrawable.getId(i);
                Drawable drawable2 = layerDrawable.getDrawable(i);
                if ((id == 16908301 || id == 16908303) && (drawable2 instanceof NinePatchDrawable)) {
                    drawable2 = new LevelNinePathDrawable((NinePatchDrawable) drawable2);
                    z = true;
                }
                drawableArr[i] = drawable2;
            }
            if (z) {
                drawable = new LayerDrawable(drawableArr);
                for (int i2 = 0; i2 < numberOfLayers; i2++) {
                    drawable.setId(i2, layerDrawable.getId(i2));
                }
            }
        }
        super.setProgressDrawable(drawable);
    }
}
