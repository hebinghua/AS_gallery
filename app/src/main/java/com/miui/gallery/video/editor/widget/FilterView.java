package com.miui.gallery.video.editor.widget;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import com.miui.gallery.R;
import com.miui.gallery.ui.StartEndSmoothScrollerController;
import com.miui.gallery.video.editor.Filter;
import com.miui.gallery.video.editor.adapter.FilterRecyclerViewAdapter;
import com.miui.gallery.video.editor.manager.FilterAdjustManager;
import com.miui.gallery.video.editor.util.ToolsUtil;
import com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView;
import com.miui.gallery.widget.overscroll.HorizontalOverScrollBounceEffectDecorator;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.ScrollHelper;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/* loaded from: classes2.dex */
public class FilterView extends LinearLayout {
    public FilterRecyclerViewAdapter mAdapter;
    public ArrayList<Filter> mFilters;
    public OnItemSelectedListener mItemSelectedListener;
    public SingleChoiceRecyclerView mSingleChoiceRecycleView;

    /* loaded from: classes2.dex */
    public interface OnItemSelectedListener {
        boolean onItemSelect(SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter singleChoiceRecyclerViewAdapter, int i, boolean z);
    }

    public FilterView(Context context) {
        super(context);
        this.mFilters = new ArrayList<>();
        init(context);
    }

    public final void init(Context context) {
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.video_editor_filterview, this);
        initData();
        initRecyclerView(context);
    }

    public final void initRecyclerView(Context context) {
        this.mSingleChoiceRecycleView = (SingleChoiceRecyclerView) findViewById(R.id.recycler_view);
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(context, 0, false);
        customScrollerLinearLayoutManager.setSmoothScroller(new StartEndSmoothScrollerController(context));
        this.mSingleChoiceRecycleView.setLayoutManager(customScrollerLinearLayoutManager);
        FilterRecyclerViewAdapter filterRecyclerViewAdapter = new FilterRecyclerViewAdapter(context, this.mFilters);
        this.mAdapter = filterRecyclerViewAdapter;
        filterRecyclerViewAdapter.setItemSelectChangeListener(new MyFilterItemSelectChangeListener());
        this.mSingleChoiceRecycleView.setAdapter(this.mAdapter);
        this.mSingleChoiceRecycleView.addItemDecoration(new BlankDivider(getResources(), R.dimen.editor_menu_filter_item_gap));
        HorizontalOverScrollBounceEffectDecorator.setOverScrollEffect(this.mSingleChoiceRecycleView);
    }

    public void initData() {
        this.mFilters = FilterAdjustManager.getFilterData();
    }

    /* loaded from: classes2.dex */
    public class MyFilterItemSelectChangeListener implements SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.ItemSelectChangeListener {
        public MyFilterItemSelectChangeListener() {
        }

        @Override // com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter.ItemSelectChangeListener
        public boolean onItemSelect(SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter singleChoiceRecyclerViewAdapter, int i, boolean z) {
            if (FilterView.this.mItemSelectedListener == null || !z) {
                return false;
            }
            ScrollHelper.onItemClick(FilterView.this.mSingleChoiceRecycleView, i);
            FilterView.this.mSingleChoiceRecycleView.smoothScrollToPosition(i);
            singleChoiceRecyclerViewAdapter.setSelectedItemPosition(i);
            singleChoiceRecyclerViewAdapter.clearLastSelectedPostion();
            return FilterView.this.mItemSelectedListener.onItemSelect(singleChoiceRecyclerViewAdapter, i, z);
        }
    }

    public int getSelectedItemPosition() {
        FilterRecyclerViewAdapter filterRecyclerViewAdapter = this.mAdapter;
        if (filterRecyclerViewAdapter != null) {
            return filterRecyclerViewAdapter.getSelectedItemPosition();
        }
        return 0;
    }

    public Filter getSelectedFilter() {
        FilterRecyclerViewAdapter filterRecyclerViewAdapter = this.mAdapter;
        if (filterRecyclerViewAdapter != null) {
            return this.mAdapter.getFilter(filterRecyclerViewAdapter.getSelectedItemPosition());
        }
        return null;
    }

    public void updateSelectedItemPosition(int i) {
        FilterRecyclerViewAdapter filterRecyclerViewAdapter = this.mAdapter;
        if (filterRecyclerViewAdapter == null) {
            return;
        }
        if (i == -1) {
            if (ToolsUtil.isRTLDirection()) {
                Collections.reverse(this.mFilters);
                FilterRecyclerViewAdapter filterRecyclerViewAdapter2 = this.mAdapter;
                filterRecyclerViewAdapter2.setSelectedItemPosition(filterRecyclerViewAdapter2.getItemCount() - 1);
                return;
            }
            this.mAdapter.setSelectedItemPosition(0);
            return;
        }
        filterRecyclerViewAdapter.setSelectedItemPosition(i);
    }

    public List<String> getFilterCurrentEffect() {
        Filter selectedFilter = getSelectedFilter();
        if (selectedFilter != null) {
            return Arrays.asList(selectedFilter.getLabel());
        }
        return null;
    }

    public void setItemSelectedListener(OnItemSelectedListener onItemSelectedListener) {
        this.mItemSelectedListener = onItemSelectedListener;
    }
}
