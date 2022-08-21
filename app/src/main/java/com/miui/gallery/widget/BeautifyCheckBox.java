package com.miui.gallery.widget;

import android.content.Context;
import android.util.AttributeSet;
import androidx.appcompat.widget.AppCompatCheckBox;
import com.miui.gallery.analytics.TrackController;
import com.miui.gallery.app.AutoTracking;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.property.ViewProperty;
import miuix.animation.utils.EaseManager;

/* loaded from: classes2.dex */
public class BeautifyCheckBox extends AppCompatCheckBox {
    public float mBeautifiedCheckBoxWidth;
    public float mBeautifyCheckBoxWidth;
    public AnimConfig mScaleXAnimConfig;
    public AnimConfig mTextAlphaAnimConfig;

    public BeautifyCheckBox(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, -1);
    }

    public BeautifyCheckBox(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        init();
    }

    public final void init() {
        this.mScaleXAnimConfig = new AnimConfig().setEase(EaseManager.getStyle(-2, 0.9f, 0.3f));
        this.mTextAlphaAnimConfig = new AnimConfig().setEase(EaseManager.getStyle(5, 300.0f));
    }

    public void initWidth(float f, float f2) {
        this.mBeautifyCheckBoxWidth = f;
        this.mBeautifiedCheckBoxWidth = f2;
        if (f2 > f) {
            this.mBeautifiedCheckBoxWidth = f2 * 1.05f;
        } else {
            this.mBeautifyCheckBoxWidth = f * 1.05f;
        }
    }

    public boolean updateWidthIfNeeded() {
        return this.mBeautifyCheckBoxWidth <= 0.0f || this.mBeautifiedCheckBoxWidth <= 0.0f;
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z) {
        super.setChecked(z);
        if (this.mBeautifyCheckBoxWidth > 0.0f) {
            if (!z && getWidth() < (this.mBeautifiedCheckBoxWidth + this.mBeautifyCheckBoxWidth) / 2.0f) {
                return;
            }
            Folme.clean(this);
            AnimState animState = new AnimState("from");
            ViewProperty viewProperty = ViewProperty.WIDTH;
            AnimState add = animState.add(viewProperty, this.mBeautifyCheckBoxWidth);
            AnimState add2 = new AnimState("to").add(viewProperty, this.mBeautifiedCheckBoxWidth);
            if (z) {
                Folme.useAt(this).state().fromTo(add, add2, this.mScaleXAnimConfig);
            } else {
                Folme.useAt(this).state().fromTo(add2, add, this.mScaleXAnimConfig);
            }
            TrackController.trackClick("403.37.0.1.11235", AutoTracking.getRef());
        }
    }
}
