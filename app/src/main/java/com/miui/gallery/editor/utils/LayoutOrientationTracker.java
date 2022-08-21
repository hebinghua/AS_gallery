package com.miui.gallery.editor.utils;

import android.content.res.Configuration;

/* loaded from: classes2.dex */
public class LayoutOrientationTracker {
    public int mLastOri;
    public final OnLayoutOrientationChangeListener mOnLayoutOrientationChangeListener;

    /* loaded from: classes2.dex */
    public interface OnLayoutOrientationChangeListener {
        void onLayoutOrientationChange();
    }

    public LayoutOrientationTracker(OnLayoutOrientationChangeListener onLayoutOrientationChangeListener) {
        this.mOnLayoutOrientationChangeListener = onLayoutOrientationChangeListener;
    }

    public void onConfigurationChange(Configuration configuration) {
        OnLayoutOrientationChangeListener onLayoutOrientationChangeListener;
        if (this.mLastOri == 0) {
            this.mLastOri = EditorOrientationHelper.layoutOrientation(configuration);
        }
        int layoutOrientation = EditorOrientationHelper.layoutOrientation(configuration);
        if (this.mLastOri != layoutOrientation && (onLayoutOrientationChangeListener = this.mOnLayoutOrientationChangeListener) != null) {
            onLayoutOrientationChangeListener.onLayoutOrientationChange();
        }
        this.mLastOri = layoutOrientation;
    }
}
