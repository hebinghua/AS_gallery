package com.miui.gallery.editor.photo.core.imports.obsoletes;

import android.animation.TypeEvaluator;
import android.graphics.RectF;

/* loaded from: classes2.dex */
public class RectFEvaluator implements TypeEvaluator<RectF> {
    public RectF mRect = new RectF();

    @Override // android.animation.TypeEvaluator
    public RectF evaluate(float f, RectF rectF, RectF rectF2) {
        float f2 = rectF.left;
        float f3 = f2 + ((rectF2.left - f2) * f);
        float f4 = rectF.top;
        float f5 = f4 + ((rectF2.top - f4) * f);
        float f6 = rectF.right;
        float f7 = rectF.bottom;
        this.mRect.set(f3, f5, f6 + ((rectF2.right - f6) * f), f7 + ((rectF2.bottom - f7) * f));
        return this.mRect;
    }
}
