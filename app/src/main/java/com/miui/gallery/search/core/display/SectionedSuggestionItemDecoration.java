package com.miui.gallery.search.core.display;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class SectionedSuggestionItemDecoration extends RecyclerView.ItemDecoration {
    public SectionedSuggestionAdapter mAdapter;
    public int mBottomLineDividerColor;
    public Context mContext;
    public boolean mDrawBottomLine;
    public boolean mDrawLineBetweenSectionItems;
    public int mLineDividerHeight;
    public Paint mPaint = new Paint();
    public int mSectionDividerColor;
    public int mSectionDividerHeight;
    public int mSectionInnerDividerStartMargin;
    public int mTopLineDividerColor;

    public SectionedSuggestionItemDecoration(Context context, SectionedSuggestionAdapter sectionedSuggestionAdapter, boolean z, int i, boolean z2) {
        this.mDrawLineBetweenSectionItems = false;
        this.mDrawBottomLine = false;
        this.mContext = context;
        this.mAdapter = sectionedSuggestionAdapter;
        Resources resources = context.getResources();
        this.mSectionDividerHeight = resources.getDimensionPixelSize(R.dimen.search_section_divider_height);
        this.mLineDividerHeight = resources.getDimensionPixelSize(R.dimen.search_line_divider_height);
        this.mBottomLineDividerColor = resources.getColor(R.color.search_section_divider_line_color);
        this.mTopLineDividerColor = resources.getColor(R.color.search_section_divider_line_color);
        this.mSectionDividerColor = resources.getColor(R.color.search_section_divider_color);
        this.mDrawLineBetweenSectionItems = z;
        this.mSectionInnerDividerStartMargin = i;
        this.mDrawBottomLine = z2;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void onDrawOver(Canvas canvas, RecyclerView recyclerView, RecyclerView.State state) {
        int i;
        int paddingLeft = recyclerView.getPaddingLeft();
        int width = recyclerView.getWidth() - recyclerView.getPaddingRight();
        int childCount = recyclerView.getChildCount();
        for (int i2 = 0; i2 < childCount; i2++) {
            View childAt = recyclerView.getChildAt(i2);
            int innerItemPosition = this.mAdapter.getInnerItemPosition(recyclerView.getChildAdapterPosition(childAt));
            if (innerItemPosition >= 0 && innerItemPosition < this.mAdapter.getInnerItemViewCount()) {
                if (isSectionHeader(this.mAdapter.getIndexes(innerItemPosition))) {
                    i = 1;
                } else {
                    i = (innerItemPosition == 0 || !this.mDrawLineBetweenSectionItems) ? 0 : 2;
                }
                drawDivider(canvas, childAt, (!this.mDrawBottomLine || innerItemPosition != this.mAdapter.getInnerItemViewCount() - 1) ? i : i | 4, paddingLeft, width);
            }
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.ItemDecoration
    public void getItemOffsets(Rect rect, View view, RecyclerView recyclerView, RecyclerView.State state) {
        int innerItemPosition = this.mAdapter.getInnerItemPosition(recyclerView.getChildAdapterPosition(view));
        if (innerItemPosition >= 0 && innerItemPosition < this.mAdapter.getInnerItemViewCount()) {
            if (isSectionHeader(this.mAdapter.getIndexes(innerItemPosition))) {
                rect.set(0, this.mSectionDividerHeight, 0, 0);
                return;
            } else {
                rect.set(0, 0, 0, 0);
                return;
            }
        }
        rect.set(0, 0, 0, 0);
    }

    public final boolean isSectionHeader(int[] iArr) {
        return iArr[1] == -1 && iArr[0] != 0;
    }

    public final void drawDivider(Canvas canvas, View view, int i, int i2, int i3) {
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        int top = view.getTop() - ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
        if ((i & 1) != 0) {
            int i4 = top - this.mSectionDividerHeight;
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setColor(this.mSectionDividerColor);
            float f = i2;
            float f2 = i3;
            float f3 = top;
            canvas.drawRect(f, i4, f2, f3, this.mPaint);
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setStrokeWidth(this.mLineDividerHeight);
            this.mPaint.setColor(this.mBottomLineDividerColor);
            int i5 = this.mLineDividerHeight;
            canvas.drawLine(f, i4 - i5, f2, i4 - i5, this.mPaint);
            this.mPaint.setColor(this.mTopLineDividerColor);
            canvas.drawLine(f, f3, f2, f3, this.mPaint);
        }
        if ((i & 2) != 0) {
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setColor(this.mBottomLineDividerColor);
            float f4 = this.mSectionInnerDividerStartMargin;
            int i6 = this.mLineDividerHeight;
            canvas.drawLine(f4, top + i6, i3, top + i6, this.mPaint);
        }
        if ((i & 4) != 0) {
            int bottom = view.getBottom() + ((ViewGroup.MarginLayoutParams) layoutParams).bottomMargin;
            this.mPaint.setStyle(Paint.Style.STROKE);
            this.mPaint.setColor(this.mBottomLineDividerColor);
            int i7 = this.mLineDividerHeight;
            canvas.drawLine(0.0f, bottom - i7, i3, bottom - i7, this.mPaint);
        }
    }

    public void refreshConfig(Context context) {
        Resources resources = context.getResources();
        this.mSectionDividerHeight = resources.getDimensionPixelSize(R.dimen.search_section_divider_height);
        this.mLineDividerHeight = resources.getDimensionPixelSize(R.dimen.search_line_divider_height);
        this.mBottomLineDividerColor = resources.getColor(R.color.search_section_divider_line_color);
        this.mTopLineDividerColor = resources.getColor(R.color.search_section_divider_line_color);
        this.mSectionDividerColor = resources.getColor(R.color.search_section_divider_color);
    }
}
