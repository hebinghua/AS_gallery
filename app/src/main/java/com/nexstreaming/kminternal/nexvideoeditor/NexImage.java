package com.nexstreaming.kminternal.nexvideoeditor;

import android.graphics.Bitmap;
import android.util.Log;

/* loaded from: classes3.dex */
public class NexImage {
    private final Bitmap a;
    private final int b;
    private final int c;
    private final int d;

    public NexImage(Bitmap bitmap, int i, int i2, int i3) {
        Log.d("NexImage", "new NexImage(" + bitmap + "," + i + "," + i2 + "," + i3 + ")");
        this.a = bitmap;
        this.b = i;
        this.c = i2;
        this.d = i3;
    }

    public NexImage(Bitmap bitmap, int i, int i2) {
        Log.d("NexImage", "new NexImage(" + bitmap + "," + i + "," + i2 + ")");
        this.a = bitmap;
        this.b = i;
        this.c = i2;
        this.d = 1;
    }

    public int getWidth() {
        return this.b;
    }

    public int getHeight() {
        return this.c;
    }

    public int getLoadedType() {
        return this.d;
    }

    public void getPixels(int[] iArr) {
        Bitmap bitmap = this.a;
        if (bitmap == null) {
            return;
        }
        try {
            int i = this.b;
            bitmap.getPixels(iArr, 0, i, 0, 0, i, this.c);
        } catch (ArrayIndexOutOfBoundsException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append("w=");
            sb.append(this.b);
            sb.append(" h=");
            sb.append(this.c);
            sb.append(" bm=");
            sb.append(this.a.getWidth());
            sb.append("x");
            sb.append(this.a.getHeight());
            sb.append(" pixels=");
            sb.append(iArr == null ? "null" : Integer.valueOf(iArr.length));
            throw new ArrayIndexOutOfBoundsException(sb.toString());
        }
    }

    public void getPixels(int[] iArr, int i, int i2, int i3, int i4, int i5, int i6) {
        Bitmap bitmap = this.a;
        if (bitmap == null) {
            return;
        }
        if (i4 + i6 > bitmap.getHeight()) {
            Log.d("NexImage", "getPixels() WARNING: y + height exceeds bitmap height!!; offset=" + i + "; stride=" + i2 + "; x,y=" + i3 + "," + i4 + "; width,height=" + i5 + "," + i6 + "; mWidth,mHeight=" + this.b + "," + this.c + "; pixels.length=" + iArr.length + "; mBitmap {width=" + this.a.getWidth() + "; height=" + this.a.getHeight() + "}");
        } else if (i3 + i5 > this.a.getWidth()) {
            Log.d("NexImage", "getPixels() WARNING: y + height exceeds bitmap height!!; offset=" + i + "; stride=" + i2 + "; x,y=" + i3 + "," + i4 + "; width,height=" + i5 + "," + i6 + "; mWidth,mHeight=" + this.b + "," + this.c + "; pixels.length=" + iArr.length + "; mBitmap {width=" + this.a.getWidth() + "; height=" + this.a.getHeight() + "}");
        } else {
            this.a.getPixels(iArr, i, i2, i3, i4, i5, i6);
        }
    }

    public Bitmap getBitmap() {
        return this.a;
    }

    public void recycle() {
        this.a.recycle();
        Log.d("NexImage", "recycle Bitmap from native");
    }
}
