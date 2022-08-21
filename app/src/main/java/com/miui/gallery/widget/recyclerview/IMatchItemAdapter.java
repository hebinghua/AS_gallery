package com.miui.gallery.widget.recyclerview;

import com.miui.gallery.ui.pictures.PictureViewMode;
import com.miui.gallery.widget.recyclerview.transition.FuzzyMatchItem;

/* loaded from: classes3.dex */
public interface IMatchItemAdapter {
    int calculateViewPosition(PictureViewMode pictureViewMode, int i);

    FuzzyMatchItem fuzzyMatchToItem(FuzzyMatchItem fuzzyMatchItem, PictureViewMode pictureViewMode);

    FuzzyMatchItem getMatchItemByGroupAndChildIndex(PictureViewMode pictureViewMode, int i, int i2, int i3);
}
