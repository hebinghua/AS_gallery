package com.miui.gallery.editor.photo.penengine;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class ColorPickView extends View {
    public int[] mColors;
    public int mCurrentColor;
    public int mCurrentSelectCol;
    public int mCurrentSelectRow;
    public int mHeight;
    public OnColorChangeListener mOnColorChangeListener;
    public Paint mPaint;
    public int mRow;
    public int mWidth;

    @FunctionalInterface
    /* loaded from: classes2.dex */
    public interface OnColorChangeListener {
        void onColorChange(ColorChangeEvent colorChangeEvent);
    }

    public ColorPickView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCurrentSelectCol = -1;
        this.mCurrentSelectRow = -1;
        init(context);
    }

    public int getCurrentColor() {
        return this.mCurrentColor;
    }

    public final void init(Context context) {
        this.mColors = context.getResources().getIntArray(R.array.color_pick_view_colors);
        this.mPaint = new Paint(1);
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        drawColors(canvas, this.mPaint);
        drawSelect(canvas, this.mPaint);
        super.draw(canvas);
    }

    @Override // android.view.View
    public void onSizeChanged(int i, int i2, int i3, int i4) {
        super.onSizeChanged(i, i2, i3, i4);
        initData();
    }

    public final void initData() {
        this.mRow = (int) Math.ceil(this.mColors.length / 13.0d);
        this.mWidth = getWidth() / 13;
        this.mHeight = getHeight() / this.mRow;
    }

    public final void drawColors(Canvas canvas, Paint paint) {
        paint.setStyle(Paint.Style.FILL);
        for (int i = 0; i < this.mRow; i++) {
            for (int i2 = 0; i2 < 13; i2++) {
                paint.setColor(this.mColors[(i * 13) + i2]);
                int i3 = this.mWidth;
                int i4 = this.mHeight;
                canvas.drawRect(i2 * i3, i * i4, (i2 * i3) + i3, (i * i4) + i4, paint);
            }
        }
    }

    public final void drawSelect(Canvas canvas, Paint paint) {
        if (this.mCurrentSelectRow < 0 || this.mCurrentSelectCol < 0) {
            return;
        }
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(this.mCurrentSelectRow < 2 ? Color.parseColor("#ababab") : -1);
        paint.setStrokeWidth(6.0f);
        int i = this.mCurrentSelectCol;
        int i2 = this.mWidth;
        int i3 = this.mCurrentSelectRow;
        int i4 = this.mHeight;
        canvas.drawRect((i * i2) + 3.0f, (i3 * i4) + 3.0f, ((i * i2) + i2) - 3.0f, ((i3 * i4) + i4) - 3.0f, paint);
    }

    public final void colorChange() {
        if (this.mOnColorChangeListener == null) {
            return;
        }
        updateLabel();
        this.mOnColorChangeListener.onColorChange(new ColorChangeEvent(this.mCurrentColor));
    }

    public void setColor(int i) {
        int[] iArr;
        this.mCurrentColor = i;
        this.mCurrentSelectCol = -1;
        this.mCurrentSelectRow = -1;
        for (int i2 : this.mColors) {
            int i3 = this.mCurrentSelectCol + 1;
            this.mCurrentSelectCol = i3;
            if (i3 % 13 == 0) {
                this.mCurrentSelectCol = 0;
                this.mCurrentSelectRow++;
            }
            if (this.mCurrentColor == i2) {
                break;
            }
        }
        invalidate();
    }

    @Override // android.view.View
    @SuppressLint({"ClickableViewAccessibility"})
    public boolean onTouchEvent(MotionEvent motionEvent) {
        if (motionEvent.getAction() != 0) {
            return true;
        }
        this.mCurrentSelectCol = (int) (motionEvent.getX() / this.mWidth);
        int y = (int) (motionEvent.getY() / this.mHeight);
        this.mCurrentSelectRow = y;
        int i = this.mCurrentSelectCol;
        if (i >= 13 || y >= this.mRow) {
            return true;
        }
        int i2 = (y * 13) + i;
        if (i2 < 0) {
            return false;
        }
        int[] iArr = this.mColors;
        if (i2 >= iArr.length) {
            return false;
        }
        this.mCurrentColor = iArr[i2];
        colorChange();
        invalidate();
        return true;
    }

    public void updateLabel() {
        setContentDescription(getContext().getString(R.string.screen_talkback_row_x_column_x, Integer.valueOf(this.mCurrentSelectRow + 1), Integer.valueOf(this.mCurrentSelectCol + 1)));
    }

    public void setOnColorChangeListener(OnColorChangeListener onColorChangeListener) {
        this.mOnColorChangeListener = onColorChangeListener;
    }

    /* loaded from: classes2.dex */
    public static class ColorChangeEvent {
        public int mColor;

        public ColorChangeEvent(int i) {
            this.mColor = i;
        }

        public int getColor() {
            return this.mColor;
        }
    }
}
