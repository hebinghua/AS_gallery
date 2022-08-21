package com.miui.gallery.editor.photo.core.imports.filter;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import com.miui.gallery.editor.photo.widgets.ProtectiveBitmapGestureGLView;

/* loaded from: classes2.dex */
public class FilterEditorView extends ProtectiveBitmapGestureGLView {
    public float mDownX;
    public float mDownY;
    public boolean mLongTouchTrigger;
    public float mMinTouchSlop;
    public OnLongTouchCallback mOnLongTouchCallback;
    public Runnable mOnLongTouchDownRunnable;

    /* loaded from: classes2.dex */
    public interface OnLongTouchCallback {
        void onLongTouchDown();

        void onLongTouchUp();
    }

    public FilterEditorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mOnLongTouchDownRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.filter.FilterEditorView.1
            @Override // java.lang.Runnable
            public void run() {
                FilterEditorView.this.mLongTouchTrigger = true;
                if (FilterEditorView.this.mOnLongTouchCallback != null) {
                    FilterEditorView.this.mOnLongTouchCallback.onLongTouchDown();
                }
            }
        };
        this.mMinTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    public void setOnLongTouchCallback(OnLongTouchCallback onLongTouchCallback) {
        this.mOnLongTouchCallback = onLongTouchCallback;
    }

    @Override // com.miui.gallery.editor.photo.widgets.glview.BitmapGestureGLView, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        OnLongTouchCallback onLongTouchCallback;
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mLongTouchTrigger = false;
            postDelayed(this.mOnLongTouchDownRunnable, 100L);
            this.mDownX = motionEvent.getX();
            this.mDownY = motionEvent.getY();
        } else {
            if (actionMasked != 1) {
                if (actionMasked != 2) {
                    if (actionMasked != 3) {
                        if (actionMasked == 5 && motionEvent.getPointerCount() > 1) {
                            removeCallbacks(this.mOnLongTouchDownRunnable);
                        }
                    }
                } else if (Math.hypot(motionEvent.getX() - this.mDownX, motionEvent.getY() - this.mDownY) > this.mMinTouchSlop) {
                    removeCallbacks(this.mOnLongTouchDownRunnable);
                }
            }
            removeCallbacks(this.mOnLongTouchDownRunnable);
            if (this.mLongTouchTrigger && (onLongTouchCallback = this.mOnLongTouchCallback) != null) {
                onLongTouchCallback.onLongTouchUp();
            }
        }
        return super.onTouchEvent(motionEvent);
    }
}
