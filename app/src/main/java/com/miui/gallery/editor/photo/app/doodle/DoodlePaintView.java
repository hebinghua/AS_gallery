package com.miui.gallery.editor.photo.app.doodle;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.GalleryApp;
import com.miui.gallery.R;
import com.miui.gallery.util.MiscUtil;

/* loaded from: classes2.dex */
public class DoodlePaintView extends View {
    public int mInnerColor;
    public int mOuterColor;
    public float mOuterHeight;
    public float mOuterWidth;
    public Paint mPaint;
    public PaintType mPaintType;
    public float mRadiusPercent;

    public DoodlePaintView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mPaint = new Paint(1);
        this.mOuterColor = Color.parseColor("#F1F1F1");
        this.mInnerColor = Color.parseColor("#FF7A31");
        this.mRadiusPercent = 0.21f;
        this.mPaintType = PaintType.MEDIUM;
        init(context);
    }

    public final void init(Context context) {
        if (MiscUtil.isNightMode(context)) {
            this.mOuterColor = Color.parseColor("#242424");
        }
    }

    @Override // android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        this.mOuterWidth = View.MeasureSpec.getSize(i);
        this.mOuterHeight = View.MeasureSpec.getSize(i2);
    }

    @Override // android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawOuterCircle(canvas);
        drawInnerCircle(canvas);
    }

    public final void drawOuterCircle(Canvas canvas) {
        this.mPaint.setColor(this.mOuterColor);
        float f = this.mOuterWidth;
        float f2 = this.mOuterHeight;
        canvas.drawCircle(f / 2.0f, f2 / 2.0f, Math.min(f, f2) / 2.0f, this.mPaint);
    }

    public final void drawInnerCircle(Canvas canvas) {
        this.mPaint.setColor(this.mInnerColor);
        float f = this.mOuterWidth;
        float f2 = this.mOuterHeight;
        float f3 = this.mRadiusPercent;
        canvas.drawCircle(f / 2.0f, f2 / 2.0f, Math.min(f * f3, f3 * f2) / 2.0f, this.mPaint);
    }

    public void setColor(int i) {
        this.mInnerColor = i;
        invalidate();
    }

    public void setClickListener(View.OnClickListener onClickListener) {
        setOnClickListener(onClickListener);
    }

    public void updateInnerRadiusPercent() {
        float f = this.mRadiusPercent;
        if (f == 0.17f) {
            this.mRadiusPercent = 0.21f;
            this.mPaintType = PaintType.MEDIUM;
        } else if (f == 0.21f) {
            this.mRadiusPercent = 0.25f;
            this.mPaintType = PaintType.HEAVY;
        } else {
            this.mRadiusPercent = 0.17f;
            this.mPaintType = PaintType.LIGHT;
        }
        invalidate();
    }

    public int getOuterColor() {
        return this.mOuterColor;
    }

    public void setOuterColor(int i) {
        this.mOuterColor = i;
    }

    public PaintType getPaintType() {
        return this.mPaintType;
    }

    /* loaded from: classes2.dex */
    public enum PaintType {
        HEAVY(13.333f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.doodle_paint_select_heavy_talkback)),
        MEDIUM(4.333f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.doodle_paint_select_medium_talkback)),
        LIGHT(1.333f, GalleryApp.sGetAndroidContext().getResources().getString(R.string.doodle_paint_select_light_talkback));
        
        public final float paintSize;
        public final String talkbackName;

        PaintType(float f, String str) {
            this.paintSize = f;
            this.talkbackName = str;
        }
    }
}
