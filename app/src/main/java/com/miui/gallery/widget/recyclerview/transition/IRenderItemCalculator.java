package com.miui.gallery.widget.recyclerview.transition;

import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.ui.pictures.PictureViewMode;
import java.util.List;

/* loaded from: classes3.dex */
public interface IRenderItemCalculator {
    List<ITransitionRender> calculateRenderItems(RecyclerView recyclerView, List<List<ItemWrapper>> list, List<List<ItemWrapper>> list2, List<HeaderTransitItem> list3, List<HeaderTransitItem> list4, int i, long j, long j2, PictureViewMode pictureViewMode, PictureViewMode pictureViewMode2, boolean z);
}
