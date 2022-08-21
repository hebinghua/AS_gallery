package com.miui.gallery.widget.recyclerview.transition;

import android.graphics.Rect;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.ui.pictures.PictureViewMode;
import java.util.List;

/* loaded from: classes3.dex */
public interface ITransitionalAdapter {
    int calculateMatchItemPosition(PictureViewMode pictureViewMode, int i, int i2, float f, float f2, int i3);

    int calculateScrollPosition(PictureViewMode pictureViewMode, int i);

    List<ITransitItem> calculateTransitItems(RecyclerView recyclerView, int i, long j, int i2, int i3, int i4, Rect rect, PictureViewMode pictureViewMode, boolean z, boolean z2);

    Rect estimateItemFrame(int i, int i2, long j, PictureViewMode pictureViewMode);

    FuzzyMatchItem fuzzyMatchItem(FuzzyMatchItem fuzzyMatchItem, PictureViewMode pictureViewMode);

    FuzzyMatchItem packageMatchItem(int i, PictureViewMode pictureViewMode);
}
