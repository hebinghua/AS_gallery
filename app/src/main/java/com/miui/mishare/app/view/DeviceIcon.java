package com.miui.mishare.app.view;

import android.content.Context;
import android.graphics.Outline;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.RelativeLayout;

/* loaded from: classes3.dex */
public class DeviceIcon extends RelativeLayout {
    public DeviceIcon(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    public final void init() {
        setClipToOutline(true);
        setOutlineProvider(new ViewOutlineProvider() { // from class: com.miui.mishare.app.view.DeviceIcon.1
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, view.getWidth(), view.getHeight());
            }
        });
    }

    public void setColor(int i) {
        setBackgroundColor(i);
    }
}
