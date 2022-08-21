package com.miui.gallery.editor.photo.penengine;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class ShapeToolView extends RelativeLayout {
    public View mIndicator;
    public ImageView mTool;

    public ShapeToolView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public final void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.screen_shape_tool_view, this);
        findView();
    }

    public final void findView() {
        this.mIndicator = findViewById(R.id.indicator);
        this.mTool = (ImageView) findViewById(R.id.tool);
    }

    @Override // android.view.View
    public void setSelected(boolean z) {
        this.mIndicator.setSelected(z);
        this.mTool.setSelected(z);
    }
}
