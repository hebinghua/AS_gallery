package com.miui.gallery.editor.photo.core.imports.text.textstyle;

import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;

/* loaded from: classes2.dex */
public class TextEditStyleItemDecoration extends RecyclerView.ItemDecoration {
    public int mColumnSpace;
    public int mRowSpace;
    public int mSpanCount;

    public TextEditStyleItemDecoration(int i, int i2, int i3) {
        this.mSpanCount = i;
        this.mColumnSpace = i3;
        this.mRowSpace = i2;
    }

    public void updateItemDecoration(int i, int i2, int i3) {
        this.mSpanCount = i;
        this.mColumnSpace = i2;
        this.mRowSpace = i3;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        int childLayoutPosition = recyclerView.getChildLayoutPosition(view);
        int i = this.mSpanCount;
        int i2 = this.mColumnSpace;
        rect.left = ((childLayoutPosition % i) * i2) / i;
        if (childLayoutPosition >= i) {
            rect.top = i2;
        }
    }
}
