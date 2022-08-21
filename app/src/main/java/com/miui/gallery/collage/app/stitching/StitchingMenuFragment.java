package com.miui.gallery.collage.app.stitching;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.miui.gallery.R;
import com.miui.gallery.collage.app.common.AbstractStitchingFragment;
import com.miui.gallery.collage.app.common.CollageMenuFragment;
import com.miui.gallery.collage.core.CollagePresenter;
import com.miui.gallery.collage.core.stitching.StitchingModel;
import com.miui.gallery.collage.core.stitching.StitchingPresenter;
import com.miui.gallery.editor.photo.widgets.recyclerview.Selectable;
import com.miui.gallery.ui.CenterSmoothScrollerController;
import com.miui.gallery.widget.recyclerview.BlankDivider;
import com.miui.gallery.widget.recyclerview.CustomScrollerLinearLayoutManager;
import com.miui.gallery.widget.recyclerview.OnItemClickListener;
import com.miui.gallery.widget.recyclerview.SimpleRecyclerView;
import java.util.ArrayList;
import java.util.List;

/* loaded from: classes.dex */
public class StitchingMenuFragment extends CollageMenuFragment<StitchingPresenter, AbstractStitchingFragment> {
    public BlankDivider mBlankDivider;
    public SimpleRecyclerView mRecyclerView;
    public StitchingAdapter mStitchingAdapter;
    public List<StitchingModel> mStitchingModelList = new ArrayList();
    public boolean mViewReady = false;
    public boolean mDataReady = false;
    public boolean mDataInit = false;
    public CollagePresenter.DataLoadListener mInitDataLoadListener = new CollagePresenter.DataLoadListener() { // from class: com.miui.gallery.collage.app.stitching.StitchingMenuFragment.1
        @Override // com.miui.gallery.collage.core.CollagePresenter.DataLoadListener
        public void onDataLoad() {
            StitchingMenuFragment.this.mDataReady = true;
            StitchingMenuFragment.this.reloadData();
            StitchingMenuFragment.this.notifyDataInit();
        }
    };
    public OnItemClickListener mOnItemClickListener = new OnItemClickListener() { // from class: com.miui.gallery.collage.app.stitching.StitchingMenuFragment.2
        @Override // com.miui.gallery.widget.recyclerview.OnItemClickListener
        public boolean OnItemClick(RecyclerView recyclerView, View view, int i) {
            ((SimpleRecyclerView) recyclerView).setSpringEnabled(false);
            recyclerView.smoothScrollToPosition(i);
            StitchingMenuFragment.this.mStitchingAdapter.setSelection(i);
            StitchingMenuFragment.this.onSelectModel(i);
            return true;
        }
    };

    @Override // com.miui.gallery.collage.app.common.CollageMenuFragment, com.miui.gallery.app.fragment.AndroidFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        ((StitchingPresenter) this.mPresenter).loadDataFromResourceAsync(this.mInitDataLoadListener);
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R.layout.collage_stitching_menu, viewGroup, false);
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        this.mRecyclerView = (SimpleRecyclerView) view.findViewById(R.id.list);
        this.mStitchingAdapter = new StitchingAdapter(getActivity(), this.mStitchingModelList, new Selectable.Selector(getResources().getDrawable(R.drawable.collage_item_background_selector)));
        CustomScrollerLinearLayoutManager customScrollerLinearLayoutManager = new CustomScrollerLinearLayoutManager(getActivity());
        customScrollerLinearLayoutManager.setSmoothScroller(new CenterSmoothScrollerController(getActivity()));
        customScrollerLinearLayoutManager.setOrientation(0);
        this.mRecyclerView.setEnableItemClickWhileSettling(true);
        this.mRecyclerView.setLayoutManager(customScrollerLinearLayoutManager);
        BlankDivider blankDivider = new BlankDivider(getResources().getDimensionPixelSize(R.dimen.collage_menu_item_margin_start));
        this.mBlankDivider = blankDivider;
        this.mRecyclerView.addItemDecoration(blankDivider);
        this.mRecyclerView.setAdapter(this.mStitchingAdapter);
        this.mViewReady = true;
        notifyDataInit();
    }

    public final void notifyDataInit() {
        if (!this.mViewReady || !this.mDataReady || this.mDataInit) {
            return;
        }
        onSelectModel(0);
        this.mStitchingAdapter.setOnItemClickListener(this.mOnItemClickListener);
        this.mDataInit = true;
    }

    public final void onSelectModel(int i) {
        if (this.mStitchingModelList.size() == 0) {
            return;
        }
        getRenderFragment().onSelectModel(this.mStitchingModelList.get(i));
    }

    public final void reloadData() {
        this.mStitchingModelList.clear();
        List<StitchingModel> stitching = ((StitchingPresenter) this.mPresenter).getStitching();
        if (stitching != null) {
            this.mStitchingModelList.addAll(stitching);
        }
        this.mStitchingAdapter.notifyDataSetChanged();
    }
}
