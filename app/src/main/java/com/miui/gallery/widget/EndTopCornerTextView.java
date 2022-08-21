package com.miui.gallery.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;
import com.miui.gallery.R$styleable;

/* loaded from: classes2.dex */
public class EndTopCornerTextView extends TextView {
    public int mCornerMargin;
    public Paint mCornerPaint;
    public CharSequence mCornerText;
    public Rect mTemp1;
    public Rect mTemp2;

    public EndTopCornerTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public EndTopCornerTextView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mTemp1 = new Rect();
        this.mTemp2 = new Rect();
        init(context, attributeSet, i);
    }

    public final void init(Context context, AttributeSet attributeSet, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.EndTopCornerTextView, i, 0);
        Paint paint = new Paint();
        this.mCornerPaint = paint;
        paint.setAntiAlias(true);
        this.mCornerPaint.setColor(obtainStyledAttributes.getColor(2, getCurrentTextColor()));
        this.mCornerPaint.setTypeface(Typeface.create("miui-regular", 0));
        this.mCornerPaint.setTextSize(obtainStyledAttributes.getDimensionPixelSize(3, (int) (getTextSize() / 2.0f)));
        this.mCornerMargin = obtainStyledAttributes.getDimensionPixelSize(0, 0);
        setCornerText(obtainStyledAttributes.getText(1));
        obtainStyledAttributes.recycle();
    }

    public void setCornerText(CharSequence charSequence) {
        this.mCornerText = charSequence;
        if (!TextUtils.isEmpty(charSequence)) {
            int max = Math.max(getPaddingEnd(), Math.round(this.mCornerPaint.measureText(this.mCornerText.toString())) + this.mCornerMargin);
            setPadding(max, getPaddingTop(), max, getPaddingBottom());
        }
        invalidate();
    }

    public void setCornerTextSize(float f) {
        this.mCornerPaint.setTextSize(f);
    }

    @Override // android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        CharSequence text = getText();
        if (TextUtils.isEmpty(text) || TextUtils.isEmpty(this.mCornerText)) {
            return;
        }
        getPaint().getTextBounds(text.toString(), 0, text.length(), this.mTemp1);
        this.mCornerPaint.getTextBounds(this.mCornerText.toString(), 0, this.mCornerText.length(), this.mTemp2);
        if (getLayoutDirection() == 0) {
            canvas.drawText(this.mCornerText.toString(), getPaddingLeft() + this.mTemp1.width() + this.mCornerMargin, getPaddingTop() + getBaseline() + this.mTemp1.top + this.mTemp2.height(), this.mCornerPaint);
        } else {
            canvas.drawText(this.mCornerText.toString(), (getPaddingLeft() - this.mCornerMargin) - this.mTemp2.width(), getPaddingTop() + getBaseline() + this.mTemp1.top + this.mTemp2.height(), this.mCornerPaint);
        }
    }
}
