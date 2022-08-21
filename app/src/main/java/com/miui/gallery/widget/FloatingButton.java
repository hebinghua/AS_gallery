package com.miui.gallery.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.miui.gallery.util.LinearMotorHelper;
import miuix.miuixbasewidget.widget.FloatingActionButton;

/* loaded from: classes2.dex */
public class FloatingButton extends FloatingActionButton implements IFloatingButton {
    public IFloatingButtonHandler mFloatingButtonHandler;

    @Override // com.miui.gallery.widget.IFloatingButton
    public View getView() {
        return this;
    }

    public FloatingButton(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.miui.gallery.widget.IFloatingButton
    public void setActionHandler(IFloatingButtonHandler iFloatingButtonHandler) {
        this.mFloatingButtonHandler = iFloatingButtonHandler;
        if (iFloatingButtonHandler != null) {
            setOnClickListener(iFloatingButtonHandler.getHandleClickListener());
        }
    }

    @Override // miuix.miuixbasewidget.widget.FloatingActionButton, android.view.View
    public boolean performClick() {
        LinearMotorHelper.performHapticFeedback(this, LinearMotorHelper.HAPTIC_TAP_NORMAL);
        return super.performClick();
    }
}
