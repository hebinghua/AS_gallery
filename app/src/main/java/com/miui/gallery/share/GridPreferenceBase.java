package com.miui.gallery.share;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ListAdapter;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;

/* loaded from: classes2.dex */
public abstract class GridPreferenceBase extends Preference {
    public ListAdapter mAdapter;
    public final View.OnClickListener mClickListener;
    public int mColumnCount;
    public int mColumnWidth;
    public DataSetObserver mDataSetObserver;
    public OnItemClickListener mItemClickListener;

    /* loaded from: classes2.dex */
    public interface OnItemClickListener {
        void onItemClick(GridPreferenceBase gridPreferenceBase, int i);
    }

    public abstract int getLayoutResourceId();

    public abstract void updateGrid(PreferenceViewHolder preferenceViewHolder);

    /* loaded from: classes2.dex */
    public final class MyDataSetObserver extends DataSetObserver {
        public MyDataSetObserver() {
        }

        @Override // android.database.DataSetObserver
        public void onChanged() {
            GridPreferenceBase.this.update();
        }

        @Override // android.database.DataSetObserver
        public void onInvalidated() {
            GridPreferenceBase.this.update();
        }
    }

    public GridPreferenceBase(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mClickListener = new View.OnClickListener() { // from class: com.miui.gallery.share.GridPreferenceBase.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                if (GridPreferenceBase.this.mItemClickListener != null) {
                    GridPreferenceBase.this.mItemClickListener.onItemClick(GridPreferenceBase.this, ((Integer) view.getTag(GridPreferenceBase.this.getTagInfo())).intValue());
                }
            }
        };
        setLayoutResource(getLayoutResourceId());
    }

    public GridPreferenceBase(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public GridPreferenceBase(Context context) {
        this(context, null);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        updateGrid(preferenceViewHolder);
    }

    public void setAdapter(ListAdapter listAdapter) {
        DataSetObserver dataSetObserver;
        ListAdapter listAdapter2 = this.mAdapter;
        if (listAdapter2 != null && (dataSetObserver = this.mDataSetObserver) != null) {
            listAdapter2.unregisterDataSetObserver(dataSetObserver);
            this.mDataSetObserver = null;
        }
        this.mAdapter = listAdapter;
        if (listAdapter != null) {
            MyDataSetObserver myDataSetObserver = new MyDataSetObserver();
            this.mDataSetObserver = myDataSetObserver;
            this.mAdapter.registerDataSetObserver(myDataSetObserver);
        }
        update();
    }

    public void setColumnCountAndWidth(int i, int i2) {
        this.mColumnCount = i;
        this.mColumnWidth = i2;
        notifyChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mItemClickListener = onItemClickListener;
    }

    public void update() {
        notifyChanged();
    }

    public int getTagInfo() {
        return getLayoutResourceId();
    }
}
