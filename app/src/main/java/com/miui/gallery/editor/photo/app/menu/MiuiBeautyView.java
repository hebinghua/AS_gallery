package com.miui.gallery.editor.photo.app.menu;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import androidx.constraintlayout.widget.Guideline;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.app.miuibeautify.DoubleParameterBeautyFragment;
import com.miui.gallery.editor.photo.app.miuibeautify.MiuiBeautyFragment;
import com.miui.gallery.editor.photo.app.miuibeautify.SingleParameterBeautyFragment;
import com.miui.gallery.editor.photo.app.miuibeautify.SmartBeautyFragment;
import com.miui.gallery.editor.photo.app.miuibeautify.SwitchParameterBeautyFragment;
import com.miui.gallery.editor.photo.widgets.PhotoEditorMenuView;

/* loaded from: classes2.dex */
public class MiuiBeautyView extends PhotoEditorMenuView {
    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initTopView(FrameLayout frameLayout) {
        return null;
    }

    public MiuiBeautyView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.photo.widgets.PhotoEditorMenuView, com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initBottomView(FrameLayout frameLayout) {
        return FrameLayout.inflate(this.mContext, R.layout.photo_editor_miui_beauty_apply_view, null);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initContentView(FrameLayout frameLayout) {
        return FrameLayout.inflate(this.mContext, R.layout.photo_editor_miui_beauty_content_layout, null);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public void modifyTopGuideline(Guideline guideline) {
        guideline.setGuidelineEnd(getResources().getDimensionPixelSize(R.dimen.editor_menu_height));
    }

    public void updateGuideLine(Object obj) {
        if (SmartBeautyFragment.class.isInstance(obj)) {
            this.mContentLine.setGuidelineEnd(getResources().getDimensionPixelSize(R.dimen.editor_beauty_smart_content_line_end));
        } else if (DoubleParameterBeautyFragment.class.isInstance(obj)) {
            this.mContentLine.setGuidelineEnd(getResources().getDimensionPixelSize(R.dimen.editor_beauty_doubleparameterbeauty_content_line_end));
        } else if (SingleParameterBeautyFragment.class.isInstance(obj)) {
            this.mContentLine.setGuidelineEnd(getResources().getDimensionPixelSize(R.dimen.editor_beauty_singleparameterbeauty_content_line_end));
        } else if (SwitchParameterBeautyFragment.class.isInstance(obj)) {
            this.mContentLine.setGuidelineEnd(getResources().getDimensionPixelSize(R.dimen.editor_beauty_switchparameterbeauty_content_line_end));
        } else if (!MiuiBeautyFragment.class.isInstance(obj)) {
        } else {
            this.mContentLine.setGuidelineEnd(getResources().getDimensionPixelSize(R.dimen.editor_content_guide_line_end));
        }
    }
}
