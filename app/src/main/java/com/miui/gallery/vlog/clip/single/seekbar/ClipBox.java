package com.miui.gallery.vlog.clip.single.seekbar;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;
import com.miui.gallery.editor.R$dimen;
import com.miui.gallery.util.LinearMotorHelper;
import com.miui.gallery.vlog.R$color;
import com.miui.gallery.vlog.R$drawable;

/* loaded from: classes2.dex */
public class ClipBox {
    public int mBottom;
    public Context mContext;
    public boolean mIsAddHaptic = true;
    public int mLeft;
    public int mLeftAxisLeft;
    public int mLeftAxisRight;
    public Drawable mLeftClipAxisBg;
    public Drawable mLeftClipAxisDisable;
    public Drawable mLeftClipAxisNormal;
    public int mLineWidth;
    public Paint mPaintBg;
    public Paint mPaintDisable;
    public Paint mPaintNormal;
    public int mRight;
    public int mRightAxisLeft;
    public int mRightAxisRight;
    public Drawable mRightClipAxisBg;
    public Drawable mRightClipAxisDisable;
    public Drawable mRightClipAxisNormal;
    public int mState;
    public int mTop;
    public int mVisibleAreaLeft;
    public int mVisibleAreaRight;

    public ClipBox(Context context) {
        Resources resources = context.getResources();
        this.mContext = context;
        this.mLeftClipAxisNormal = resources.getDrawable(R$drawable.vlog_left_clip_axis_normal, null);
        this.mLeftClipAxisDisable = resources.getDrawable(R$drawable.vlog_left_clip_axis_disable, null);
        this.mLeftClipAxisBg = resources.getDrawable(R$drawable.vlog_clip_box_left_axis_bg, null);
        this.mRightClipAxisNormal = resources.getDrawable(R$drawable.vlog_right_clip_axis_normal, null);
        this.mRightClipAxisDisable = resources.getDrawable(R$drawable.vlog_right_clip_axis_disable, null);
        this.mRightClipAxisBg = resources.getDrawable(R$drawable.vlog_clip_box_right_axis_bg, null);
        this.mLineWidth = context.getResources().getDimensionPixelSize(R$dimen.px_10);
        Paint paint = new Paint(1);
        this.mPaintNormal = paint;
        paint.setColor(resources.getColor(R$color.vlog_single_clip_box_color_normal, null));
        this.mPaintNormal.setStrokeWidth(this.mLineWidth);
        Paint paint2 = new Paint(1);
        this.mPaintDisable = paint2;
        paint2.setColor(resources.getColor(R$color.vlog_single_clip_box_color_disable, null));
        this.mPaintDisable.setStrokeWidth(this.mLineWidth);
        Paint paint3 = new Paint(1);
        this.mPaintBg = paint3;
        paint3.setColor(resources.getColor(R$color.vlog_clip_box_bg_color, null));
        this.mPaintBg.setStrokeWidth(this.mLineWidth);
    }

    public final void drawBg(Canvas canvas) {
        Drawable drawable = this.mLeftClipAxisBg;
        int i = this.mVisibleAreaLeft;
        drawable.setBounds(i, this.mTop, this.mLeftClipAxisNormal.getIntrinsicWidth() + i, this.mBottom);
        this.mLeftClipAxisBg.draw(canvas);
        this.mRightClipAxisBg.setBounds(this.mVisibleAreaRight - this.mRightClipAxisNormal.getIntrinsicWidth(), this.mTop, this.mVisibleAreaRight, this.mBottom);
        this.mRightClipAxisBg.draw(canvas);
        canvas.drawLine(this.mVisibleAreaLeft + this.mLeftClipAxisNormal.getIntrinsicWidth(), this.mTop + (this.mPaintBg.getStrokeWidth() / 2.0f), this.mVisibleAreaRight - this.mRightClipAxisNormal.getIntrinsicWidth(), this.mTop + (this.mPaintBg.getStrokeWidth() / 2.0f), this.mPaintBg);
        canvas.drawLine(this.mVisibleAreaLeft + this.mLeftClipAxisNormal.getIntrinsicWidth(), this.mBottom - (this.mPaintBg.getStrokeWidth() / 2.0f), this.mVisibleAreaRight - this.mRightClipAxisNormal.getIntrinsicWidth(), this.mBottom - (this.mPaintBg.getStrokeWidth() / 2.0f), this.mPaintBg);
    }

    public void draw(Canvas canvas) {
        drawBg(canvas);
        Drawable drawable = this.mLeftClipAxisNormal;
        Drawable drawable2 = this.mRightClipAxisNormal;
        Paint paint = this.mPaintNormal;
        if (this.mState == 1) {
            drawable = this.mLeftClipAxisDisable;
            drawable2 = this.mRightClipAxisDisable;
            paint = this.mPaintDisable;
            if (this.mIsAddHaptic) {
                LinearMotorHelper.performHapticFeedback(this.mContext, LinearMotorHelper.HAPTIC_MESH_NORMAL);
                this.mIsAddHaptic = false;
            }
        } else {
            this.mIsAddHaptic = true;
        }
        Paint paint2 = paint;
        int intrinsicWidth = this.mLeft + drawable.getIntrinsicWidth();
        int intrinsicWidth2 = this.mRight - drawable2.getIntrinsicWidth();
        Log.d("ClipBox", "draw: mClipBoxLeft=" + this.mLeft + ",lockAreaStart=" + intrinsicWidth + ",locakAreaEnd=" + intrinsicWidth2);
        canvas.save();
        canvas.clipRect(this.mVisibleAreaLeft, this.mTop, this.mVisibleAreaRight, this.mBottom);
        int i = this.mLeft;
        this.mLeftAxisLeft = i;
        int i2 = this.mVisibleAreaLeft;
        if (i < i2) {
            this.mLeftAxisLeft = i2;
        }
        int intrinsicWidth3 = this.mLeftAxisLeft + drawable.getIntrinsicWidth();
        this.mLeftAxisRight = intrinsicWidth3;
        drawable.setBounds(this.mLeftAxisLeft, this.mTop, intrinsicWidth3, this.mBottom);
        drawable.draw(canvas);
        int i3 = this.mRight;
        this.mRightAxisRight = i3;
        int i4 = this.mVisibleAreaRight;
        if (i3 > i4) {
            this.mRightAxisRight = i4;
        }
        int intrinsicWidth4 = this.mRightAxisRight - drawable2.getIntrinsicWidth();
        this.mRightAxisLeft = intrinsicWidth4;
        drawable2.setBounds(intrinsicWidth4, this.mTop, this.mRightAxisRight, this.mBottom);
        drawable2.draw(canvas);
        canvas.restore();
        canvas.save();
        canvas.clipRect(this.mVisibleAreaLeft + drawable.getIntrinsicWidth(), this.mTop, this.mVisibleAreaRight - drawable2.getIntrinsicWidth(), this.mBottom);
        float f = intrinsicWidth;
        float f2 = intrinsicWidth2;
        canvas.drawLine(f, this.mTop + (paint2.getStrokeWidth() / 2.0f), f2, this.mTop + (paint2.getStrokeWidth() / 2.0f), paint2);
        canvas.drawLine(f, this.mBottom - (paint2.getStrokeWidth() / 2.0f), f2, this.mBottom - (paint2.getStrokeWidth() / 2.0f), paint2);
        canvas.restore();
    }

    public void setCallback(Drawable.Callback callback) {
        this.mLeftClipAxisNormal.setCallback(callback);
        this.mLeftClipAxisDisable.setCallback(callback);
        this.mRightClipAxisNormal.setCallback(callback);
        this.mRightClipAxisDisable.setCallback(callback);
    }

    public int getLeftClipAxisWidth() {
        return this.mLeftClipAxisNormal.getIntrinsicWidth();
    }

    public int getLeftClipAxisHeight() {
        return this.mLeftClipAxisNormal.getIntrinsicHeight();
    }

    public int getRightClipAxisWidth() {
        return this.mRightClipAxisNormal.getIntrinsicWidth();
    }

    public int getLineWidth() {
        return this.mLineWidth;
    }

    public int getTop() {
        return this.mTop;
    }

    public int getBottom() {
        return this.mBottom;
    }

    public int getLeft() {
        return this.mLeft;
    }

    public int getRight() {
        return this.mRight;
    }

    public void setState(int i) {
        this.mState = i;
    }

    public void setVisibleArea(int i, int i2, int i3, int i4) {
        this.mVisibleAreaLeft = i;
        this.mVisibleAreaRight = i3;
        this.mLeft = i;
        this.mTop = i2;
        this.mRight = i3;
        this.mBottom = i4;
    }

    public void setClipBoxLeftAndRight(int i, int i2) {
        this.mLeft = i;
        this.mRight = i2;
    }
}
