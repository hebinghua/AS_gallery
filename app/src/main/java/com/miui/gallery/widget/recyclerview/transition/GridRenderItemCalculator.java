package com.miui.gallery.widget.recyclerview.transition;

import android.graphics.Rect;
import android.graphics.RectF;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.ui.pictures.PictureViewMode;
import com.miui.gallery.util.logger.DefaultLogger;
import java.lang.reflect.Array;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/* loaded from: classes3.dex */
public class GridRenderItemCalculator implements IRenderItemCalculator {
    @Override // com.miui.gallery.widget.recyclerview.transition.IRenderItemCalculator
    public List<ITransitionRender> calculateRenderItems(RecyclerView recyclerView, List<List<ItemWrapper>> list, List<List<ItemWrapper>> list2, List<HeaderTransitItem> list3, List<HeaderTransitItem> list4, int i, long j, long j2, PictureViewMode pictureViewMode, PictureViewMode pictureViewMode2, boolean z) {
        int i2;
        Iterator<List<ItemWrapper>> it;
        ImageTransitionRender[] imageTransitionRenderArr;
        int i3;
        if (list == null || list2 == null) {
            DefaultLogger.e("NonTimeLineRenderItemCalculator", "parse transitional items error");
            return null;
        }
        ItemWrapper findItem = TransitionHelper.findItem(list, j);
        ItemWrapper findItem2 = TransitionHelper.findItem(list2, j2);
        if (findItem == null || findItem2 == null) {
            DefaultLogger.e("NonTimeLineRenderItemCalculator", "find anchor error");
            return null;
        }
        int i4 = findItem.mSpanGroupIndex;
        int i5 = findItem2.mSpanGroupIndex;
        recyclerView.getContext().getResources().getConfiguration();
        float centerX = findItem2.getTransitFrame().centerX() - findItem.getTransitFrame().centerX();
        float centerY = findItem2.getTransitFrame().centerY() - findItem.getTransitFrame().centerY();
        float width = findItem2.getTransitFrame().width() / findItem.getTransitFrame().width();
        float height = findItem2.getTransitFrame().height() / findItem.getTransitFrame().height();
        Rect rect = new Rect();
        recyclerView.getLocalVisibleRect(rect);
        RectF rectF = new RectF(rect);
        ImageTransitionRender[][] imageTransitionRenderArr2 = (ImageTransitionRender[][]) Array.newInstance(ImageTransitionRender.class, list2.size(), pictureViewMode2.getSpan());
        int i6 = findItem2.mSpanIndex;
        Iterator<List<ItemWrapper>> it2 = list2.iterator();
        while (it2.hasNext()) {
            Iterator<ItemWrapper> it3 = it2.next().iterator();
            while (it3.hasNext()) {
                ItemWrapper next = it3.next();
                if ((next.getWrapped() instanceof ImageTransitItem) && RectF.intersects(rectF, next.getTransitFrame())) {
                    int i7 = next.mSpanGroupIndex;
                    Iterator<List<ItemWrapper>> it4 = it2;
                    int i8 = next.mSpanIndex;
                    Iterator<ItemWrapper> it5 = it3;
                    RectF operateRectF = operateRectF(next.getTransitFrame(), -centerX, -centerY, 1.0f / width, 1.0f / height, pictureViewMode.getSpacing() - pictureViewMode2.getSpacing(), i7 - i5, i8 - i6);
                    imageTransitionRenderArr2[i7][i8] = TransitionHelper.buildImageTransitionRender(recyclerView, (ImageTransitItem) next.getWrapped(), operateRectF, next.getTransitFrame(), RectF.intersects(rectF, operateRectF) ? 0 : 255, 255, TransitionHelper.SLOW_ALPHA_INTERPOLATOR);
                    it2 = it4;
                    it3 = it5;
                }
            }
        }
        ImageTransitionRender[][] imageTransitionRenderArr3 = (ImageTransitionRender[][]) Array.newInstance(ImageTransitionRender.class, list.size(), pictureViewMode.getSpan());
        int i9 = findItem.mSpanIndex;
        Iterator<List<ItemWrapper>> it6 = list.iterator();
        while (it6.hasNext()) {
            for (ItemWrapper itemWrapper : it6.next()) {
                if (itemWrapper.getWrapped() instanceof ImageTransitItem) {
                    int i10 = itemWrapper.mSpanGroupIndex;
                    int i11 = itemWrapper.mSpanIndex;
                    RectF operateRectF2 = operateRectF(itemWrapper.getTransitFrame(), centerX, centerY, width, height, pictureViewMode2.getSpacing() - pictureViewMode.getSpacing(), i10 - i4, i11 - i9);
                    int length = imageTransitionRenderArr2.length;
                    int i12 = i9;
                    int i13 = 0;
                    boolean z2 = false;
                    while (true) {
                        i2 = i4;
                        if (i13 >= length) {
                            it = it6;
                            break;
                        }
                        ImageTransitionRender[] imageTransitionRenderArr4 = imageTransitionRenderArr2[i13];
                        int i14 = length;
                        int length2 = imageTransitionRenderArr4.length;
                        it = it6;
                        int i15 = 0;
                        while (true) {
                            if (i15 >= length2) {
                                break;
                            }
                            ImageTransitionRender imageTransitionRender = imageTransitionRenderArr4[i15];
                            if (imageTransitionRender != null) {
                                imageTransitionRenderArr = imageTransitionRenderArr4;
                                i3 = length2;
                                if (itemWrapper.getTransitFrame().equals(imageTransitionRender.getFromFrame()) && operateRectF2.equals(imageTransitionRender.getToFrame())) {
                                    z2 = true;
                                    break;
                                }
                            } else {
                                imageTransitionRenderArr = imageTransitionRenderArr4;
                                i3 = length2;
                            }
                            i15++;
                            imageTransitionRenderArr4 = imageTransitionRenderArr;
                            length2 = i3;
                        }
                        if (z2) {
                            break;
                        }
                        i13++;
                        it6 = it;
                        i4 = i2;
                        length = i14;
                    }
                    imageTransitionRenderArr3[i10][i11] = TransitionHelper.buildImageTransitionRender(recyclerView, (ImageTransitItem) itemWrapper.getWrapped(), itemWrapper.getTransitFrame(), operateRectF2, 255, (z2 || !RectF.intersects(rectF, operateRectF2)) ? 255 : 0, TransitionHelper.SLOW_ALPHA_INTERPOLATOR);
                    it6 = it;
                    i9 = i12;
                    i4 = i2;
                }
            }
        }
        LinkedList linkedList = new LinkedList();
        for (ImageTransitionRender[] imageTransitionRenderArr5 : imageTransitionRenderArr3) {
            for (ImageTransitionRender imageTransitionRender2 : imageTransitionRenderArr5) {
                if (imageTransitionRender2 != null) {
                    linkedList.add(imageTransitionRender2);
                }
            }
        }
        for (ImageTransitionRender[] imageTransitionRenderArr6 : imageTransitionRenderArr2) {
            for (ImageTransitionRender imageTransitionRender3 : imageTransitionRenderArr6) {
                if (imageTransitionRender3 != null) {
                    linkedList.add(imageTransitionRender3);
                }
            }
        }
        Collections.sort(linkedList, GridRenderItemCalculator$$ExternalSyntheticLambda0.INSTANCE);
        return linkedList;
    }

    public static /* synthetic */ int lambda$calculateRenderItems$0(ITransitionRender iTransitionRender, ITransitionRender iTransitionRender2) {
        return Integer.compare(iTransitionRender.sortFactor(), iTransitionRender2.sortFactor());
    }

    public static RectF operateRectF(RectF rectF, float f, float f2, float f3, float f4, int i, int i2, int i3) {
        float f5 = i;
        float centerX = rectF.centerX() + f + (i3 * (((f3 - 1.0f) * rectF.width()) + f5));
        float centerY = rectF.centerY() + f2 + (i2 * (((f4 - 1.0f) * rectF.height()) + f5));
        float width = (rectF.width() * f3) / 2.0f;
        float height = (rectF.height() * f4) / 2.0f;
        return new RectF(centerX - width, centerY - height, centerX + width, centerY + height);
    }
}
