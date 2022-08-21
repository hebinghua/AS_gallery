package com.miui.gallery.text;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/* loaded from: classes2.dex */
public class UrlSpan extends ClickableSpan {
    public UrlSpanOnClickListener mOnClickListener;

    /* loaded from: classes2.dex */
    public interface UrlSpanOnClickListener {
        void onClick();
    }

    public UrlSpan(UrlSpanOnClickListener urlSpanOnClickListener) {
        this.mOnClickListener = urlSpanOnClickListener;
    }

    @Override // android.text.style.ClickableSpan, android.text.style.CharacterStyle
    public void updateDrawState(TextPaint textPaint) {
        textPaint.setUnderlineText(true);
        textPaint.setColor(-16738305);
    }

    @Override // android.text.style.ClickableSpan
    public void onClick(View view) {
        UrlSpanOnClickListener urlSpanOnClickListener = this.mOnClickListener;
        if (urlSpanOnClickListener != null) {
            urlSpanOnClickListener.onClick();
        }
    }
}
