package com.miui.gallery.share.baby;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import androidx.gridlayout.widget.GridLayout;
import androidx.preference.PreferenceViewHolder;
import com.miui.gallery.R;
import com.miui.gallery.share.GridPreferenceBase;

/* loaded from: classes2.dex */
public class BabyAlbumSharerPreference extends GridPreferenceBase {
    @Override // com.miui.gallery.share.GridPreferenceBase
    public int getLayoutResourceId() {
        return R.layout.baby_album_sharer_pref_view;
    }

    public BabyAlbumSharerPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        setSelectable(false);
    }

    public BabyAlbumSharerPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BabyAlbumSharerPreference(Context context) {
        this(context, null);
    }

    @Override // com.miui.gallery.share.GridPreferenceBase
    public void updateGrid(PreferenceViewHolder preferenceViewHolder) {
        int i;
        int i2;
        GridLayout.Alignment alignment;
        ListAdapter listAdapter = this.mAdapter;
        if (listAdapter == null) {
            return;
        }
        int count = listAdapter.getCount();
        ViewGroup viewGroup = (ViewGroup) preferenceViewHolder.findViewById(R.id.father_grid);
        ViewGroup viewGroup2 = (ViewGroup) preferenceViewHolder.findViewById(R.id.mother_grid);
        viewGroup.setVisibility(8);
        viewGroup2.setVisibility(8);
        int i3 = 0;
        for (int i4 = 0; i4 < count; i4++) {
            int itemViewType = this.mAdapter.getItemViewType(i4);
            if (itemViewType == 0 || itemViewType == 1) {
                ViewGroup viewGroup3 = i3 == 0 ? viewGroup : viewGroup2;
                viewGroup3.setVisibility(0);
                View childAt = viewGroup3.getChildCount() == 1 ? viewGroup3.getChildAt(0) : null;
                View view = this.mAdapter.getView(i4, childAt, viewGroup3);
                view.setTag(getTagInfo(), Integer.valueOf(i4));
                if (childAt == null) {
                    viewGroup3.addView(view);
                    view.setOnClickListener(((GridPreferenceBase) this).mClickListener);
                } else if (childAt != view) {
                    throw new UnsupportedOperationException("convert view must be reused!");
                }
                i3++;
            }
        }
        GridLayout gridLayout = (GridLayout) preferenceViewHolder.findViewById(16908298);
        if (gridLayout.getColumnCount() == 0) {
            gridLayout.removeAllViews();
            return;
        }
        int childCount = gridLayout.getChildCount();
        int i5 = count - i3;
        int columnCount = gridLayout.getColumnCount();
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        while (i6 < count) {
            if (this.mAdapter.getItemViewType(i6) == 2) {
                View childAt2 = gridLayout.getChildCount() > i7 ? gridLayout.getChildAt(i7) : null;
                View view2 = this.mAdapter.getView(i6, childAt2, gridLayout);
                view2.setTag(getTagInfo(), Integer.valueOf(i6));
                GridLayout.LayoutParams layoutParams = (GridLayout.LayoutParams) view2.getLayoutParams();
                i = count;
                boolean z = getContext().getResources().getConfiguration().getLayoutDirection() == 1;
                int i9 = i7 % columnCount;
                if (i9 == 0) {
                    if (z) {
                        alignment = GridLayout.END;
                    } else {
                        alignment = GridLayout.START;
                    }
                    i2 = i3;
                } else {
                    i2 = i3;
                    if (i9 != columnCount - 1) {
                        alignment = GridLayout.CENTER;
                    } else if (z) {
                        alignment = GridLayout.START;
                    } else {
                        alignment = GridLayout.END;
                    }
                }
                layoutParams.columnSpec = GridLayout.spec(i8, alignment, 1.0f);
                i8 = (i8 + 1) % columnCount;
                if (childAt2 == null) {
                    gridLayout.addView(view2);
                    view2.setOnClickListener(((GridPreferenceBase) this).mClickListener);
                } else if (childAt2 != view2) {
                    throw new UnsupportedOperationException("convert view must be reused!");
                }
                i7++;
            } else {
                i = count;
                i2 = i3;
            }
            i6++;
            count = i;
            i3 = i2;
        }
        int i10 = i3;
        if (i5 < childCount) {
            gridLayout.removeViews(i5, childCount - i5);
            gridLayout.requestLayout();
            gridLayout.setColumnCount(this.mColumnCount);
        }
        preferenceViewHolder.findViewById(R.id.divider).setVisibility((i10 <= 0 || i5 <= 0) ? 8 : 0);
        preferenceViewHolder.itemView.requestLayout();
    }
}
