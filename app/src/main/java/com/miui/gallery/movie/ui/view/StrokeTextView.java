package com.miui.gallery.movie.ui.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;
import com.miui.gallery.movie.R$color;

/* loaded from: classes2.dex */
public class StrokeTextView extends TextView {
    public TextView mOutlineTextView;

    public StrokeTextView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mOutlineTextView = null;
        this.mOutlineTextView = new TextView(context, attributeSet);
        init();
    }

    public void init() {
        TextPaint paint = this.mOutlineTextView.getPaint();
        paint.setStrokeWidth(2.0f);
        paint.setStyle(Paint.Style.STROKE);
        this.mOutlineTextView.setTextColor(getResources().getColor(R$color.movie_time_shader));
        this.mOutlineTextView.setGravity(getGravity());
    }

    @Override // android.view.View
    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        super.setLayoutParams(layoutParams);
        this.mOutlineTextView.setLayoutParams(layoutParams);
    }

    @Override // android.widget.TextView, android.view.View
    public void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        CharSequence text = this.mOutlineTextView.getText();
        if (text == null || !text.equals(getText())) {
            this.mOutlineTextView.setText(getText());
        }
        this.mOutlineTextView.measure(i, i2);
    }

    @Override // android.widget.TextView, android.view.View
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mOutlineTextView.layout(i, i2, i3, i4);
    }

    @Override // android.widget.TextView, android.view.View
    public void onDraw(Canvas canvas) {
        this.mOutlineTextView.draw(canvas);
        super.onDraw(canvas);
    }
}
