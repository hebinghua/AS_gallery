package com.miui.gallery.video.editor.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miui.gallery.R;

/* loaded from: classes2.dex */
public class FilterAdjustHeadView extends LinearLayout implements View.OnClickListener {
    public FilterHeadViewClickListener mHeadViewClickListener;
    public TextView mTvAdjust;
    public TextView mTvTitleFilter;

    /* loaded from: classes2.dex */
    public interface FilterHeadViewClickListener {
        void onAdjustClick();

        void onFilterClick();
    }

    public FilterAdjustHeadView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init(context);
    }

    public final void init(Context context) {
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.video_editor_filter_head_view, this);
        this.mTvTitleFilter = (TextView) findViewById(R.id.tv_title_filter);
        this.mTvAdjust = (TextView) findViewById(R.id.tv_title_adjust);
        this.mTvTitleFilter.setOnClickListener(this);
        this.mTvAdjust.setOnClickListener(this);
        selectFilter(true);
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view != null) {
            switch (view.getId()) {
                case R.id.tv_title_adjust /* 2131363687 */:
                    if (this.mTvAdjust.isSelected()) {
                        return;
                    }
                    selectFilter(false);
                    FilterHeadViewClickListener filterHeadViewClickListener = this.mHeadViewClickListener;
                    if (filterHeadViewClickListener == null) {
                        return;
                    }
                    filterHeadViewClickListener.onAdjustClick();
                    return;
                case R.id.tv_title_filter /* 2131363688 */:
                    if (this.mTvTitleFilter.isSelected()) {
                        return;
                    }
                    selectFilter(true);
                    FilterHeadViewClickListener filterHeadViewClickListener2 = this.mHeadViewClickListener;
                    if (filterHeadViewClickListener2 == null) {
                        return;
                    }
                    filterHeadViewClickListener2.onFilterClick();
                    return;
                default:
                    return;
            }
        }
    }

    public void setHeadViewClickListener(FilterHeadViewClickListener filterHeadViewClickListener) {
        this.mHeadViewClickListener = filterHeadViewClickListener;
    }

    public void selectFilter(boolean z) {
        this.mTvTitleFilter.setSelected(z);
        this.mTvAdjust.setSelected(!z);
    }
}
