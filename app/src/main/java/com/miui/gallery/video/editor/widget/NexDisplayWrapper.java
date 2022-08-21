package com.miui.gallery.video.editor.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import com.nexstreaming.nexeditorsdk.nexEngineView;

/* loaded from: classes2.dex */
public class NexDisplayWrapper extends DisplayWrapper {
    public NexDisplayWrapper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // com.miui.gallery.video.editor.widget.DisplayWrapper
    public View createDisplayView() {
        return new nexEngineView(getContext());
    }
}
