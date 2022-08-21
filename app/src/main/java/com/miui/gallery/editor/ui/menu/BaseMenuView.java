package com.miui.gallery.editor.ui.menu;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.editor.R$id;
import com.miui.gallery.editor.R$layout;

/* loaded from: classes2.dex */
public abstract class BaseMenuView extends FrameLayout {
    public FrameLayout mBottomLayout;
    public Guideline mBottomLine;
    public View mBottomView;
    public Guideline mContentLine;
    public Context mContext;
    public FrameLayout mMenuLayout;
    public View mMenuView;
    public FrameLayout mTopLayout;
    public Guideline mTopLine;
    public View mTopView;

    public abstract View initBottomView(FrameLayout frameLayout);

    public abstract View initContentView(FrameLayout frameLayout);

    public abstract View initTopView(FrameLayout frameLayout);

    public abstract void modifyBottomGuideline(Guideline guideline);

    public abstract void modifyContentGuideline(Guideline guideline);

    public abstract void modifyTopGuideline(Guideline guideline);

    public BaseMenuView(Context context) {
        this(context, null);
    }

    public BaseMenuView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public final void init(Context context) {
        this.mContext = context;
        inflateView();
        initView();
        modifyGuideLinePos();
    }

    public void inflateView() {
        FrameLayout.inflate(this.mContext, R$layout.common_editor_menu_layout, this);
    }

    public final void initView() {
        this.mBottomLayout = (FrameLayout) findViewById(R$id.layout_bottom_area);
        this.mMenuLayout = (FrameLayout) findViewById(R$id.layout_content_area);
        this.mTopLayout = (FrameLayout) findViewById(R$id.layout_top_area);
        this.mTopLine = (Guideline) findViewById(R$id.top_guide_line);
        this.mContentLine = (Guideline) findViewById(R$id.content_guide_line);
        this.mBottomLine = (Guideline) findViewById(R$id.bottom_guide_line);
        this.mTopView = initTopView(this.mTopLayout);
        this.mMenuView = initContentView(this.mMenuLayout);
        this.mBottomView = initBottomView(this.mBottomLayout);
        addChildViewToParent(this.mTopLayout, this.mTopView);
        addChildViewToParent(this.mMenuLayout, this.mMenuView);
        addChildViewToParent(this.mBottomLayout, this.mBottomView);
    }

    public final void modifyGuideLinePos() {
        modifyTopGuideline((Guideline) findViewById(R$id.top_guide_line));
        modifyContentGuideline((Guideline) findViewById(R$id.content_guide_line));
        modifyBottomGuideline((Guideline) findViewById(R$id.bottom_guide_line));
    }

    public final void addChildViewToParent(ViewGroup viewGroup, View view) {
        if (viewGroup == null || view == null) {
            return;
        }
        viewGroup.addView(view);
    }

    public View getBottomView() {
        return this.mBottomView;
    }

    public View getContentView() {
        return this.mMenuView;
    }

    /* renamed from: getTopView */
    public View mo1805getTopView() {
        return this.mTopView;
    }

    public Guideline getTopLine() {
        return (Guideline) findViewById(R$id.top_guide_line);
    }

    public Guideline getContentLine() {
        return (Guideline) findViewById(R$id.content_guide_line);
    }

    public Guideline getBottomLine() {
        return (Guideline) findViewById(R$id.bottom_guide_line);
    }
}
