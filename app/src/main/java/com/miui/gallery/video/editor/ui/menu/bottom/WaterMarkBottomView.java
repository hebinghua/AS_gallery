package com.miui.gallery.video.editor.ui.menu.bottom;

import android.content.Context;
import android.view.ViewGroup;
import com.miui.gallery.R;
import com.miui.gallery.editor.ui.menu.bottom.BaseEditBottomView;

/* loaded from: classes2.dex */
public class WaterMarkBottomView extends BaseEditBottomView {
    public WaterMarkBottomView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.ui.menu.bottom.BaseEditBottomView
    public void init(Context context) {
        ViewGroup.inflate(context, R.layout.video_editor_water_mark_bottom_view, this);
    }
}
