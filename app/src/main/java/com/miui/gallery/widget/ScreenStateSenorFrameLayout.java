package com.miui.gallery.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import com.miui.gallery.miplay.ScreenListener;
import com.miui.gallery.util.logger.DefaultLogger;

/* loaded from: classes2.dex */
public class ScreenStateSenorFrameLayout extends FrameLayout {
    public ScreenListener mScreenStateListener;

    public ScreenStateSenorFrameLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    public void onScreenStateChanged(int i) {
        super.onScreenStateChanged(i);
        DefaultLogger.d("ScreenStateSenorConstraintLayout", "onScreenStateChanged: state->%s", Integer.valueOf(i));
        ScreenListener screenListener = this.mScreenStateListener;
        if (screenListener != null) {
            if (i == 0) {
                screenListener.onScreenOff();
            } else if (i != 1) {
            } else {
                screenListener.onScreenOn();
            }
        }
    }

    public void registerScreenStateListener(ScreenListener screenListener) {
        this.mScreenStateListener = screenListener;
    }

    public void unregisterScreenStateListener() {
        this.mScreenStateListener = null;
    }
}
