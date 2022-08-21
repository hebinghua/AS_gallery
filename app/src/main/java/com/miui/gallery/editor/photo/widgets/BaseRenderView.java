package com.miui.gallery.editor.photo.widgets;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public abstract class BaseRenderView extends FrameLayout {
    public FrameLayout mContentLayout;
    public View mContentView;
    public FrameLayout mTopLayout;
    public View mTopView;

    public abstract View initContentView();

    public void modifyContentGuideLine(Guideline guideline) {
    }

    public BaseRenderView(Context context) {
        this(context, null);
    }

    public BaseRenderView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        FrameLayout.inflate(context, R.layout.common_editor_render_layout, this);
        this.mContentLayout = (FrameLayout) findViewById(R.id.layout_content_area);
        this.mTopLayout = (FrameLayout) findViewById(R.id.layout_top_area);
        Guideline guideline = (Guideline) findViewById(R.id.guide_line_content);
        this.mContentView = initContentView();
        this.mTopView = initTopView();
        View view = this.mContentView;
        if (view != null) {
            this.mContentLayout.addView(view);
        }
        View view2 = this.mTopView;
        if (view2 != null) {
            this.mTopLayout.addView(view2);
        }
        modifyContentGuideLine(guideline);
    }

    public View initTopView() {
        return FrameLayout.inflate(getContext(), R.layout.photo_editor_title_layout, null);
    }

    public View getTopView() {
        return this.mTopView;
    }
}
