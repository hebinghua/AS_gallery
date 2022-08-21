package com.miui.gallery.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import androidx.appcompat.widget.AppCompatEditText;

/* loaded from: classes2.dex */
public class EditTextPreIme extends AppCompatEditText {
    public OnBackKeyListener mOnBackKeyListener;

    /* loaded from: classes2.dex */
    public interface OnBackKeyListener {
        void onClose();
    }

    public EditTextPreIme(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public EditTextPreIme(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public void setOnBackKeyListener(OnBackKeyListener onBackKeyListener) {
        this.mOnBackKeyListener = onBackKeyListener;
    }

    @Override // android.view.View
    public boolean dispatchKeyEventPreIme(KeyEvent keyEvent) {
        OnBackKeyListener onBackKeyListener;
        if (keyEvent.getKeyCode() == 4 && (onBackKeyListener = this.mOnBackKeyListener) != null) {
            onBackKeyListener.onClose();
            return true;
        }
        return super.dispatchKeyEventPreIme(keyEvent);
    }
}
