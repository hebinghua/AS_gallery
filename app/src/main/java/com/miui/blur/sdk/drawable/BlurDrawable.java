package com.miui.blur.sdk.drawable;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.lang.reflect.Method;

/* loaded from: classes.dex */
public class BlurDrawable extends Drawable {
    public static final Handler mainThreadHandler = new Handler(Looper.getMainLooper());
    public int mAlpha;
    public long mFunctor;
    public Method mMethodCallDrawGLFunction;
    public Paint mPaint;
    public boolean mBlurEnabled = true;
    public int mBlurWidth = getBounds().width();
    public int mBlurHeight = getBounds().height();

    public static native long nCreateNativeFunctor(int i, int i2);

    public static native long nDeleteNativeFunctor(long j);

    public static native void nSetAlpha(long j, float f);

    public static native void nSetBlurRatio(long j, float f);

    public static native void nSetMixColor(long j, int i, int i2);

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return 0;
    }

    static {
        try {
            if (!isSupportBlurStatic()) {
                return;
            }
            System.loadLibrary("miuiblursdk");
        } catch (Throwable th) {
            Log.e("BlurDrawable", "Failed to load miuiblursdk library", th);
            try {
                System.loadLibrary("miuiblur");
            } catch (Throwable th2) {
                Log.e("BlurDrawable", "Failed to load miuiblur library", th2);
            }
        }
    }

    public BlurDrawable() {
        this.mFunctor = 0L;
        Paint paint = new Paint();
        this.mPaint = paint;
        paint.setColor(0);
        if (isSupportBlur()) {
            this.mFunctor = nCreateNativeFunctor(this.mBlurWidth, this.mBlurHeight);
            initMethod();
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Log.e("BlurDrawable", "draw");
        if (canvas.isHardwareAccelerated() && this.mBlurEnabled && isSupportBlur()) {
            drawBlurBack(canvas);
        } else {
            canvas.drawRect(getBounds(), this.mPaint);
        }
    }

    public final void drawBlurBack(Canvas canvas) {
        try {
            this.mMethodCallDrawGLFunction.setAccessible(true);
            this.mMethodCallDrawGLFunction.invoke(canvas, Long.valueOf(this.mFunctor));
        } catch (Throwable th) {
            Log.e("BlurDrawable", "canvas function [callDrawGLFunction()] error", th);
        }
    }

    public final void initMethod() {
        try {
            int i = Build.VERSION.SDK_INT;
            if (i > 28) {
                this.mMethodCallDrawGLFunction = (Method) Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class).invoke((Class) Class.class.getDeclaredMethod("forName", String.class).invoke(null, "android.graphics.RecordingCanvas"), "callDrawGLFunction2", new Class[]{Long.TYPE});
            } else if (i > 22) {
                this.mMethodCallDrawGLFunction = Class.forName("android.view.DisplayListCanvas").getMethod("callDrawGLFunction2", Long.TYPE);
            } else if (i == 21) {
                this.mMethodCallDrawGLFunction = Class.forName("android.view.HardwareCanvas").getMethod("callDrawGLFunction", Long.TYPE);
            } else if (i == 22) {
                this.mMethodCallDrawGLFunction = Class.forName("android.view.HardwareCanvas").getMethod("callDrawGLFunction2", Long.TYPE);
            } else {
                this.mMethodCallDrawGLFunction = Class.forName("android.view.HardwareCanvas").getMethod("callDrawGLFunction", Integer.TYPE);
            }
        } catch (Exception e) {
            Log.e("BlurDrawable", "canvas function [callDrawGLFunction()] error", e);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
        this.mAlpha = i;
        nSetAlpha(this.mFunctor, i / 255.0f);
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
        Log.d("BlurDrawable", "nothing in setColorFilter");
    }

    public void finalize() throws Throwable {
        if (isSupportBlur()) {
            nDeleteNativeFunctor(this.mFunctor);
        }
        Log.e("BlurDrawable", "finalize");
        super.finalize();
    }

    public void setMixColor(int i, int i2) {
        if (isSupportBlur()) {
            nSetMixColor(this.mFunctor, i2, i);
            invalidateOnMainThread();
        }
    }

    public void setBlurRatio(float f) {
        if (isSupportBlur()) {
            nSetBlurRatio(this.mFunctor, f);
            invalidateOnMainThread();
        }
    }

    public boolean isSupportBlur() {
        return Build.VERSION.SDK_INT > 25;
    }

    public static boolean isSupportBlurStatic() {
        return Build.VERSION.SDK_INT > 25;
    }

    public final void invalidateOnMainThread() {
        Looper myLooper = Looper.myLooper();
        if (myLooper == null || !myLooper.equals(Looper.getMainLooper())) {
            mainThreadHandler.post(new Runnable() { // from class: com.miui.blur.sdk.drawable.BlurDrawable.1
                @Override // java.lang.Runnable
                public void run() {
                    BlurDrawable.this.invalidateSelf();
                }
            });
        } else {
            invalidateSelf();
        }
    }
}
