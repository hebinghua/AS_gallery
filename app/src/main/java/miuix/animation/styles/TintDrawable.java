package miuix.animation.styles;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import miuix.animation.Folme;
import miuix.animation.property.ViewPropertyExt;

/* loaded from: classes3.dex */
public class TintDrawable extends Drawable {
    public static final View.OnAttachStateChangeListener sListener = new View.OnAttachStateChangeListener() { // from class: miuix.animation.styles.TintDrawable.1
        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewAttachedToWindow(View view) {
        }

        @Override // android.view.View.OnAttachStateChangeListener
        public void onViewDetachedFromWindow(View view) {
            TintDrawable tintDrawable = TintDrawable.get(view);
            if (tintDrawable == null || Build.VERSION.SDK_INT < 23) {
                return;
            }
            Drawable drawable = tintDrawable.mOriDrawable;
            if (drawable != null) {
                view.setForeground(drawable);
            }
            tintDrawable.clear();
            view.removeOnAttachStateChangeListener(this);
        }
    };
    public Bitmap mBitmap;
    public Drawable mOriDrawable;
    public View mView;
    public Paint mPaint = new Paint();
    public RectF mBounds = new RectF();
    public Rect mSrcRect = new Rect();
    public RectF mCornerBounds = new RectF();
    public boolean isSetCorner = false;
    public float mEadius = 0.0f;

    @Override // android.graphics.drawable.Drawable
    public int getOpacity() {
        return -2;
    }

    @Override // android.graphics.drawable.Drawable
    public void setAlpha(int i) {
    }

    @Override // android.graphics.drawable.Drawable
    public void setColorFilter(ColorFilter colorFilter) {
    }

    public static TintDrawable get(View view) {
        if (Build.VERSION.SDK_INT >= 23) {
            Drawable foreground = view.getForeground();
            if (!(foreground instanceof TintDrawable)) {
                return null;
            }
            return (TintDrawable) foreground;
        }
        return null;
    }

    public static TintDrawable setAndGet(final View view) {
        TintDrawable tintDrawable = get(view);
        if (tintDrawable != null || Build.VERSION.SDK_INT < 23) {
            return tintDrawable;
        }
        final TintDrawable tintDrawable2 = new TintDrawable();
        tintDrawable2.mView = view;
        tintDrawable2.setOriDrawable(view.getForeground());
        view.addOnAttachStateChangeListener(sListener);
        Folme.post(view, new Runnable() { // from class: miuix.animation.styles.TintDrawable.2
            @Override // java.lang.Runnable
            public void run() {
                view.setForeground(tintDrawable2);
            }
        });
        return tintDrawable2;
    }

    public final void setOriDrawable(Drawable drawable) {
        this.mOriDrawable = drawable;
    }

    public void setCorner(float f) {
        this.isSetCorner = f != 0.0f;
        this.mEadius = f;
    }

    public void initTintBuffer(int i) {
        View view = this.mView;
        if (view == null) {
            return;
        }
        int width = view.getWidth();
        int height = this.mView.getHeight();
        if (width == 0 || height == 0) {
            recycleBitmap();
            return;
        }
        createBitmap(width, height);
        initBitmap(i);
    }

    public final void createBitmap(int i, int i2) {
        if (Build.VERSION.SDK_INT >= 23) {
            Bitmap bitmap = this.mBitmap;
            if (bitmap != null && bitmap.getWidth() == i && this.mBitmap.getHeight() == this.mView.getHeight()) {
                return;
            }
            recycleBitmap();
            this.mPaint.setAntiAlias(true);
            try {
                this.mBitmap = Bitmap.createBitmap(i, i2, Bitmap.Config.ARGB_8888);
            } catch (OutOfMemoryError unused) {
                Log.w("miuix_anim", "TintDrawable.createBitmap failed, out of memory");
            }
        }
    }

    public final void clear() {
        recycleBitmap();
    }

    public final void recycleBitmap() {
        Bitmap bitmap = this.mBitmap;
        if (bitmap != null) {
            bitmap.recycle();
            this.mBitmap = null;
        }
    }

    public final void initBitmap(int i) {
        if (Build.VERSION.SDK_INT < 23) {
            return;
        }
        Bitmap bitmap = this.mBitmap;
        if (bitmap == null || bitmap.isRecycled()) {
            this.mView.setForeground(this.mOriDrawable);
            return;
        }
        try {
            this.mBitmap.eraseColor(0);
            Canvas canvas = new Canvas(this.mBitmap);
            canvas.translate(-this.mView.getScrollX(), -this.mView.getScrollY());
            this.mView.setForeground(this.mOriDrawable);
            this.mView.draw(canvas);
            this.mView.setForeground(this);
            if (i != 0) {
                return;
            }
            try {
                this.mPaint.setColorFilter(new ColorMatrixColorFilter(new ColorMatrix(new float[]{1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, Float.MAX_VALUE, 0.0f})));
                canvas.drawBitmap(this.mBitmap, 0.0f, 0.0f, this.mPaint);
            } catch (Exception unused) {
                Log.w("miuix_anim", "the Bitmap empty or Recycled");
            }
        } catch (Exception e) {
            Log.w("miuix_anim", "TintDrawable.initBitmap failed, " + e);
        }
    }

    @Override // android.graphics.drawable.Drawable
    public void draw(Canvas canvas) {
        Bitmap bitmap;
        int scrollX = this.mView.getScrollX();
        int scrollY = this.mView.getScrollY();
        int width = this.mView.getWidth();
        int height = this.mView.getHeight();
        this.mBounds.set(scrollX, scrollY, scrollX + width, scrollY + height);
        this.mSrcRect.set(0, 0, width, height);
        canvas.save();
        int intValue = ViewPropertyExt.FOREGROUND.getIntValue(this.mView);
        try {
            try {
                canvas.clipRect(this.mBounds);
                canvas.drawColor(0);
                Drawable drawable = this.mOriDrawable;
                if (drawable != null) {
                    drawable.draw(canvas);
                }
                bitmap = this.mBitmap;
            } catch (RuntimeException e) {
                dealOOM(e, canvas);
            }
            if (bitmap != null && !bitmap.isRecycled()) {
                if (this.isSetCorner) {
                    this.mCornerBounds.set(this.mSrcRect);
                    this.mPaint.setColorFilter(new PorterDuffColorFilter(intValue, PorterDuff.Mode.SRC_IN));
                    RectF rectF = this.mCornerBounds;
                    float f = this.mEadius;
                    canvas.drawRoundRect(rectF, f, f, this.mPaint);
                } else {
                    this.mPaint.setColorFilter(new PorterDuffColorFilter(intValue, PorterDuff.Mode.SRC_IN));
                    canvas.drawBitmap(this.mBitmap, this.mSrcRect, this.mBounds, this.mPaint);
                }
                return;
            }
            if (Build.VERSION.SDK_INT >= 23) {
                this.mView.setForeground(this.mOriDrawable);
            }
        } finally {
            canvas.restore();
        }
    }

    public final void dealOOM(RuntimeException runtimeException, Canvas canvas) {
        if (runtimeException.getMessage() == null || runtimeException.getMessage().length() <= 0 || !runtimeException.getMessage().contains("Canvas: trying to draw too large")) {
            return;
        }
        try {
            Bitmap compressImage = compressImage(this.mBitmap);
            this.mBitmap = compressImage;
            canvas.drawBitmap(compressImage, this.mSrcRect, this.mBounds, this.mPaint);
        } catch (Exception e) {
            Log.w("miuix_anim", "TintDrawable.dealOOM failed, " + e);
        }
    }

    public final Bitmap compressImage(Bitmap bitmap) {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            return BitmapFactory.decodeStream(parseToInputStream(byteArrayOutputStream), null, options);
        } catch (Exception e) {
            Log.w("miuix_anim", "TintDrawable.compressImage failed, " + e);
            return null;
        }
    }

    public final ByteArrayInputStream parseToInputStream(ByteArrayOutputStream byteArrayOutputStream) throws Exception {
        return new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
    }

    public void restoreOriginalDrawable() {
        clear();
        invalidateSelf();
    }
}
