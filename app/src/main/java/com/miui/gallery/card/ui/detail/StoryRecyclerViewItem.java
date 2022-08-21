package com.miui.gallery.card.ui.detail;

import android.content.Context;
import android.util.AttributeSet;
import com.miui.gallery.ui.MicroThumbGridItem;

/* loaded from: classes.dex */
public class StoryRecyclerViewItem extends MicroThumbGridItem {
    @Override // com.miui.gallery.ui.MicroThumbGridItem
    public boolean isSquareItem() {
        return false;
    }

    public StoryRecyclerViewItem(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }
}
