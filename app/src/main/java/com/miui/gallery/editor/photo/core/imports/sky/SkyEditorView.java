package com.miui.gallery.editor.photo.core.imports.sky;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import com.miui.gallery.editor.photo.utils.EditorMiscHelper;
import com.miui.gallery.editor.photo.widgets.ProtectiveImageView;

/* loaded from: classes2.dex */
public class SkyEditorView extends ProtectiveImageView {
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

    public SkyEditorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mOnLongTouchDownRunnable = new Runnable() { // from class: com.miui.gallery.editor.photo.core.imports.sky.SkyEditorView.1
            @Override // java.lang.Runnable
            public void run() {
                SkyEditorView.this.mLongTouchTrigger = true;
                if (SkyEditorView.this.mOnLongTouchCallback != null) {
                    SkyEditorView.this.mOnLongTouchCallback.onLongTouchDown();
                }
            }
        };
        this.mMinTouchSlop = ViewConfiguration.get(getContext()).getScaledTouchSlop();
    }

    @Override // com.miui.gallery.editor.photo.widgets.ProtectiveImageView, android.view.View
    public void setLayoutParams(ViewGroup.LayoutParams layoutParams) {
        super.setLayoutParams(layoutParams);
        EditorMiscHelper.configProtectiveArea(this);
    }

    public void setOnLongTouchCallback(OnLongTouchCallback onLongTouchCallback) {
        this.mOnLongTouchCallback = onLongTouchCallback;
    }

    /* JADX WARN: Code restructure failed: missing block: B:8:0x000d, code lost:
        if (r0 != 3) goto L8;
     */
    @Override // android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean onTouchEvent(android.view.MotionEvent r7) {
        /*
            r6 = this;
            int r0 = r7.getAction()
            r1 = 1
            if (r0 == 0) goto L42
            if (r0 == r1) goto L31
            r2 = 2
            if (r0 == r2) goto L10
            r7 = 3
            if (r0 == r7) goto L31
            goto L58
        L10:
            float r0 = r7.getX()
            float r2 = r6.mDownX
            float r0 = r0 - r2
            double r2 = (double) r0
            float r7 = r7.getY()
            float r0 = r6.mDownY
            float r7 = r7 - r0
            double r4 = (double) r7
            double r2 = java.lang.Math.hypot(r2, r4)
            float r7 = r6.mMinTouchSlop
            double r4 = (double) r7
            int r7 = (r2 > r4 ? 1 : (r2 == r4 ? 0 : -1))
            if (r7 <= 0) goto L58
            java.lang.Runnable r7 = r6.mOnLongTouchDownRunnable
            r6.removeCallbacks(r7)
            goto L58
        L31:
            java.lang.Runnable r7 = r6.mOnLongTouchDownRunnable
            r6.removeCallbacks(r7)
            boolean r7 = r6.mLongTouchTrigger
            if (r7 == 0) goto L58
            com.miui.gallery.editor.photo.core.imports.sky.SkyEditorView$OnLongTouchCallback r7 = r6.mOnLongTouchCallback
            if (r7 == 0) goto L58
            r7.onLongTouchUp()
            goto L58
        L42:
            r0 = 0
            r6.mLongTouchTrigger = r0
            java.lang.Runnable r0 = r6.mOnLongTouchDownRunnable
            r2 = 100
            r6.postDelayed(r0, r2)
            float r0 = r7.getX()
            r6.mDownX = r0
            float r7 = r7.getY()
            r6.mDownY = r7
        L58:
            return r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.core.imports.sky.SkyEditorView.onTouchEvent(android.view.MotionEvent):boolean");
    }
}
