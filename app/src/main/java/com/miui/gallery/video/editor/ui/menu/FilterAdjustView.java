package com.miui.gallery.video.editor.ui.menu;

import android.content.Context;
import android.view.View;
import android.widget.FrameLayout;
import com.miui.gallery.R;
import com.miui.gallery.editor.ui.menu.type.BaseMenuTopView;

/* loaded from: classes2.dex */
public class FilterAdjustView extends BaseMenuTopView {
    public FilterAdjustView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initTopView(FrameLayout frameLayout) {
        return FrameLayout.inflate(this.mContext, R.layout.video_editor_filter_top_layout, null);
    }

    @Override // com.miui.gallery.editor.ui.menu.type.BaseMenuBottomView, com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initBottomView(FrameLayout frameLayout) {
        return FrameLayout.inflate(this.mContext, R.layout.video_editor_filter_adjust_apply_layout, null);
    }

    @Override // com.miui.gallery.editor.ui.menu.BaseMenuView
    public View initContentView(FrameLayout frameLayout) {
        return FrameLayout.inflate(this.mContext, R.layout.video_editor_filter_content_layout, null);
    }
}
