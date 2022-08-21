package com.google.android.flexbox;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/* loaded from: classes.dex */
public class FlexboxItemDecoration extends RecyclerView.ItemDecoration {
    public static final int[] LIST_DIVIDER_ATTRS = {16843284};
    public Drawable mDrawable;
    public int mOrientation;

    public FlexboxItemDecoration(Context context) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(LIST_DIVIDER_ATTRS);
        this.mDrawable = obtainStyledAttributes.getDrawable(0);
        obtainStyledAttributes.recycle();
        setOrientation(3);
    }

    public void setDrawable(Drawable drawable) {
        if (drawable == null) {
            throw new IllegalArgumentException("Drawable cannot be null.");
        }
        this.mDrawable = drawable;
    }

    public void setOrientation(int i) {
        this.mOrientation = i;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void onDraw(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        drawHorizontalDecorations(canvas, recyclerView);
        drawVerticalDecorations(canvas, recyclerView);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        int childAdapterPosition = recyclerView.getChildAdapterPosition(view);
        if (childAdapterPosition == 0) {
            return;
        }
        if (!needsHorizontalDecoration() && !needsVerticalDecoration()) {
            rect.set(0, 0, 0, 0);
            return;
        }
        FlexboxLayoutManager flexboxLayoutManager = (FlexboxLayoutManager) recyclerView.getLayoutManager();
        List<FlexLine> flexLines = flexboxLayoutManager.getFlexLines();
        setOffsetAlongMainAxis(rect, childAdapterPosition, flexboxLayoutManager, flexLines, flexboxLayoutManager.getFlexDirection());
        setOffsetAlongCrossAxis(rect, childAdapterPosition, flexboxLayoutManager, flexLines);
    }

    public final void setOffsetAlongCrossAxis(Rect rect, int i, FlexboxLayoutManager flexboxLayoutManager, List<FlexLine> list) {
        if (list.size() == 0 || flexboxLayoutManager.getPositionToFlexLineIndex(i) == 0) {
            return;
        }
        if (flexboxLayoutManager.isMainAxisDirectionHorizontal()) {
            if (!needsHorizontalDecoration()) {
                rect.top = 0;
                rect.bottom = 0;
                return;
            }
            rect.top = this.mDrawable.getIntrinsicHeight();
            rect.bottom = 0;
        } else if (!needsVerticalDecoration()) {
        } else {
            if (flexboxLayoutManager.isLayoutRtl()) {
                rect.right = this.mDrawable.getIntrinsicWidth();
                rect.left = 0;
                return;
            }
            rect.left = this.mDrawable.getIntrinsicWidth();
            rect.right = 0;
        }
    }

    public final void setOffsetAlongMainAxis(Rect rect, int i, FlexboxLayoutManager flexboxLayoutManager, List<FlexLine> list, int i2) {
        if (isFirstItemInLine(i, list, flexboxLayoutManager)) {
            return;
        }
        if (flexboxLayoutManager.isMainAxisDirectionHorizontal()) {
            if (!needsVerticalDecoration()) {
                rect.left = 0;
                rect.right = 0;
            } else if (flexboxLayoutManager.isLayoutRtl()) {
                rect.right = this.mDrawable.getIntrinsicWidth();
                rect.left = 0;
            } else {
                rect.left = this.mDrawable.getIntrinsicWidth();
                rect.right = 0;
            }
        } else if (!needsHorizontalDecoration()) {
            rect.top = 0;
            rect.bottom = 0;
        } else if (i2 == 3) {
            rect.bottom = this.mDrawable.getIntrinsicHeight();
            rect.top = 0;
        } else {
            rect.top = this.mDrawable.getIntrinsicHeight();
            rect.bottom = 0;
        }
    }

    public final void drawVerticalDecorations(Canvas canvas, RecyclerView recyclerView) {
        int left;
        int intrinsicWidth;
        int max;
        int bottom;
        int i;
        int i2;
        if (!needsVerticalDecoration()) {
            return;
        }
        FlexboxLayoutManager flexboxLayoutManager = (FlexboxLayoutManager) recyclerView.getLayoutManager();
        int top = recyclerView.getTop() - recyclerView.getPaddingTop();
        int bottom2 = recyclerView.getBottom() + recyclerView.getPaddingBottom();
        int childCount = recyclerView.getChildCount();
        int flexDirection = flexboxLayoutManager.getFlexDirection();
        for (int i3 = 0; i3 < childCount; i3++) {
            View childAt = recyclerView.getChildAt(i3);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childAt.getLayoutParams();
            if (flexboxLayoutManager.isLayoutRtl()) {
                intrinsicWidth = childAt.getRight() + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                left = this.mDrawable.getIntrinsicWidth() + intrinsicWidth;
            } else {
                left = childAt.getLeft() - ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
                intrinsicWidth = left - this.mDrawable.getIntrinsicWidth();
            }
            if (flexboxLayoutManager.isMainAxisDirectionHorizontal()) {
                max = childAt.getTop() - ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                bottom = childAt.getBottom();
                i = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
            } else if (flexDirection == 3) {
                int min = Math.min(childAt.getBottom() + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin + this.mDrawable.getIntrinsicHeight(), bottom2);
                max = childAt.getTop() - ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                i2 = min;
                this.mDrawable.setBounds(intrinsicWidth, max, left, i2);
                this.mDrawable.draw(canvas);
            } else {
                max = Math.max((childAt.getTop() - ((ViewGroup.MarginLayoutParams) layoutParams).topMargin) - this.mDrawable.getIntrinsicHeight(), top);
                bottom = childAt.getBottom();
                i = ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
            }
            i2 = bottom + i;
            this.mDrawable.setBounds(intrinsicWidth, max, left, i2);
            this.mDrawable.draw(canvas);
        }
    }

    public final void drawHorizontalDecorations(Canvas canvas, RecyclerView recyclerView) {
        int top;
        int intrinsicHeight;
        int left;
        int right;
        int i;
        int i2;
        int i3;
        if (!needsHorizontalDecoration()) {
            return;
        }
        FlexboxLayoutManager flexboxLayoutManager = (FlexboxLayoutManager) recyclerView.getLayoutManager();
        int flexDirection = flexboxLayoutManager.getFlexDirection();
        int left2 = recyclerView.getLeft() - recyclerView.getPaddingLeft();
        int right2 = recyclerView.getRight() + recyclerView.getPaddingRight();
        int childCount = recyclerView.getChildCount();
        for (int i4 = 0; i4 < childCount; i4++) {
            View childAt = recyclerView.getChildAt(i4);
            RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) childAt.getLayoutParams();
            if (flexDirection == 3) {
                intrinsicHeight = childAt.getBottom() + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
                top = this.mDrawable.getIntrinsicHeight() + intrinsicHeight;
            } else {
                top = childAt.getTop() - ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                intrinsicHeight = top - this.mDrawable.getIntrinsicHeight();
            }
            if (flexboxLayoutManager.isMainAxisDirectionHorizontal()) {
                if (flexboxLayoutManager.isLayoutRtl()) {
                    i2 = Math.min(childAt.getRight() + ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin + this.mDrawable.getIntrinsicWidth(), right2);
                    i3 = childAt.getLeft() - ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
                    this.mDrawable.setBounds(i3, intrinsicHeight, i2, top);
                    this.mDrawable.draw(canvas);
                } else {
                    left = Math.max((childAt.getLeft() - ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin) - this.mDrawable.getIntrinsicWidth(), left2);
                    right = childAt.getRight();
                    i = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
                }
            } else {
                left = childAt.getLeft() - ((ViewGroup.MarginLayoutParams) layoutParams).leftMargin;
                right = childAt.getRight();
                i = ((ViewGroup.MarginLayoutParams) layoutParams).rightMargin;
            }
            int i5 = left;
            i2 = right + i;
            i3 = i5;
            this.mDrawable.setBounds(i3, intrinsicHeight, i2, top);
            this.mDrawable.draw(canvas);
        }
    }

    public final boolean needsHorizontalDecoration() {
        return (this.mOrientation & 1) > 0;
    }

    public final boolean needsVerticalDecoration() {
        return (this.mOrientation & 2) > 0;
    }

    public final boolean isFirstItemInLine(int i, List<FlexLine> list, FlexboxLayoutManager flexboxLayoutManager) {
        int positionToFlexLineIndex = flexboxLayoutManager.getPositionToFlexLineIndex(i);
        if ((positionToFlexLineIndex == -1 || positionToFlexLineIndex >= flexboxLayoutManager.getFlexLinesInternal().size() || flexboxLayoutManager.getFlexLinesInternal().get(positionToFlexLineIndex).mFirstIndex != i) && i != 0) {
            return list.size() != 0 && list.get(list.size() - 1).mLastIndex == i - 1;
        }
        return true;
    }
}
