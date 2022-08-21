package com.miui.gallery.editor.photo.penengine;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import com.miui.gallery.R;
import com.miui.gallery.R$styleable;

/* loaded from: classes2.dex */
public class PenSeekBar extends FrameLayout {
    public ImageView mBgIv;
    public float mCurrentProgress;
    public float mDownTranX;
    public float mDownX;
    public OnProgressChangeListener mOnProgressChangeListener;
    public Drawable mSeekBarDrawable;
    public ImageView mThumbIv;
    public float mThumbMaxTranX;
    public float mThumbMinTranX;
    public Rect mThumbTouchRect;

    /* loaded from: classes2.dex */
    public interface OnProgressChangeListener {
        void onProgressChange(float f);

        void onStartTrackingTouch();

        void onStopTrackingTouch(float f);
    }

    public PenSeekBar(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mThumbTouchRect = new Rect();
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.PenSeekBar);
        this.mSeekBarDrawable = obtainStyledAttributes.getDrawable(0);
        obtainStyledAttributes.recycle();
        init(context);
    }

    public final void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.pen_seek_bar, (ViewGroup) this, true);
        ImageView imageView = (ImageView) findViewById(R.id.seek_bar_bg);
        this.mBgIv = imageView;
        Drawable drawable = this.mSeekBarDrawable;
        if (drawable != null) {
            imageView.setBackground(drawable);
        }
        ImageView imageView2 = (ImageView) findViewById(R.id.seek_bar_thumb);
        this.mThumbIv = imageView2;
        this.mThumbMaxTranX = 0.0f;
        imageView2.setTranslationX(0.0f);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 1) {
            onStopTrackingTouch(this.mCurrentProgress);
            return true;
        } else if (!this.mThumbTouchRect.contains((int) motionEvent.getX(), (int) motionEvent.getY())) {
            return false;
        } else {
            if (action == 0) {
                this.mDownX = motionEvent.getX();
                this.mDownTranX = this.mThumbIv.getTranslationX();
                onStartTrackingTouch();
                return true;
            } else if (action != 2) {
                return false;
            } else {
                moveThumb(this.mThumbIv, motionEvent.getX() - this.mDownX);
                return true;
            }
        }
    }

    public final void moveThumb(View view, float f) {
        float max = Math.max(Math.min(f + this.mDownTranX, this.mThumbMaxTranX), this.mThumbMinTranX);
        view.setTranslationX(max);
        float f2 = this.mThumbMinTranX;
        float f3 = (max - f2) / (this.mThumbMaxTranX - f2);
        this.mCurrentProgress = f3;
        onProgressChange(f3);
    }

    public final void onProgressChange(float f) {
        OnProgressChangeListener onProgressChangeListener = this.mOnProgressChangeListener;
        if (onProgressChangeListener != null) {
            onProgressChangeListener.onProgressChange(f);
        }
    }

    public final void onStartTrackingTouch() {
        OnProgressChangeListener onProgressChangeListener = this.mOnProgressChangeListener;
        if (onProgressChangeListener != null) {
            onProgressChangeListener.onStartTrackingTouch();
        }
    }

    public final void onStopTrackingTouch(float f) {
        OnProgressChangeListener onProgressChangeListener = this.mOnProgressChangeListener;
        if (onProgressChangeListener != null) {
            onProgressChangeListener.onStopTrackingTouch(f);
        }
    }

    @Override // android.widget.FrameLayout, android.view.ViewGroup, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (z) {
            this.mThumbMinTranX = 0.0f;
            this.mThumbMaxTranX = this.mBgIv.getWidth() - this.mThumbIv.getWidth();
            this.mBgIv.getHitRect(this.mThumbTouchRect);
            Rect rect = this.mThumbTouchRect;
            rect.left -= 20;
            rect.top -= 20;
            int i5 = rect.right;
            rect.right = i5 + i5 + 20;
            rect.bottom += 20;
        }
    }

    public void setProgress(final float f) {
        if (f < 0.0f || f > 1.0f) {
            return;
        }
        post(new Runnable() { // from class: com.miui.gallery.editor.photo.penengine.PenSeekBar.1
            @Override // java.lang.Runnable
            public void run() {
                PenSeekBar.this.mThumbIv.setTranslationX((f * (PenSeekBar.this.mThumbMaxTranX - PenSeekBar.this.mThumbMinTranX)) + PenSeekBar.this.mThumbMinTranX);
            }
        });
    }

    public void setOnProgressChangeListener(OnProgressChangeListener onProgressChangeListener) {
        this.mOnProgressChangeListener = onProgressChangeListener;
    }

    public void setColor(int i) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.screen_brush_config_alpha_bg);
        if (drawable == null) {
            return;
        }
        Drawable mutate = drawable.mutate();
        if (!(mutate instanceof LayerDrawable)) {
            return;
        }
        LayerDrawable layerDrawable = (LayerDrawable) mutate;
        GradientDrawable gradientDrawable = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, new int[]{i, i & 16777215});
        gradientDrawable.setCornerRadius(50.0f);
        layerDrawable.addLayer(gradientDrawable);
        this.mBgIv.setBackground(layerDrawable);
    }
}
