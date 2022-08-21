package com.miui.gallery.widget.recyclerview.transition;

import android.graphics.RectF;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes3.dex */
public class TimeLineRenderItemCalculator implements IRenderItemCalculator {
    /* JADX WARN: Removed duplicated region for block: B:17:0x0052  */
    /* JADX WARN: Removed duplicated region for block: B:18:0x0055  */
    /* JADX WARN: Removed duplicated region for block: B:21:0x005d  */
    /* JADX WARN: Removed duplicated region for block: B:22:0x0060  */
    /* JADX WARN: Removed duplicated region for block: B:25:0x007f  */
    /* JADX WARN: Removed duplicated region for block: B:50:0x00d7  */
    /* JADX WARN: Removed duplicated region for block: B:79:0x0166  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.util.List<com.miui.gallery.widget.recyclerview.transition.ITransitionRender> parseRendersByLine(androidx.recyclerview.widget.RecyclerView r26, android.graphics.RectF r27, java.util.List<com.miui.gallery.widget.recyclerview.transition.ItemWrapper> r28, java.util.List<com.miui.gallery.widget.recyclerview.transition.ItemWrapper> r29, float r30, float r31, int r32, int r33, boolean r34) {
        /*
            Method dump skipped, instructions count: 376
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.recyclerview.transition.TimeLineRenderItemCalculator.parseRendersByLine(androidx.recyclerview.widget.RecyclerView, android.graphics.RectF, java.util.List, java.util.List, float, float, int, int, boolean):java.util.List");
    }

    public static int parseHeaderBindGroupIndex(List<List<ItemWrapper>> list, RectF rectF) {
        int i;
        int size = list.size();
        int i2 = (int) rectF.top;
        int i3 = (int) rectF.bottom;
        int i4 = 0;
        while (true) {
            i = size - 1;
            if (i4 >= i) {
                break;
            }
            int i5 = i4 + 1;
            int i6 = (int) list.get(i4).get(0).getTransitFrame().bottom;
            int i7 = (int) list.get(i5).get(0).getTransitFrame().top;
            if (i6 <= i2 && i7 >= i3) {
                return i4;
            }
            if (i6 > i2) {
                break;
            }
            i4 = i5;
        }
        if (((int) list.get(i).get(0).getTransitFrame().bottom) <= i2) {
            return i;
        }
        return -1;
    }

    public static List<ItemWrapper> padTransitItems(List<ItemWrapper> list, float f, int i, int i2, boolean z, boolean z2) {
        LinkedList linkedList = new LinkedList();
        int size = list.size();
        int i3 = z2 ? z ? i - size : i2 - size : !z ? i2 - i : 0;
        ItemWrapper itemWrapper = list.get(0);
        RectF transitFrame = itemWrapper.getTransitFrame();
        float width = transitFrame.width();
        float f2 = 0.0f;
        for (int i4 = 0; i4 < i3; i4++) {
            f2 -= f + width;
            RectF rectF = new RectF(transitFrame);
            rectF.offset(f2, 0.0f);
            linkedList.add(0, new ItemWrapper(TransitFiller.obtain(rectF), itemWrapper.mSpanGroupIndex, itemWrapper.mSpanIndex));
        }
        linkedList.addAll(list);
        int i5 = z2 ? z ? i2 - i : 0 : z ? i2 - size : i - size;
        ItemWrapper itemWrapper2 = list.get(list.size() - 1);
        RectF transitFrame2 = itemWrapper2.getTransitFrame();
        float f3 = 0.0f;
        for (int i6 = 0; i6 < i5; i6++) {
            f3 += f + width;
            RectF rectF2 = new RectF(transitFrame2);
            rectF2.offset(f3, 0.0f);
            linkedList.add(new ItemWrapper(TransitFiller.obtain(rectF2), itemWrapper2.mSpanGroupIndex, itemWrapper2.mSpanIndex));
        }
        return linkedList;
    }

    /* JADX WARN: Removed duplicated region for block: B:110:0x05e7  */
    /* JADX WARN: Removed duplicated region for block: B:113:0x05ef  */
    /* JADX WARN: Removed duplicated region for block: B:71:0x04bd  */
    /* JADX WARN: Removed duplicated region for block: B:72:0x04cb  */
    @Override // com.miui.gallery.widget.recyclerview.transition.IRenderItemCalculator
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public java.util.List<com.miui.gallery.widget.recyclerview.transition.ITransitionRender> calculateRenderItems(androidx.recyclerview.widget.RecyclerView r27, java.util.List<java.util.List<com.miui.gallery.widget.recyclerview.transition.ItemWrapper>> r28, java.util.List<java.util.List<com.miui.gallery.widget.recyclerview.transition.ItemWrapper>> r29, java.util.List<com.miui.gallery.widget.recyclerview.transition.HeaderTransitItem> r30, java.util.List<com.miui.gallery.widget.recyclerview.transition.HeaderTransitItem> r31, int r32, long r33, long r35, com.miui.gallery.ui.pictures.PictureViewMode r37, com.miui.gallery.ui.pictures.PictureViewMode r38, boolean r39) {
        /*
            Method dump skipped, instructions count: 1691
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.miui.gallery.widget.recyclerview.transition.TimeLineRenderItemCalculator.calculateRenderItems(androidx.recyclerview.widget.RecyclerView, java.util.List, java.util.List, java.util.List, java.util.List, int, long, long, com.miui.gallery.ui.pictures.PictureViewMode, com.miui.gallery.ui.pictures.PictureViewMode, boolean):java.util.List");
    }

    public static /* synthetic */ int lambda$calculateRenderItems$0(ITransitionRender iTransitionRender, ITransitionRender iTransitionRender2) {
        return Integer.compare(iTransitionRender.sortFactor(), iTransitionRender2.sortFactor());
    }
}
