package com.miui.gallery.share;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import androidx.gridlayout.widget.GridLayout;
import androidx.preference.PreferenceViewHolder;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class GridPreference extends GridPreferenceBase {
    @Override // com.miui.gallery.share.GridPreferenceBase
    public int getLayoutResourceId() {
        return R.layout.share_album_user_grid_pref_view;
    }

    public GridPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public GridPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public GridPreference(Context context) {
        this(context, null);
    }

    @Override // com.miui.gallery.share.GridPreferenceBase
    public void updateGrid(PreferenceViewHolder preferenceViewHolder) {
        GridLayout gridLayout = (GridLayout) preferenceViewHolder.findViewById(16908298);
        if (this.mAdapter == null || gridLayout.getColumnCount() == 0) {
            gridLayout.removeAllViews();
            return;
        }
        int count = this.mAdapter.getCount();
        int childCount = gridLayout.getChildCount();
        int min = Math.min(count, childCount);
        int i = 0;
        while (i < min) {
            View childAt = gridLayout.getChildAt(i);
            if (childAt == this.mAdapter.getView(i, childAt, gridLayout)) {
                childAt.setTag(getTagInfo(), Integer.valueOf(i));
                i++;
            } else {
                throw new UnsupportedOperationException("convert view must be reused!");
            }
        }
        while (i < count) {
            View view = this.mAdapter.getView(i, null, gridLayout);
            gridLayout.addView(view);
            view.setOnClickListener(((GridPreferenceBase) this).mClickListener);
            view.setTag(getTagInfo(), Integer.valueOf(i));
            GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) view.getLayoutParams();
            layoutParams.columnSpec = GridLayout.spec(Integer.MIN_VALUE, 1, 1.0f);
            layoutParams.rowSpec = GridLayout.spec(Integer.MIN_VALUE, 1, 1.0f);
            boolean z = getContext().getResources().getConfiguration().getLayoutDirection() == 1;
            int columnCount = gridLayout.getColumnCount();
            int i2 = i % columnCount;
            if (i2 == 0) {
                if (z) {
                    layoutParams.setGravity(8388613);
                } else {
                    layoutParams.setGravity(8388611);
                }
            } else if (i2 != columnCount - 1) {
                layoutParams.setGravity(1);
            } else if (z) {
                layoutParams.setGravity(8388611);
            } else {
                layoutParams.setGravity(8388613);
            }
            i++;
        }
        if (i < childCount) {
            gridLayout.removeViews(i, childCount - i);
            gridLayout.requestLayout();
            gridLayout.setColumnCount(this.mColumnCount);
        }
        gridLayout.requestLayout();
    }
}
