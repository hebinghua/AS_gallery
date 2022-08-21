package com.nexstreaming.app.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Log;
import com.nexstreaming.nexeditorsdk.nexFont;
import com.nexstreaming.nexeditorsdk.nexOverlayImage;

/* compiled from: utilityTextOverlay.java */
/* loaded from: classes3.dex */
public class q implements nexOverlayImage.runTimeMakeBitMap {
    private Context a;
    private String b;
    private int c;
    private int d;
    private int e;
    private transient int f;
    private transient int g;
    private transient boolean h;
    private transient StaticLayout i;
    private float j;
    private a k = null;
    private c l = null;
    private b m = null;
    private d n;
    private TextPaint o;

    @Override // com.nexstreaming.nexeditorsdk.nexOverlayImage.runTimeMakeBitMap
    public boolean isAniMate() {
        return false;
    }

    public q(Context context, String str, int i, int i2, float f) {
        this.a = context;
        this.b = str;
        this.d = i;
        this.e = i2;
        this.j = f;
    }

    public void a(int i) {
        this.c = i;
    }

    /* compiled from: utilityTextOverlay.java */
    /* loaded from: classes3.dex */
    public class a {
        private float b;
        private Paint.Align c;
        private int d;
        private String e;
        private int f;

        public a(float f, int i, Paint.Align align, String str, int i2) {
            this.b = f;
            this.d = i;
            this.c = align;
            this.e = str;
            this.f = i2;
        }
    }

    public void a(float f, int i, Paint.Align align, String str, int i2) {
        this.k = new a(f, i, align, str, i2);
    }

    /* compiled from: utilityTextOverlay.java */
    /* loaded from: classes3.dex */
    public class c {
        private boolean b;
        private int c;
        private float d;

        public c(boolean z, int i, float f) {
            this.b = z;
            this.c = i;
            this.d = f;
        }
    }

    public void a(boolean z, int i, float f) {
        this.l = new c(z, i, f);
    }

    /* compiled from: utilityTextOverlay.java */
    /* loaded from: classes3.dex */
    public class b {
        private boolean b;
        private int c;
        private float d;

        public b(boolean z, int i, float f) {
            this.b = z;
            this.c = i;
            this.d = f;
        }
    }

    public void b(boolean z, int i, float f) {
        this.m = new b(z, i, f);
    }

    /* compiled from: utilityTextOverlay.java */
    /* loaded from: classes3.dex */
    public class d {
        private boolean b;
        private int c;
        private float d;
        private float e;
        private float f;

        public d(boolean z, int i, float f, float f2, float f3) {
            this.b = z;
            this.c = i;
            this.d = f;
            this.e = f2;
            this.f = f3;
        }
    }

    public void a(boolean z, int i, float f, float f2, float f3) {
        this.n = new d(z, i, f, f2, f3);
    }

    private Bitmap a() {
        d();
        float f = this.j;
        Bitmap createBitmap = Bitmap.createBitmap((int) (this.f * f), (int) (this.e * f), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        float f2 = this.j;
        canvas.scale(f2, f2);
        canvas.translate(c(), c());
        Typeface typeface = nexFont.getTypeface(this.a, this.k.e);
        if (typeface == null) {
            typeface = Typeface.createFromAsset(this.a.getAssets(), this.k.e);
        }
        TextPaint paint = this.i.getPaint();
        paint.setStyle(Paint.Style.FILL);
        paint.setTypeface(typeface);
        if (this.k.c == Paint.Align.RIGHT) {
            paint.setTextAlign(Paint.Align.LEFT);
        }
        if (this.k.f != 1) {
            if (this.k.f != 2) {
                if (this.k.f == 3) {
                    float f3 = this.j;
                    canvas.translate(0.0f, (this.e * f3) - (this.g * f3));
                }
            } else {
                float f4 = this.j;
                canvas.translate(0.0f, ((this.e * f4) - (this.g * f4)) / 2.0f);
            }
        }
        if (this.n != null) {
            paint.setMaskFilter(new BlurMaskFilter(this.n.d / this.j, BlurMaskFilter.Blur.NORMAL));
            paint.setColor(this.n.c);
            canvas.save();
            canvas.translate(this.n.e, this.n.f);
            this.i.draw(canvas);
            canvas.restore();
        }
        if (this.m != null) {
            paint.setMaskFilter(new BlurMaskFilter(this.m.d / this.j, BlurMaskFilter.Blur.OUTER));
            paint.setColor(this.m.c);
            this.i.draw(canvas);
        }
        if (this.k != null) {
            paint.setMaskFilter(null);
            paint.setTextSize(this.k.b);
            paint.setColor(this.k.d);
            this.i.draw(canvas);
        }
        if (this.l != null) {
            paint.setMaskFilter(null);
            this.i.getPaint().setStyle(Paint.Style.STROKE);
            paint.setColor(this.l.c);
            paint.setStrokeWidth(this.l.d / this.j);
            this.i.draw(canvas);
        }
        return createBitmap;
    }

    private TextPaint b() {
        TextPaint textPaint = this.o;
        if (textPaint == null) {
            this.o = new TextPaint();
        } else {
            textPaint.reset();
        }
        a aVar = this.k;
        if (aVar != null) {
            this.o.setTextSize(aVar.b);
            this.o.setColor(this.k.d);
            Typeface typeface = nexFont.getTypeface(this.a, this.k.e);
            if (typeface == null) {
                typeface = Typeface.createFromAsset(this.a.getAssets(), this.k.e);
            }
            this.o.setTypeface(typeface);
        }
        c cVar = this.l;
        if (cVar != null) {
            this.o.setStrokeWidth(cVar.d / this.j);
        }
        this.o.setAntiAlias(true);
        this.o.setStyle(Paint.Style.FILL_AND_STROKE);
        this.o.setTextAlign(Paint.Align.LEFT);
        return this.o;
    }

    private int c() {
        d dVar = this.n;
        float f = 0.0f;
        float max = dVar != null ? Math.max(Math.abs(dVar.e), Math.abs(this.n.f)) : 0.0f;
        b bVar = this.m;
        float f2 = bVar != null ? bVar.d : 0.0f;
        c cVar = this.l;
        if (cVar != null) {
            f = cVar.d;
        }
        return (int) Math.ceil(Math.max(f, Math.max(f2, max)));
    }

    /* JADX WARN: Type inference failed for: r1v12 */
    /* JADX WARN: Type inference failed for: r1v3 */
    /* JADX WARN: Type inference failed for: r1v4, types: [boolean, int] */
    private void d() {
        ?? r1;
        if (!this.h || this.i == null) {
            float f = 1.0f;
            int c2 = c();
            TextPaint b2 = b();
            this.f = this.d;
            boolean z = true;
            this.g = this.e + 1;
            Layout.Alignment alignment = Layout.Alignment.ALIGN_CENTER;
            if (this.k.c != Paint.Align.CENTER) {
                if (this.k.c != Paint.Align.LEFT) {
                    if (this.k.c == Paint.Align.RIGHT) {
                        alignment = Layout.Alignment.ALIGN_OPPOSITE;
                    }
                } else {
                    alignment = Layout.Alignment.ALIGN_NORMAL;
                }
            } else {
                alignment = Layout.Alignment.ALIGN_CENTER;
            }
            Layout.Alignment alignment2 = alignment;
            StaticLayout staticLayout = null;
            Log.d("utilityTextOverlay", String.format("calcDimension(%s, %d %d)", alignment2.toString(), Integer.valueOf(this.g), Integer.valueOf(this.e)));
            while (true) {
                if (this.g <= this.e) {
                    r1 = z;
                    break;
                }
                String str = this.b;
                int i = c2 * 2;
                float f2 = f;
                boolean z2 = z;
                StaticLayout staticLayout2 = new StaticLayout(str, 0, str.length(), b2, this.d - i, alignment2, f2, 0.0f, true);
                this.g = Math.max(z2 ? 1 : 0, staticLayout2.getHeight()) + i;
                float textSize = b2.getTextSize();
                if (this.g <= this.e) {
                    staticLayout = staticLayout2;
                    r1 = z2;
                    break;
                }
                b2.setTextSize(textSize - 1.0f);
                boolean z3 = z2 ? 1 : 0;
                boolean z4 = z2 ? 1 : 0;
                z = z3;
                staticLayout = staticLayout2;
                f = 1.0f;
            }
            this.i = staticLayout;
            int i2 = c2 * 2;
            this.f = Math.max(r1 == true ? 1 : 0, staticLayout.getWidth()) + i2;
            this.g = Math.max((int) r1, this.i.getHeight()) + i2;
            this.h = r1;
        }
    }

    @Override // com.nexstreaming.nexeditorsdk.nexOverlayImage.runTimeMakeBitMap
    public int getBitmapID() {
        return this.c;
    }

    @Override // com.nexstreaming.nexeditorsdk.nexOverlayImage.runTimeMakeBitMap
    public Bitmap makeBitmap() {
        return a();
    }
}
