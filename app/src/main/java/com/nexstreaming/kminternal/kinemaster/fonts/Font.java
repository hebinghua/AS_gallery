package com.nexstreaming.kminternal.kinemaster.fonts;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import java.io.File;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/* loaded from: classes3.dex */
public class Font implements Comparable<Font> {
    private static final b[] k = {new b(" thin", 1), new b(" light", 2), new b(" regular", 3), new b(" medium", 4), new b(" bold", 5), new b(" black", 6), new b(" italic", 100), new b(" condensed", 1000)};
    private final String a;
    private final boolean b;
    private final String c;
    private final File d;
    private transient WeakReference<Bitmap> e;
    private transient WeakReference<Typeface> f;
    private final transient Typeface g;
    private final String h;
    private final String i;
    private a j;

    public Font(String str, String str2, File file, String str3) {
        this.a = str;
        this.h = str2;
        this.b = false;
        this.g = null;
        this.c = null;
        this.d = file;
        this.i = str3;
    }

    public Font(String str, String str2, String str3, String str4) {
        this.a = str;
        this.h = str2;
        this.b = str3 != null;
        this.c = str3;
        this.d = null;
        this.g = null;
        this.i = str4;
    }

    public Font(String str, String str2, Typeface typeface, String str3) {
        this.a = str;
        this.h = str2;
        this.b = typeface != null;
        this.g = typeface;
        this.c = null;
        this.d = null;
        this.i = str3;
    }

    public String a(Context context) {
        return this.i;
    }

    /* loaded from: classes3.dex */
    public static class TypefaceLoadException extends Exception {
        private static final long serialVersionUID = 1;

        public TypefaceLoadException() {
        }

        public TypefaceLoadException(String str, Throwable th) {
            super(str, th);
        }

        public TypefaceLoadException(String str) {
            super(str);
        }

        public TypefaceLoadException(Throwable th) {
            super(th);
        }
    }

    public Typeface b(Context context) throws TypefaceLoadException {
        Typeface typeface;
        WeakReference<Typeface> weakReference = this.f;
        if (weakReference == null || (typeface = weakReference.get()) == null) {
            if (this.b) {
                Typeface typeface2 = this.g;
                if (typeface2 == null) {
                    typeface2 = Typeface.createFromAsset(context.getAssets(), this.c);
                }
                this.f = new WeakReference<>(typeface2);
                return typeface2;
            }
            File file = this.d;
            if (file == null) {
                return null;
            }
            try {
                Typeface createFromFile = Typeface.createFromFile(file);
                this.f = new WeakReference<>(createFromFile);
                return createFromFile;
            } catch (RuntimeException e) {
                if (e.getMessage().contains("native typeface cannot be made")) {
                    throw new TypefaceLoadException(e);
                }
                throw e;
            }
        }
        return typeface;
    }

    public Typeface a(Context context, Typeface typeface) {
        try {
            return b(context);
        } catch (TypefaceLoadException unused) {
            return typeface;
        }
    }

    public Bitmap c(Context context) {
        Bitmap bitmap;
        WeakReference<Bitmap> weakReference = this.e;
        if (weakReference == null || (bitmap = weakReference.get()) == null) {
            Bitmap d = d(context);
            if (d == null) {
                return null;
            }
            int width = d.getWidth() * d.getHeight();
            int[] iArr = new int[width];
            d.getPixels(iArr, 0, d.getWidth(), 0, 0, d.getWidth(), d.getHeight());
            for (int i = 0; i < width; i++) {
                iArr[i] = iArr[i] & ((iArr[i] << 8) | 255);
            }
            Bitmap createBitmap = Bitmap.createBitmap(iArr, d.getWidth(), d.getHeight(), Bitmap.Config.ARGB_8888);
            createBitmap.setHasAlpha(true);
            this.e = new WeakReference<>(createBitmap);
            return createBitmap;
        }
        return bitmap;
    }

    private Bitmap d(Context context) {
        TextPaint textPaint = new TextPaint();
        textPaint.setTypeface(a(context, Typeface.DEFAULT));
        textPaint.setAntiAlias(true);
        textPaint.setColor(-1);
        textPaint.setTextSize(60.0f);
        StaticLayout staticLayout = new StaticLayout(TextUtils.ellipsize(this.i, textPaint, 940, TextUtils.TruncateAt.END), textPaint, 940, Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, true);
        Bitmap createBitmap = Bitmap.createBitmap(1000, 100, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        canvas.save();
        canvas.translate(30, (100 - staticLayout.getHeight()) / 2);
        staticLayout.draw(canvas);
        canvas.restore();
        return createBitmap;
    }

    public String a() {
        return this.a;
    }

    public String b() {
        return this.h;
    }

    /* loaded from: classes3.dex */
    public static class b {
        public final String a;
        public final int b;

        private b(String str, int i) {
            this.a = str;
            this.b = i;
        }
    }

    /* loaded from: classes3.dex */
    public static class a {
        public static final Map<String, a> c = new HashMap();
        public final String a;
        public final int b;

        public static a a(String str) {
            a aVar = c.get(str);
            return aVar != null ? aVar : new a(str);
        }

        private a(String str) {
            b[] bVarArr;
            boolean z;
            String trim = str.toLowerCase(Locale.ENGLISH).trim();
            int i = 0;
            do {
                z = false;
                for (b bVar : Font.k) {
                    if (trim.endsWith(bVar.a)) {
                        trim = trim.substring(0, trim.length() - bVar.a.length());
                        i += bVar.b;
                        z = true;
                    }
                }
            } while (z);
            this.a = trim;
            this.b = i;
        }
    }

    private a d() {
        if (this.j == null) {
            this.j = a.a(this.i);
        }
        return this.j;
    }

    @Override // java.lang.Comparable
    /* renamed from: a */
    public int compareTo(Font font) {
        a d = d();
        a d2 = font.d();
        int compareTo = d.a.compareTo(d2.a);
        return compareTo != 0 ? compareTo : d.b - d2.b;
    }

    public String toString() {
        return "[font:" + this.a + ":" + System.identityHashCode(this) + "]";
    }
}
