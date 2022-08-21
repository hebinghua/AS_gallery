package com.miui.gallery.editor.photo.app.sticker;

import android.content.Context;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;

/* loaded from: classes2.dex */
public class StickerRecyclerView extends SimpleRecyclerView {
    public int mEndX;
    public int mStartX;

    public StickerRecyclerView(Context context) {
        super(context);
        this.mStartX = 0;
        this.mEndX = 0;
    }

    /* JADX WARN: Code restructure failed: missing block: B:13:0x002d, code lost:
        if (r6 != 3) goto L12;
     */
    @Override // miuix.recyclerview.widget.RecyclerView, android.view.ViewGroup, android.view.View
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean dispatchTouchEvent(android.view.MotionEvent r10) {
        /*
            r9 = this;
            r0 = 2
            int[] r1 = new int[r0]
            int[] r2 = new int[r0]
            android.view.ViewParent r3 = r9.getParent()
            androidx.recyclerview.widget.RecyclerView$Adapter r4 = r9.getAdapter()
            com.miui.gallery.editor.photo.app.sticker.CategoryDetailAdapter r4 = (com.miui.gallery.editor.photo.app.sticker.CategoryDetailAdapter) r4
            androidx.recyclerview.widget.RecyclerView$LayoutManager r5 = r9.getLayoutManager()
            androidx.recyclerview.widget.StaggeredGridLayoutManager r5 = (androidx.recyclerview.widget.StaggeredGridLayoutManager) r5
            if (r3 == 0) goto L6c
            if (r4 == 0) goto L6c
            if (r5 != 0) goto L1c
            goto L6c
        L1c:
            int r4 = r4.getItemCount()
            int r6 = r10.getAction()
            r7 = 1
            if (r6 == 0) goto L5d
            r8 = 0
            if (r6 == r7) goto L59
            if (r6 == r0) goto L30
            r0 = 3
            if (r6 == r0) goto L59
            goto L67
        L30:
            float r0 = r10.getRawX()
            int r0 = (int) r0
            r9.mEndX = r0
            int r6 = r9.mStartX
            int r0 = r0 - r6
            r5.findFirstCompletelyVisibleItemPositions(r1)
            r5.findLastCompletelyVisibleItemPositions(r2)
            r1 = r1[r8]
            if (r1 != 0) goto L4a
            if (r0 <= 0) goto L4a
            r3.requestDisallowInterceptTouchEvent(r8)
            goto L67
        L4a:
            r1 = r2[r7]
            int r4 = r4 - r7
            if (r1 != r4) goto L55
            if (r0 >= 0) goto L55
            r3.requestDisallowInterceptTouchEvent(r8)
            goto L67
        L55:
            r3.requestDisallowInterceptTouchEvent(r7)
            goto L67
        L59:
            r3.requestDisallowInterceptTouchEvent(r8)
            goto L67
        L5d:
            float r0 = r10.getRawX()
            int r0 = (int) r0
            r9.mStartX = r0
            r3.requestDisallowInterceptTouchEvent(r7)
        L67:
            boolean r10 = super.dispatchTouchEvent(r10)
            return r10
        L6c:
            boolean r10 = super.dispatchTouchEvent(r10)
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.editor.photo.app.sticker.StickerRecyclerView.dispatchTouchEvent(android.view.MotionEvent):boolean");
    }
}
