package com.miui.gallery.vlog.view;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.editor.R$layout;
import com.miui.gallery.editor.ui.menu.BaseMenuView;
import com.miui.gallery.vlog.R$id;
import com.miui.gallery.vlog.nav.VlogMenuBottomNavView;
import com.miui.gallery.vlog.tools.VlogUtils;

/* loaded from: classes2.dex */
public class VlogMenuView extends BaseMenuView {
    public VlogMenuContentView mContentView;
    public VlogMenuBottomNavView mNavView;
    public VlogMenuTopView mTopView;

    public VlogMenuView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void inflateView() {
        if (VlogUtils.isLandscape(getContext())) {
            FrameLayout.inflate(this.mContext, R$layout.common_editor_menu_layout_rotatable, this);
        } else {
            FrameLayout.inflate(this.mContext, R$layout.common_editor_menu_layout, this);
        }
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initTopView(FrameLayout frameLayout) {
        VlogMenuTopView vlogMenuTopView = new VlogMenuTopView(this.mContext);
        this.mTopView = vlogMenuTopView;
        return vlogMenuTopView;
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initContentView(FrameLayout frameLayout) {
        VlogMenuContentView vlogMenuContentView = new VlogMenuContentView(this.mContext);
        this.mContentView = vlogMenuContentView;
        return vlogMenuContentView;
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initBottomView(FrameLayout frameLayout) {
        VlogMenuBottomNavView vlogMenuBottomNavView = new VlogMenuBottomNavView(this.mContext);
        this.mNavView = vlogMenuBottomNavView;
        return vlogMenuBottomNavView;
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyTopGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(VlogUtils.menuTopAndContentDimenId(getContext())));
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyContentGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(VlogUtils.menuContentAndBottomDimenId(getContext())));
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyBottomGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(VlogUtils.menuBottomLineDimenId(getContext())));
    }

    public int getContentContainerId() {
        return R$id.menu_content_view_container;
    }

    public int getNavContainerId() {
        return R$id.navRoot;
    }

    public void setDefaultContentView() {
        VlogMenuBottomNavView vlogMenuBottomNavView = (VlogMenuBottomNavView) getBottomView();
        if (vlogMenuBottomNavView != null) {
            vlogMenuBottomNavView.navToDefaultView();
        }
    }

    public void setContentGuidelineEnd(int i) {
        getContentLine().setGuidelineEnd(i);
    }

    public void setTopGuidelineEnd(int i) {
        getTopLine().setGuidelineEnd(i);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    /* renamed from: getTopView  reason: collision with other method in class */
    public VlogMenuTopView mo1805getTopView() {
        return this.mTopView;
    }

    public VlogMenuBottomNavView getNavView() {
        return this.mNavView;
    }
}
