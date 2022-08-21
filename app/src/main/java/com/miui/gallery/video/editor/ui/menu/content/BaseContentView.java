package com.miui.gallery.video.editor.ui.menu.content;

import android.content.Context;
import android.view.ViewGroup;
import com.miui.gallery.R;
import com.miui.gallery.editor.ui.menu.content.BaseEditContentView;

/* loaded from: classes2.dex */
public class BaseContentView extends BaseEditContentView {
    public BaseContentView(Context context) {
        super(context);
    }

    @Override // com.miui.gallery.editor.ui.menu.content.BaseEditContentView
    public void inflateContentView(Context context) {
        ViewGroup.inflate(context, R.layout.video_editor_smart_video_content_layout, this);
    }
}
