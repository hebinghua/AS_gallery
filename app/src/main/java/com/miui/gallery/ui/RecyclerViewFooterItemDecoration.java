package com.miui.gallery.ui;

import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.itemdrag.RecyclerViewUtils;

/* loaded from: classes2.dex */
public class RecyclerViewFooterItemDecoration extends RecyclerView.ItemDecoration {
    public View mDecorView;

    public RecyclerViewFooterItemDecoration(View view) {
        if (view.getParent() != null) {
            throw new IllegalStateException("transient view should not have a parent");
        }
        this.mDecorView = view;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        super.onDrawOver(canvas, recyclerView, state);
        drawFooterDecor(canvas, recyclerView);
    }

    public final void drawFooterDecor(Canvas canvas, RecyclerView recyclerView) {
        if (recyclerView.getAdapter() == null) {
            return;
        }
        int itemCount = recyclerView.getAdapter().getItemCount();
        boolean z = true;
        View childAt = recyclerView.getChildAt(recyclerView.getChildCount() - 1);
        int childAdapterPosition = recyclerView.getChildAdapterPosition(childAt);
        if (childAdapterPosition == -1 || childAdapterPosition != itemCount - 1) {
            z = false;
        }
        if (!z) {
            return;
        }
        int bottom = childAt.getBottom();
        if (this.mDecorView.getHeight() + bottom <= recyclerView.getHeight()) {
            bottom = recyclerView.getHeight() - this.mDecorView.getHeight();
        }
        canvas.save();
        canvas.translate(0.0f, bottom);
        this.mDecorView.draw(canvas);
        canvas.restore();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        if (isLastRow(recyclerView, recyclerView.getChildAdapterPosition(view))) {
            calculateFooterOffset(rect, view, recyclerView);
        } else {
            super.getItemOffsets(rect, view, recyclerView, state);
        }
    }

    public final boolean isLastRow(RecyclerView recyclerView, int i) {
        int itemCount = recyclerView.getAdapter().getItemCount() - 1;
        return i == itemCount || i >= itemCount - RecyclerViewUtils.getSpanIndex(recyclerView, itemCount);
    }

    public final void calculateFooterOffset(Rect rect, View view, RecyclerView recyclerView) {
        this.mDecorView.measure(0, 0);
        this.mDecorView.measure(View.MeasureSpec.makeMeasureSpec(recyclerView.getMeasuredWidth(), 1073741824), View.MeasureSpec.makeMeasureSpec(this.mDecorView.getMeasuredHeight(), 1073741824));
        View view2 = this.mDecorView;
        view2.layout(0, 0, view2.getMeasuredWidth(), this.mDecorView.getMeasuredHeight());
        rect.set(0, 0, 0, this.mDecorView.getHeight());
    }
}
