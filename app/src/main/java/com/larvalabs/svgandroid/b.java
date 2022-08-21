package com.larvalabs.svgandroid;

import android.graphics.Picture;
import android.graphics.RectF;
import android.graphics.drawable.PictureDrawable;
import java.util.Set;

/* compiled from: SVG.java */
/* loaded from: classes.dex */
public class b {
    public final Set<Integer> a;
    public Picture b;
    public RectF c;
    public RectF d = null;

    public b(Picture picture, RectF rectF, Set<Integer> set) {
        this.b = picture;
        this.c = rectF;
        this.a = set;
    }

    public void a(RectF rectF) {
        this.d = rectF;
    }

    public PictureDrawable a() {
        return new com.nexstreaming.app.common.drawable.a(this.b);
    }

    public Picture b() {
        return this.b;
    }
}
