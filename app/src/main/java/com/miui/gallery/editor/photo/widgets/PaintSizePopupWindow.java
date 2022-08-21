package com.miui.gallery.editor.photo.widgets;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.PopupWindow;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.doodle.DoodlePaintView;

/* loaded from: classes2.dex */
public class PaintSizePopupWindow extends PopupWindow {
    public ValueAnimator.AnimatorUpdateListener mAnimatorUpdateListener;
    public PaintSizeView mPaintView;
    public ValueAnimator mValueAnimator;

    public PaintSizePopupWindow(Context context) {
        super(getCustomView(context), getCustomWidth(context), getCustomHeight(context));
        this.mAnimatorUpdateListener = new ValueAnimator.AnimatorUpdateListener() { // from class: com.miui.gallery.editor.photo.widgets.PaintSizePopupWindow.1
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                PaintSizePopupWindow.this.getContentView().setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        };
        setBackgroundDrawable(new ColorDrawable(0));
        setAnimationStyle(2131952155);
        PaintSizeView paintSizeView = (PaintSizeView) getContentView().findViewById(R.id.paint_view);
        this.mPaintView = paintSizeView;
        paintSizeView.setPaintStyle(Paint.Style.STROKE);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mValueAnimator = ofFloat;
        ofFloat.setDuration(context.getResources().getInteger(R.integer.paint_anim_duration));
        this.mValueAnimator.setInterpolator(new DecelerateInterpolator());
        this.mValueAnimator.addUpdateListener(this.mAnimatorUpdateListener);
    }

    public static View getCustomView(Context context) {
        return View.inflate(context, R.layout.paint_size_popupwindow, null);
    }

    public static int getCustomWidth(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.paint_window_size);
    }

    public static int getCustomHeight(Context context) {
        return context.getResources().getDimensionPixelSize(R.dimen.paint_window_size);
    }

    public void setPaintSize(int i) {
        this.mPaintView.setDiameter(i);
    }

    public void setPaintSizeWithAnimation(int i) {
        this.mPaintView.setDiameterWithAnimation(i);
    }

    public void setPaintTypeWithAnimation(Context context, DoodlePaintView.PaintType paintType) {
        int dimensionPixelSize;
        if (paintType == DoodlePaintView.PaintType.LIGHT) {
            dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.photo_editor_doodle_paint_size_light);
        } else if (paintType == DoodlePaintView.PaintType.MEDIUM) {
            dimensionPixelSize = context.getResources().getDimensionPixelSize(R.dimen.photo_editor_doodle_paint_size_medium);
        } else {
            dimensionPixelSize = paintType == DoodlePaintView.PaintType.HEAVY ? context.getResources().getDimensionPixelSize(R.dimen.photo_editor_doodle_paint_size_heavy) : 0;
        }
        setPaintSizeWithAnimation(dimensionPixelSize);
    }

    @Override // android.widget.PopupWindow
    public void showAtLocation(View view, int i, int i2, int i3) {
        super.showAtLocation(view, i, i2, i3);
        getContentView().setAlpha(0.0f);
        this.mValueAnimator.cancel();
        this.mValueAnimator.start();
    }

    @Override // android.widget.PopupWindow
    public void dismiss() {
        this.mValueAnimator.cancel();
        super.dismiss();
    }
}
