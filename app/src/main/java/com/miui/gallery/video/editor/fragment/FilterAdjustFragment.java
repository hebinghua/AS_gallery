package com.miui.gallery.video.editor.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.miui.gallery.R;
import com.miui.gallery.editor.photo.widgets.NoScrollViewPager;
import com.miui.gallery.util.logger.DefaultLogger;
import com.miui.gallery.video.editor.Filter;
import com.miui.gallery.video.editor.VideoEditor;
import com.miui.gallery.video.editor.adapter.FilterAdjustPageAdapter;
import com.miui.gallery.video.editor.adapter.FilterRecyclerViewAdapter;
import com.miui.gallery.video.editor.ui.MenuFragment;
import com.miui.gallery.video.editor.ui.menu.FilterAdjustView;
import com.miui.gallery.video.editor.widget.AdjustView;
import com.miui.gallery.video.editor.widget.FilterAdjustHeadView;
import com.miui.gallery.video.editor.widget.FilterView;
import com.miui.gallery.video.editor.widget.SingleChoiceRecyclerView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes2.dex */
public class FilterAdjustFragment extends MenuFragment {
    public AdjustView mAdjustView;
    public View mCancelView;
    public FilterAdjustHeadView mFilterAdjustHeadView;
    public FilterView mFilterView;
    public ConstraintLayout mHeadBar;
    public NoScrollViewPager mNoScrollViewPager;
    public View mOkView;
    public FilterAdjustPageAdapter mPageAdapter;
    public TextView mTitleView;
    public List<View> mViewList;
    public boolean showFilterView = true;
    public int mSavedSelectedFilterIndex = -1;

    public static /* synthetic */ void $r8$lambda$0ViQTDBMk6vTFTA3BkOckz22JII(FilterAdjustFragment filterAdjustFragment, View view) {
        filterAdjustFragment.lambda$initListener$1(view);
    }

    public static /* synthetic */ void $r8$lambda$1RDSRsOyzfG8KdYLv3C1HnFO2Zg(FilterAdjustFragment filterAdjustFragment, View view) {
        filterAdjustFragment.lambda$initListener$0(view);
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public int getEffectId() {
        return R.id.video_editor_filter;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        FilterAdjustView filterAdjustView = new FilterAdjustView(viewGroup.getContext());
        this.mNoScrollViewPager = (NoScrollViewPager) filterAdjustView.findViewById(R.id.filter_pager);
        this.mFilterAdjustHeadView = (FilterAdjustHeadView) filterAdjustView.findViewById(R.id.filter_and_just_head_view);
        this.mCancelView = filterAdjustView.findViewById(R.id.cancel);
        this.mOkView = filterAdjustView.findViewById(R.id.ok);
        this.mTitleView = (TextView) filterAdjustView.findViewById(R.id.title);
        this.mHeadBar = (ConstraintLayout) filterAdjustView.findViewById(R.id.head_area);
        initData();
        initListener();
        return filterAdjustView;
    }

    public final void initData() {
        this.mViewList = new ArrayList();
        this.mFilterView = new FilterView(this.mContext);
        this.mAdjustView = new AdjustView(this.mContext);
        this.mViewList.add(this.mFilterView);
        this.mViewList.add(this.mAdjustView);
        this.mTitleView.setText(this.mContext.getResources().getString(R.string.video_editor_filter));
        this.mFilterAdjustHeadView.selectFilter(this.showFilterView);
        this.mFilterView.updateSelectedItemPosition(this.mSavedSelectedFilterIndex);
        FilterAdjustPageAdapter filterAdjustPageAdapter = new FilterAdjustPageAdapter(this.mViewList);
        this.mPageAdapter = filterAdjustPageAdapter;
        this.mNoScrollViewPager.setAdapter(filterAdjustPageAdapter);
    }

    public final void initListener() {
        this.mFilterAdjustHeadView.setHeadViewClickListener(new FilterAdjustHeadView.FilterHeadViewClickListener() { // from class: com.miui.gallery.video.editor.fragment.FilterAdjustFragment.1
            {
                FilterAdjustFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.widget.FilterAdjustHeadView.FilterHeadViewClickListener
            public void onFilterClick() {
                if (FilterAdjustFragment.this.mFilterView != null) {
                    FilterAdjustFragment.this.mNoScrollViewPager.setCurrentItem(0);
                    FilterAdjustFragment.this.mTitleView.setText(FilterAdjustFragment.this.mContext.getResources().getString(R.string.video_editor_filter));
                    FilterAdjustFragment.this.showFilterView = true;
                }
            }

            @Override // com.miui.gallery.video.editor.widget.FilterAdjustHeadView.FilterHeadViewClickListener
            public void onAdjustClick() {
                if (FilterAdjustFragment.this.mAdjustView != null) {
                    FilterAdjustFragment.this.mNoScrollViewPager.setCurrentItem(1);
                    FilterAdjustFragment.this.mTitleView.setText(FilterAdjustFragment.this.mContext.getResources().getString(R.string.video_editor_adjust));
                    FilterAdjustFragment.this.showFilterView = false;
                    FilterAdjustFragment.this.mAdjustView.refreshData();
                }
            }
        });
        this.mAdjustView.setFilterAdjustHeadViewListener(new AdjustView.IFilterAdjustHeadViewListener() { // from class: com.miui.gallery.video.editor.fragment.FilterAdjustFragment.2
            {
                FilterAdjustFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.widget.AdjustView.IFilterAdjustHeadViewListener
            public void removeAllViewFromHeadBar() {
                FilterAdjustFragment.this.mHeadBar.removeAllViews();
            }

            @Override // com.miui.gallery.video.editor.widget.AdjustView.IFilterAdjustHeadViewListener
            public void addSeekBarToHeadBar(View view) {
                if (view != null) {
                    FilterAdjustFragment.this.mHeadBar.addView(view, -1, -2);
                    FilterAdjustFragment.this.mHeadBar.setPadding(0, FilterAdjustFragment.this.getResources().getDimensionPixelSize(R.dimen.editor_menu_seek_bar_top), 0, 0);
                }
            }

            @Override // com.miui.gallery.video.editor.widget.AdjustView.IFilterAdjustHeadViewListener
            public void addFilterViewToHeadBar(View view) {
                FilterAdjustFragment.this.mHeadBar.removeAllViews();
                FilterAdjustFragment.this.mHeadBar.setPadding(0, 0, 0, 0);
                FilterAdjustFragment.this.mHeadBar.addView(FilterAdjustFragment.this.mFilterAdjustHeadView);
            }
        });
        this.mAdjustView.setAdjustEffectChangeListener(new AdjustView.IAdjustEffectChangeListener() { // from class: com.miui.gallery.video.editor.fragment.FilterAdjustFragment.3
            {
                FilterAdjustFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.widget.AdjustView.IAdjustEffectChangeListener
            public void adjustBrightness(int i) {
                FilterAdjustFragment.this.mVideoEditor.adjustBrightness(i);
                FilterAdjustFragment.this.mVideoEditor.setVideoEditorAdjust(true);
            }

            @Override // com.miui.gallery.video.editor.widget.AdjustView.IAdjustEffectChangeListener
            public void adjustSaturation(int i) {
                FilterAdjustFragment.this.mVideoEditor.adjustSaturation(i);
                FilterAdjustFragment.this.mVideoEditor.setVideoEditorAdjust(true);
            }

            @Override // com.miui.gallery.video.editor.widget.AdjustView.IAdjustEffectChangeListener
            public void adjustContrast(int i) {
                FilterAdjustFragment.this.mVideoEditor.adjustContrast(i);
                FilterAdjustFragment.this.mVideoEditor.setVideoEditorAdjust(true);
            }

            @Override // com.miui.gallery.video.editor.widget.AdjustView.IAdjustEffectChangeListener
            public void adjustSharpness(int i) {
                FilterAdjustFragment.this.mVideoEditor.adjustSharpness(i);
                FilterAdjustFragment.this.mVideoEditor.setVideoEditorAdjust(true);
            }

            @Override // com.miui.gallery.video.editor.widget.AdjustView.IAdjustEffectChangeListener
            public void adjustVignetteRange(int i) {
                FilterAdjustFragment.this.mVideoEditor.adjustVignetteRange(i);
                FilterAdjustFragment.this.mVideoEditor.setVideoEditorAdjust(true);
            }
        });
        this.mOkView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.video.editor.fragment.FilterAdjustFragment$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                FilterAdjustFragment.$r8$lambda$1RDSRsOyzfG8KdYLv3C1HnFO2Zg(FilterAdjustFragment.this, view);
            }
        });
        this.mCancelView.setOnClickListener(new View.OnClickListener() { // from class: com.miui.gallery.video.editor.fragment.FilterAdjustFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                FilterAdjustFragment.$r8$lambda$0ViQTDBMk6vTFTA3BkOckz22JII(FilterAdjustFragment.this, view);
            }
        });
        this.mFilterView.setItemSelectedListener(new FilterView.OnItemSelectedListener() { // from class: com.miui.gallery.video.editor.fragment.FilterAdjustFragment.4
            {
                FilterAdjustFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.widget.FilterView.OnItemSelectedListener
            public boolean onItemSelect(SingleChoiceRecyclerView.SingleChoiceRecyclerViewAdapter singleChoiceRecyclerViewAdapter, int i, boolean z) {
                Filter filter = ((FilterRecyclerViewAdapter) singleChoiceRecyclerViewAdapter).getFilter(i);
                if (filter != null) {
                    FilterAdjustFragment.this.mVideoEditor.setFilter(filter);
                    return FilterAdjustFragment.this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.fragment.FilterAdjustFragment.4.1
                        {
                            AnonymousClass4.this = this;
                        }

                        @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
                        public void onCompleted() {
                            FilterAdjustFragment.this.mVideoEditor.play();
                            FilterAdjustFragment.this.recordEventWithEffectChanged();
                            FilterAdjustFragment.this.updatePlayBtnView();
                        }
                    });
                }
                return true;
            }
        });
    }

    public /* synthetic */ void lambda$initListener$0(View view) {
        doApply();
    }

    public /* synthetic */ void lambda$initListener$1(View view) {
        doCancel();
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public boolean doCancel() {
        AdjustView adjustView = this.mAdjustView;
        if (adjustView == null || !adjustView.isTracking()) {
            if (this.showFilterView) {
                doFilterCancel();
            } else {
                this.mAdjustView.doCancel();
                VideoEditor videoEditor = this.mVideoEditor;
                if (videoEditor != null) {
                    videoEditor.setVideoEditorAdjust(false);
                }
                doFilterCancel();
            }
            recordEventWithCancel();
            return true;
        }
        return false;
    }

    public boolean doApply() {
        VideoEditor videoEditor;
        AdjustView adjustView = this.mAdjustView;
        if ((adjustView != null && adjustView.isTracking()) || (videoEditor = this.mVideoEditor) == null || videoEditor.getState() == 200) {
            return false;
        }
        this.mVideoEditor.saveEditState();
        FilterView filterView = this.mFilterView;
        if (filterView != null) {
            this.mSavedSelectedFilterIndex = filterView.getSelectedItemPosition();
        }
        recordEventWithApply();
        onExitMode();
        return true;
    }

    public final boolean doFilterCancel() {
        VideoEditor videoEditor = this.mVideoEditor;
        if (videoEditor == null) {
            DefaultLogger.d("FilterAdjustFragment", "doCancel: videoEditor is null.");
            return false;
        }
        videoEditor.restoreEditState();
        return this.mVideoEditor.apply(new VideoEditor.OnCompletedListener() { // from class: com.miui.gallery.video.editor.fragment.FilterAdjustFragment.5
            {
                FilterAdjustFragment.this = this;
            }

            @Override // com.miui.gallery.video.editor.VideoEditor.OnCompletedListener
            public void onCompleted() {
                if (FilterAdjustFragment.this.mVideoEditor == null) {
                    return;
                }
                FilterAdjustFragment.this.mVideoEditor.play();
                FilterAdjustFragment.this.onExitMode();
            }
        });
    }

    @Override // com.miui.gallery.video.editor.ui.MenuFragment
    public List<String> getCurrentEffect() {
        List<String> filterCurrentEffect;
        ArrayList arrayList = new ArrayList();
        FilterView filterView = this.mFilterView;
        if (filterView != null && (filterCurrentEffect = filterView.getFilterCurrentEffect()) != null) {
            arrayList.addAll(filterCurrentEffect);
        }
        AdjustView adjustView = this.mAdjustView;
        if (adjustView != null) {
            List<String> adjustCurrentEffect = adjustView.getAdjustCurrentEffect();
            if (adjustCurrentEffect != null) {
                arrayList.addAll(adjustCurrentEffect);
            }
            this.mAdjustView.clearCurrentEffects();
        }
        return arrayList;
    }

    @Override // androidx.fragment.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        if (this.mPageAdapter != null) {
            this.mPageAdapter = null;
        }
        FilterAdjustHeadView filterAdjustHeadView = this.mFilterAdjustHeadView;
        if (filterAdjustHeadView != null) {
            filterAdjustHeadView.setHeadViewClickListener(null);
        }
        if (this.mFilterView != null) {
            this.mFilterView = null;
        }
        if (this.mAdjustView != null) {
            this.mAdjustView = null;
        }
    }
}
