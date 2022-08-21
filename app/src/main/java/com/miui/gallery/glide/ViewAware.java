package com.miui.gallery.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.util.Preconditions;
import com.miui.gallery.util.glide.GlideRequestManagerHelper;
import java.lang.ref.WeakReference;

/* loaded from: classes2.dex */
public abstract class ViewAware<T extends View> implements ImageAware {
    public static Integer mMaxDisplayLength;
    public WeakReference<T> mViewRef;

    public abstract void setImageBitmapInto(Bitmap bitmap, T t);

    public abstract void setImageDrawableInto(Drawable drawable, T t);

    public ViewAware(T t) {
        this.mViewRef = new WeakReference<>(t);
    }

    @Override // com.miui.gallery.glide.ImageAware
    public RequestManager getRequestManager() {
        T t = this.mViewRef.get();
        if (t != null) {
            return GlideRequestManagerHelper.safeGet(t);
        }
        return null;
    }

    @Override // com.miui.gallery.glide.ImageAware
    public View getWrappedView() {
        return this.mViewRef.get();
    }

    @Override // com.miui.gallery.glide.ImageAware
    public int getWidth() {
        T t = this.mViewRef.get();
        if (t != null) {
            return getTargetWidth(t);
        }
        return 0;
    }

    @Override // com.miui.gallery.glide.ImageAware
    public int getHeight() {
        T t = this.mViewRef.get();
        if (t != null) {
            return getTargetHeight(t);
        }
        return 0;
    }

    @Override // com.miui.gallery.glide.ImageAware
    public boolean isCollected() {
        return this.mViewRef.get() == null;
    }

    @Override // com.miui.gallery.glide.ImageAware
    public boolean setImageBitmap(Bitmap bitmap) {
        T t;
        if (Looper.myLooper() != Looper.getMainLooper() || (t = this.mViewRef.get()) == null) {
            return false;
        }
        setImageBitmapInto(bitmap, t);
        return true;
    }

    @Override // com.miui.gallery.glide.ImageAware
    public boolean setImageDrawable(Drawable drawable) {
        T t;
        if (Looper.myLooper() != Looper.getMainLooper() || (t = this.mViewRef.get()) == null) {
            return false;
        }
        setImageDrawableInto(drawable, t);
        return true;
    }

    @Override // com.miui.gallery.glide.ImageAware
    public void setTag(int i, Object obj) {
        T t = this.mViewRef.get();
        if (t != null) {
            t.setTag(i, obj);
        }
    }

    @Override // com.miui.gallery.glide.ImageAware
    public Object getTag(int i) {
        T t = this.mViewRef.get();
        if (t != null) {
            return t.getTag(i);
        }
        return null;
    }

    public static int getTargetHeight(View view) {
        int paddingTop = view.getPaddingTop() + view.getPaddingBottom();
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        return getTargetDimen(view, view.getHeight(), layoutParams != null ? layoutParams.height : 0, paddingTop);
    }

    public static int getTargetWidth(View view) {
        int paddingLeft = view.getPaddingLeft() + view.getPaddingRight();
        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        return getTargetDimen(view, view.getWidth(), layoutParams != null ? layoutParams.width : 0, paddingLeft);
    }

    public static int getTargetDimen(View view, int i, int i2, int i3) {
        int i4 = i2 - i3;
        if (i4 > 0) {
            return i4;
        }
        int i5 = i - i3;
        if (i5 > 0) {
            return i5;
        }
        if (!view.isLayoutRequested() && i2 == -2) {
            return getMaxDisplayLength(view.getContext());
        }
        return 0;
    }

    public static int getMaxDisplayLength(Context context) {
        if (mMaxDisplayLength == null) {
            Display defaultDisplay = ((WindowManager) Preconditions.checkNotNull((WindowManager) context.getSystemService("window"))).getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            mMaxDisplayLength = Integer.valueOf(Math.max(point.x, point.y));
        }
        return mMaxDisplayLength.intValue();
    }
}
