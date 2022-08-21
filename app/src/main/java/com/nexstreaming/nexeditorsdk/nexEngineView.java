package com.nexstreaming.nexeditorsdk;

import android.content.Context;
import android.util.AttributeSet;
import com.nexstreaming.kminternal.nexvideoeditor.NexThemeView;

/* loaded from: classes3.dex */
public class nexEngineView extends NexThemeView implements NexThemeView.b {
    private NexViewListener nexThemeViewListener;

    /* loaded from: classes3.dex */
    public interface NexViewListener {
        void onEngineViewAvailable(int i, int i2);

        void onEngineViewDestroyed();

        void onEngineViewSizeChanged(int i, int i2);
    }

    @Override // com.nexstreaming.kminternal.nexvideoeditor.NexThemeView.b
    public void onEventNotify(int i, Object obj, int i2, int i3, int i4) {
        NexViewListener nexViewListener = this.nexThemeViewListener;
        if (nexViewListener != null) {
            if (i == 1) {
                nexViewListener.onEngineViewAvailable(i2, i3);
            } else if (i == 2) {
                nexViewListener.onEngineViewSizeChanged(i2, i3);
            } else if (i != 3) {
            } else {
                nexViewListener.onEngineViewDestroyed();
            }
        }
    }

    public nexEngineView(Context context) {
        super(context);
        NexThemeView.setAspectRatio(nexApplicationConfig.getAspectRatioInScreenMode());
        super.setNotify(this);
    }

    public nexEngineView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        NexThemeView.setAspectRatio(nexApplicationConfig.getAspectRatioInScreenMode());
        super.setNotify(this);
    }

    @Override // com.nexstreaming.kminternal.nexvideoeditor.NexThemeView
    @Deprecated
    public void setBlackOut(boolean z) {
        super.setBlackOut(z);
    }

    public void setListener(NexViewListener nexViewListener) {
        this.nexThemeViewListener = nexViewListener;
        if (isSurfaceAvailable()) {
            this.nexThemeViewListener.onEngineViewAvailable(getWidth(), getHeight());
        }
    }
}
