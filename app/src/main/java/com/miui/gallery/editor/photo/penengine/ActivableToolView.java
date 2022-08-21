package com.miui.gallery.editor.photo.penengine;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.miui.gallery.R;
import com.miui.gallery.R$styleable;
import miuix.animation.Folme;
import miuix.animation.property.ViewProperty;

/* loaded from: classes2.dex */
public class ActivableToolView extends RelativeLayout {
    public View mIndicator;
    public int mIndicatorMarginStart;
    public boolean mSelected;
    public boolean mShowIndicator;
    public ImageView mTool;
    public float mToolTransY;

    public ActivableToolView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R$styleable.ActivableToolView);
        this.mIndicatorMarginStart = obtainStyledAttributes.getDimensionPixelSize(0, 0);
        this.mShowIndicator = obtainStyledAttributes.getBoolean(1, true);
        obtainStyledAttributes.recycle();
        init(context);
    }

    public final void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.screen_tool_view, this);
        findView();
        initView();
        this.mToolTransY = context.getResources().getDimension(R.dimen.screen_tool_select_tran_y);
    }

    public final void initView() {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mIndicator.getLayoutParams();
        layoutParams.setMarginStart(this.mIndicatorMarginStart);
        this.mIndicator.setLayoutParams(layoutParams);
        this.mIndicator.setVisibility(this.mShowIndicator ? 0 : 4);
    }

    public final void findView() {
        this.mIndicator = findViewById(R.id.indicator);
        this.mTool = (ImageView) findViewById(R.id.tool);
    }

    public void setToolForeground(Drawable drawable) {
        this.mTool.setForeground(drawable);
    }

    public void setToolBackground(Drawable drawable) {
        this.mTool.setBackground(drawable);
    }

    @Override // android.view.View
    public void setSelected(boolean z) {
        this.mSelected = z;
        this.mIndicator.setSelected(z);
    }

    public void performSelectAnim() {
        Folme.useAt(this.mTool).state().to(ViewProperty.TRANSLATION_Y, 0);
    }

    public void performUnselectAnim() {
        Folme.useAt(this.mTool).state().to(ViewProperty.TRANSLATION_Y, Float.valueOf(this.mToolTransY));
    }
}
